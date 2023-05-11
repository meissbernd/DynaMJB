module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.dyna.main to javafx.fxml;
    exports com.dyna.main;
    exports com.dyna.controller;
    opens com.dyna.controller to javafx.fxml;
}