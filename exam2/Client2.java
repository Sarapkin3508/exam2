package master;

public class Client2 {

        public static String ipAddr = "localhost";
        public static int port = 8180;


        public static void main(String[] args) {
            ChatClient client =  new ChatClient(ipAddr, port);
            client.startClient();
        }
    }

