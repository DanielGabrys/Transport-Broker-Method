package tbm.com.transportbrokermethod;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static java.lang.Integer.parseInt;

public class MainPageController implements Initializable
{


    int input_arr[][];
    int cur_pos_x;
    int cur_pos_y;

    static  int col=0;
    static int row=0;

    static int[][] result;

    @FXML
    private TableView<ObservableList<String>> table_input = new TableView<>();

    @FXML
    ObservableList<Supplier> D_list = FXCollections.observableArrayList();

    @FXML
    ObservableList<Receiver> O_list = FXCollections.observableArrayList();

    @FXML
    private TableView<ObservableList<String>> result_table = new TableView<>();
    @FXML
    private TableView<ObservableList<String>> result_table2 = new TableView<>();

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
    private Text buying_cost_final;

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




    @FXML
    void add_sup_rec(ActionEvent event)
    {


        int size_rec = parseInt(receiver_input_field.getText());
        int size_sup = parseInt(supplier_input_field.getText());

        col = size_rec;
        row =size_sup;
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

        int[] provider = new int[row];
        int[] recipient = new int[col];
        float[][] profits = new float[row][col];

        /*
        provider[0] = 20;
        provider[1] = 30;
        recipient[0] = 10;
        recipient[1] = 28;
        recipient[2] = 27;
        profits[0][0] = 12.F;
        profits[0][1] = 1.F;
        profits[0][2] = 3.F;
        profits[1][0] = 6.F;
        profits[1][1] = 4.F;
        profits[1][2] = -1.F;
        */


        for (int i=0;i<table_input.getItems().size();i++)
        {
            provider[i]= parseInt(D_input.getItems().get(i).getCost());

            System.out.print(provider[i]+" ");
        }
        System.out.println();

        for(int i=0;i<table_input.getColumns().size()-1;i++)
        {
            recipient[i]= parseInt(O_input.getItems().get(i).getDemand());

            System.out.print(recipient[i]+" ");
        }
        System.out.println();

        for (int i=0;i<table_input.getItems().size();i++)
        {

            for(int j=0;j<table_input.getColumns().size()-1;j++)
            {
                int value = parseInt(O_input.getItems().get(j).getCost()) - input_arr[i][j]-parseInt(D_input.getItems().get(i).getSupply());
                //System.out.println(O_input.getItems().get(j).getCost()+" "+input_arr[i][j]+" "+D_input.getItems().get(i).getSupply());
                profits[i][j] = value;
                System.out.print(profits[i][j]+" ");
            }
            System.out.println();
        }

        System.out.println();


        result = tbm.com.algorithm.TBMAlgorithm.compute(provider, recipient, profits);


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


        final_results();

        /*
        Parent root;
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("result.fxml"));
            Stage stage = new Stage();
            stage.setTitle("TBM Result");
            stage.setScene(new Scene(fxmlLoader.load(), 900, 600));
            stage.show();
            // Hide this current window (if this is what you want)

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        */



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
        result_table.setVisible(false);
        result_table2.setVisible(false);


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


    int[][] get_result()
    {
        return result;
    }

    void final_results()
    {


    for (int i = 0; i < result.length; i++)
    {
        for (int j = 0; j < result[0].length; j++)
        {
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

        double size = table_input.getPrefWidth()/(col +1);
        final int finalIdx = i;
        TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames.get(i));
        column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx)));
        column.setPrefWidth(size);

        result_table.getColumns().add(column);
        //result_table2.getColumns().add(column);

    }

    for (int i = 0; i < result.length; i++)
    {
        ArrayList<String> a = new ArrayList<>();
        ArrayList<String> b = new ArrayList<>();

        String name2;
        if (i >= row) {
            name2 = "DF-" + (i - row);
        } else {
            name2 = "D-" + (i + 1);
        }

        a.add(name2);
        b.add(name2);

        for (int j = 0; j < col; j++)
        {
            a.add(String.valueOf(result[i][j]));
           // b.add(String.valueOf(result[i][j]));
        }

        result_table.setVisible(true);
        result_table2.setVisible(true);

        result_table.getItems().add(FXCollections.observableArrayList(a));
        //result_table2.getItems().add(FXCollections.observableArrayList(b));

        //calculate_buying_cost();
        calculate_transport_cost();
       // calculate_income();
       // calculate_final_income();

    }
}


    int calculate_transport_cost()
    {
        int sum=0;

        for(int i=0; i<result.length; i++)
        {
            for(int j=0; j<result[0].length; j++)
            {
                if(i<row && j<col)
                {
                    sum += result[i][j] * input_arr[i][j];
                    System.out.print(result[i][j]+"*"+input_arr[i][j]+"\n");
                }
            }
            System.out.print(sum+"\n");
        }

        cost.setText(String.valueOf(sum));
        return sum;
    }

    int calculate_buying_cost()
    {
        int sum=0;

        for(int i=0; i<result.length; i++)
        {
            for(int j=0; j<result[0].length; j++)
            {
                    sum += parseInt(D_input.getItems().get(i).getSupply())*result[i][j];
            }

        }
        buying_cost_final.setText(String.valueOf(sum));
        return sum;
    }

    int calculate_income()
    {
        int sum=0;

        for(int i=0; i<result.length; i++)
        {
            for(int j=0; j<result[0].length; j++)
            {
                sum += parseInt(O_input.getItems().get(i).getCost())*result[i][j];
            }

        }
        income.setText(String.valueOf(sum));
        return sum;
    }

    int calculate_final_income()
    {
        int income= calculate_income()-calculate_transport_cost()-calculate_buying_cost();
        final_income.setText(String.valueOf(income));

        return income;
    }

}
