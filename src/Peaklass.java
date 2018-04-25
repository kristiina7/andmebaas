import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;

public class Peaklass extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage pealava) throws SQLException{
        BorderPane piirid = new BorderPane(); //üldine paigutus

        //logo
        ImageView logo = new ImageView();
        File fail = new File("triiniks_valge.png");
        Image pilt = new Image(fail.toURI().toString());
        logo.setImage(pilt);
        logo.setFitWidth(150);
        logo.setPreserveRatio(true);

        //menüü vasakul
        VBox menüü = new VBox(10);
        menüü.getStyleClass().add("v-box");
        Label otsimine = new Label("Otsi");
        Label lisamine = new Label("Lisa");
        Label aktiivsus = new Label("Aktiivsus"); //rühma põhjal (annab nimekirja ja kohalolud)
        Label saavutused = new Label("Saavutused"); //hooaja põhjal
        menüü.getChildren().addAll(logo, otsimine, lisamine, aktiivsus, saavutused);
        piirid.setLeft(menüü);

        //menüü värvi muutmine kui hiirega peale liikuda
        otsimine.setOnMouseEntered(event -> otsimine.getStyleClass().add("label-event"));
        lisamine.setOnMouseEntered(event -> lisamine.getStyleClass().add("label-event"));
        aktiivsus.setOnMouseEntered(event -> aktiivsus.getStyleClass().add("label-event"));
        saavutused.setOnMouseEntered(event -> saavutused.getStyleClass().add("label-event"));

        //värv tagasi kui hiirega ära liikuda
        otsimine.setOnMouseExited(event -> {otsimine.getStyleClass().clear(); otsimine.getStyleClass().add("label");});
        lisamine.setOnMouseExited(event -> {lisamine.getStyleClass().clear(); lisamine.getStyleClass().add("label");});
        aktiivsus.setOnMouseExited(event -> {aktiivsus.getStyleClass().clear(); aktiivsus.getStyleClass().add("label");});
        saavutused.setOnMouseExited(event -> {saavutused.getStyleClass().clear(); saavutused.getStyleClass().add("label");});

        //andmebaas
        Andmebaas andmebaas = new Andmebaas();

        Tegevused tegevus = new Tegevused(andmebaas);

        //kui vajutada otsing
        //otsimine.setOnMouseClicked(event -> piirid.setCenter(tegevus.annaOtsing())); //kas saab kuidagi nii, et laseks exceptioni throwida?
        otsimine.setOnMouseClicked(event ->{
            try {
                piirid.setCenter(tegevus.annaOtsing());
            }
            catch (SQLException e){
            }
        });

        //kui vajutada lisa
        lisamine.setOnMouseClicked(event-> piirid.setCenter(tegevus.annaLisamine()));

        //kui vajutada aktiivsus
        aktiivsus.setOnMouseClicked(event-> piirid.setCenter(tegevus.annaAktiivsus()));

        //kui vajutadas saavutused
        saavutused.setOnMouseClicked(event-> piirid.setCenter(tegevus.annaSaavutused()));

        //taust
        StackPane taust = new StackPane();
        taust.setId("pane");

        //taust ja borderpane
        StackPane stack = new StackPane();
        stack.getChildren().addAll(taust, piirid);


        Scene stseen = new Scene(stack, 800, 500);
        stseen.getStylesheets().addAll(getClass().getResource("stylesheet.css").toExternalForm()); //stiilid

        pealava.setScene(stseen);
        pealava.show();
        andmebaas.sulgeConnection(); //kuhu oleks mõistlik see panna?
    }
}