package tbm.com.transportbrokermethod;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
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
    int cur_pos_x;
    int cur_pos_y;

    @FXML
    private Label title_label;

    @FXML
    private TextField supplier_input_field;

    @FXML
    private TextField receiver_input_field;

    @FXML
    private Button add_sup_rec_button;



    @FXML
    private TableView<ObservableList<String>> table_input = new TableView<>();

    @FXML
    private TableColumn<InputData,String> col;

    @FXML
    void cosik(MouseEvent event)
    {


    }


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

        int size_rec = parseInt(receiver_input_field.getText());
        int size_sup = parseInt(supplier_input_field.getText());

        X=size_sup;
        Y=size_rec;

        int arr_size=Math.max(size_rec, size_sup);

        input_arr = new int[size_sup][size_rec];




        //adding columns(receivers)
        table_input.getColumns().clear();


        ArrayList<String> columnNames = new ArrayList<>();

        for (int i = 0; i <= size_rec; i++)
        {

            String name;
            if(i==0)
            {
                 name = "D/O";
            }
            else
            {
                 name = "O-" + (i);
            }
            columnNames.add(name);

            final int finalIdx = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames.get(i));
            column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx)));

            table_input.getColumns().add(column);

        }


        // adding rows
        int counter =0;
        for (int i = 0; i < size_sup; i++)
        {
            ArrayList<String> a = new ArrayList<>();
            String name = "D-"+(i+1);
            a.add(name);

            for(int j=0;j<size_rec;j++)
            {
                a.add(counter+"");
                counter++;
            }

           // for(int j=0;j<a.size();j++)
                //System.out.println(a.get(j));

            table_input.getItems().add(FXCollections.observableArrayList(a));
        }
        //table_input.setItems(this.list);

        grid_panel.setVisible(true);
    }

    @FXML
    void load_data(ActionEvent event)
    {
        for (int i=0;i<table_input.getItems().size();i++)
        {
            System.out.println();
            for(int j=0;j<table_input.getColumns().size();j++)
            {
                System.out.print(table_input.getItems().get(i).get(j)+" ");
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
                for (TablePosition pos : selectedCells)
                {
                    //System.out.println("Cell selected in row "+pos.getRow()+" and column "+pos.getTableColumn().getText());
                    cur_pos_x=pos.getRow();
                    cur_pos_y=pos.getColumn();
                }
            };

        });
    }


    @FXML
    void add_grid_value(ActionEvent event)
    {


            String value = grid_value.getText();

            ObservableList<String> a = table_input.getItems().get(cur_pos_x);
            a.set(cur_pos_y, value);
            System.out.println(cur_pos_x + " " + cur_pos_y);
            table_input.getItems().set(cur_pos_x, FXCollections.observableArrayList(a));



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

        table_input.getSelectionModel().setCellSelectionEnabled(true);
        table_input.setEditable(true);



    }




}
