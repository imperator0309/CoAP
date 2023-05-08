package ProjectDemo.Server.GUI;

import ProjectDemo.Server.Database.Database;
import ProjectDemo.Server.Mechanic.Server;
import Protocol.CoapServer;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import javafx.scene.web.WebView;

import javafx.stage.Stage;


import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;


import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class MenuController {
    @FXML
    private ListView<Integer> sensorView;
    @FXML
    private Label noteLabel;
    @FXML
    private Label resultLabel;
    @FXML
    private WebView sensorDetail;
    @FXML
    private Pane sensorPane;
    final int WINDOW_SIZE = 10;
    private ScheduledExecutorService scheduledExecutorService;
    private Database database;

    public void showSensorChart(MouseEvent mouseEvent) {
        final CategoryAxis xAxis = new CategoryAxis(); // we are going to plot against time
        final NumberAxis yAxis = new NumberAxis(0, 20, 1);
        xAxis.setLabel("Time");
        xAxis.setAnimated(false);
        yAxis.setLabel("Number of sensors");
        yAxis.setAnimated(false);
        yAxis.setForceZeroInRange(false);
        yAxis.setTickLabelsVisible(true);
        yAxis.setMinorTickVisible(true);

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

    public void showThroughputChart(MouseEvent mouseEvent) {
        final CategoryAxis xAxis = new CategoryAxis(); // we are going to plot against time
        final NumberAxis yAxis = new NumberAxis(0, 100, 1);
        xAxis.setLabel("Time");
        xAxis.setAnimated(false);
        yAxis.setLabel("Average Throughput (kbps)");
        yAxis.setAnimated(false);
        yAxis.setForceZeroInRange(false);
        yAxis.setTickLabelsVisible(true);
        yAxis.setMinorTickVisible(true);

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
            double avgThroughput = database.getThroughput();

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
    public void showTemperatureChart(MouseEvent mouseEvent) {
        final CategoryAxis xAxis = new CategoryAxis(); // we are going to plot against time
        final NumberAxis yAxis = new NumberAxis(10, 40, 1);
        xAxis.setLabel("Time");
        xAxis.setAnimated(false);
        yAxis.setLabel("Average Temperature °C");
        yAxis.setAnimated(false);
        yAxis.setForceZeroInRange(false);
        yAxis.setTickLabelsVisible(true);
        yAxis.setMinorTickVisible(true);

        final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Show Avarage Temperature");
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
    public void showDelayChart(MouseEvent mouseEvent) {
        final CategoryAxis xAxis = new CategoryAxis(); // we are going to plot against time
        final NumberAxis yAxis = new NumberAxis(0, 100, 0.5);
        xAxis.setLabel("Time");
        xAxis.setAnimated(false);
        yAxis.setLabel("Average Delay (ms)");
        yAxis.setAnimated(false);
        yAxis.setForceZeroInRange(false);
        yAxis.setTickLabelsVisible(true);
        yAxis.setMinorTickVisible(true);

        final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Show Avarage Delay");
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
            double avgDelay = database.getDelay();

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
        if (!sensorView.getItems().isEmpty()) {
            if (sensorView.getItems().size() > 1)
                resultLabel.setText(sensorView.getItems().size() + " sensors activated");
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
        sensorView.getItems().clear();
        addSensor(sensorView.getItems(), sensorList);
        sensorView.setVisible(true);
        updateResultLabel();
    }

    public void initialize() {
        try {
            database = Database.getDatabase();
            Server server = new Server();
            server.start();
            resultLabel.setText("Sensor statistics");
            noteLabel.setText("Right-click on a sensor to modify the status");
            sensorView.setVisible(false);
            updateSensorView();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Warning!");
            alert1.setHeaderText("Error !!!");
            alert1.setContentText("Something isn't working. Try again later.\nClick 'OK' to continue !");
            alert1.show();
        }
    }

    public void onChangingStatus(ActionEvent mouseEvent) {
        Integer currentSensor = sensorView.getSelectionModel().getSelectedItem();
        //Nếu sensor đang bật, sử dụng alert hỏi người dùng muốn tắt nó hay không.
        //Nếu sensor đang tắt, hỏi ngược lại.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Do you want to turn on this sensor?");
        alert.setContentText("Choose your option below");

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yesButton) {
            //Status sensor chuyển về tắt
            //Chỉnh vị trí trong list view
            // Show alert
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert1.setTitle("Information");
            alert1.setHeaderText("Sensor off");
            alert1.setContentText("The sensor has been disabled");
            alert1.show();
        }
    }

    public void onClickingASensor(MouseEvent mouseEvent) {
        Integer currentSensor = sensorView.getSelectionModel().getSelectedItem();
        if (currentSensor != null) {
            sensorPane.setVisible(true);
            sensorDetail.getEngine().loadContent("Content of sensor");
        } else {
            sensorDetail.getEngine().loadContent("No sensor clicked");
            sensorPane.setVisible(false);
        }
    }
}
