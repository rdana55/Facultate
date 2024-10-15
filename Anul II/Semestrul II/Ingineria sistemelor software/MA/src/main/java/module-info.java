module ro.iss.ma {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens ro.iss.ma.controller to javafx.fxml;
    opens ro.iss.ma.domain to javafx.base;

    exports ro.iss.ma;
}