package tbm.com.transportbrokermethod;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class MainPageController implements Initializable {

    ObservableList<InputData> list = FXCollections.observableArrayList();

    @FXML
    private Label title_label;

    @FXML
    private TextField supplier_input_field;

    @FXML
    private TextField receiver_input_field;

    @FXML
    private Button add_sup_rec_button;



    @FXML
    private TableView<InputData> table_input;

    @FXML
    private TableColumn<InputData,String> D_O;

    @FXML
    private TableColumn<InputData,String> col;




    @FXML
    private Button delete_activity;

    @FXML
    private Button load_data;

    @FXML
    private HBox input_panel;

    @FXML
    private TextField activity_input_field;

    @FXML
    private TextField time_input_field;

    @FXML
    private TextField sequence_input_field;


    @FXML
    void add_sup_rec(ActionEvent event)
    {

        double max_size = table_input.getMinWidth()-D_O.getPrefWidth();
        int size_rec = parseInt(receiver_input_field.getText());
        int size_sup = parseInt(supplier_input_field.getText());
        int arr_size=Math.max(size_rec, size_sup);
        System.out.println(arr_size);


        //adding columns(receivers)
        table_input.getColumns().clear();
        table_input.getColumns().add(D_O);

        for (int i = 0; i < size_rec; i++)
        {
            String name = "O-"+(i+1);
            col = new TableColumn(name);
            col.setPrefWidth(max_size/size_rec);

            table_input.getColumns().add(col);

            col.setCellFactory(TextFieldTableCell.forTableColumn());
            //col.setCellValueFactory(new PropertyValueFactory<InputData,String>(name));

            //table_input.getColumns().add(col);
        }


        // adding rows

        for (int i = 0; i < size_sup; i++)
        {
            //add row to list
            ArrayList<String> temp = new ArrayList<>();
            int name_id = list.size();
            String name = "D-"+(name_id+1);

            for (int j = 0; j< size_rec; j++)
            {
                temp.add("0");
            }

            InputData a = new InputData(name,temp);

            this.list.add(a);


        }
        table_input.setItems(this.list);

    }

    @FXML
    void load_data(ActionEvent event)
    {
        for (int i=0;i<list.size();i++)
        {
            System.out.println(list.get(i).getSupplier());
            for(int j=0;j<list.get(i).getReceivers().size();j++)
            {
                System.out.print(list.get(i).getReceivers().get(j));
            }
            System.out.println();
        }
    }

    @FXML
    void update_input_data(MouseEvent event)
    {
        final ObservableList<TablePosition> selectedCells = table_input.getSelectionModel().getSelectedCells();
        selectedCells.addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(Change change) {
                for (TablePosition pos : selectedCells) {
                    //System.out.println("Cell selected in row "+pos.getRow()+" and column "+pos.getTableColumn());
                    TablePosition posit = table_input.getSelectionModel().getSelectedCells().get(0);
                    int row = pos.getRow();
                    InputData item = table_input.getItems().get(row);
                    TableColumn col = pos.getTableColumn();
                    String data = (String) col.getCellObservableValue(item).getValue();
                    System.out.println(data);

                }
            }
        });
    }


    /*
    @FXML
    void delete_activity(ActionEvent event)
    {
        ObservableList<Activity> selectedRows = table_input.getSelectionModel().getSelectedItems();

        ArrayList<Activity> rows = new ArrayList<>(selectedRows);
        rows.forEach(row -> table_input.getItems().remove(row));
    }

    @FXML
    void changeActivityTime(TableColumn.CellEditEvent cell)
    {
        Activity selected = table_input.getSelectionModel().getSelectedItem();
        selected.setTime(cell.getNewValue().toString());

        int id = table_input.getSelectionModel().getSelectedIndex();
        list.set(id,table_input.getItems().get(id));
    }

    @FXML
    void changeActivitySequnce(TableColumn.CellEditEvent cell)
    {
        Activity selected = table_input.getSelectionModel().getSelectedItem();
        selected.setSequence(cell.getNewValue().toString());

        int id = table_input.getSelectionModel().getSelectedIndex();
        list.set(id,table_input.getItems().get(id));


    }


     */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        table_input.getSelectionModel().setCellSelectionEnabled(true);

        D_O.setStyle( "-fx-alignment: CENTER");

        table_input.setEditable(true);

        D_O.setCellFactory(TextFieldTableCell.forTableColumn());
        D_O.setCellValueFactory(new PropertyValueFactory<InputData,String>("supplier"));

        table_input.setItems(list);

    }




}
