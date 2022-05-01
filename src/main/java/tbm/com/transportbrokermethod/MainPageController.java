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
import java.util.*;

import static java.lang.Integer.parseInt;

public class MainPageController implements Initializable {


    int input_arr[][];
    int cur_pos_x;
    int cur_pos_y;

    @FXML
    private TableView<ObservableList<String>> table_input = new TableView<>();

    ObservableList<Supplier> D_list = FXCollections.observableArrayList();

    ObservableList<Receiver> O_list = FXCollections.observableArrayList();

    @FXML
    private Label title_label;

    @FXML
    private TextField supplier_input_field;

    @FXML
    private TextField receiver_input_field;

    @FXML
    private Button add_sup_rec_button;

    @FXML
    private Button load_data;


    @FXML
    private TableView<Supplier> D_input;

    @FXML
    private TableColumn<Supplier, String> buying_cost;

    @FXML
    private TableColumn<Supplier, String> supplier;

    @FXML
    private TableColumn<Supplier, String> supply;


    @FXML
    private TableView<Receiver> O_input;


    @FXML
    private TableColumn<Receiver, String> receiver;

    @FXML
    private TableColumn<Receiver, String> demand;

    @FXML
    private TableColumn<Receiver, String> sell_cost;


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
        //System.out.println(size_rec+" "+size_sup);

        double size = table_input.getPrefWidth()/(size_rec +1);
        input_arr = new int[size_sup][size_rec];
        for(int i =0;i<size_sup;i++)
        {
            for(int j=0;j<size_rec;j++)
            {
                input_arr[i][j]=0;
            }
        }

        //adding columns
        ArrayList<String> columnNames = new ArrayList<>();

        //clearing data
        table_input.getColumns().clear();
        table_input.getItems().clear();

        D_input.getItems().clear();
        O_input.getItems().clear();

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
            column.setPrefWidth(size);

            table_input.getColumns().add(column);


            if(i!=0)
            {
                //adding receiver table
                Receiver r = new Receiver(name, "-", "-");
                O_list.add(r);
                O_input.setItems(O_list);
            }

        }

        // adding rows
        for (int i = 0; i < size_sup; i++) {
            ArrayList<String> a = new ArrayList<>();
            String name = "D-" + (i + 1);
            a.add(name);

            for (int j = 0; j < size_rec; j++) {
                a.add("-");
            }

            table_input.getItems().add(FXCollections.observableArrayList(a));

            //adding supplier table
            Supplier s = new Supplier(name, "-", "-");
            D_list.add(s);
            D_input.setItems(D_list);


            grid_panel.setVisible(true);
            D_input.setVisible(true);
            O_input.setVisible(true);
            load_data.setVisible(true);
        }
    }

    @FXML
    void load_data(ActionEvent event)
    {

        System.out.println("Koszty transportu");
        for (int i=0;i<table_input.getItems().size();i++)
        {

            for(int j=0;j<table_input.getColumns().size()-1;j++)
            {
                System.out.print(input_arr[i][j]+" ");
            }
            System.out.println();
        }

        System.out.println("Dostawca");
        for (int i=0;i<D_input.getItems().size();i++)
        {
            System.out.print(D_input.getItems().get(i).getName() + " ");
            System.out.print(D_input.getItems().get(i).getCost() + " ");
            System.out.println(D_input.getItems().get(i).getSupply() + " ");
        }

        System.out.println("Odbiorca");
        for (int i=0;i<O_input.getItems().size();i++)
        {

            System.out.print(O_input.getItems().get(i).getName() + " ");
            System.out.print(O_input.getItems().get(i).getDemand() + " ");
            System.out.println(O_input.getItems().get(i).getCost() + " ");

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
    void change_cost(TableColumn.CellEditEvent cell)
    {
        Supplier selected = D_input.getSelectionModel().getSelectedItem();
        selected.setSupply(cell.getNewValue().toString());
    }

    @FXML
    void change_supply(TableColumn.CellEditEvent cell)
    {
        Supplier selected = D_input.getSelectionModel().getSelectedItem();
        selected.setCost(cell.getNewValue().toString());
    }


    @FXML
    void add_grid_value(ActionEvent event)
    {

            String value = grid_value.getText();

            //output array
            input_arr[cur_pos_x][cur_pos_y-1]=parseInt(value);


            ObservableList<String> a = table_input.getItems().get(cur_pos_x);
            a.set(cur_pos_y, value);
            System.out.println(cur_pos_x + " " + cur_pos_y);
            table_input.getItems().set(cur_pos_x, FXCollections.observableArrayList(a));

    }

    @FXML
    void change_demand(TableColumn.CellEditEvent cell)
    {
        Receiver selected = O_input.getSelectionModel().getSelectedItem();
        selected.setDemand(cell.getNewValue().toString());
    }

    @FXML
    void change_sell_cost(TableColumn.CellEditEvent cell)
    {
        Receiver selected = O_input.getSelectionModel().getSelectedItem();
        selected.setCost(cell.getNewValue().toString());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        grid_panel.setVisible(false);
        D_input.setVisible(false);
        O_input.setVisible(false);
        load_data.setVisible(false);


        table_input.getSelectionModel().setCellSelectionEnabled(true);
        table_input.setEditable(true);
        D_input.setEditable(true);
        O_input.setEditable(true);

        //supplier
        supplier.setCellFactory(TextFieldTableCell.forTableColumn());
        supply.setCellFactory(TextFieldTableCell.forTableColumn());
        buying_cost.setCellFactory(TextFieldTableCell.forTableColumn());

        supplier.setCellValueFactory(new PropertyValueFactory<Supplier,String>("name"));
        supply.setCellValueFactory(new PropertyValueFactory<Supplier, String>("supply"));
        buying_cost.setCellValueFactory(new PropertyValueFactory<Supplier, String>("cost"));

        //receiver
        receiver.setCellFactory(TextFieldTableCell.forTableColumn());
        demand.setCellFactory(TextFieldTableCell.forTableColumn());
        sell_cost.setCellFactory(TextFieldTableCell.forTableColumn());

        receiver.setCellValueFactory(new PropertyValueFactory<Receiver,String>("name"));
        demand.setCellValueFactory(new PropertyValueFactory<Receiver, String>("demand"));
        sell_cost.setCellValueFactory(new PropertyValueFactory<Receiver, String>("cost"));



    }




}
