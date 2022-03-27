module com.ou.controllers {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.ou.controllers to javafx.fxml;
    exports com.ou.controllers;
}
