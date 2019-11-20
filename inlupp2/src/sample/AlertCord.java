package sample;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.text.ParseException;


public class AlertCord extends Alert {
    private TextField xField = new TextField();
    private TextField yField = new TextField();


    public AlertCord() {
        super(AlertType.CONFIRMATION);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(5);
        grid.setVgap(10);
        grid.addRow(1, new Label("X:"), xField);
        grid.addRow(2, new Label("Y: "), yField);
        setHeaderText(null);
        getDialogPane().setContent(grid);

    }



    public double getXField() {

        return Double.parseDouble(xField.getText());
    }
    public double getYField() {
        return Double.parseDouble(yField.getText());
    }



}