package tbm.com.transportbrokermethod;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class InputData {

    private String supplier_name;
    private ArrayList<String> receivers;

    public InputData(String name, ArrayList<String> list)
    {
        this.supplier_name=name;
        this.receivers=list;

    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public ArrayList<String> getReceivers() {
        return receivers;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public void setReceivers(ArrayList<String> receivers) {
        this.receivers = receivers;
    }
}
