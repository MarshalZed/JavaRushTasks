package task3008.client;

import task3008.Connection;
import task3008.ConsoleHelper;
import task3008.Message;
import task3008.MessageType;

import java.io.IOException;
import java.net.Socket;

public class Client {
    protected Connection connection;
    private volatile boolean clientConnected = false;
    //private static final String IP_ADDRESS_PATTERN = "^(?:[01]?\\d\\d?|2[0-4]\\d|25[0-5]\\.){3}(?:[01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    //private final Pattern pattern = Pattern.compile(IP_ADDRESS_PATTERN);

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

    public void run() {
        SocketThread socketThread = getSocketThread();
        socketThread.setDaemon(true);
        socketThread.start();
        synchronized (this) {
            try {
                this.wait();
                if (clientConnected) {
                    ConsoleHelper.writeMessage("Соединение установлено. Для выхода наберите команду 'exit'.");
                    String inLine;
                    while (clientConnected) {
                        inLine = ConsoleHelper.readString();
                        if ("exit".equalsIgnoreCase(inLine)) break;
                        else if (shouldSendTextFromConsole()) {
                            sendTextMessage(inLine);
                        }
                    }
                } else {
                    ConsoleHelper.writeMessage("Произошла ошибка во время работы клиента.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected String getServerAddress() {
        String address = ConsoleHelper.readString();
        /*while (!"localhost".equalsIgnoreCase(address)||!pattern.matcher(address).matches()){
            address = ConsoleHelper.readString();
        }*/
        return address;
    }

    protected int getServerPort() {
        return ConsoleHelper.readInt();
    }

    protected String getUserName() {
        return ConsoleHelper.readString();
    }

    protected boolean shouldSendTextFromConsole() {
        return true;
    }

    protected SocketThread getSocketThread() {
        return new SocketThread();
    }

    protected void sendTextMessage(String text) {
        try {
            connection.send(new Message(MessageType.TEXT, text));
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Send error");
            clientConnected = false;
        }
    }

    public class SocketThread extends Thread {
        @Override
        public void run() {
            String serverAddress = getServerAddress();
            int serverPort = getServerPort();
            try {
                Socket socket = new Socket(serverAddress, serverPort);
                Client.this.connection = new Connection(socket);
                clientHandshake();
                clientMainLoop();
            } catch (IOException | ClassNotFoundException e) {
                notifyConnectionStatusChanged(false);
            }
        }

        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
        }

        protected void informAboutAddingNewUser(String userName) {
            ConsoleHelper.writeMessage(userName + " connected");
        }

        protected void informAboutDeletingNewUser(String userName) {
            ConsoleHelper.writeMessage(userName + " disconnected");
        }

        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            synchronized (Client.this) {
                Client.this.clientConnected = clientConnected;
                Client.this.notify();
            }
        }

        protected void clientHandshake() throws IOException, ClassNotFoundException {
            while (true) {
                Message message = connection.receive();
                if (message.getType() == MessageType.NAME_REQUEST) {
                    connection.send(new Message(MessageType.USER_NAME, getUserName()));
                } else if (message.getType() == MessageType.NAME_ACCEPTED) {
                    notifyConnectionStatusChanged(true);
                    break;
                } else {
                    throw new IOException("Unexpected MessageType");
                }
            }
        }

        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            while (true) {
                Message message = connection.receive();
                if (message.getType() == MessageType.TEXT) {
                    processIncomingMessage(message.getData());
                } else if (message.getType() == MessageType.USER_ADDED) {
                    informAboutAddingNewUser(message.getData());
                } else if (message.getType() == MessageType.USER_REMOVED) {
                    informAboutDeletingNewUser(message.getData());
                } else {
                    throw new IOException("Unexpected MessageType");
                }
            }
        }
    }
}
