import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class Saavutused {
    private GridPane grid = new GridPane();
    private ObservableList<String> valitavad = FXCollections.observableArrayList("2017/18", "2016/17", "2015/16");
    private ComboBox<String> valikud = new ComboBox<>(valitavad);
    private Label hooaeg = new Label("Hooaeg");

    public GridPane annaSaavutused(){
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
}
