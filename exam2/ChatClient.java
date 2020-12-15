package exam2;

import java.io.*;
import java.net.Socket;

public class ChatClient {
    private static Socket clientSocket;
    private static BufferedReader reader;
    private static BufferedWriter out;
    private static BufferedReader in;

    public static void main(String[] args) {
        try {
            try {

            clientSocket = new Socket("localhost", 4004);

            reader = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            System.out.println("Введите слово: ");

            String word = in.readLine();

            out.write(word + "\n");
            out.flush();

            String line = in.readLine();
            System.out.println(line);


            } finally {
                System.out.println("Клиент закрыт!");
                clientSocket.close();
                out.close();
                in.close();
            }




        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


}
