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
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
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
    private Database database;

    public void showChart() throws IOException {
        final CategoryAxis xAxis1 = new CategoryAxis();
        final NumberAxis yAxis1 = new NumberAxis();
        xAxis1.setLabel("Time");
        xAxis1.setAnimated(false);
        yAxis1.setLabel("Number of sensors");
        yAxis1.setAnimated(false);
        yAxis1.setForceZeroInRange(false);
        yAxis1.setTickLabelsVisible(true);
        yAxis1.setMinorTickVisible(false);
        final LineChart<String, Number> lineChart1 = new LineChart<>(xAxis1, yAxis1);
        lineChart1.setTitle("Number of sensors active by time");
        lineChart1.setAnimated(false);

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Number of sensors");
        lineChart1.getData().add(series1);
        lineChart1.getStylesheets().add("legend1.css");
        lineChart1.setPrefSize(500, 350);

        final CategoryAxis xAxis2 = new CategoryAxis();
        final NumberAxis yAxis2 = new NumberAxis();
        xAxis2.setLabel("Time");
        xAxis2.setAnimated(false);
        yAxis2.setLabel("Average Throughput (kbps)");
        yAxis2.setAnimated(false);
        yAxis2.setForceZeroInRange(false);
        yAxis2.setTickLabelsVisible(true);
        yAxis2.setMinorTickVisible(false);

        final LineChart<String, Number> lineChart2 = new LineChart<>(xAxis2, yAxis2);
        lineChart2.setTitle("Show Average Throughput");
        lineChart2.setAnimated(false);

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Average throughput");
        lineChart2.getData().add(series2);
        lineChart2.getStylesheets().add("legend2.css");
        lineChart2.setPrefSize(500, 350);

        final CategoryAxis xAxis3 = new CategoryAxis();
        final NumberAxis yAxis3 = new NumberAxis(0, 60, 5);
        xAxis3.setLabel("Time");
        xAxis3.setAnimated(false);
        yAxis3.setLabel("Average Temperature °C");
        yAxis3.setAnimated(false);
        yAxis3.setForceZeroInRange(false);
        yAxis3.setTickLabelsVisible(true);
        yAxis3.setMinorTickVisible(false);

        final LineChart<String, Number> lineChart3 = new LineChart<>(xAxis3, yAxis3);
        lineChart3.setTitle("Show Average Temperature");
        lineChart3.setAnimated(false);

        XYChart.Series<String, Number> series3 = new XYChart.Series<>();
        series3.setName("Average Temperature");
        lineChart3.getData().add(series3);
        lineChart3.getStylesheets().add("legend3.css");
        lineChart3.setPrefSize(500, 350);

        final CategoryAxis xAxis4 = new CategoryAxis();
        final NumberAxis yAxis4 = new NumberAxis();
        xAxis4.setLabel("Time");
        xAxis4.setAnimated(false);
        yAxis4.setLabel("Average Delay (ms)");
        yAxis4.setAnimated(false);
        yAxis4.setForceZeroInRange(false);
        yAxis4.setTickLabelsVisible(true);
        yAxis4.setMinorTickVisible(false);

        final LineChart<String, Number> lineChart4 = new LineChart<>(xAxis4, yAxis4);
        lineChart4.setTitle("Show Average Delay");
        lineChart4.setAnimated(false);

        XYChart.Series<String, Number> series4 = new XYChart.Series<>();
        series4.setName("Average Delay");
        lineChart4.getData().add(series4);
        lineChart4.getStylesheets().add("legend4.css");
        lineChart4.setPrefSize(500, 350);


        Stage stage2 = new Stage();
        stage2.setTitle("Sensor analytics");
        stage2.getIcons().add(new Image(getClass().getClassLoader().getResource("icon.png").openStream()));
        FlowPane sensorFlowPane = new FlowPane();
        sensorFlowPane.getChildren().addAll(lineChart1, lineChart2, lineChart3, lineChart4);
        Scene scene = new Scene(sensorFlowPane, 1000, 700);
        stage2.setScene(scene);
        stage2.show();

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        ScheduledExecutorService scheduledExecutorService1 = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService1.scheduleAtFixedRate(() -> {
            ArrayList <Integer> sensorArray = database.getSensorId();
            int runningSensor = (int) sensorArray.stream().filter(item -> database.getStatus(item).equals("RUNNING")).count();
            Platform.runLater(() -> {
                Date now = new Date();
                XYChart.Data<String, Number> data1 = new XYChart.Data<>(simpleDateFormat.format(now), runningSensor);
                series1.getData().add(data1);
                series1.getNode().setStyle("-fx-stroke: blue;");
                if (series1.getData().size() > WINDOW_SIZE)
                    series1.getData().remove(0);

                String style = "-fx-background-color: blue;";
                data1.getNode().setStyle(style);
            });
        }, 0, 3000, TimeUnit.MILLISECONDS);

        ScheduledExecutorService scheduledExecutorService2 = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService2.scheduleAtFixedRate(() -> {
            double avgThroughput = database.getThroughput();

            Platform.runLater(() -> {
                Date now = new Date();
                XYChart.Data<String, Number> data2 = new XYChart.Data<>(simpleDateFormat.format(now), avgThroughput);
                series2.getData().add(data2);
                series2.getNode().setStyle("-fx-stroke: green;");
                if (series2.getData().size() > WINDOW_SIZE)
                    series2.getData().remove(0);
                String style2 = "-fx-background-color: green;";
                data2.getNode().setStyle(style2);
            });
        }, 0, 3000, TimeUnit.MILLISECONDS);

        ScheduledExecutorService scheduledExecutorService3 = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService3.scheduleAtFixedRate(() -> {
            double avgTemp = database.getSensorData();
            Platform.runLater(() -> {
                Date now = new Date();
                XYChart.Data<String, Number> data3 = new XYChart.Data<>(simpleDateFormat.format(now), avgTemp);
                series3.getData().add(data3);
                series3.getNode().setStyle("-fx-stroke: orange;");
                if (series3.getData().size() > WINDOW_SIZE)
                    series3.getData().remove(0);
                String style2 = "-fx-background-color: orange;";
                data3.getNode().setStyle(style2);
            });
        }, 0, 3000, TimeUnit.MILLISECONDS);

        ScheduledExecutorService scheduledExecutorService4 = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService4.scheduleAtFixedRate(() -> {
            double avgDelay = database.getDelay();

            Platform.runLater(() -> {
                Date now = new Date();
                XYChart.Data<String, Number> data4 = new XYChart.Data<>(simpleDateFormat.format(now), avgDelay);
                series4.getData().add(data4);
                series4.getNode().setStyle("-fx-stroke: black;");
                if (series4.getData().size() > WINDOW_SIZE)
                    series4.getData().remove(0);
                String style = "-fx-background-color: black;";
                data4.getNode().setStyle(style);
            });
        }, 0, 3000, TimeUnit.MILLISECONDS);
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
        sensorView.setPrefHeight(-1 * sensorList.size() * sensorView.getFixedCellSize());
        Collections.sort(sensorList);
        sensorView.getItems().clear();
        addSensor(sensorView.getItems(), sensorList);
        sensorView.setCellFactory(list -> new CellFormat());
        updateResultLabel();
    }

    private void checkSensor() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(6), ev -> {
            updateSensorView();
            sensorDetail.getEngine().loadContent("<br /><br />" + "Click any active sensor to see the information");
            database.removeDisconnectedSensor();
            updateSensorView();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void initialize() {
        try {
            database = Database.getDatabase();
            database.clearDatabase();
            Server server = new Server();
            server.start();
            mainLabel.setText("Sensor statistics");
            resultLabel.setText("No sensor activated");
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
            alert1.initOwner(getCurrentStage());
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
            alert.initOwner(getCurrentStage());
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
                alert1.initOwner(getCurrentStage());
                alert1.setTitle("Information");
                alert1.setHeaderText("Sensor off");
                alert1.setContentText("The sensor has been disabled");
                alert1.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.initOwner(getCurrentStage());
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
                alert1.initOwner(getCurrentStage());
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
        }
    }

    private Stage getCurrentStage() {
        return (Stage) sensorDetail.getScene().getWindow();
    }
}
