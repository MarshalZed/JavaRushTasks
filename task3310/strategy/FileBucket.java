package task3310.strategy;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBucket {
    private Path path;

    public FileBucket() {
        try {
            path = Files.createTempFile("Strtgy",null);
            Files.deleteIfExists(path);
            Files.createFile(path);
            path.toFile().deleteOnExit();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public long getFileSize(){
        try {
            return Files.size(path);
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return 0;
    }

    public void putEntry(Entry entry){
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(path))) {
            objectOutputStream.writeObject(entry);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public Entry getEntry(){
        Entry entry = null;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(path))) {
            if (getFileSize()==0) return entry;
            entry = (Entry) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            //e.printStackTrace();
        }
        return entry;
    }

    public void remove(){
        try {
            Files.delete(path);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }
}
