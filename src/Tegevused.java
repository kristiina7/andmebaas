import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.sql.SQLException;


public class Tegevused{
    private Andmebaas andmebaas;
    public Tegevused(Andmebaas andmebaas){
        this.andmebaas = andmebaas;
    }

    public VBox annaOtsing(){
        GridPane grid = new GridPane();
        VBox box = new VBox();
        ObservableList<String> otsitavad = FXCollections.observableArrayList("Õpilane", "Rühm", "Trenn");
        ComboBox<String> valikud = new ComboBox<>(otsitavad);

        valikud.getStyleClass().add("combo-box");
        valikud.getSelectionModel().select(0); //valib automaatselt alguses õpilase
        grid.getStyleClass().add("grid-pane");
        grid.add(valikud, 0, 0);
        TextField otsitav = new TextField();
        otsitav.setPromptText("Eesnimi Perenimi");
        ToggleButton otsi = new ToggleButton("Otsi");
        grid.add(otsi, 2,5);

        Label nimi = new Label("Õpilase nimi");
        grid.add(nimi, 0, 5);
        grid.add(otsitav, 1, 5);
        TextField otsitavRühm = new TextField(); //trenni otsimiseks
        otsitavRühm.setPromptText("Nimi");
        Label nimiRühm = new Label("Rühma nimi"); //trenni otsimiseks

        Label otsinguTulemus = new Label();
        otsinguTulemus.getStyleClass().add("label-vastus");

        valikud.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
            public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue){
                if(grid.getChildren().contains(otsitavRühm)){ //juhuks kui enne otsiti trenni ja nüüd midagi muud
                    grid.getChildren().removeAll(otsitavRühm, nimiRühm);
                }
                otsitav.setText("");
                if (grid.getChildren().contains(otsinguTulemus)) grid.getChildren().remove(otsinguTulemus);

                if (newValue.equals("Õpilane")) {
                    nimi.setText("Õpilase nimi");
                    otsitav.setPromptText("Eesnimi Perenimi");
                    grid.setRowIndex(otsi, 5);
                }
                else if (newValue.equals("Rühm")){
                    nimi.setText("Rühma nimi");
                    otsitav.setPromptText("Nimi");
                    grid.setRowIndex(otsi, 5);
                }
                else if (newValue.equals("Trenn")){
                    nimi.setText("Trenni aeg");
                    otsitav.setPromptText("aaaa-kk-pp");
                    grid.setRowIndex(otsi, 6);
                    grid.add(otsitavRühm, 1, 6);
                    grid.add(nimiRühm, 0,6);

                }
            }});
        box.getChildren().add(grid);

        otsi.setOnMouseClicked(event-> {

            if (box.getChildren().contains(otsinguTulemus)) box.getChildren().remove(otsinguTulemus);

            if (nimi.getText().equals("Õpilase nimi")) {
                try {
                    otsinguTulemus.setText(andmebaas.sqlÕpilaseAndmed(otsitav.getText()));
                    box.getChildren().add(otsinguTulemus);
                } catch (SQLException e){
                    e.printStackTrace();
                    throw new RuntimeException();
                }
            }
            if (nimi.getText().equals("Rühma nimi")){
                try{
                    otsinguTulemus.setText(andmebaas.sqlRühmaAndmed(otsitav.getText()));
                    box.getChildren().add(otsinguTulemus);
                }
                catch (SQLException e){
                    e.printStackTrace();
                    throw new RuntimeException();
                }
            }
            if (nimi.getText().equals("Trenni aeg")) {
                try {
                    otsinguTulemus.setText(andmebaas.sqlTrennisOsalejad(otsitav.getText(), otsitavRühm.getText()));
                    box.getChildren().add(otsinguTulemus);
                } catch (SQLException e){
                    e.printStackTrace();
                    throw new RuntimeException();
                }
            }
        });


        return box;
    }

    public VBox annaSaavutused() throws SQLException{
        VBox box = new VBox();
        GridPane grid = new GridPane();
        ObservableList<String> valitavad = FXCollections.observableArrayList("2018", "2017", "2016");
        ComboBox<String> valikud = new ComboBox<>(valitavad);
        Label aasta = new Label("Aasta");

        grid.getStyleClass().add("grid-pane");
        valikud.getStyleClass().add("combo-box");
        valikud.getSelectionModel().select(0); //alguses on ees praeguse hooaja andmed
        grid.add(aasta, 0, 0);
        grid.add(valikud,1,0);

        Label tulemus = new Label();
        tulemus.getStyleClass().add("label-vastus");

        tulemus.setText(andmebaas.sqlSaavutused(2018));

        valikud.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
                try{
                    tulemus.setText(andmebaas.sqlSaavutused(Integer.parseInt(newValue)));
                }
                catch (SQLException e){
                    throw new RuntimeException();
                }
            }
        });
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(tulemus);

        box.getChildren().addAll(grid, scroll);
        return box;
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
    public VBox annaAktiivsus() throws SQLException{
        VBox box = new VBox();
        GridPane grid = new GridPane();

        ObservableList<String> otsitavad = FXCollections.observableArrayList(andmebaas.sqlRühmad());
        ComboBox<String> valikud = new ComboBox<>(otsitavad);
        valikud.getStyleClass().add("combo-box");
        valikud.getSelectionModel().select(0); //valib automaatselt alguses esimese

        Label nimi = new Label("Rühm");
        grid.getStyleClass().add("grid-pane");
        grid.add(nimi, 0, 0);
        grid.add(valikud, 1, 0);
        Label tulemus = new Label();
        tulemus.getStyleClass().add("label-vastus");
        tulemus.setText(andmebaas.sqlAktiivsus(valikud.getValue()));

        valikud.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
            public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue){
                try{
                    tulemus.setText(andmebaas.sqlAktiivsus(valikud.getValue()));
                }
                catch(SQLException e){
                    throw new RuntimeException();
                }

            }});
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(tulemus);
        box.getChildren().addAll(grid, scroll);

        return box;
}

}
