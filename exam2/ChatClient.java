package master;

import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


public class ChatClient {

    private Socket socket;
    private ObjectOutputStream out;
    // private BufferedWriter outString;
    private ObjectInputStream in;
    private BufferedReader inputUser;
    private String addr;
    private int port;
    private Connection connection;
    private String nickname;
    private Date time;
    private String dtime;
    private SimpleDateFormat dt1;


    public ChatClient(String addr, int port) {
        inputUser = new BufferedReader(new InputStreamReader(System.in));
        this.nickname = pressNickname();
        try {
            connection = new Connection(new Socket(addr, port));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private String pressNickname() {
        System.out.print("Press your nick: ");
        try {
            nickname = inputUser.readLine();
            System.out.println("Hello " + nickname);
        } catch (IOException ignored) {
        }

        return nickname;
    }

    

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                inputUser.close();
            }
        } catch (IOException ignored) {}
    }
    
    private class ReaderMessage extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    if (connection.getSender() != this.getName()) {
                        Message message = (Message) connection.getInput().readObject();
                        message.setTime();

                        System.out.println(message.getSender() + " ("
                                + message.getTime() + "): "
                                + message.getText());
                    }else{
                        System.out.println("Сообщение отправлено!");

                    }

                }
            } catch (IOException | ClassNotFoundException ioException) {
                System.out.println("Соединение оборвано");
            }
        }
    }
    
    public class WriterMessage extends Thread {

        @Override
        public void run() {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                while (true) {
                    String inputLine = reader.readLine();
                    connection.getOutput().writeObject(new Message(nickname, inputLine));
                    connection.getOutput().flush();

                    if (inputLine.equalsIgnoreCase("stop")) {
                        connection.close();
                        reader.close();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void startClient() {
        Thread writerThread, readerThread;
        try {
            writerThread = new Thread(new WriterMessage());
            writerThread.start();

            readerThread = new Thread(new ReaderMessage());
            readerThread.start();

            System.out.println("Клиент с именем " + nickname + " запустился");
            writerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

