/*

 * Copyright (c) 1997, 2017, Oracle and/or its affiliates. All rights reserved.

 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.

 *

 * This code is free software; you can redistribute it and/or modify it

 * under the terms of the GNU General Public License version 2 only, as

 * published by the Free Software Foundation.  Oracle designates this

 * particular file as subject to the "Classpath" exception as provided

 * by Oracle in the LICENSE file that accompanied this code.

 *

 * This code is distributed in the hope that it will be useful, but WITHOUT

 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or

 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License

 * version 2 for more details (a copy is included in the LICENSE file that

 * accompanied this code).

 *

 * You should have received a copy of the GNU General Public License version

 * 2 along with this work; if not, write to the Free Software Foundation,

 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.

 *

 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA

 * or visit www.oracle.com if you need additional information or have any

 * questions.

 */


package com.milla.study.netbase.expert.concurrent.juc;

import java.io.*;
import java.util.*;


/**
 * Hash table based implementation of the <tt>Map</tt> interface.  This
 * <p>
 * implementation provides all of the optional map operations, and permits
 *
 * <tt>null</tt> values and the <tt>null</tt> key.  (The <tt>HashMap</tt>
 * <p>
 * class is roughly equivalent to <tt>Hashtable</tt>, except that it is
 * <p>
 * unsynchronized and permits nulls.)  This class makes no guarantees as to
 * <p>
 * the order of the map; in particular, it does not guarantee that the order
 * <p>
 * will remain constant over time.
 *
 *
 *
 * <p>This implementation provides constant-time performance for the basic
 * <p>
 * operations (<tt>get</tt> and <tt>put</tt>), assuming the hash function
 * <p>
 * disperses the elements properly among the buckets.  Iteration over
 * <p>
 * collection views requires time proportional to the "capacity" of the
 *
 * <tt>HashMap</tt> instance (the number of buckets) plus its size (the number
 * <p>
 * of key-value mappings).  Thus, it's very important not to set the initial
 * <p>
 * capacity too high (or the load factor too low) if iteration performance is
 * <p>
 * important.
 *
 *
 *
 * <p>An instance of <tt>HashMap</tt> has two parameters that affect its
 * <p>
 * performance: <i>initial capacity</i> and <i>load factor</i>.  The
 *
 * <i>capacity</i> is the number of buckets in the hash table, and the initial
 * <p>
 * capacity is simply the capacity at the time the hash table is created.  The
 *
 * <i>load factor</i> is a measure of how full the hash table is allowed to
 * <p>
 * get before its capacity is automatically increased.  When the number of
 * <p>
 * entries in the hash table exceeds the product of the load factor and the
 * <p>
 * current capacity, the hash table is <i>rehashed</i> (that is, internal data
 * <p>
 * structures are rebuilt) so that the hash table has approximately twice the
 * <p>
 * number of buckets.
 *
 *
 *
 * <p>As a general rule, the default load factor (.75) offers a good tradeoff
 * <p>
 * between time and space costs.  Higher values decrease the space overhead
 * <p>
 * but increase the lookup cost (reflected in most of the operations of the
 *
 * <tt>HashMap</tt> class, including <tt>get</tt> and <tt>put</tt>).  The
 * <p>
 * expected number of entries in the map and its load factor should be taken
 * <p>
 * into account when setting its initial capacity, so as to minimize the
 * <p>
 * number of rehash operations.  If the initial capacity is greater
 * <p>
 * than the maximum number of entries divided by the load factor, no
 * <p>
 * rehash operations will ever occur.
 *
 *
 *
 * <p>If many mappings are to be stored in a <tt>HashMap</tt> instance,
 * <p>
 * creating it with a sufficiently large capacity will allow the mappings to
 * <p>
 * be stored more efficiently than letting it perform automatic rehashing as
 * <p>
 * needed to grow the table.
 *
 *
 *
 * <p><strong>Note that this implementation is not synchronized.</strong>
 * <p>
 * If multiple threads access a hash map concurrently, and at least one of
 * <p>
 * the threads modifies the map structurally, it <i>must</i> be
 * <p>
 * synchronized externally.  (A structural modification is any operation
 * <p>
 * that adds or deletes one or more mappings; merely changing the value
 * <p>
 * associated with a key that an instance already contains is not a
 * <p>
 * structural modification.)  This is typically accomplished by
 * <p>
 * synchronizing on some object that naturally encapsulates the map.
 * <p>
 * <p>
 * <p>
 * If no such object exists, the map should be "wrapped" using the
 * <p>
 * {@link Collections#synchronizedMap Collections.synchronizedMap}
 * <p>
 * method.  This is best done at creation time, to prevent accidental
 * <p>
 * unsynchronized access to the map:<pre>
 *
 *   Map m = Collections.synchronizedMap(new HashMap(...));</pre>
 *
 *
 *
 * <p>The iterators returned by all of this class's "collection view methods"
 * <p>
 * are <i>fail-fast</i>: if the map is structurally modified at any time after
 * <p>
 * the iterator is created, in any way except through the iterator's own
 *
 * <tt>remove</tt> method, the iterator will throw a
 * <p>
 * {@link ConcurrentModificationException}.  Thus, in the face of concurrent
 * <p>
 * modification, the iterator fails quickly and cleanly, rather than risking
 * <p>
 * arbitrary, non-deterministic behavior at an undetermined time in the
 * <p>
 * future.
 *
 *
 *
 * <p>Note that the fail-fast behavior of an iterator cannot be guaranteed
 * <p>
 * as it is, generally speaking, impossible to make any hard guarantees in the
 * <p>
 * presence of unsynchronized concurrent modification.  Fail-fast iterators
 * <p>
 * throw <tt>ConcurrentModificationException</tt> on a best-effort basis.
 * <p>
 * Therefore, it would be wrong to write a program that depended on this
 * <p>
 * exception for its correctness: <i>the fail-fast behavior of iterators
 * <p>
 * should be used only to detect bugs.</i>
 *
 *
 *
 * <p>This class is a member of the
 *
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * <p>
 * Java Collections Framework</a>.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * @author Doug Lea
 * @author Josh Bloch
 * @author Arthur van Hoff
 * @author Neal Gafter
 * @see Object#hashCode()
 * @see Collection
 * @see Map
 * @see TreeMap
 * @see Hashtable
 * @since 1.2
 */

// jdk1.7代码
public class HashMap1_7<K, V>

        extends AbstractMap<K, V>

        implements Map<K, V>, Cloneable, Serializable {


    /**
     * The default initial capacity - MUST be a power of two.
     */
    // 默认的HashMap的空间大小16
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16


    /**
     * The maximum capacity, used if a higher value is implicitly specified
     * <p>
     * by either of the constructors with arguments.
     * <p>
     * MUST be a power of two <= 1<<30.
     */
    // hashMap最大的空间大小
    static final int MAXIMUM_CAPACITY = 1 << 30;


    /**
     * The load factor used when none specified in constructor.
     */
    // HashMap默认负载因子，负载因子越小，hash冲突机率越低，至于为什么，看完下面源码就知道了
    static final float DEFAULT_LOAD_FACTOR = 0.75f;


    /**
     * An empty table instance to share when the table is not inflated.
     */

    static final Entry<?, ?>[] EMPTY_TABLE = {};


    /**
     * The table, resized as necessary. Length MUST Always be a power of two.
     */
    // table就是HashMap实际存储数组的地方
    transient Entry<K, V>[] table = (Entry<K, V>[]) EMPTY_TABLE;


    /**
     * The number of key-value mappings contained in this map.
     */
    // HashMap 实际存储的元素个数
    transient int size;


    /**
     * The next size value at which to resize (capacity * load factor).
     *
     * @serial
     */

    // If table == EMPTY_TABLE then this is the initial capacity at which the

    // table will be created when inflated.
    // 临界值（超过这个值则开始扩容）,公式为(threshold = capacity * loadFactor)
    int threshold;


    /**
     * The load factor for the hash table.
     *
     * @serial
     */
    // HashMap 负载因子
    final float loadFactor;


    /**
     * The number of times this HashMap has been structurally modified
     * <p>
     * Structural modifications are those that change the number of mappings in
     * <p>
     * the HashMap or otherwise modify its internal structure (e.g.,
     * <p>
     * rehash).  This field is used to make iterators on Collection-views of
     * <p>
     * the HashMap fail-fast.  (See ConcurrentModificationException).
     */

    transient int modCount;


    /**
     * The default threshold of map capacity above which alternative hashing is
     * <p>
     * used for String keys. Alternative hashing reduces the incidence of
     * <p>
     * collisions due to weak hash code calculation for String keys.
     * <p>
     * <p/>
     * <p>
     * This value may be overridden by defining the system property
     * <p>
     * {@code jdk.map.althashing.threshold}. A property value of {@code 1}
     * <p>
     * forces alternative hashing to be used at all times whereas
     * <p>
     * {@code -1} value ensures that alternative hashing is never used.
     */

    static final int ALTERNATIVE_HASHING_THRESHOLD_DEFAULT = Integer.MAX_VALUE;


    /**
     * holds values which can't be initialized until after VM is booted.
     */

    private static class Holder {


        /**
         * Table capacity above which to switch to use alternative hashing.
         */

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


    /**
     * A randomizing value associated with this instance that is applied to
     * <p>
     * hash code of keys to make hash collisions harder to find. If 0 then
     * <p>
     * alternative hashing is disabled.
     */

    transient int hashSeed = 0;


    /**
     * Constructs an empty <tt>HashMap</tt> with the specified initial
     * <p>
     * capacity and load factor.
     *
     * @param initialCapacity the initial capacity
     * @param loadFactor      the load factor
     * @throws IllegalArgumentException if the initial capacity is negative
     *                                  <p>
     *                                  or the load factor is nonpositive
     */

    public HashMap1_7(int initialCapacity, float loadFactor) {

        if (initialCapacity < 0)

            throw new IllegalArgumentException("Illegal initial capacity: " +

                    initialCapacity);

        if (initialCapacity > MAXIMUM_CAPACITY)

            initialCapacity = MAXIMUM_CAPACITY;

        if (loadFactor <= 0 || Float.isNaN(loadFactor))

            throw new IllegalArgumentException("Illegal load factor: " +

                    loadFactor);


        this.loadFactor = loadFactor;

        threshold = initialCapacity;

        init();

    }


    /**
     * Constructs an empty <tt>HashMap</tt> with the specified initial
     * <p>
     * capacity and the default load factor (0.75).
     *
     * @param initialCapacity the initial capacity.
     * @throws IllegalArgumentException if the initial capacity is negative.
     */

    public HashMap1_7(int initialCapacity) {

        this(initialCapacity, DEFAULT_LOAD_FACTOR);

    }


    /**
     * Constructs an empty <tt>HashMap</tt> with the default initial capacity
     * <p>
     * (16) and the default load factor (0.75).
     */

    public HashMap1_7() {

        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);

    }


    /**
     * Constructs a new <tt>HashMap</tt> with the same mappings as the
     * <p>
     * specified <tt>Map</tt>.  The <tt>HashMap</tt> is created with
     * <p>
     * default load factor (0.75) and an initial capacity sufficient to
     * <p>
     * hold the mappings in the specified <tt>Map</tt>.
     *
     * @param m the map whose mappings are to be placed in this map
     * @throws NullPointerException if the specified map is null
     */

    public HashMap1_7(Map<? extends K, ? extends V> m) {

        this(Math.max((int) (m.size() / DEFAULT_LOAD_FACTOR) + 1,

                DEFAULT_INITIAL_CAPACITY), DEFAULT_LOAD_FACTOR);

        inflateTable(threshold);


        putAllForCreate(m);

    }


    static int roundUpToPowerOf2(int number) {

        // assert number >= 0 : "number must be non-negative";
        // 返回最接近临界值的2的N次方
        return number >= MAXIMUM_CAPACITY

                ? MAXIMUM_CAPACITY

                : (number > 1) ? Integer.highestOneBit((number - 1) << 1) : 1;

    }


    /**
     * Inflates the table.
     */

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


    /**
     * Initialization hook for subclasses. This method is called
     * <p>
     * in all constructors and pseudo-constructors (clone, readObject)
     * <p>
     * after HashMap has been initialized but before any entries have
     * <p>
     * been inserted.  (In the absence of this method, readObject would
     * <p>
     * require explicit knowledge of subclasses.)
     */

    void init() {

    }


    /**
     * Initialize the hashing mask value. We defer initialization until we
     * <p>
     * really need it.
     */

    final boolean initHashSeedAsNeeded(int capacity) {
        // 初始化的时候hashSeed为0,0!=0 这时为false.
        boolean currentAltHashing = hashSeed != 0;

        boolean useAltHashing = sun.misc.VM.isBooted() &&

                (capacity >= Holder.ALTERNATIVE_HASHING_THRESHOLD);

        boolean switching = currentAltHashing ^ useAltHashing;

        if (switching) {
            // Tony，为了防止放在项目里面编译报错，这段代码是我注释的
//            hashSeed = useAltHashing
//
//                ? sun.misc.Hashing.randomHashSeed(this)
//
//                : 0;

        }

        return switching;

    }


    /**
     * Retrieve object hash code and applies a supplemental hash function to the
     * <p>
     * result hash, which defends against poor quality hash functions.  This is
     * <p>
     * critical because HashMap uses power-of-two length hash tables, that
     * <p>
     * otherwise encounter collisions for hashCodes that do not differ
     * <p>
     * in lower bits. Note: Null keys always map to hash 0, thus index 0.
     */

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


    /**
     * Returns index for hash code h.
     */

    static int indexFor(int h, int length) {

        // assert Integer.bitCount(length) == 1 : "length must be a non-zero power of 2";
        // 简单理解就是hash值和长度取模
        return h & (length - 1);

    }


    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map
     */

    public int size() {

        return size;

    }


    /**
     * Returns <tt>true</tt> if this map contains no key-value mappings.
     *
     * @return <tt>true</tt> if this map contains no key-value mappings
     */

    public boolean isEmpty() {

        return size == 0;

    }


    /**
     * Returns the value to which the specified key is mapped,
     * <p>
     * or {@code null} if this map contains no mapping for the key.
     *
     *
     *
     * <p>More formally, if this map contains a mapping from a key
     * <p>
     * {@code k} to a value {@code v} such that {@code (key==null ? k==null :
     * <p>
     * key.equals(k))}, then this method returns {@code v}; otherwise
     * <p>
     * it returns {@code null}.  (There can be at most one such mapping.)
     *
     *
     *
     * <p>A return value of {@code null} does not <i>necessarily</i>
     * <p>
     * indicate that the map contains no mapping for the key; it's also
     * <p>
     * possible that the map explicitly maps the key to {@code null}.
     * <p>
     * The {@link #containsKey containsKey} operation may be used to
     * <p>
     * distinguish these two cases.
     *
     * @see #put(Object, Object)
     */

    public V get(Object key) {

        if (key == null)

            return getForNullKey();

        Entry<K, V> entry = getEntry(key);


        return null == entry ? null : entry.getValue();

    }


    /**
     * Offloaded version of get() to look up null keys.  Null keys map
     * <p>
     * to index 0.  This null case is split out into separate methods
     * <p>
     * for the sake of performance in the two most commonly used
     * <p>
     * operations (get and put), but incorporated with conditionals in
     * <p>
     * others.
     */

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


    /**
     * Returns <tt>true</tt> if this map contains a mapping for the
     * <p>
     * specified key.
     *
     * @param key The key whose presence in this map is to be tested
     * @return <tt>true</tt> if this map contains a mapping for the specified
     * <p>
     * key.
     */

    public boolean containsKey(Object key) {

        return getEntry(key) != null;

    }


    /**
     * Returns the entry associated with the specified key in the
     * <p>
     * HashMap.  Returns null if the HashMap contains no mapping
     * <p>
     * for the key.
     */

    final Entry<K, V> getEntry(Object key) {

        if (size == 0) {

            return null;

        }


        int hash = (key == null) ? 0 : hash(key);
        // 确定key对应的数组位置,遍历查找比对链表内容
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


    /**
     * Associates the specified value with the specified key in this map.
     * <p>
     * If the map previously contained a mapping for the key, the old
     * <p>
     * value is replaced.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with <tt>key</tt>, or
     *
     * <tt>null</tt> if there was no mapping for <tt>key</tt>.
     * <p>
     * (A <tt>null</tt> return can also indicate that the map
     * <p>
     * previously associated <tt>null</tt> with <tt>key</tt>.)
     */

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


    /**
     * This method is used instead of put by constructors and
     * <p>
     * pseudoconstructors (clone, readObject).  It does not resize the table,
     * <p>
     * check for comodification, etc.  It calls createEntry rather than
     * <p>
     * addEntry.
     */

    private void putForCreate(K key, V value) {

        int hash = null == key ? 0 : hash(key);

        int i = indexFor(hash, table.length);


        /**
         * Look for preexisting entry for key.  This will never happen for
         * clone or deserialize.  It will only happen for construction if the
         * input Map is a sorted map whose ordering is inconsistent w/ equals.

         */

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


    /**
     * Rehashes the contents of this map into a new array with a
     * <p>
     * larger capacity.  This method is called automatically when the
     * <p>
     * number of keys in this map reaches its threshold.
     * <p>
     * <p>
     * <p>
     * If current capacity is MAXIMUM_CAPACITY, this method does not
     * <p>
     * resize the map, but sets threshold to Integer.MAX_VALUE.
     * <p>
     * This has the effect of preventing future calls.
     *
     * @param newCapacity the new capacity, MUST be a power of two;
     *                    <p>
     *                    must be greater than current capacity unless current
     *                    <p>
     *                    capacity is MAXIMUM_CAPACITY (in which case value
     *                    <p>
     *                    is irrelevant).
     */

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

    void transfer(Entry[] newTable, boolean rehash) {

        int newCapacity = newTable.length;
        // 遍历旧的数组
        for (Entry<K, V> e : table) {

            while (null != e) {

                Entry<K, V> next = e.next;

                if (rehash) {

                    e.hash = null == e.key ? 0 : hash(e.key);

                }

                int i = indexFor(e.hash, newCapacity);

                e.next = newTable[i];

                newTable[i] = e;

                e = next;

            }

        }

    }


    /**
     * Copies all of the mappings from the specified map to this map.
     * <p>
     * These mappings will replace any mappings that this map had for
     * <p>
     * any of the keys currently in the specified map.
     *
     * @param m mappings to be stored in this map
     * @throws NullPointerException if the specified map is null
     */

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


    /**
     * Removes the mapping for the specified key from this map if present.
     *
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with <tt>key</tt>, or
     *
     * <tt>null</tt> if there was no mapping for <tt>key</tt>.
     * <p>
     * (A <tt>null</tt> return can also indicate that the map
     * <p>
     * previously associated <tt>null</tt> with <tt>key</tt>.)
     */

    public V remove(Object key) {

        Entry<K, V> e = removeEntryForKey(key);

        return (e == null ? null : e.value);

    }


    /**
     * Removes and returns the entry associated with the specified key
     * <p>
     * in the HashMap.  Returns null if the HashMap contains no mapping
     * <p>
     * for this key.
     */

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


    /**
     * Returns a shallow copy of this <tt>HashMap</tt> instance: the keys and
     * <p>
     * values themselves are not cloned.
     *
     * @return a shallow copy of this map
     */

    public Object clone() {

        HashMap1_7<K, V> result = null;

        try {

            result = (HashMap1_7<K, V>) super.clone();

        } catch (CloneNotSupportedException e) {

            // assert false;

        }

        if (result.table != EMPTY_TABLE) {

            result.inflateTable(Math.min(

                    (int) Math.min(

                            size * Math.min(1 / loadFactor, 4.0f),

                            // we have limits...

                            HashMap1_7.MAXIMUM_CAPACITY),

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


        public final String toString() {

            return getKey() + "=" + getValue();

        }


        /**
         * This method is invoked whenever the value in an entry is
         * <p>
         * overwritten by an invocation of put(k,v) for a key k that's already
         * <p>
         * in the HashMap.
         */

        void recordAccess(HashMap1_7<K, V> m) {

        }


        /**
         * This method is invoked whenever the entry is
         * <p>
         * removed from the table.
         */

        void recordRemoval(HashMap1_7<K, V> m) {

        }

    }


    /**
     * Adds a new entry with the specified key, value and hash code to
     * <p>
     * the specified bucket.  It is the responsibility of this
     * <p>
     * method to resize the table if appropriate.
     * <p>
     * <p>
     * <p>
     * Subclass overrides this to alter the behavior of put method.
     */

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


    /**
     * Like addEntry except that this version is used when creating entries
     * <p>
     * as part of Map construction or "pseudo-construction" (cloning,
     * <p>
     * deserialization).  This version needn't worry about resizing the table.
     * <p>
     * <p>
     * <p>
     * Subclass overrides this to alter the behavior of HashMap(Map),
     * <p>
     * clone, and readObject.
     */
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

            HashMap1_7.this.removeEntryForKey(k);

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


    /**
     * Returns a {@link Set} view of the keys contained in this map.
     * <p>
     * The set is backed by the map, so changes to the map are
     * <p>
     * reflected in the set, and vice-versa.  If the map is modified
     * <p>
     * while an iteration over the set is in progress (except through
     * <p>
     * the iterator's own <tt>remove</tt> operation), the results of
     * <p>
     * the iteration are undefined.  The set supports element removal,
     * <p>
     * which removes the corresponding mapping from the map, via the
     *
     * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>,
     *
     * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt>
     * <p>
     * operations.  It does not support the <tt>add</tt> or <tt>addAll</tt>
     * <p>
     * operations.
     */

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

            return HashMap1_7.this.removeEntryForKey(o) != null;

        }

        public void clear() {

            HashMap1_7.this.clear();

        }

    }


    /**
     * Returns a {@link Collection} view of the values contained in this map.
     * <p>
     * The collection is backed by the map, so changes to the map are
     * <p>
     * reflected in the collection, and vice-versa.  If the map is
     * <p>
     * modified while an iteration over the collection is in progress
     * <p>
     * (except through the iterator's own <tt>remove</tt> operation),
     * <p>
     * the results of the iteration are undefined.  The collection
     * <p>
     * supports element removal, which removes the corresponding
     * <p>
     * mapping from the map, via the <tt>Iterator.remove</tt>,
     *
     * <tt>Collection.remove</tt>, <tt>removeAll</tt>,
     *
     * <tt>retainAll</tt> and <tt>clear</tt> operations.  It does not
     * <p>
     * support the <tt>add</tt> or <tt>addAll</tt> operations.
     */

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

            HashMap1_7.this.clear();

        }

    }


    /**
     * Returns a {@link Set} view of the mappings contained in this map.
     * <p>
     * The set is backed by the map, so changes to the map are
     * <p>
     * reflected in the set, and vice-versa.  If the map is modified
     * <p>
     * while an iteration over the set is in progress (except through
     * <p>
     * the iterator's own <tt>remove</tt> operation, or through the
     *
     * <tt>setValue</tt> operation on a map entry returned by the
     * <p>
     * iterator) the results of the iteration are undefined.  The set
     * <p>
     * supports element removal, which removes the corresponding
     * <p>
     * mapping from the map, via the <tt>Iterator.remove</tt>,
     *
     * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and
     *
     * <tt>clear</tt> operations.  It does not support the
     *
     * <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a set view of the mappings contained in this map
     */

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

            HashMap1_7.this.clear();

        }

    }


    /**
     * Save the state of the <tt>HashMap</tt> instance to a stream (i.e.,
     * <p>
     * serialize it).
     *
     * @serialData The <i>capacity</i> of the HashMap (the length of the
     * <p>
     * bucket array) is emitted (int), followed by the
     *
     * <i>size</i> (an int, the number of key-value
     * <p>
     * mappings), followed by the key (Object) and value (Object)
     * <p>
     * for each key-value mapping.  The key-value mappings are
     * <p>
     * emitted in no particular order.
     */

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

                HashMap1_7.MAXIMUM_CAPACITY);


        // allocate the bucket array;

        if (mappings > 0) {

            inflateTable(capacity);

        } else {

            threshold = capacity;

        }


        init();  // Give subclass a chance to do its thing.


        // Check Map.Entry[].class since it's the nearest public type to

        // what we're actually creating.
        // Tony: 下面这行代码是我注释的,为了防止编译报错
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