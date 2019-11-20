package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class AlertDesc extends Alert {
    private TextField nameField = new TextField();
    private TextField descField = new TextField();

    public AlertDesc(){
        super(AlertType.CONFIRMATION);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(5);
        grid.setVgap(10);
        grid.addRow(0, new Label("Namn:"), nameField);
        grid.addRow(1, new Label("Description:"), descField);
        setHeaderText(null);
        getDialogPane().setContent(grid);

    }

    public String getName() {return nameField.getText();}



    public String getDesc() {return descField.getText();}


}
