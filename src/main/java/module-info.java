module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.dyna.demo to javafx.fxml;
    exports com.dyna.demo;
}