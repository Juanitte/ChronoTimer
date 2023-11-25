module com.juanite {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires jlayer;

    opens com.juanite to javafx.fxml;
    exports com.juanite;
    exports com.juanite.controller;
    opens com.juanite.controller to javafx.fxml;
}
