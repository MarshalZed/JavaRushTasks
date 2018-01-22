package task3008;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;

public class Connection implements Closeable{
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public Connection(Socket socket) throws IOException{
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    public void send(Message message) throws IOException{
        synchronized (out) {
            out.writeObject(message);
        }
    }

    public Message receive() throws IOException, ClassNotFoundException{
        synchronized (in) {
            return (Message) in.readObject();
        }
    }

    public SocketAddress getRemoteSocketAddress(){
        return socket.getRemoteSocketAddress();
    }

    @Override
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
/*Конструктор, который должен принимать Socket в качестве параметра и
инициализировать поля класса. Для инициализации полей in и out используй
соответствующие потоки сокета. Конструктор может бросать исключение IOException.
Создать объект класса ObjectOutputStream нужно до того, как будет создаваться объект
класса ObjectInputStream, иначе может возникнуть взаимная блокировка потоков,
которые хотят установить соединение через класс Connection. Более подробно об этом
ты можешь прочитать в спецификации класса ObjectInputStream.*/