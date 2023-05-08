package ProjectDemo.Server.GUI;

import ProjectDemo.Server.Database.Database;
import ProjectDemo.Server.Mechanic.Server;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MenuController {
    @FXML
    private ListView<Integer> sensorView;
    @FXML
    private Label noteLabel;
    @FXML
    private Label mainLabel;
    @FXML
    private Label resultLabel;
    @FXML
    private WebView sensorDetail;
    @FXML
    private Pane sensorPane;
    final int WINDOW_SIZE = 10;
    private ScheduledExecutorService scheduledExecutorService;
    private Database database;

    public void showSensorChart() {
        final CategoryAxis xAxis = new CategoryAxis(); // we are going to plot against time
<<<<<<< HEAD
        final NumberAxis yAxis = new NumberAxis();
=======
        final NumberAxis yAxis = new NumberAxis(0, 40, 1);
>>>>>>> 28f0269562fc081e3e6604a18ab51ec42b17e097
        xAxis.setLabel("Time");
        xAxis.setAnimated(false);
        yAxis.setLabel("Number of sensors");
        yAxis.setAnimated(false);
        yAxis.setForceZeroInRange(false);
        yAxis.setTickLabelsVisible(true);
        yAxis.setMinorTickVisible(false);

        final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Number of sensors active by time");
        lineChart.setAnimated(false);

        // defining a series to display data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Num. of sensors");

        // add series to chart
        lineChart.getData().add(series);
        lineChart.setPrefSize(600, 400);
        Stage stage2 = new Stage();
        stage2.setTitle("Sensor chart");
        Scene scene = new Scene(lineChart);
        stage2.setScene(scene);
        stage2.show();

        // this is used to display time in mm:ss format
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");

        // set up a scheduled executor to periodically put data into the chart
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            ArrayList <Integer> sensorArray = database.getSensorId();
            Integer numberOfSensor = sensorArray.size();

            Platform.runLater(() -> {
                Date now = new Date();
                series.getData().add(
                        new XYChart.Data<>(simpleDateFormat.format(now), numberOfSensor));
                if (series.getData().size() > WINDOW_SIZE)
                    series.getData().remove(0);
            });
        }, 0, 4000, TimeUnit.MILLISECONDS);
    }

    public void showThroughputChart() {
        final CategoryAxis xAxis = new CategoryAxis(); // we are going to plot against time
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        xAxis.setAnimated(false);
        yAxis.setLabel("Average Throughput (kbps)");
        yAxis.setAnimated(false);
        yAxis.setForceZeroInRange(false);
        yAxis.setTickLabelsVisible(true);
        yAxis.setMinorTickVisible(false);

        final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Show Average Throughput");
        lineChart.setAnimated(false);

        // defining a series to display data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Average throughput");

        // add series to chart
        lineChart.getData().add(series);
        lineChart.setPrefSize(600, 400);
        Stage stage2 = new Stage();
        stage2.setTitle("Sensor chart");
        Scene scene = new Scene(lineChart);
        stage2.setScene(scene);
        stage2.show();

        // this is used to display time in mm:ss format
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

        // set up a scheduled executor to periodically put data into the chart
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            // get a random integer between 0-10
            double avgThroughput = database.getThroughput()*1e6;


            // Update the chart
            Platform.runLater(() -> {
                // get current time
                Date now = new Date();
                // put random number with current time
                series.getData().add(
                        new XYChart.Data<>(simpleDateFormat.format(now), avgThroughput));
                if (series.getData().size() > WINDOW_SIZE)
                    series.getData().remove(0);
            });
        }, 0, 4000, TimeUnit.MILLISECONDS);


    }
    public void showTemperatureChart() {
        final CategoryAxis xAxis = new CategoryAxis(); // we are going to plot against time
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        xAxis.setAnimated(false);
        yAxis.setLabel("Average Temperature °C");
        yAxis.setAnimated(false);
        yAxis.setForceZeroInRange(false);
        yAxis.setTickLabelsVisible(true);
        yAxis.setMinorTickVisible(true);

        final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Show Average Temperature");
        lineChart.setAnimated(false);

        // defining a series to display data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Average Temperature");

        // add series to chart
        lineChart.getData().add(series);
        lineChart.setPrefSize(600, 400);
        Stage stage2 = new Stage();
        stage2.setTitle("Sensor chart");
        Scene scene = new Scene(lineChart);
        stage2.setScene(scene);
        stage2.show();

        // this is used to display time in mm:ss format
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

        // set up a scheduled executor to periodically put data into the chart
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            double avgTemp = database.getSensorData();

            // Update the chart
            Platform.runLater(() -> {
                // get current time
                Date now = new Date();
                // put random number with current time
                series.getData().add(
                        new XYChart.Data<>(simpleDateFormat.format(now), avgTemp));
                if (series.getData().size() > WINDOW_SIZE)
                    series.getData().remove(0);
            });
        }, 0, 4000, TimeUnit.MILLISECONDS);
    }
    public void showDelayChart() {
        final CategoryAxis xAxis = new CategoryAxis(); // we are going to plot against time
<<<<<<< HEAD
        final NumberAxis yAxis = new NumberAxis();
=======
        final NumberAxis yAxis = new NumberAxis(0, 200, 1);
>>>>>>> 28f0269562fc081e3e6604a18ab51ec42b17e097
        xAxis.setLabel("Time");
        xAxis.setAnimated(false);
        yAxis.setLabel("Average Delay (ms)");
        yAxis.setAnimated(false);
        yAxis.setForceZeroInRange(false);
        yAxis.setTickLabelsVisible(true);
        yAxis.setMinorTickVisible(false);

        final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Show Average Delay");
        lineChart.setAnimated(false);

        // defining a series to display data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Average Delay");

        // add series to chart
        lineChart.getData().add(series);
        lineChart.setPrefSize(600, 400);
        Stage stage2 = new Stage();
        stage2.setTitle("Delay Chart");
        Scene scene = new Scene(lineChart);
        stage2.setScene(scene);
        stage2.show();

        // this is used to display time in mm:ss format
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

        // set up a scheduled executor to periodically put data into the chart
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            // get a random integer between 0-10
            double avgDelay = database.getDelay()*1e-6;

            // Update the chart
            Platform.runLater(() -> {
                // get current time
                Date now = new Date();
                // put random number with current time
                series.getData().add(
                        new XYChart.Data<>(simpleDateFormat.format(now), avgDelay));
                if (series.getData().size() > WINDOW_SIZE)
                    series.getData().remove(0);
            });
        }, 0, 4000, TimeUnit.MILLISECONDS);

    }

    public void updateResultLabel() {
        ObservableList<Integer> items = sensorView.getItems();
        int runningSensor = 0;
        for (Integer item : items) {
            if (database.getStatus(item).equals("RUNNING")) {
                runningSensor++;
            }
        }
        if (runningSensor > 0) {
            if (runningSensor > 1)
                resultLabel.setText(runningSensor + " sensors activated");
            else
                resultLabel.setText("1 sensor activated");
        }
        else resultLabel.setText("No sensor activated");
    }

    public void addSensor(ObservableList<Integer> items, ArrayList<Integer> sensorList) {
        for(int i = 0; i<sensorList.size(); i++) {
            items.add(i, sensorList.get(i));
        }
    }

    public void updateSensorView() {
        ArrayList<Integer> sensorList = database.getSensorId();
        Collections.sort(sensorList);
        sensorView.getItems().clear();
        addSensor(sensorView.getItems(), sensorList);
        sensorView.setCellFactory(list -> new CellFormat());
        updateResultLabel();
    }

    private void checkSensor() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(6), ev -> {
            updateSensorView();
            sensorDetail.getEngine().loadContent("<br /><br />" + "New data collected. Click a sensor to receive");
            ArrayList<Integer> sensorList = database.getSensorId();
            for(Integer i: sensorList) {
                if(System.nanoTime()/1e6 - database.getLastModifiedID(i)/1e6 >= 60000) {
                    database.removeSensor(i);
                }
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void initialize() {
        try {
            database = Database.getDatabase();
            Server server = new Server();
            server.start();
            mainLabel.setText("Sensor statistics");
            resultLabel.setText("30 sensors activated");
            noteLabel.setText("Right-click on a sensor to modify the status");
            sensorPane.setVisible(true);
            sensorDetail.setVisible(true);
            updateSensorView();
            checkSensor();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Warning!");
            alert1.setHeaderText("Error !!!");
            alert1.setContentText("Something isn't working. Try again later.\nClick 'OK' to continue !");
            alert1.show();
        }
    }

    public void onChangingStatus() {
        Integer currentSensor = sensorView.getSelectionModel().getSelectedItem();
        if(currentSensor == null) {
            return ;
        }
        if(database.getStatus(currentSensor).equals("RUNNING")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Do you want to turn off this sensor?");
            alert.setContentText("Choose your option below");

            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(yesButton, noButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == yesButton) {
                database.changeSensorStatus(currentSensor, "SUSPENDED");
                updateSensorView();
                // Show alert
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert1.setTitle("Information");
                alert1.setHeaderText("Sensor off");
                alert1.setContentText("The sensor has been disabled");
                alert1.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Do you want to turn on this sensor?");
            alert.setContentText("Choose your option below");

            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(yesButton, noButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == yesButton) {
                database.changeSensorStatus(currentSensor, "RUNNING");
                updateSensorView();
                // Show alert
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert1.setTitle("Information");
                alert1.setHeaderText("Sensor on");
                alert1.setContentText("The sensor has been turned on successfully");
                alert1.show();
            }
        }
    }

    private String sensorContent(Integer currentSensor) {
        DecimalFormat df = new DecimalFormat("#.##");
        double roundedThroughput = Double.parseDouble(df.format(database.getThroughputID(currentSensor)));
        double roundedDelay = Double.parseDouble(df.format(database.getDelayID(currentSensor)));
        String tempStatus = "The temperature measured by this sensor is " + database.getDataID(currentSensor) + " ℃";
        String throughputStatus = "The throughput measured by this sensor is " + roundedThroughput + " kbps";
        String delayStatus = "The delay measured by this sensor is " + roundedDelay + " ms";
        return tempStatus + "<br /><br />"  + throughputStatus + "<br /><br />" + delayStatus;
    }

    public void onClickingASensor() {
        Integer currentSensor = sensorView.getSelectionModel().getSelectedItem();
        if (currentSensor != null) {
            if(database.getStatus(currentSensor).equals("RUNNING"))
                sensorDetail.getEngine().loadContent("<br /><br />" + sensorContent(currentSensor));
            else
                sensorDetail.getEngine().loadContent("<br /><br />" + "Sensor disabled");
        } else {
            sensorDetail.getEngine().loadContent("<br /><br />" + "No sensor clicked");
            //sensorPane.setVisible(false);
        }
    }
}
