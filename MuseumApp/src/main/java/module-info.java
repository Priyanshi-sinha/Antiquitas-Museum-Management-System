module com.museum {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    opens com.museum to javafx.fxml, javafx.graphics;
    opens com.museum.controller to javafx.fxml;
    opens com.museum.model to javafx.fxml;

    exports com.museum;
    exports com.museum.controller;
    exports com.museum.model;
    exports com.museum.context;
    exports com.museum.data;
    exports com.museum.util;
}
