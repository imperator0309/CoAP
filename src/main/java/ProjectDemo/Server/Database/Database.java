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
        Database database = getDatabase();
        Connection connection = database.getConnection();

        try {
            String query = "SELECT * FROM sensor WHERE sensor_id=" + 1 + ";";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);

            if (!result.next()) {
                System.out.println("NULL");
            }
        } catch (Exception e) {

        }
    }

    private Database() {

    }

    public void updateSensorValue(String jsonMessage) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            SensorMessage message = mapper.readValue(jsonMessage, SensorMessage.class);

            String query = "SELECT * FROM sensor WHERE sensor_id=" + message.getSensor_id() + ";";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);

            if (!result.next()) {
                double delay = System.currentTimeMillis() - message.getLast_time_modified();
                double throughput = jsonMessage.length() / delay; //kbps
                String status = "\"RUNNING\"";

                query = "INSERT INTO sensor(sensor_id, sensor_data, last_time_modified, sensor_status, delay, throughput)"
                        + "VALUES(" + message.getSensor_id() + "," + message.getTemperature() + ","
                        + message.getLast_time_modified() + "," + status + "," + delay + "," + throughput +")";
                statement.executeUpdate(query);
            } else {
                double preDelay = result.getDouble(5);
                double preThroughput = result.getDouble(6);

                double delay = (1 - CALCULATING_COEFFICIENT) * preDelay +
                        CALCULATING_COEFFICIENT * (System.currentTimeMillis() - message.getLast_time_modified());
                double throughput = (1 - CALCULATING_COEFFICIENT) * preThroughput +
                        CALCULATING_COEFFICIENT * (Double.valueOf(jsonMessage.length()) /
                                (System.currentTimeMillis() - message.getLast_time_modified()));

                query = "UPDATE sensor SET sensor_data=" + message.getTemperature() + ",delay=" + delay +
                        "throughput=" + throughput + " WHERE sensor_id=" + message.getSensor_id() +";";
                statement.executeUpdate(query);
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
     * Doi trang thai sensor trong database co id = sensor_id, status dau vao la RUNNING hoac SUSPENDED
     * @param sensor_id
     * @param status
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
}
