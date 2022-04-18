package tbm.com.transportbrokermethod;

import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class MainPageController implements Initializable {

    ObservableList<InputData> list = FXCollections.observableArrayList();
    int input_arr[][];
    int counter =0;
    int X=0;
    int Y=0;
    private ArrayList<String> rec_tem = new ArrayList<String>();

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
    private Pane grid_panel;

    @FXML
    private Label D_label;

    @FXML
    private Label O_label;

    @FXML
    private TextField grid_value;

    @FXML
    void add_sup_rec(ActionEvent event)
    {


        double max_size = table_input.getMinWidth()-D_O.getPrefWidth();
        int size_rec = parseInt(receiver_input_field.getText());
        int size_sup = parseInt(supplier_input_field.getText());

        X=size_sup;
        Y=size_rec;

        int arr_size=Math.max(size_rec, size_sup);

        input_arr = new int[size_sup][size_rec];




        //adding columns(receivers)
        table_input.getColumns().clear();
        table_input.getColumns().add(D_O);

        for (int i = 0; i < size_rec; i++)
        {
            String name = "O-"+(i);
            //col = new TableColumn(name);
            //col.setPrefWidth(max_size/size_rec);

            TableColumn<InputData, String> col = new TableColumn<>(name);

            table_input.getColumns().add(col);

        }


        // adding rows


        for (int i = 0; i < size_sup; i++)
        {
            //add row to list
            ArrayList<String> temp = new ArrayList<>();
            int name_id = list.size();
            String name = "D-"+(name_id);

            for (int j = 0; j< size_rec; j++)
            {
                temp.add("0");
            }

            InputData a = new InputData(name,temp);

            this.list.add(a);


        }
        table_input.setItems(this.list);

        grid_panel.setVisible(true);
    }

    @FXML
    void load_data(ActionEvent event)
    {
        for (int i=0;i<list.size();i++)
        {
            System.out.println(list.get(i).getSupplier_name());
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

    }

    @FXML
    void add_grid_value(ActionEvent event)
    {
        int x = counter/Y;
        int y = counter%Y;

        if(y==Y-1)
        {
            rec_tem.clear();
        }

        input_arr[x][y]=parseInt(grid_value.getText());
        rec_tem.add(grid_value.getText());
        grid_value.clear();


        O_label.setText("O-" + (y + 1));
        D_label.setText("D-" + (x));

        //table_input.setItems().set(x,new InputData("D-"+x,rec_tem));
       // list.get(x).getReceivers().add(grid_value.getText());
       // table_input.setItems(list);


        counter++;


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

        grid_panel.setVisible(false);
        O_label.setText("O-0");
        D_label.setText("D-0");

        table_input.getSelectionModel().setCellSelectionEnabled(true);

        D_O.setStyle( "-fx-alignment: CENTER");

        table_input.setEditable(true);

        D_O.setCellFactory(TextFieldTableCell.forTableColumn());
        D_O.setCellValueFactory(new PropertyValueFactory<InputData,String>("supplier"));


        table_input.setItems(list);

    }




}
