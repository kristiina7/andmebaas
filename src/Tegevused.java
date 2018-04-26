import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;

public class Tegevused{
    private Andmebaas andmebaas;
    public Tegevused(Andmebaas andmebaas){
        this.andmebaas = andmebaas;

    }

    public GridPane annaOtsing() throws SQLException{
        GridPane grid = new GridPane();
        ObservableList<String> otsitavad = FXCollections.observableArrayList("Õpilane", "Rühm", "Trenn");
        ComboBox<String> valikud = new ComboBox<>(otsitavad);

        valikud.getStyleClass().add("combo-box");
        valikud.getSelectionModel().select(0); //valib automaatselt alguses õpilase
        grid.getStyleClass().add("grid-pane");
        grid.add(valikud, 0, 0);
        TextField otsitav = new TextField();
        ToggleButton otsi = new ToggleButton("Otsi");
        grid.add(otsi, 2,5);

        //String[] input = new String[1];

        Label nimi = new Label("Õpilase nimi");
        grid.add(nimi, 0, 5);
        grid.add(otsitav, 1, 5);
        TextField otsitavRühm = new TextField(); //trenni otsimiseks
        Label nimiRühm = new Label("Rühma nimi"); //trenni otsimiseks

        Label otsinguTulemus = new Label();

        valikud.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
            public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue){
                if(grid.getChildren().contains(otsitavRühm)){ //juhuks kui enne otsiti trenni ja nüüd midagi muud
                    grid.getChildren().removeAll(otsitavRühm, nimiRühm);
                }
                otsitav.setText("");
                if (grid.getChildren().contains(otsinguTulemus)) grid.getChildren().remove(otsinguTulemus);

                if (newValue.equals("Õpilane")) {
                    nimi.setText("Õpilase nimi");
                    grid.setRowIndex(otsi, 5);
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

        otsi.setOnMouseClicked(event-> {//ta ei tahab SQLExceptioni throwimist, aga ei oska seda kuhugi panna

            if (grid.getChildren().contains(otsinguTulemus)) grid.getChildren().remove(otsinguTulemus);

            if (nimi.getText().equals("Õpilase nimi")) {
                try {
                    otsinguTulemus.setText(andmebaas.sqlÕpilaseAndmed(otsitav.getText()));
                    grid.add(otsinguTulemus, 0, 8);
                } catch (SQLException e){
                    e.printStackTrace();
                    throw new RuntimeException();
                }
            }
            if (nimi.getText().equals("Rühma nimi")){
                try{
                    otsinguTulemus.setText(andmebaas.sqlRühmaAndmed(otsitav.getText()));
                    grid.add(otsinguTulemus, 0, 8);
                }
                catch (SQLException e){
                    e.printStackTrace();
                    throw new RuntimeException();
                }
            }
        });

        return grid;
    }

    public GridPane annaSaavutused(){
        GridPane grid = new GridPane();
        ObservableList<String> valitavad = FXCollections.observableArrayList("2017/18", "2016/17", "2015/16");
        ComboBox<String> valikud = new ComboBox<>(valitavad);
        Label hooaeg = new Label("Hooaeg");

        grid.getStyleClass().add("grid-pane");
        valikud.getStyleClass().add("combo-box");
        valikud.getSelectionModel().select(0); //alguses on ees praeguse hooaja andmed
        grid.add(hooaeg, 0, 0);
        grid.add(valikud,1,0);

        valikud.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {

            }
        });
        return grid;
    }

    public GridPane annaLisamine() {
        GridPane grid = new GridPane();
        ObservableList<String> valitavad = FXCollections.observableArrayList("Trenn", "Õpilane", "Võistlus");
        ComboBox<String> valikud = new ComboBox<>(valitavad);
        Label keda = new Label("Lisa uus");

        grid.getStyleClass().add("grid-pane");
        valikud.getStyleClass().add("combo-box");
        valikud.getSelectionModel().select(0); //alguses on ees trenni lisamine
        grid.add(keda, 0, 0);
        grid.add(valikud, 1, 0);

        valikud.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {

            }
        });

        return grid;
    }
    public GridPane annaAktiivsus(){
        GridPane grid = new GridPane();

        grid.getStyleClass().add("grid-pane");
        TextField rühm = new TextField();
        Label nimetus = new Label("Rühma nimi");
        grid.add(nimetus, 0, 0);
        grid.add(rühm, 1, 0);
        ToggleButton otsi = new ToggleButton("Otsi");
        grid.add(otsi, 2, 0);

        return grid;
}

}
