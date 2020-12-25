package master;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;


    public class Server {

        public static final int port = 8180;
        public static LinkedList<ChatServer> serverList = new LinkedList<>(); // список всех нитей - экземпляров

        public static void main(String[] args) throws IOException {
            ChatServer server = new ChatServer(port);
            server.startServer();

        }
    }

