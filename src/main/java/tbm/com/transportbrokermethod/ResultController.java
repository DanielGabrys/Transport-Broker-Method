package tbm.com.transportbrokermethod;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ResultController implements Initializable
{
        int[][] result = MainPageController.result;
        int col = MainPageController.col;
        int row = MainPageController.row;

        private TableView<ObservableList<String>> table_input = new TableView<>();
        ObservableList<Supplier> D_list = FXCollections.observableArrayList();
        ObservableList<Receiver> O_list = FXCollections.observableArrayList();


        @FXML
        private Text buying_cost;

        @FXML
        private Label buying_cost_label;

        @FXML
        private Text cost;

        @FXML
        private Label cost_label;

        @FXML
        private Text final_income;

        @FXML
        private Label final_incone_label;

        @FXML
        private Text income;

        @FXML
        private Label income_label;

        @Override
        public void initialize(URL location, ResourceBundle resources)
        {

                table_input.getSelectionModel().setCellSelectionEnabled(true);

                for (int i = 0; i < result.length; i++) {
                        for (int j = 0; j < result[0].length; j++) {
                                System.out.print(result[i][j] + " ");
                        }
                        System.out.print("\n");
                }

                ArrayList<String> columnNames = new ArrayList<>();

                for (int i = 0; i < result[0].length; i++) {

                        String name;
                        if (i == 0) {
                                name = "D/O";
                        }
                        if (i > col) {
                                name = "OF" + (i - col);
                        } else {
                                name = "O-" + (i);
                        }
                        columnNames.add(name);

                        final int finalIdx = i;
                        TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames.get(i));
                        column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx)));

                        table_input.getColumns().add(column);

                }

                for (int i = 0; i < result.length; i++)
                {
                        ArrayList<String> a = new ArrayList<>();

                        String name2;
                        if (i >= row) {
                                name2 = "DF" + (i - row);
                        } else {
                                name2 = "D-" + (i + 1);
                        }

                        a.add(name2);

                        for (int j = 0; j < col; j++) {
                                a.add(String.valueOf(result[i][j]));
                        }

                        table_input.getItems().add(FXCollections.observableArrayList(a));


                }
                System.out.println("elo");
        }

}

