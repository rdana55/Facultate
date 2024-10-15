module ro.clientfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires ro.model;
    requires ro.server;
    requires ro.networking;
    requires ro.services;
    requires ro.persistence;
    opens ro.clientfx to javafx.fxml;
    exports ro.clientfx;
}