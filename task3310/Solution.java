package task3310;

import task3310.strategy.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Solution {
    public static void main(String[] args) {
        JDBCStorageStrategy jdbcStrategy = new JDBCStorageStrategy();
        String dbPath = Solution.class.getClassLoader().getResource("").getPath().replaceFirst("/","")
                +Solution.class.getPackage().toString().split(" ")[1].replaceAll("\\.", "/")+"/"
                +"StorageStrategy.db";
        if (jdbcStrategy.connect("jdbc:sqlite:"+dbPath)){
            Solution.testStrategy(jdbcStrategy,100);
            jdbcStrategy.closeConnection();
        }
        Solution.testStrategy(new HashMapStorageStrategy(),10000);
        Solution.testStrategy(new OurHashMapStorageStrategy(),10000);
        Solution.testStrategy(new HashBiMapStorageStrategy(),10000);
        Solution.testStrategy(new OurHashBiMapStorageStrategy(),10000);
        Solution.testStrategy(new DualHashBidiMapStorageStrategy(),10000);
        Solution.testStrategy(new OurMultiThreadHashMapStorageStrategy(),10000);
        Solution.testStrategy(new FileStorageStrategy(),10);
    }

    public static Set<Long> getIds(Shortener shortener, Set<String> strings){
        HashSet<Long> resultSet = new HashSet<>();
        for (String line :
                strings) {
            resultSet.add(shortener.getId(line));
        }
        return resultSet;
    }

    public static Set<String> getStrings(Shortener shortener, Set<Long> keys){
        HashSet<String> resultSet = new HashSet<>();
        for (Long key :
                keys) {
            resultSet.add(shortener.getString(key));
        }
        return resultSet;
    }

    /*Метод будет тестировать работу переданной стратегии на определенном количестве элементов elementsNumber. Реализация метода должна:
6.2.3.1. Выводить имя класса стратегии. Имя не должно включать имя пакета.
6.2.3.2. Генерировать тестовое множество строк, используя Helper и заданное количество элементов elementsNumber.
6.2.3.3. Создавать объект типа Shortener, используя переданную стратегию.
6.2.3.4. Замерять и выводить время необходимое для отработки метода getIds для заданной стратегии
и заданного множества элементов. Время вывести в миллисекундах.
При замере времени работы метода можно пренебречь переключением процессора на другие потоки, временем,
которое тратится на сам вызов, возврат значений и вызов методов получения времени (даты).
Замер времени произведи с использованием объектов типа Date.
6.2.3.5. Замерять и выводить время необходимое для отработки метода getStrings для заданной стратегии и полученного
 в предыдущем пункте множества идентификаторов.
6.2.3.6. Сравнивать одинаковое ли содержимое множества строк, которое было сгенерировано и множества,
которое было возвращено методом getStrings. Если множества одинаковы, то выведи "Тест пройден.",
иначе "Тест не пройден.".*/
    public static void testStrategy(StorageStrategy strategy, long elementsNumber){
        Helper.printMessage(strategy.getClass().getSimpleName());

        Shortener shortener = new Shortener(strategy);
        HashSet<String> strings = new HashSet<>();
        for (long i = 0; i < elementsNumber; i++) {
            strings.add(Helper.generateRandomString());
        }

        Date start = new Date();
        Set<Long> ids = getIds(shortener,strings);
        Date end = new Date();
        Helper.printMessage("get Id's = "+String.valueOf(end.getTime()-start.getTime())+" ms");

        start = new Date();
        Set<String> lines = getStrings(shortener,ids);
        end = new Date();
        Helper.printMessage("get Strings = "+String.valueOf(end.getTime()-start.getTime())+" ms");

        if (ids.size()==lines.size()) Helper.printMessage("Тест пройден.");
        else Helper.printMessage("Тест не пройден. "+ids.size()+"!="+lines.size());

        System.out.println();
    }
}
