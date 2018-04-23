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


        valikud.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {

                if (newValue.equals("Õpilane")){
                    nimi.setText("Õpilase nimi");
                }
                else if (newValue.equals("Rühm")){
                    nimi.setText("Rühma nimetus");
                    //Label rühm = new Label("Rühm");
                    //grid.add(rühm, 0, 1);
                }
                else if (newValue.equals("Trenn")){
                    nimi.setText("Trenni aeg");
                    //Label trenn = new Label("Trenn");
                    //grid.add(trenn, 0, 1);
                }
            }});

        return grid;
    }
}