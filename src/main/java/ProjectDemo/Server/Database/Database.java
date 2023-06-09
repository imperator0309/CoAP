package ProjectDemo.Server.Database;

import ProjectDemo.Message.CommandMessage;
import ProjectDemo.Message.SensorMessage;
import Protocol.CoapClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    private final String host = "jdbc:mySQL://localhost:3306/sensor_database";
    private final String admin = "root";
    private final String password = "";

    private Connection connection;
    private static final Database database = new Database();

    private Database() {
        try {
            connection = DriverManager.getConnection(host, admin, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Database getDatabase() {
        return database;
    }


    public Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(host, admin, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getStatus(Integer sensorID) {
        String sensorStatus = "";

        try {
            String query = "SELECT sensor_status FROM sensor WHERE sensor_id = " + sensorID + ";";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                sensorStatus = resultSet.getString(1);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return sensorStatus;
    }

    public void updateSensorValue(String jsonMessage) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            SensorMessage message = mapper.readValue(jsonMessage, SensorMessage.class);

            String query = "SELECT * FROM sensor WHERE sensor_id = " + message.getSensor_id() + ";";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);

            if (!result.next()) {
                double delay = System.nanoTime() - message.getLast_time_modified(); //nanosecond
                double throughput = jsonMessage.getBytes().length / delay; //bytes per nanosecond
                String status = "RUNNING";

                query = "INSERT INTO sensor(sensor_id, sensor_data, last_time_modified, sensor_status, delay, throughput)"
                        + "VALUES(?, ?, ?, ?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, message.getSensor_id());
                preparedStatement.setDouble(2, message.getTemperature());
                preparedStatement.setLong(3, message.getLast_time_modified());
                preparedStatement.setString(4, status);
                preparedStatement.setDouble(5, delay);
                preparedStatement.setDouble(6, throughput);

                preparedStatement.executeUpdate();
            } else {
                double delay = System.nanoTime() - message.getLast_time_modified();
                double throughput = jsonMessage.getBytes().length * 1.0 / delay;

                query = "update sensor "
                        + "set sensor_data =?," +
                        "last_time_modified=?," +
                        "delay=?," +
                        "throughput=? " +
                        "where sensor_id=?";

                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setDouble(1, message.getTemperature());
                preparedStatement.setLong(2, message.getLast_time_modified());
                preparedStatement.setDouble(3, delay);
                preparedStatement.setDouble(4, throughput);
                preparedStatement.setInt(5, message.getSensor_id());

                preparedStatement.executeUpdate();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            System.err.println("Cannot update database");
            ex.printStackTrace();
        }
    }

    public void removeDisconnectedSensor() {
        try {
            String query = "DELETE " +
                    "FROM sensor " +
                    "WHERE last_time_modified < ? AND sensor_status = ?";

            double checkSensorTimeOut = 6 * 1e9;

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, System.nanoTime() - checkSensorTimeOut);
            preparedStatement.setString(2, "RUNNING");
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Change sensor status
     * @param sensor_id the id of the sensor need to be changed status
     * @param status gets values RUNNING or SUSPENDED
     */
    public void changeSensorStatus(int sensor_id, String status) {
        boolean sendCommandMessageFlag = true;
        try {
            CoapClient client = new CoapClient("coap://localhost:5683/command");
            CommandMessage.Command command;

            if (status.equals("RUNNING")) {
                command = CommandMessage.Command.RESUME;
            } else {
                command = CommandMessage.Command.SUSPEND;
            }

            CommandMessage message = new CommandMessage(sensor_id, command);
            ObjectMapper mapper = new ObjectMapper();
            String jsonMessage = mapper.writeValueAsString(message);
            client.post(jsonMessage.getBytes());

            client.destroy();
        } catch (Exception e) {
            e.printStackTrace();
            sendCommandMessageFlag = false;
        }

        if (sendCommandMessageFlag) {
            try {
                String query = "UPDATE sensor " + "SET sensor_status= " + "\"" + status + "\""
                        + " " + "WHERE sensor_id=" + sensor_id + ";";
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearDatabase() {
        try {
            String query = "DELETE FROM sensor";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public long getLastModifiedID(Integer sensorID) {
        long sensorLastModified = -1;

        try {
            String query = "SELECT last_time_modified FROM sensor WHERE sensor_id = " + sensorID + ";";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                sensorLastModified = resultSet.getLong(1);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return sensorLastModified;
    }

    public ArrayList<Integer> getSensorId() {
        ArrayList<Integer> sensorId = new ArrayList<>();
        try {
            String query = "SELECT sensor_id FROM sensor;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                sensorId.add(resultSet.getInt(1));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return sensorId;
    }

    public double getDataID(Integer sensorID) {
        double sensorData = -1;

        try {
            String query = "SELECT sensor_data FROM sensor WHERE sensor_id = " + sensorID + ";";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                sensorData = resultSet.getDouble(1);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return sensorData;
    }

    public double getSensorData() {
        double sensorData = -1;

        try {
            String query = "SELECT AVG(sensor_data) FROM sensor WHERE sensor_status = \"RUNNING\";";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                sensorData = resultSet.getDouble(1);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return sensorData;
    }

    public double getDelayID(Integer sensorID) {
        double delay = -1;

        try {
            String query = "SELECT delay FROM sensor WHERE sensor_id = " + sensorID + ";";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                delay = resultSet.getDouble(1);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return delay * 1e-6;
    }

    public double getDelay() {
        double delay = -1;
        try {
            String query = "SELECT AVG(delay) FROM sensor WHERE sensor_status = \"RUNNING\";";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                delay = resultSet.getDouble(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return delay * 1e-6;
    }

    public double getThroughputID(Integer sensorID) {
        double throughput = -1;

        try {
            String query = "SELECT throughput FROM sensor WHERE sensor_id = " + sensorID + ";";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                throughput = resultSet.getDouble(1);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return throughput * 8 * 1e6;
    }

    public double getThroughput() {
        double throughput = -1;
        try {
            String query = "SELECT AVG(throughput) FROM sensor WHERE sensor_status = \"RUNNING\";";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                throughput = resultSet.getDouble(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return throughput * 8 * 1e6;
    }
}
