package task3310.strategy;

public class FileStorageStrategy implements StorageStrategy {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final long DEFAULT_BUCKET_SIZE_LIMIT = 10000;
    private FileBucket[] table = new FileBucket[DEFAULT_INITIAL_CAPACITY];
    private int size;
    private long bucketSizeLimit = DEFAULT_BUCKET_SIZE_LIMIT;
    private long maxBucketSize;

    int hash(Long k) {
        int h;
        return (k == null) ? 0 : (h = k.hashCode()) ^ (h >>> 16);
    }

    int indexFor(int hash, int length) {
        return (length - 1) & hash;
    }

    Entry getEntry(Long key) {
        int hash = (key == null) ? 0 : hash(key);
        FileBucket fBucket = table[indexFor(hash, table.length)];
        for (Entry e = fBucket.getEntry(); e != null; e = e.next) {
            Object k;
            if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                return e;
        }
        return null;
    }

    void resize(int newCapacity) {
        FileBucket[] oldTable = table;
        int oldCapacity = oldTable.length;
        if (oldCapacity >= 1 << 30) {
            return;
        }

        FileBucket[] newTable = new FileBucket[newCapacity];
        transfer(newTable);
        table = newTable;
    }

    void transfer(FileBucket[] newTable) {
        FileBucket[] src = table;
        int newCapacity = newTable.length;
        maxBucketSize = 0;
        for (int j = 0; j < src.length; j++) {
            if (src[j]!=null){
                Entry e = src[j].getEntry();
                if (e != null) {
                    src[j].remove();
                    src[j] = null;
                    do {
                        Entry next = e.next;
                        int i = indexFor(e.hash, newCapacity);
                        if (newTable[i]!=null){
                            e.next = newTable[i].getEntry();
                        } else {
                            e.next = null;
                        }
                        newTable[i] = new FileBucket();
                        newTable[i].putEntry(e);
                        if (newTable[i].getFileSize()>maxBucketSize){
                            maxBucketSize = newTable[i].getFileSize();
                        }
                        e = next;
                    } while (e != null);
                }
            }
        }
    }

    void addEntry(int hash, Long key, String value, int bucketIndex) {
        FileBucket fBucket = table[bucketIndex];
        Entry e = null;
        if (fBucket!=null){
            e = fBucket.getEntry();
        } else {
            fBucket = new FileBucket();
        }
        fBucket.putEntry(new Entry(hash, key, value, e));
        table[bucketIndex] = fBucket;
        size++;
        if (fBucket.getFileSize()>maxBucketSize) maxBucketSize = fBucket.getFileSize();
        if (maxBucketSize >= bucketSizeLimit){
            resize(2 * table.length);
        }
    }

    void createEntry(int hash, Long key, String value, int bucketIndex) {
        FileBucket fBucket = table[bucketIndex];
        Entry e = null;
        if (fBucket!=null){
            e = fBucket.getEntry();
        } else {
            fBucket = new FileBucket();
        }
        fBucket.putEntry(new Entry(hash, key, value, e));
        table[bucketIndex] = fBucket;
        if (fBucket.getFileSize()>maxBucketSize) maxBucketSize = fBucket.getFileSize();
        size++;
    }

    @Override
    public boolean containsKey(Long key) {
        return getEntry(key) != null;
    }

    @Override
    public boolean containsValue(String value) {
        return getKey(value) != null;
    }

    @Override
    public void put(Long key, String value) {
        int hash = hash(key);
        int i = indexFor(hash, table.length);
        if (table[i]==null){
            table[i] = new FileBucket();
        }
        for (Entry e = table[i].getEntry(); e != null; e = e.next) {
            Object k;
            if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
                e.value = value;
            }
        }
        addEntry(hash, key, value, i);
    }

    @Override
    public Long getKey(String value) {
        for (int i = 0; i < table.length; i++) {
            if (table[i]!=null){
                for (Entry e = table[i].getEntry(); e != null; e = e.next) {
                    if (value.equals(e.value))
                        return e.key;
                }
            }
        }
        return null;
    }

    @Override
    public String getValue(Long key) {
        Entry e;
        return (e = getEntry(key)) == null ? null : e.value;
    }
}
