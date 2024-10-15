module ro.server {
    requires ro.services;
    requires ro.model;
    requires ro.persistence;
    requires java.rmi;
    requires ro.networking;
    exports ro.server;
}
