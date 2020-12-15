package exam2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    private static Socket clientSocket;
    private static ServerSocket server;
    private static BufferedReader in;
    private static BufferedWriter out;

    public static void main(String[] args) {
        try {
            try {
                server = new ServerSocket(4004);
                System.out.println("Сервер подключен");
                server.accept();

                try {

                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                    String word = in.readLine();
                    System.out.println(word);

                    System.out.println("Сервер принял слово: " + word + "\n");
                    out.flush();
                } finally {


                    clientSocket.close();
                    out.close();
                    in.close();

                }

            }finally {
                    System.out.println("Сервер закрыт!");
                    server.close();
                }


            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }
