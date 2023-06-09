package ProjectDemo.Server.GUI;

import ProjectDemo.Server.Database.Database;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CellFormat extends ListCell<Integer> {
    Database database = Database.getDatabase();
    @Override
    protected void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);
        if(item != null) {
            String str = Integer.toString(item + 1);
            setText("Sensor " + str);
            setFont(Font.font("System", FontWeight.BOLD, 20));
            setAlignment(Pos.BASELINE_CENTER);
            setTextFill(database.getStatus(item).equals("RUNNING") ? Color.GREEN : Color.ORANGERED);
        }
        else {
            setGraphic(null);
            setTextFill(Color.TRANSPARENT);
        }
    }
}

