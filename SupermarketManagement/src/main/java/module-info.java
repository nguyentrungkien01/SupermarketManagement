module com.ou.supermarketmanagement {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.ou.supermarketmanagement to javafx.fxml;
    exports com.ou.supermarketmanagement;
}
