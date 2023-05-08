package ProjectDemo.Server.GUI;

import ProjectDemo.Server.Database.Database;
import ProjectDemo.Server.Mechanic.Server;
import Protocol.CoapServer;

import javafx.application.Platform;
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
    private ListView<String> sensorView;
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
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

        // set up a scheduled executor to periodically put data into the chart
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            // get a random integer between 0-10
            Integer random = ThreadLocalRandom.current().nextInt(10);

            // Update the chart
            Platform.runLater(() -> {
                // get current time
                Date now = new Date();
                // put random number with current time
                series.getData().add(
                        new XYChart.Data<>(simpleDateFormat.format(now), random));
                if (series.getData().size() > WINDOW_SIZE)
                    series.getData().remove(0);
            });
        }, 0, 4000, TimeUnit.MILLISECONDS);
    }

    public void showThroughputChart(MouseEvent mouseEvent) {
        final CategoryAxis xAxis = new CategoryAxis(); // we are going to plot against time
        final NumberAxis yAxis = new NumberAxis(0, 20, 1);
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
    public void showTemperatureChart(MouseEvent mouseEvent) {
        final CategoryAxis xAxis = new CategoryAxis(); // we are going to plot against time
        final NumberAxis yAxis = new NumberAxis(15, 30, 0.5);
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
        final NumberAxis yAxis = new NumberAxis(15, 30, 0.5);
        xAxis.setLabel("Time");
        xAxis.setAnimated(false);
        yAxis.setLabel("Average Delay (ms)");
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
            // get a random integer between 0-10
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

    public void initialize() {
        try {
             database = Database.getDatabase();
            Server server = new Server();
            server.start();
            //throughputTab.setContent(FXMLLoader.load(getClass().getClassLoader().getResource("Throughput.fxml")));
            resultLabel.setText("Sensor statistics");
            noteLabel.setText("Right-click on a sensor to modify the status");
            sensorView.setVisible(false);
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
        String currentSensor = sensorView.getSelectionModel().getSelectedItem();
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
        String currentSensor = sensorView.getSelectionModel().getSelectedItem();
        if (currentSensor != null) {
            sensorPane.setVisible(true);
            sensorDetail.getEngine().loadContent("Content of sensor");
        } else {
            sensorDetail.getEngine().loadContent("No sensor clicked");
            sensorPane.setVisible(false);
        }
    }
}
