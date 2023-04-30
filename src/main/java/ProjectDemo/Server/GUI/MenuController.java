package ProjectDemo.Server.GUI;

import Protocol.CoapServer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.SnapshotParameters;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class MenuController extends Application {
    @FXML
    private ListView<String> sensorView;
    @FXML
    private Tab throughputTab;
    @FXML
    private Label noteLabel;
    @FXML
    private Label resultLabel;
    @FXML
    private WebView chartView;
    final int WINDOW_SIZE = 10;
    private ScheduledExecutorService scheduledExecutorService;

    private void showChart(ScheduledExecutorService scheduledExecutorService) throws IOException {
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
        lineChart.setPrefSize(561, 374);

        /*WritableImage image = lineChart.snapshot(new SnapshotParameters(), null);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        String html = "<html><body><img src=\"data:image/png;base64," + base64Image + "\"/></body></html>";
        chartView.getEngine().loadContent(html);
         */

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

    public void initialize() {
        try {
            CoapServer server = new CoapServer();
            server.add(new Main.TemperatureResource());
            server.add(new Main.CommandResource());
            server.start();
            //throughputTab.setContent(FXMLLoader.load(getClass().getClassLoader().getResource("Throughput.fxml")));
            noteLabel.setText("This window displays the number of active sensors varied by time:");
            resultLabel.setText("Right-click on a sensor to modify the status");
            sensorView.setVisible(false);
            System.out.println("Goes here");
            showChart(scheduledExecutorService);
        } catch (IOException e) {
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

    @Override
    public void start(Stage stage) throws Exception {

    }

    @Override
    public void stop() throws Exception {
        super.stop();
        scheduledExecutorService.shutdownNow();
    }
}
