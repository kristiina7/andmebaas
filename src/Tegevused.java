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
        otsitav.setPromptText("Eesnimi Perenimi"); //vihjed tekstikasti kirjutamiseks
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
                if (box.getChildren().contains(otsinguTulemus)) box.getChildren().remove(otsinguTulemus);

                otsitav.setText(""); //tekstikastid tühjaks
                otsitavRühm.setText("");
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

        otsi.setOnMouseClicked(event-> { //kui vajutatakse otsi

            if (box.getChildren().contains(otsinguTulemus)) box.getChildren().remove(otsinguTulemus); //eelnevalt otsitud asjade eemaldamine

            if (nimi.getText().equals("Õpilase nimi")) {
                try {
                    otsinguTulemus.setText(andmebaas.sqlÕpilaseAndmed(otsitav.getText()));
                    box.getChildren().add(otsinguTulemus);
                } catch (SQLException e){
                    throw new RuntimeException(e);
                }
            }
            if (nimi.getText().equals("Rühma nimi")){
                try{
                    otsinguTulemus.setText(andmebaas.sqlRühmaAndmed(otsitav.getText()));
                    box.getChildren().add(otsinguTulemus);
                }
                catch (SQLException e){
                    throw new RuntimeException(e);
                }
            }
            if (nimi.getText().equals("Trenni aeg")) {
                try {
                    otsinguTulemus.setText(andmebaas.sqlTrennisOsalejad(otsitav.getText(), otsitavRühm.getText()));
                    box.getChildren().add(otsinguTulemus);
                } catch (SQLException e){
                    throw new RuntimeException(e);
                }
            }
        });


        return box;
    }

    public VBox annaSaavutused() throws SQLException{
        VBox box = new VBox();
        GridPane grid = new GridPane();
        ObservableList<String> valitavad = FXCollections.observableArrayList(andmebaas.sqlAastad());
        ComboBox<String> valikud = new ComboBox<>(valitavad);
        Label aasta = new Label("Aasta");

        grid.getStyleClass().add("grid-pane");
        valikud.getStyleClass().add("combo-box");
        valikud.getSelectionModel().select(0); //alguses on ees viimase aasta andmed
        grid.add(aasta, 0, 0);
        grid.add(valikud,1,0);

        Label tulemus = new Label();
        tulemus.getStyleClass().add("label-vastus");

        tulemus.setText(andmebaas.sqlSaavutused("2018"));

        valikud.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
                try{
                    tulemus.setText(andmebaas.sqlSaavutused(newValue));
                }
                catch (SQLException e){
                    throw new RuntimeException(e);
                }
            }
        });
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(tulemus);

        box.getChildren().addAll(grid, scroll);
        return box;
    }
    public void eemalda(TextField[] sisend){
        for (TextField el: sisend){
        el.setText("");
    }}

    public VBox annaLisamine() {

        GridPane grid = new GridPane();
        VBox box = new VBox();
        ObservableList<String> valitavad = FXCollections.observableArrayList("Trenn", "Õpilane", "Võistlus");
        ComboBox<String> valikud = new ComboBox<>(valitavad);
        Label keda = new Label("Lisa uus");

        grid.getStyleClass().add("grid-pane");
        valikud.getStyleClass().add("combo-box");
        valikud.getSelectionModel().select(0); //alguses on ees trenni lisamine
        grid.add(keda, 0, 0);
        grid.add(valikud, 1, 0);
        ToggleButton lisa = new ToggleButton("Lisa");
        lisa.getStyleClass().add("toggle-button-lisa");
        grid.add(lisa, 2,0);

        Label esimene = new Label("Toimumisaeg");
        TextField sisse_esimene = new TextField();
        grid.add(sisse_esimene, 1, 5);
        grid.add(esimene, 0, 5);
        sisse_esimene.setPromptText("aaaa-kk-pp tt:mm");
        Label teine = new Label("Rühm");
        TextField sisse_teine = new TextField();
        grid.add(sisse_teine, 1, 6);
        grid.add(teine, 0, 6);
        Label kolmas = new Label("Asukoht");
        TextField sisse_kolmas = new TextField();
        grid.add(sisse_kolmas, 1, 7);
        grid.add(kolmas, 0, 7);
        Label neljas = new Label("Juhendaja");
        TextField sisse_neljas = new TextField();
        grid.add(sisse_neljas, 1, 8);
        grid.add(neljas, 0, 8);
        Label viies = new Label("");
        TextField sisse_viies = new TextField();
        grid.add(viies, 0, 9);
        Label kuues = new Label("");
        TextField sisse_kuues = new TextField();
        grid.add(kuues, 0, 10);
        Label aadress = new Label("");
        TextField sisse_aadress = new TextField();
        grid.add(aadress, 0, 11);
        Label telefon = new Label("");
        TextField sisse_telefon = new TextField();
        grid.add(telefon, 0, 12);
        Label email = new Label("");
        TextField sisse_email = new TextField();
        grid.add(email, 0, 13);
        Label kommentaar = new Label("");
        TextField sisse_kommentaar = new TextField();
        grid.add(kommentaar, 0, 14);


        //Lapsevanema olemasolu kontroll
        ObservableList<String> olemas = FXCollections.observableArrayList("Olemas", "Ei ole");
        ComboBox<String> olemasolu = new ComboBox<>(olemas);
        Label vanem = new Label("");
        grid.add(vanem, 0, 1);
        olemasolu.getStyleClass().add("combo-box");
        olemasolu.getSelectionModel().select(0);

        TextField[] sisend = {sisse_esimene, sisse_teine, sisse_kolmas, sisse_neljas, sisse_viies,sisse_kuues, sisse_aadress, sisse_telefon,sisse_email,sisse_kommentaar};
        valikud.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
                eemalda(sisend);
                olemasolu.getSelectionModel().select(0);
                if (grid.getChildren().contains(sisse_viies)) {
                    grid.getChildren().removeAll(sisse_viies, sisse_kuues, olemasolu, sisse_aadress, sisse_email, sisse_telefon, sisse_kommentaar);
                    viies.setText("");
                    kuues.setText("");
                    aadress.setText("");
                    email.setText("");
                    telefon.setText("");
                    kommentaar.setText("");
                    vanem.setText("");

                }
                if (newValue.equals("Trenn")) {
                    esimene.setText("Toimumisaeg");
                    teine.setText("Rühm");
                    kolmas.setText("Asukoht");
                    neljas.setText("Juhendaja");
                    sisse_esimene.setPromptText("aaaa-kk-pp tt:mm");
                    sisse_teine.setPromptText("");

                }
                if (newValue.equals("Võistlus")) {
                    esimene.setText("Toimumisaeg");
                    kolmas.setText("Asukoht");
                    teine.setText("Nimi");
                    neljas.setText("Osalev rühm");
                    viies.setText("Tulemus");
                    grid.add(sisse_viies, 1, 9);
                    sisse_esimene.setPromptText("aaaa-kk-pp");
                    sisse_teine.setPromptText("");
                }
                if (newValue.equals("Õpilane")) {
                    if (!grid.getChildren().contains(sisse_neljas)) {
                        grid.add(sisse_neljas, 1, 8);
                    }
                    vanem.setText("Lapsevanem: ");
                    grid.add(olemasolu, 1, 1);

                    esimene.setText("Nimi");
                    sisse_esimene.setPromptText("Eesnimi Perenimi");
                    sisse_teine.setPromptText("");
                    kolmas.setText("Aadress");
                    neljas.setText("Telefon");
                    viies.setText("E-Mail");
                    teine.setText("Isikukood");
                    kuues.setText("Lapsevanem");
                    grid.add(sisse_kuues, 1, 10);
                    sisse_kuues.setPromptText("Eesnimi Perenimi");
                    grid.add(sisse_viies, 1, 9);


                    olemasolu.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                        public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
                            if (newValue.equals("Olemas")) {
                                grid.getChildren().removeAll(sisse_aadress, sisse_email, sisse_telefon, sisse_kommentaar);
                                aadress.setText("");
                                email.setText("");
                                telefon.setText("");
                                kommentaar.setText("");
                            }
                            if (newValue.equals("Ei ole")){
                                grid.getChildren().removeAll(sisse_aadress, sisse_email, sisse_telefon, sisse_kommentaar);
                                aadress.setText("Vanema aadress");
                                email.setText("Vanema e-Mail");
                                telefon.setText("Vanema telefon");
                                kommentaar.setText("Vanema kommentaar");
                                grid.add(sisse_aadress, 1, 11);
                                grid.add(sisse_telefon, 1, 12);
                                grid.add(sisse_email, 1, 13);
                                grid.add(sisse_kommentaar, 1, 14);
                            }

                        }});
                }
            }});
        lisa.setOnMouseClicked(event -> {
            if (teine.getText().equals("Rühm")){
                try {
                    andmebaas.sqlLisaTrenn(sisse_neljas.getText(), sisse_teine.getText(), sisse_kolmas.getText(), sisse_esimene.getText());

                    //õpilaste kohaolu märkimine
                    box.getChildren().clear(); //aken tühjaks
                    ScrollPane scroll = new ScrollPane();
                    GridPane kohalolu = new GridPane();
                    kohalolu.getStyleClass().add("grid-pane");

                    String [] õpilased = andmebaas.sqlRühmaAndmed(sisse_teine.getText()).split("\n"); //rühmas olevad õpilased
                    CheckBox[] kohalolijad = new CheckBox[õpilased.length];

                    for (int i = 0; i < õpilased.length; i ++){
                        CheckBox uus = new CheckBox(õpilased[i]);
                        kohalolijad[i] = uus;
                        kohalolu.add(uus, 1, i + 2);
                    }
                    Label pealkiri = new Label("Kohalolu");
                    kohalolu.add(pealkiri, 1, 0);
                    ToggleButton kinnita = new ToggleButton("Kinnita");
                    kinnita.getStyleClass().add("toggle-button-lisa");
                    kohalolu.add(kinnita, 1, õpilased.length + 3);
                    scroll.setContent(kohalolu);
                    box.getChildren().add(scroll);

                    String aeg = sisse_esimene.getText();
                    String rühm = sisse_teine.getText();
                    kinnita.setOnMouseClicked(eventK -> {
                        for (int i = 0; i < kohalolijad.length; i++){ //kontrollib iga õpilase puhul, kas oli kohalolijaks märgitud
                            if (kohalolijad[i].isSelected()){
                                try {
                                    andmebaas.sqlLisaKohalolu(kohalolijad[i].getText(), aeg, rühm);
                                }
                                catch (SQLException e){
                                    throw new RuntimeException(e);
                                }
                        }
                        box.getChildren().clear(); //pärast kohalolijate näitamist ekraan tühjaks
                    }});

                }
                catch (SQLException e){
                    throw new RuntimeException(e);
                }
                eemalda(sisend);
            }
            if (teine.getText().equals("Nimi")){
                try {
                    andmebaas.sqlLisaVõistlus(sisse_kolmas.getText(), sisse_esimene.getText(), sisse_teine.getText());
                    andmebaas.sqlLisaTulemus(sisse_teine.getText(), sisse_neljas.getText(), sisse_viies.getText());
                }catch (SQLException e){
                    throw new RuntimeException(e);
                }
            }
            if (teine.getText().equals("Isikukood") && !grid.getChildren().contains(sisse_email)){
                try {
                    andmebaas.sqlLisaÕpilane(sisse_esimene.getText(), sisse_kolmas.getText(), sisse_teine.getText(), sisse_viies.getText(), sisse_neljas.getText(), sisse_kuues.getText());
                }catch (SQLException e){
                    throw new RuntimeException(e);
                }
                eemalda(sisend);
            }
            if (teine.getText().equals("Isikukood") && grid.getChildren().contains(sisse_email)){
                try {
                    andmebaas.sqlLisaVanem(sisse_kuues.getText(), sisse_aadress.getText(), sisse_kommentaar.getText(), sisse_email.getText(), sisse_telefon.getText());
                    andmebaas.sqlLisaÕpilane(sisse_esimene.getText(), sisse_kolmas.getText(), sisse_teine.getText(), sisse_viies.getText(), sisse_neljas.getText(), sisse_kuues.getText());
                }catch (SQLException e){
                    throw new RuntimeException(e);
                }
                eemalda(sisend);
            }
        });
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(grid);
        box.getChildren().addAll(grid, scroll);
        return box;
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
                    throw new RuntimeException(e);
                }

            }});
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(tulemus);
        box.getChildren().addAll(grid, scroll);

        return box;
    }

}
