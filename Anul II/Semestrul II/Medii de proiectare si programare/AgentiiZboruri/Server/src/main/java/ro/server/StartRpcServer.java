package ro.server;

import ro.networking.ServicesImpl;
import ro.persistence.*;
import ro.networking.utils.AbstractServer;
import ro.networking.utils.RpcConcurrentServer;
import ro.services.Services;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties "+e);
            return;
        }
        DbUtils dbUtils = new DbUtils(serverProps);

        AngajatDbRepo angajatDbRepo=new AngajatDbRepo(dbUtils);
        PersoanaDbRepo persoanaDbRepo=new PersoanaDbRepo(dbUtils);
        ZborDbRepo zborDbRepo=new ZborDbRepo(dbUtils);
        BiletDbRepo biletDbRepo=new BiletDbRepo(dbUtils, persoanaDbRepo, zborDbRepo, angajatDbRepo);
        Services servicesImpl=new ServicesImpl(angajatDbRepo, biletDbRepo, zborDbRepo);

        int ServerPort=defaultPort;
        try {
            ServerPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+ServerPort);
        AbstractServer server = new RpcConcurrentServer(ServerPort, servicesImpl);
        try {
            server.start();
        } catch (ro.networking.utils.ServerException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                server.stop();
            } catch (ro.networking.utils.ServerException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
