module org.example.taximetrie {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens org.example.taximetrie to javafx.fxml;
    exports org.example.taximetrie;
    exports org.example.taximetrie.controller;
    opens org.example.taximetrie.controller to javafx.fxml;
}