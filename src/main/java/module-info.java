module org.example.laxilab {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.laxilab to javafx.fxml;
    exports org.example.laxilab;
    exports org.example.laxilab.Controller;
    exports org.example.laxilab.Modal;
}