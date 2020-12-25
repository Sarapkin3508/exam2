package master;

import java.io.*;
import java.net.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;

public class ChatServer extends Thread {
    
    private Socket socket;
    private CopyOnWriteArraySet<Connection> connections;
    private ArrayBlockingQueue<Message> messages;

    private ObjectOutputStream outObj = null;
   private BufferedWriter out;
    private BufferedReader in;
    private ObjectInputStream inObj = null;
    private int port;


    
    public ChatServer(int port) throws IOException {
        this.port = port;
        connections = new CopyOnWriteArraySet<>();
        messages = new ArrayBlockingQueue<>(Runtime.getRuntime().availableProcessors(), true);

    }


    private class Reader implements Runnable {
        private Connection connection;
        private ObjectInputStream input;

        public Reader(Connection connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Message message;
                    message = (Message) connection.getInput().readObject();
                    connection.setSender(message.getSender());

                    System.out.println((message.getTime()) + " " + message.getSender() + ": " + message.getText());

                    if (message.getText().equalsIgnoreCase("stop")) {
                        removeClientConnectionNet(connection);
                        break;
                    }

                    messages.put(message);
                }
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                if (connection.getSocket() != null)
                    System.out.println("Connect with " + connection.getSocket() + " is already closed.");
            } finally {
                this.connection.close();
            }
        }
    }


    private void send(Message msg) {
        try {
            outObj.writeObject(msg);
            outObj.flush();
        } catch (IOException ignored) {}
        
    }
    public void removeClientConnectionNet(Connection connection) {
        if (!connections.isEmpty()) {
            connections.remove(connection);
            System.out.println("Client " + connection.getSender() + " was terminated.");
        }
    }

    private class Writer implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Message message = messages.take();
                    for (Connection connection : connections) {
                        if (connection.getSender().equalsIgnoreCase(message.getSender())) {

                                try {
                                    connection.getOutput().writeObject(message);
                                    connection.getOutput().flush();
                                    for (Connection vr : connections) {
                                        vr.getOutput().writeObject(message);
                                        vr.getOutput().flush();

                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        }
    }


    private void downService() {
            try {
            if(!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                inObj.close();
                outObj.close(); 
                for (ChatServer vr : Server.serverList) {
                    if(vr.equals(this)) vr.interrupt();
                    Server.serverList.remove(this);
                }
            }
        } catch (IOException ignored) {}
    }



        public void startServer () {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("Сервер запустился");

                Thread writerThread = new Thread(new Writer());
                writerThread.start();

                while (true) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        Connection connection = new Connection(clientSocket);
                        connections.add(connection);

                        Reader reader = new Reader(connection);
                        Thread readerThread = new Thread(reader);
                        readerThread.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Сервер остановлен ");
                try {
                    for (Connection connection : connections) {
                        try {
                            connection.getSocket().close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (serverSocket != null) serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



