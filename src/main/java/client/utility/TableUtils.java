package client.utility;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableUtils {
    public static <T, U> void setupColumn(TableColumn<T, U> column, String propertyName) {
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
    }
}
