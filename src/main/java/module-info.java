module tbm.com.transportbrokermethod {
    requires javafx.controls;
    requires javafx.fxml;


    opens tbm.com.transportbrokermethod to javafx.fxml;
    exports tbm.com.transportbrokermethod;
}