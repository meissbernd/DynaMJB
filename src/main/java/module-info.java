module com.example.dynamjb {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens com.example.dynamjb to javafx.fxml;
    exports com.example.dynamjb;
}