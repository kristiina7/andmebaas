import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;

public class Aktiivsus {
    private GridPane grid = new GridPane();

    public GridPane annaAktiivsus(){
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