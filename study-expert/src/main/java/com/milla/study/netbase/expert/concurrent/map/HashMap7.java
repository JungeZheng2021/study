package com.milla.study.netbase.expert.concurrent.map;

import java.io.*;
import java.util.*;

// jdk1.7代码
public class HashMap7<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable, Serializable {
    // 默认的HashMap的空间大小16
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16


    // hashMap最大的空间大小
    static final int MAXIMUM_CAPACITY = 1 << 30;

    // HashMap默认负载因子，负载因子越小，hash冲突机率越低，至于为什么，看完下面源码就知道了
    static final float DEFAULT_LOAD_FACTOR = 0.75f;


    static final Entry<?, ?>[] EMPTY_TABLE = {};

    // table就是HashMap实际存储数组的地方
    transient Entry<K, V>[] table = (Entry<K, V>[]) EMPTY_TABLE;


    // HashMap 实际存储的元素个数
    transient int size;


    // 临界值（超过这个值则开始扩容）,公式为(threshold = capacity * loadFactor)
    int threshold;

    // HashMap 负载因子
    final float loadFactor;


    transient int modCount;


    static final int ALTERNATIVE_HASHING_THRESHOLD_DEFAULT = Integer.MAX_VALUE;

    private static class Holder {

        static final int ALTERNATIVE_HASHING_THRESHOLD;


        static {

            String altThreshold = java.security.AccessController.doPrivileged(

                    new sun.security.action.GetPropertyAction(

                            "jdk.map.althashing.threshold"));


            int threshold;

            try {

                threshold = (null != altThreshold)

                        ? Integer.parseInt(altThreshold)

                        : ALTERNATIVE_HASHING_THRESHOLD_DEFAULT;


                // disable alternative hashing if -1

                if (threshold == -1) {

                    threshold = Integer.MAX_VALUE;

                }


                if (threshold < 0) {

                    throw new IllegalArgumentException("value must be positive integer.");

                }

            } catch (IllegalArgumentException failed) {

                throw new Error("Illegal value for 'jdk.map.althashing.threshold'", failed);

            }


            ALTERNATIVE_HASHING_THRESHOLD = threshold;

        }

    }


    transient int hashSeed = 0;

    //3.指定大小和负载因子
    public HashMap7(int initialCapacity, float loadFactor) {
        //此处对传入的初始容量进行校验，最大不能超过MAXIMUM_CAPACITY = 1<<30(2的30次方)
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);

        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;

        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        this.loadFactor = loadFactor;

        threshold = initialCapacity;
        //init方法在HashMap中没有实际实现，不过在其子类如 linkedHashMap中就会有对应实现
        init();
    }

    //2.指定大小但不指定负载因子
    public HashMap7(int initialCapacity) {

        this(initialCapacity, DEFAULT_LOAD_FACTOR);

    }

    //1.默认构造，会调用默认默认空间大小16和默认负载因子0.75
    public HashMap7() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    //4.使用默认构造创建对象并将指定的map集合放入新创建的对象中
    public HashMap7(Map<? extends K, ? extends V> m) {

        this(Math.max((int) (m.size() / DEFAULT_LOAD_FACTOR) + 1,

                DEFAULT_INITIAL_CAPACITY), DEFAULT_LOAD_FACTOR);
        //初始化数组
        inflateTable(threshold);

        putAllForCreate(m);

    }

    //    确保capacity为大于或等于toSize的最接近toSize的二次幂，比如toSize=13,则capacity=16;to_size=16,capacity=16;to_size=17,capacity=32.
    static int roundUpToPowerOf2(int number) {
        // assert number >= 0 : "number must be non-negative";
        // 返回最接近临界值的2的N次方
        return number >= MAXIMUM_CAPACITY
                ? MAXIMUM_CAPACITY
                : (number > 1) ? Integer.highestOneBit((number - 1) << 1) : 1;

    }


    private void inflateTable(int toSize) {

        // Find a power of 2 >= toSize 保证数组大小一定是 2 的 n 次方。
        // new HashMap(519)，大小是1024
        int capacity = roundUpToPowerOf2(toSize);

        // 计算扩容阈值：capacity * loadFactor
        threshold = (int) Math.min(capacity * loadFactor, MAXIMUM_CAPACITY + 1);
        // 初始化数组
        table = new Entry[capacity];

        initHashSeedAsNeeded(capacity);

    }


    // internal utilities

    void init() {

    }


    final boolean initHashSeedAsNeeded(int capacity) {
        // 初始化的时候hashSeed为0,0!=0 这时为false.
        boolean currentAltHashing = hashSeed != 0;

        boolean useAltHashing = sun.misc.VM.isBooted() &&

                (capacity >= Holder.ALTERNATIVE_HASHING_THRESHOLD);

        boolean switching = currentAltHashing ^ useAltHashing;

        if (switching) {
            //为了防止放在项目里面编译报错，这段代码是我注释的
//            hashSeed = useAltHashing
//
//                ? sun.misc.Hashing.randomHashSeed(this)
//
//                : 0;

        }

        return switching;

    }

    final int hash(Object k) {

        int h = hashSeed;

        if (0 != h && k instanceof String) {
            // Tony: Hashing这个用不了,为了防止编译报错,下面的代码是我注释的
//            return sun.misc.Hashing.stringHash32((String) k);

        }


        h ^= k.hashCode();


        // This function ensures that hashCodes that differ only by

        // constant multiples at each bit position have a bounded

        // number of collisions (approximately 8 at default load factor).

        h ^= (h >>> 20) ^ (h >>> 12);

        return h ^ (h >>> 7) ^ (h >>> 4);

    }

    static int indexFor(int h, int length) {
        // assert Integer.bitCount(length) == 1 : "length must be a non-zero power of 2";
        // 简单理解就是hash值和长度取模
        return h & (length - 1);
    }

    public int size() {

        return size;

    }

    public boolean isEmpty() {

        return size == 0;

    }

    //获取数据
    public V get(Object key) {

        if (key == null)
            //如果key为null，就从table[0]获取(put中，key为null也是存储在该位置)
            return getForNullKey();
        Entry<K, V> entry = getEntry(key);
        return null == entry ? null : entry.getValue();
    }

    private V getForNullKey() {

        if (size == 0) {

            return null;

        }

        for (Entry<K, V> e = table[0]; e != null; e = e.next) {

            if (e.key == null)

                return e.value;

        }

        return null;

    }


    public boolean containsKey(Object key) {

        return getEntry(key) != null;

    }


    final Entry<K, V> getEntry(Object key) {
        if (size == 0) {
            return null;
        }
        int hash = (key == null) ? 0 : hash(key);
        // 确定key对应的数组位置,遍历链表直至找到，或者最终找不到返回null
        for (Entry<K, V> e = table[indexFor(hash, table.length)];

             e != null;

             e = e.next) {

            Object k;

            if (e.hash == hash &&

                    ((k = e.key) == key || (key != null && key.equals(k))))

                return e;

        }
        return null;

    }


    public V put(K key, V value) {
        // 当插入第一个元素的时候，需要先初始化数组大小
        if (table == EMPTY_TABLE) {
            // 数组初始化
            inflateTable(threshold);

        }
        // 如果 key 为 null，感兴趣的可以往里看，最终会将这个 entry 放到 table[0] 中
        if (key == null)

            return putForNullKey(value);
        // 1. 求 key 的 hash 值
        int hash = hash(key);
        // 2. 找到对应的数组下标
        int i = indexFor(hash, table.length);
        // 3. 遍历一下对应下标处的链表，看是否有重复的 key 已经存在，如果有，直接覆盖，put 方法返回旧值就结束了
        for (Entry<K, V> e = table[i]; e != null; e = e.next) {

            Object k;

            if (e.hash == hash && ((k = e.key) == key || key.equals(k))) { // key -> value

                V oldValue = e.value;

                e.value = value;

                e.recordAccess(this);

                return oldValue;

            }
        }
        modCount++;
        // 4. 不存在重复的 key，将此 entry 添加到链表中
        addEntry(hash, key, value, i);
        return null;
    }


    /**
     * Offloaded version of put for null keys
     */

    private V putForNullKey(V value) {

        for (Entry<K, V> e = table[0]; e != null; e = e.next) {

            if (e.key == null) {

                V oldValue = e.value;

                e.value = value;

                e.recordAccess(this);

                return oldValue;

            }

        }

        modCount++;

        addEntry(0, null, value, 0);

        return null;

    }

    private void putForCreate(K key, V value) {

        int hash = null == key ? 0 : hash(key);

        int i = indexFor(hash, table.length);


        for (Entry<K, V> e = table[i]; e != null; e = e.next) {

            Object k;

            if (e.hash == hash &&

                    ((k = e.key) == key || (key != null && key.equals(k)))) {

                e.value = value;

                return;

            }

        }


        createEntry(hash, key, value, i);

    }


    private void putAllForCreate(Map<? extends K, ? extends V> m) {

        for (Map.Entry<? extends K, ? extends V> e : m.entrySet())

            putForCreate(e.getKey(), e.getValue());

    }


    void resize(int newCapacity) {
        Entry[] oldTable = table;
        int oldCapacity = oldTable.length;
        // 如果之前的HashMap已经扩充到最大了，那么就将临界值threshold设置为最大的int值
        if (oldCapacity == MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }
        // 新的数组
        Entry[] newTable = new Entry[newCapacity];
        // 将原来数组中的值迁移到新的更大的数组中
        transfer(newTable, initHashSeedAsNeeded(newCapacity));

        table = newTable;
        // 阈值计算
        threshold = (int) Math.min(newCapacity * loadFactor, MAXIMUM_CAPACITY + 1);

    }


    /**
     * Transfers all entries from current table to newTable.
     */
    //数组的拷贝
    void transfer(Entry[] newTable, boolean rehash) {
        int newCapacity = newTable.length;
        // 遍历旧的数组
        for (Entry<K, V> e : table) {
            while (null != e) {
                //获取下一个entry对象
                Entry<K, V> next = e.next;
                if (rehash) {
                    e.hash = null == e.key ? 0 : hash(e.key);
                }
                //当前对象的hash值
                int i = indexFor(e.hash, newCapacity);
                //头插法，Entry对象放在新数组上第一位置，其他对象放在该对象的后一位置
                e.next = newTable[i];
                //将整体的对象放在指定的索引位置
                newTable[i] = e;
                //继续循环下一个Entry
                e = next;
            }
        }
    }

    @Override
    public String toString() {
        return "HashMap{" +
                "table=" + Arrays.toString(table) +
                ", size=" + size +
                ", threshold=" + threshold +
                ", loadFactor=" + loadFactor +
                ", modCount=" + modCount +
                ", hashSeed=" + hashSeed +
                ", entrySet=" + entrySet +
                "} " + super.toString();
    }

    public void putAll(Map<? extends K, ? extends V> m) {

        int numKeysToBeAdded = m.size();

        if (numKeysToBeAdded == 0)

            return;


        if (table == EMPTY_TABLE) {

            inflateTable((int) Math.max(numKeysToBeAdded * loadFactor, threshold));

        }


        /*

         * Expand the map if the map if the number of mappings to be added

         * is greater than or equal to threshold.  This is conservative; the

         * obvious condition is (m.size() + size) >= threshold, but this

         * condition could result in a map with twice the appropriate capacity,

         * if the keys to be added overlap with the keys already in this map.

         * By using the conservative calculation, we subject ourself

         * to at most one extra resize.

         */

        if (numKeysToBeAdded > threshold) {

            int targetCapacity = (int) (numKeysToBeAdded / loadFactor + 1);

            if (targetCapacity > MAXIMUM_CAPACITY)

                targetCapacity = MAXIMUM_CAPACITY;

            int newCapacity = table.length;

            while (newCapacity < targetCapacity)

                newCapacity <<= 1;


            if (newCapacity > table.length)

                resize(newCapacity);
        }


        for (Map.Entry<? extends K, ? extends V> e : m.entrySet())

            put(e.getKey(), e.getValue());

    }


    public V remove(Object key) {

        Entry<K, V> e = removeEntryForKey(key);

        return (e == null ? null : e.value);

    }


    final Entry<K, V> removeEntryForKey(Object key) {

        if (size == 0) {

            return null;

        }
        // 定位到key的位置
        int hash = (key == null) ? 0 : hash(key);

        int i = indexFor(hash, table.length);

        Entry<K, V> prev = table[i];

        Entry<K, V> e = prev;

        // 删除链表中的元素
        while (e != null) {

            Entry<K, V> next = e.next;

            Object k;

            if (e.hash == hash &&

                    ((k = e.key) == key || (key != null && key.equals(k)))) {

                modCount++;

                size--;

                if (prev == e)

                    table[i] = next;

                else

                    prev.next = next;

                e.recordRemoval(this);

                return e;

            }

            prev = e;

            e = next;

        }


        return e;

    }


    /**
     * Special version of remove for EntrySet using {@code Map.Entry.equals()}
     * <p>
     * for matching.
     */

    final Entry<K, V> removeMapping(Object o) {

        if (size == 0 || !(o instanceof Map.Entry))

            return null;


        Map.Entry<K, V> entry = (Map.Entry<K, V>) o;

        Object key = entry.getKey();

        int hash = (key == null) ? 0 : hash(key);

        int i = indexFor(hash, table.length);

        Entry<K, V> prev = table[i];

        Entry<K, V> e = prev;


        while (e != null) {

            Entry<K, V> next = e.next;

            if (e.hash == hash && e.equals(entry)) {

                modCount++;

                size--;

                if (prev == e)

                    table[i] = next;

                else

                    prev.next = next;

                e.recordRemoval(this);

                return e;

            }

            prev = e;

            e = next;

        }


        return e;

    }


    /**
     * Removes all of the mappings from this map.
     * <p>
     * The map will be empty after this call returns.
     */

    public void clear() {

        modCount++;

        Arrays.fill(table, null);

        size = 0;

    }


    /**
     * Returns <tt>true</tt> if this map maps one or more keys to the
     * <p>
     * specified value.
     *
     * @param value value whose presence in this map is to be tested
     * @return <tt>true</tt> if this map maps one or more keys to the
     * <p>
     * specified value
     */

    public boolean containsValue(Object value) {

        if (value == null)

            return containsNullValue();


        Entry[] tab = table;

        for (int i = 0; i < tab.length; i++)

            for (Entry e = tab[i]; e != null; e = e.next)

                if (value.equals(e.value))

                    return true;

        return false;

    }


    /**
     * Special-case code for containsValue with null argument
     */

    private boolean containsNullValue() {

        Entry[] tab = table;

        for (int i = 0; i < tab.length; i++)

            for (Entry e = tab[i]; e != null; e = e.next)

                if (e.value == null)

                    return true;

        return false;

    }


    public Object clone() {

        HashMap7 result = null;

        try {

            result = (HashMap7) super.clone();

        } catch (CloneNotSupportedException e) {

            // assert false;

        }

        if (result.table != EMPTY_TABLE) {

            result.inflateTable(Math.min(

                    (int) Math.min(

                            size * Math.min(1 / loadFactor, 4.0f),

                            // we have limits...

                            com.milla.study.netbase.expert.concurrent.map.HashMap7.MAXIMUM_CAPACITY),

                    table.length));

        }

        result.entrySet = null;

        result.modCount = 0;

        result.size = 0;

        result.init();

        result.putAllForCreate(this);


        return result;

    }


    static class Entry<K, V> implements Map.Entry<K, V> {

        final K key;

        V value;

        Entry<K, V> next;

        int hash;


        /**
         * Creates new entry.
         */

        Entry(int h, K k, V v, Entry<K, V> n) {

            value = v;

            next = n;

            key = k;

            hash = h;

        }


        public final K getKey() {

            return key;

        }


        public final V getValue() {

            return value;

        }


        public final V setValue(V newValue) {

            V oldValue = value;

            value = newValue;

            return oldValue;

        }


        public final boolean equals(Object o) {

            if (!(o instanceof Map.Entry))

                return false;

            Map.Entry e = (Map.Entry) o;

            Object k1 = getKey();

            Object k2 = e.getKey();

            if (k1 == k2 || (k1 != null && k1.equals(k2))) {

                Object v1 = getValue();

                Object v2 = e.getValue();

                if (v1 == v2 || (v1 != null && v1.equals(v2)))

                    return true;

            }

            return false;

        }


        public final int hashCode() {

            return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());

        }

        @Override
        public String toString() {
            return "Entry{" +
                    "key=" + key +
                    ", value=" + value +
                    ", next=" + next +
                    ", hash=" + hash +
                    '}';
        }
//        public final String toString() {
//
//            return getKey() + "=" + getValue();
//
//        }


        void recordAccess(HashMap7 m) {

        }


        void recordRemoval(HashMap7 m) {

        }

    }


    void addEntry(int hash, K key, V value, int bucketIndex) {
        // 如果当前 HashMap 大小已经达到了阈值，并且新值要插入的数组位置已经有元素了，那么要扩容
        if ((size >= threshold) && (null != table[bucketIndex])) {
            // 扩容，容量 * 2
            resize(2 * table.length);
            // 扩容以后，重新计算 hash 值
            hash = (null != key) ? hash(key) : 0;
            // 重新计算扩容后的新的下标
            bucketIndex = indexFor(hash, table.length);
        }

        // 创建元素
        createEntry(hash, key, value, bucketIndex);

    }

    // 将新值放到链表的表头，然后 size++
    void createEntry(int hash, K key, V value, int bucketIndex) {

        Entry<K, V> e = table[bucketIndex];

        table[bucketIndex] = new Entry<>(hash, key, value, e);

        size++;

    }


    private abstract class HashIterator<E> implements Iterator<E> {

        Entry<K, V> next;        // next entry to return

        int expectedModCount;   // For fast-fail

        int index;              // current slot

        Entry<K, V> current;     // current entry


        HashIterator() {

            expectedModCount = modCount;

            if (size > 0) { // advance to first entry

                Entry[] t = table;

                while (index < t.length && (next = t[index++]) == null)

                    ;

            }

        }


        public final boolean hasNext() {

            return next != null;

        }


        final Entry<K, V> nextEntry() {

            if (modCount != expectedModCount)

                throw new ConcurrentModificationException();

            Entry<K, V> e = next;

            if (e == null)

                throw new NoSuchElementException();


            if ((next = e.next) == null) {

                Entry[] t = table;

                while (index < t.length && (next = t[index++]) == null)

                    ;

            }

            current = e;

            return e;

        }


        public void remove() {

            if (current == null)

                throw new IllegalStateException();

            if (modCount != expectedModCount)

                throw new ConcurrentModificationException();

            Object k = current.key;

            current = null;

            HashMap7.this.removeEntryForKey(k);

            expectedModCount = modCount;

        }

    }


    private final class ValueIterator extends HashIterator<V> {

        public V next() {

            return nextEntry().value;

        }

    }


    private final class KeyIterator extends HashIterator<K> {

        public K next() {

            return nextEntry().getKey();

        }

    }


    private final class EntryIterator extends HashIterator<Map.Entry<K, V>> {

        public Map.Entry<K, V> next() {

            return nextEntry();

        }

    }


    // Subclass overrides these to alter behavior of views' iterator() method

    Iterator<K> newKeyIterator() {

        return new KeyIterator();

    }

    Iterator<V> newValueIterator() {

        return new ValueIterator();

    }

    Iterator<Map.Entry<K, V>> newEntryIterator() {

        return new EntryIterator();

    }


    // Views


    private transient Set<Map.Entry<K, V>> entrySet = null;


    public Set<K> keySet() {
        // Tony: keySet用不了,为了防止编译报错,下面的代码是我注释的
//        Set<K> ks = keySet;

//        return (ks != null ? ks : (keySet = new KeySet()));

        return null;
    }


    private final class KeySet extends AbstractSet<K> {

        public Iterator<K> iterator() {

            return newKeyIterator();

        }

        public int size() {

            return size;

        }

        public boolean contains(Object o) {

            return containsKey(o);

        }

        public boolean remove(Object o) {

            return HashMap7.this.removeEntryForKey(o) != null;

        }

        public void clear() {

            HashMap7.this.clear();

        }

    }


    public Collection<V> values() {
        // Tony: values,为了防止编译报错,下面的代码是我注释的
//        Collection<V> vs = values;
//
//        return (vs != null ? vs : (values = new Values()));
        return null;
    }


    private final class Values extends AbstractCollection<V> {

        public Iterator<V> iterator() {

            return newValueIterator();

        }

        public int size() {

            return size;

        }

        public boolean contains(Object o) {

            return containsValue(o);

        }

        public void clear() {

            HashMap7.this.clear();

        }

    }


    public Set<Map.Entry<K, V>> entrySet() {

        return entrySet0();

    }


    private Set<Map.Entry<K, V>> entrySet0() {

        Set<Map.Entry<K, V>> es = entrySet;

        return es != null ? es : (entrySet = new EntrySet());

    }


    private final class EntrySet extends AbstractSet<Map.Entry<K, V>> {

        public Iterator<Map.Entry<K, V>> iterator() {

            return newEntryIterator();

        }

        public boolean contains(Object o) {

            if (!(o instanceof Map.Entry))

                return false;

            Map.Entry<K, V> e = (Map.Entry<K, V>) o;

            Entry<K, V> candidate = getEntry(e.getKey());

            return candidate != null && candidate.equals(e);

        }

        public boolean remove(Object o) {

            return removeMapping(o) != null;

        }

        public int size() {

            return size;

        }

        public void clear() {

            HashMap7.this.clear();

        }

    }

    private void writeObject(ObjectOutputStream s)

            throws IOException {

        // Write out the threshold, loadfactor, and any hidden stuff

        s.defaultWriteObject();


        // Write out number of buckets

        if (table == EMPTY_TABLE) {

            s.writeInt(roundUpToPowerOf2(threshold));

        } else {

            s.writeInt(table.length);

        }


        // Write out size (number of Mappings)

        s.writeInt(size);


        // Write out keys and values (alternating)

        if (size > 0) {

            for (Map.Entry<K, V> e : entrySet0()) {

                s.writeObject(e.getKey());

                s.writeObject(e.getValue());

            }

        }

    }


    private static final long serialVersionUID = 362498820763181265L;


    /**
     * Reconstitute the {@code HashMap} instance from a stream (i.e.,
     * <p>
     * deserialize it).
     */

    private void readObject(ObjectInputStream s)

            throws IOException, ClassNotFoundException {

        // Read in the threshold (ignored), loadfactor, and any hidden stuff

        s.defaultReadObject();

        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {

            throw new InvalidObjectException("Illegal load factor: " +

                    loadFactor);

        }


        // set other fields that need values

        table = (Entry<K, V>[]) EMPTY_TABLE;


        // Read in number of buckets

        s.readInt(); // ignored.


        // Read number of mappings

        int mappings = s.readInt();

        if (mappings < 0)

            throw new InvalidObjectException("Illegal mappings count: " +

                    mappings);


        // capacity chosen by number of mappings and desired load (if >= 0.25)

        int capacity = (int) Math.min(

                mappings * Math.min(1 / loadFactor, 4.0f),

                // we have limits...

                com.milla.study.netbase.expert.concurrent.map.HashMap7.MAXIMUM_CAPACITY);


        // allocate the bucket array;

        if (mappings > 0) {

            inflateTable(capacity);

        } else {

            threshold = capacity;

        }


        init();  // Give subclass a chance to do its thing.


        // Check Map.Entry[].class since it's the nearest public type to

        // what we're actually creating.
        //  下面这行代码是我注释的,为了防止编译报错
        // SharedSecrets.getJavaOISAccess().checkArray(s, Map.Entry[].class, capacity);


        // Read the keys and values, and put the mappings in the HashMap

        for (int i = 0; i < mappings; i++) {

            K key = (K) s.readObject();

            V value = (V) s.readObject();

            putForCreate(key, value);

        }

    }


    // These methods are used when serializing HashSets

    int capacity() {
        return table.length;
    }

    float loadFactor() {
        return loadFactor;
    }

}