package tbm.com.transportbrokermethod;

import java.util.ArrayList;

public class InputData {

    private String supplier;
    private ArrayList<String> receivers = new ArrayList<String>();

    public InputData(String name, ArrayList<String> list)
    {
        this.supplier = name;
     //   this.receivers = list;

    }

    public String getSupplier() {
        return supplier;
    }


    public ArrayList<String> getReceivers() {
        return receivers;

    }



    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public void setReceivers(ArrayList<String> receivers) {
       this.receivers = receivers;
    }
}
