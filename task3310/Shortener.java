package task3310;

import task3310.strategy.StorageStrategy;

public class Shortener {
    private volatile Long lastId = 0L;
    private StorageStrategy storageStrategy;

    public Shortener(StorageStrategy storageStrategy) {
        this.storageStrategy = storageStrategy;
    }

/*3.4. Реализуй метод getId, он должен:
3.4.1. Проверить есть ли переданное значение в хранилище, если есть – вернуть его ключ.
3.4.2. Если преданного значения нет в хранилище, то:
3.4.2.1. Увеличить значение lastId на единицу;
3.4.2.2. Добавить в хранилище новую пару ключ-значение (новое значение lastId и переданную строку);
3.4.2.3. Вернуть новое значение lastId.*/
    public synchronized Long getId(String string){
        if (storageStrategy.containsValue(string)){
            return storageStrategy.getKey(string);
        } else {
            lastId++;
            storageStrategy.put(lastId,string);
            return lastId;
        }
    }

    public synchronized String getString(Long id){
        return storageStrategy.getValue(id);
    }
}
