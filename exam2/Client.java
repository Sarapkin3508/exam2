package master;

import example.ClientChat;

public class Client {

        public static String ipAddr = "localhost";
        public static int port = 8180;

        public static void main(String[] args) {
           ChatClient client =  new ChatClient(ipAddr, port);
            client.startClient();

        }
    }

