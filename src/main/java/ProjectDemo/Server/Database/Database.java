package ProjectDemo.Server.Database;

import ProjectDemo.Message.SensorMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;

public class Database {
    //Ai dung cai dat gi thi tu sua may thong tin ve host, admin vs pass
    private final String host = "jdbc:mySQL://localhost:3306/sensor_database";
    private final String admin = "root";
    private final String password = "kaiser0309";

    private Connection connection;
    private static final Database database = new Database();

    public static double CALCULATING_COEFFICIENT = 0.25;

    public static void main(String[] args) {
        Database database1 = getDatabase();
        Connection connection1 = database.getConnection();
        database1.closeConnection();
    }

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

    public void updateSensorValue(String jsonMessage) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            SensorMessage message = mapper.readValue(jsonMessage, SensorMessage.class);

            String query = "SELECT * FROM sensor WHERE sensor_id = " + message.getSensor_id() + ";";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);

            if (!result.next()) {
                double delay = System.nanoTime() - message.getLast_time_modified(); //nano second
                double throughput = jsonMessage.getBytes().length * 8 / (delay / 1000); //kbps
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
                double preDelay = result.getDouble("delay");
                double preThroughput = result.getDouble("throughput");

                double delay = (1 - CALCULATING_COEFFICIENT) * preDelay +
                        CALCULATING_COEFFICIENT * (System.nanoTime() - message.getLast_time_modified());
                double throughput = (1 - CALCULATING_COEFFICIENT) * preThroughput +
                        CALCULATING_COEFFICIENT * (Double.valueOf(jsonMessage.length()) /
                                (System.nanoTime() - message.getLast_time_modified()));

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

    public void removeSensor(int sensor_id) {
        try {
            String query = "DELETE FROM sensor WHERE sensor_id=" + sensor_id + ";";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Doi trang thai sensor
     * @param sensor_id id cua sensor can thay doi trang thai
     * @param status dau vao la RUNNING hoac SUSPENDED
     */
    public void changeSensorStatus(int sensor_id, String status) {
        try {
            String query = "UPDATE sensor SET sensor_status=" + status + "WHERE sensor_id=" + sensor_id + ";";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getSensorData() {
        double sensorData = -1;

        try {
            String query = "SELECT AVG(sensor_data) FROM sensor;";
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

    public double getDelay() {
        double delay = -1;
        try {
            String query = "SELECT AVG(delay) FROM sensor;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                delay = resultSet.getDouble(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return delay;
    }

    public double getThroughput() {
        double throughput = -1;
        try {
            String query = "SELECT AVG(throughput) FROM sensor;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                throughput = resultSet.getDouble(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return throughput;
    }
}
