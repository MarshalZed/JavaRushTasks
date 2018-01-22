package task3008;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(ConsoleHelper.readInt())) {
            ConsoleHelper.writeMessage("Server started");
            while (true) {
                new Handler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendBroadcastMessage(Message message) {
        connectionMap.values().forEach((connection -> {
            try {
                connection.send(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    private static class Handler extends Thread {
        private Socket socket;

        private Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run(){
            ConsoleHelper.writeMessage("Connected "+socket.getRemoteSocketAddress());
            String newClientName = "";
            try (Connection connection = new Connection(socket)){
                newClientName = serverHandshake(connection);
                sendBroadcastMessage(new Message(MessageType.USER_ADDED, newClientName));
                sendListOfUsers(connection, newClientName);
                serverMainLoop(connection, newClientName);
            } catch (IOException | ClassNotFoundException e) {
                ConsoleHelper.writeMessage("Error with connection "+socket.getRemoteSocketAddress());
            } finally {
                if (!newClientName.isEmpty()){
                    connectionMap.remove(newClientName);
                    sendBroadcastMessage(new Message(MessageType.USER_REMOVED, newClientName));
                }
            }
            ConsoleHelper.writeMessage("Disconnected "+socket.getRemoteSocketAddress());
        }

        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
            while (true) {
                connection.send(new Message(MessageType.NAME_REQUEST));
                Message answer = connection.receive();
                if (answer.getType() == MessageType.USER_NAME
                        && !answer.getData().isEmpty()
                        && !connectionMap.containsKey(answer.getData())) {
                    connectionMap.put(answer.getData(), connection);
                    connection.send(new Message(MessageType.NAME_ACCEPTED));
                    return answer.getData();
                }
            }
        }

        private void sendListOfUsers(Connection connection, String userName) throws IOException {
            connectionMap.keySet().stream()
                    .filter(entryName -> !userName.equals(entryName))
                    .forEach(entryName -> {
                        try {
                            connection.send(new Message(MessageType.USER_ADDED, entryName));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }

        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException{
            StringBuilder textMessage = new StringBuilder();
            while (true){
                Message connectionMessage = connection.receive();
                if (connectionMessage.getType()==MessageType.TEXT){
                    textMessage.setLength(0);
                    textMessage = textMessage.append(userName).append(": ").append(connectionMessage.getData());
                    sendBroadcastMessage(new Message(MessageType.TEXT,textMessage.toString()));
                } else {
                    ConsoleHelper.writeMessage("Illegal message from "+userName);
                }
            }
        }
    }
}
