module com.example.socialnetworkmap {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.example.socialnetworkmap to javafx.fxml;
    opens com.example.socialnetworkmap.controller to javafx.fxml;
    exports com.example.socialnetworkmap;
    //exports com.example.socialnetworkmap.controller;

}