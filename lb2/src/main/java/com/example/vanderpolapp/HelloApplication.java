package com.example.vanderpolapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    private TextField paramBField;
    private LineChart<Number, Number> chartX;
    private LineChart<Number, Number> chartPhase;
    private Scene mainScene, phaseScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Рівняння Ейлера");

        // Головний інтерфейс
        Label paramBLabel = new Label("Введіть параметр b:");
        paramBLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        paramBField = new TextField("0.01");
        paramBField.setPrefWidth(100);
        paramBField.setStyle("-fx-font-size: 14;");

        Button calculateButton = new Button("Розпочати обчислення");
        calculateButton.setStyle("-fx-font-size: 14; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10;");
        calculateButton.setOnAction(e -> calculate());

        Button switchToPhaseButton = new Button("➡ Фазова траєкторія");
        switchToPhaseButton.setStyle("-fx-font-size: 14; -fx-background-color: #2196F3; -fx-text-fill: white; -fx-padding: 10;");
        switchToPhaseButton.setOnAction(e -> primaryStage.setScene(phaseScene));

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Час");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("x");

        chartX = new LineChart<>(xAxis, yAxis);
        chartX.setTitle("Графік змінної x(t)");

        VBox mainControls = new VBox(10, paramBLabel, paramBField, calculateButton, switchToPhaseButton);
        mainControls.setAlignment(Pos.CENTER);
        mainControls.setPadding(new Insets(20));

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(chartX);
        mainLayout.setBottom(mainControls);

        mainScene = new Scene(mainLayout, 900, 600);

        // Другий інтерфейс
        NumberAxis phaseXAxis = new NumberAxis();
        phaseXAxis.setLabel("x");
        NumberAxis phaseYAxis = new NumberAxis();
        phaseYAxis.setLabel("v");

        chartPhase = new LineChart<>(phaseXAxis, phaseYAxis);
        chartPhase.setTitle("Фазова траєкторія");

        Button switchToMainButton = new Button("⬅ Назад");
        switchToMainButton.setStyle("-fx-font-size: 14; -fx-background-color: #FF5722; -fx-text-fill: white; -fx-padding: 10;");
        switchToMainButton.setOnAction(e -> primaryStage.setScene(mainScene));

        HBox phaseControls = new HBox(switchToMainButton);
        phaseControls.setAlignment(Pos.CENTER);
        phaseControls.setPadding(new Insets(20));

        BorderPane phaseLayout = new BorderPane();
        phaseLayout.setCenter(chartPhase);
        phaseLayout.setBottom(phaseControls);

        phaseScene = new Scene(phaseLayout, 900, 600);

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private void calculate() {
        double b;
        try {
            b = Double.parseDouble(paramBField.getText());
        } catch (NumberFormatException e) {
            paramBField.setText("Неправильний ввід");
            return;
        }

        double h = 0.005;  // Крок часу
        double x = 1.0, v = 0.0;  // Початкові значення
        int steps = 1000;  // Кількість кроків інтегрування

        XYChart.Series<Number, Number> seriesX = new XYChart.Series<>();
        seriesX.setName("x(t) при b = " + b);

        XYChart.Series<Number, Number> seriesPhase = new XYChart.Series<>();
        seriesPhase.setName("Фазова траєкторія при b = " + b);

        // Ітераційний процес із чисельного методу Ейлера
        for (int i = 0; i < steps; i++) {
            double t = i * h;
            double newX = x + h * v;
            double newV = v + h * (b * (1 - x * x) * v - x);

            seriesX.getData().add(new XYChart.Data<>(t, x));
            seriesPhase.getData().add(new XYChart.Data<>(x, v));

            x = newX;
            v = newV;
        }

        chartX.getData().clear();
        chartX.getData().add(seriesX);

        chartPhase.getData().clear();
        chartPhase.getData().add(seriesPhase);
    }
}
