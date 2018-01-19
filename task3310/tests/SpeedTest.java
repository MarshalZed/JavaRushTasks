package task3310.tests;

import task3310.Helper;
import task3310.Shortener;
import task3310.strategy.HashBiMapStorageStrategy;
import task3310.strategy.HashMapStorageStrategy;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SpeedTest {

    public long getTimeForGettingIds(Shortener shortener, Set<String> strings, Set<Long> ids){
        Date start = new Date();
        for (String line :
                strings) {
            ids.add(shortener.getId(line));
        }
        Date end = new Date();

        return end.getTime()-start.getTime();
    }

    public long getTimeForGettingStrings(Shortener shortener, Set<Long> ids, Set<String> strings){
        Date start = new Date();
        for (Long id :
                ids) {
            strings.add(shortener.getString(id));
        }
        Date end = new Date();

        return end.getTime()-start.getTime();
    }

    @Test
    public void testHashMapStorage(){
        Shortener shortener1 = new Shortener(new HashMapStorageStrategy());
        Shortener shortener2 = new Shortener(new HashBiMapStorageStrategy());

        HashSet<String> origStrings = new HashSet<>();
        HashSet<Long> origIDs = new HashSet<>();

        for (int i = 0; i < 10000; i++) {
            origStrings.add(Helper.generateRandomString());
        }

        Long getIDsShort1 = getTimeForGettingIds(shortener1,origStrings,origIDs);
        Long getIDsShort2 = getTimeForGettingIds(shortener2,origStrings,origIDs);
        System.out.println(getIDsShort1-getIDsShort2);
        Assert.assertTrue(getIDsShort1>getIDsShort2);

        Long getStrShort1 = getTimeForGettingStrings(shortener1,origIDs,origStrings);
        Long getStrShort2 = getTimeForGettingStrings(shortener2,origIDs,origStrings);
        System.out.println(getStrShort1-getStrShort2);
        Assert.assertEquals(getStrShort1,getStrShort2,30);
    }
}
