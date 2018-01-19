package task3310.tests;

import task3310.Shortener;
import task3310.strategy.*;
import org.junit.Assert;
import org.junit.Test;

public class FunctionalTest {

    public void testStorage(Shortener shortener){
        String line1 = "String Equal";
        String line2 = "Unique String";
        String line3 = "String Equal";

        Long id1 = shortener.getId(line1);
        Long id2 = shortener.getId(line2);
        Long id3 = shortener.getId(line3);

        Assert.assertNotEquals(id2,id1);
        Assert.assertNotEquals(id2,id3);

        Assert.assertEquals(id1,id3);

        String line1Get = shortener.getString(id1);
        String line2Get = shortener.getString(id2);
        String line3Get = shortener.getString(id3);

        Assert.assertEquals(line1,line1Get);
        Assert.assertEquals(line2,line2Get);
        Assert.assertEquals(line3,line3Get);
    }

    @Test
    public void testHashMapStorageStrategy(){
        Shortener shortener = new Shortener(new HashMapStorageStrategy());
        testStorage(shortener);
    }

    @Test
    public void testOurHashMapStorageStrategy(){
        Shortener shortener = new Shortener(new OurHashMapStorageStrategy());
        testStorage(shortener);
    }

    @Test
    public void testFileStorageStrategy(){
        Shortener shortener = new Shortener(new FileStorageStrategy());
        testStorage(shortener);
    }

    @Test
    public void testHashBiMapStorageStrategy(){
        Shortener shortener = new Shortener(new HashBiMapStorageStrategy());
        testStorage(shortener);
    }

    @Test
    public void testDualHashBidiMapStorageStrategy(){
        Shortener shortener = new Shortener(new DualHashBidiMapStorageStrategy());
        testStorage(shortener);
    }

    @Test
    public void testOurHashBiMapStorageStrategy(){
        Shortener shortener = new Shortener(new OurHashMapStorageStrategy());
        testStorage(shortener);
    }

    @Test
    public void testOurMultiThreadHashMapStorageStrategy(){
        Shortener shortener = new Shortener(new OurMultiThreadHashMapStorageStrategy());
        testStorage(shortener);
    }

    @Test
    public void testJDBCStorageStrategy(){
        /*JDBCStorageStrategy jdbcStrategy = new JDBCStorageStrategy();
        if (jdbcStrategy.connect("jdbc:sqlite:C:\\Users\\User\\IdeaProjects\\JavaRushTasks\\StorageStrategy.db")){
            Shortener shortener = new Shortener(jdbcStrategy);
            testStorage(shortener);
            jdbcStrategy.closeConnection();
        }*/
    }
}
