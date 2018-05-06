import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

//Klass popup akna jaoks
public class Hoiatus {
public static void aken(String teade){

    Stage popupwindow=new Stage();

    popupwindow.initModality(Modality.APPLICATION_MODAL);

    popupwindow.setTitle("Hoiatusaken");
    Label label1= new Label(teade);
    Button button1= new Button("OK");

    button1.setOnAction(e -> popupwindow.close());

    VBox layout= new VBox(10);
    layout.getChildren().addAll(label1, button1);
    layout.setAlignment(Pos.CENTER);
    Scene scene1= new Scene(layout, 300, 100);
    popupwindow.setScene(scene1);
    popupwindow.showAndWait();
}
}
