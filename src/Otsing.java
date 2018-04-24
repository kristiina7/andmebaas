import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import javax.swing.*;

public class Otsing {
    private GridPane grid = new GridPane();
    private ObservableList<String> otsitavad = FXCollections.observableArrayList("Õpilane", "Rühm", "Trenn");
    private ComboBox<String> valikud = new ComboBox<>(otsitavad);

    //kas panna kõik loodavad elemendid isendi külge või luua kõik meetodis annaOtsing?

    public GridPane annaOtsing(){
        valikud.getStyleClass().add("combo-box");
        valikud.getSelectionModel().select(0); //valib automaatselt alguses õpilase
        grid.getStyleClass().add("grid-pane");
        grid.add(valikud, 0, 0);
        TextField otsitav = new TextField();
        ToggleButton otsi = new ToggleButton("Otsi");
        grid.add(otsi, 2,5);


        Label nimi = new Label("Õpilase nimi");
        grid.add(nimi, 0, 5);
        grid.add(otsitav, 1, 5);
        TextField otsitavRühm = new TextField(); //trenni otsimiseks
        Label nimiRühm = new Label("Rühma nimi"); //trenni otsimiseks

        Label otsinguTulemus = new Label();
        StringBuilder päring = new StringBuilder();


        valikud.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
                if(grid.getChildren().contains(otsitavRühm)){ //juhuks kui enne otsiti trenni ja nüüd midagi muud
                    grid.getChildren().removeAll(otsitavRühm, nimiRühm);
                }

                if (newValue.equals("Õpilane")){
                    nimi.setText("Õpilase nimi");
                    grid.setRowIndex(otsi, 5);
                    //otsi.setOnMouseClicked(event-> ); //siia tuleb select päring

                }
                else if (newValue.equals("Rühm")){
                    nimi.setText("Rühma nimi");
                    grid.setRowIndex(otsi, 5);
                }
                else if (newValue.equals("Trenn")){
                    nimi.setText("Trenni aeg");
                    grid.setRowIndex(otsi, 6);
                    grid.add(otsitavRühm, 1, 6);
                    grid.add(nimiRühm, 0,6);

                }
            }});

        return grid;
    }
}