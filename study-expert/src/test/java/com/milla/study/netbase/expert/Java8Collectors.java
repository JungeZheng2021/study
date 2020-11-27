package com.milla.study.netbase.expert;

import lombok.Data;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * @Package: com.milla.study.netbase.expert
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/09/02 12:58
 * @UpdateUser: milla
 * @UpdateDate: 2020/09/02 12:58
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class Java8Collectors {
    final static List<Book> books = new ArrayList<>();

    public static void main(String[] args) {
        initBooks();
        testAvg();
        testCollectingAndThen();
        testCounting();
        testGroupBy();
        testGroupByConcurrent();
        testJoining();
        testMapping();
        testMaxBy();
        testMaxMin();
        testPartitioningBy();
        testReducing();
        testSummingDouble();
        testToCollection();
        testToConcurrentMap();
        testToMap();
    }

    /**
     *转换成map
     */
    private static void testToMap() {
        //调用merge的时候有空值校验，如果值为null会报空指针//HashMap
        Map<String, Double> collect = books.stream().collect(Collectors.toMap(Book::getName, item -> item.getPrice() == null ? 0 : item.getPrice()));
        System.out.println("书名和价格： " + collect);

        Map<String, String> collect1 = books.stream().collect(Collectors.toMap(Book::getAuthor, Book::getName, (one, two) -> one));//HashMap
        System.out.println("作者和书名-有重复的选择第一个书名： " + collect1);

        ConcurrentHashMap<String, String> collect2 = books.stream().collect(Collectors.toMap(Book::getAuthor, Book::getName,
                (one, two) -> one.concat(two), ConcurrentHashMap::new));
        System.out.println("作者和书名-有重复的选择第一个书名-指定返回值类型： " + collect2);
    }

    /**
     * 支持高并发
     */
    private static void testToConcurrentMap() {

        //ConcurrentHashMap 不支持null键和null值，所以在处理的时候需要注意
        ConcurrentMap<String, Double> collect = books.stream().collect(Collectors.toConcurrentMap(Book::getName,
                item -> item.getPrice() == null ? 0 : item.getPrice()));
        System.out.println("书名和价格： " + collect);

        //出现相同的key的时候会报错-故而需要指定有相同key时候的处理策略
        ConcurrentMap<String, String> collect1 = books.stream().collect(Collectors.toConcurrentMap(Book::getAuthor,
                item -> item.getName(), (left, right) -> left));
        System.out.println("作者和书名： " + collect1);


        ConcurrentSkipListMap<String, Double> collect2 = books.stream().collect(Collectors.toConcurrentMap(Book::getAuthor,
                item -> item.getPrice() == null ? 0 : item.getPrice(), (a, b) -> a + b, ConcurrentSkipListMap::new));
        System.out.println("作者和书的单价和： " + collect2);
    }

    /**
     * 转换成集合
     */
    private static void testToCollection() {
        //转换成set集合
        Set<Book> collect2 = books.stream().collect(Collectors.toSet());//HashSet
        Set<Book> set = books.stream().collect(Collectors.toCollection(HashSet::new));

        //
        List<Book> collect3 = books.stream().collect(Collectors.toList());//ArrayList

        LinkedList<Book> list = books.stream().collect(Collectors.toCollection(LinkedList::new));

        ArrayList<Book> collect = books.stream().collect(Collectors.toCollection(ArrayList::new));

        CopyOnWriteArrayList<Book> collect1 = books.stream().collect(Collectors.toCollection(CopyOnWriteArrayList::new));

        //toCollection可以指定转换成的集合
    }

    /**
     * 求和
     */
    private static void testSummingDouble() {
        Integer pages = books.stream().collect(Collectors.summingInt(Book::getPages));
        System.out.println("所有书籍的页数总和： " + pages);

        Long inventories = books.stream().collect(Collectors.summingLong(Book::getInventory));
        System.out.println("所有书籍的库存总和： " + inventories);

        Double price = books.stream().collect(Collectors.summingDouble(item -> item.getPrice() == null ? 0D : item.getPrice()));
        System.out.println("所有书籍的单价总和： " + price);

        Double priceNotNull = books.stream().filter(item -> Objects.nonNull(item.getPrice()))
                .collect(Collectors.summingDouble(item -> item.getPrice() == null ? 0D : item.getPrice()));
        System.out.println("所有书籍的单价总和： " + priceNotNull);

    }

    /**
     * 各种运算
     */
    private static void testReducing() {
        //获取库存最大的书籍
        Book book = books.stream().collect(Collectors.reducing(BinaryOperator.maxBy(Comparator.comparingLong(Book::getInventory)))).get();
        System.out.println("库存最大的书籍： " + book);

        Book book1 = books.stream().reduce(BinaryOperator.maxBy(Comparator.comparingLong(Book::getInventory))).get();
        System.out.println("库存最大的书籍： " + book1);

        //获取书籍的总库存数量
        Long inventoryC = books.stream().map(Book::getInventory).reduce(0L, (left, right) -> left + right);
        System.out.println("所有书籍的库存： " + inventoryC);

        Long inventory = books.stream().map(Book::getInventory).collect(Collectors.reducing(0L, (left, right) -> left + right));
        System.out.println("所有书籍的库存： " + inventory);

        //获取所有书籍的总页数
        Integer pagesC = books.stream().collect(Collectors.reducing(0, Book::getPages, (left, right) -> left + right));
        System.out.println("所有书籍的页数总和： " + pagesC);

        Integer pages = books.stream().map(Book::getPages).collect(Collectors.reducing(0, (left, right) -> left + right));
        System.out.println("所有书籍的页数总和： " + pages);

    }

    /**
     * 根据条件分类，返回的是true和false作为key的map
     */
    private static void testPartitioningBy() {
        //查询java类别数据和非java类别数据的map集合
        Map<Boolean, List<Book>> java = books.stream().collect(Collectors.partitioningBy(item -> item.getCategory().equals("Java")));
        System.out.println("Java书籍和非Java书籍的集合： " + java);

        //平均价格
        Map<Boolean, Double> avgPrice = books.stream().collect(Collectors.partitioningBy(item -> item.getCategory().equals("Java"),
                Collectors.averagingDouble(item -> item.getPrice() == null ? 0 : item.getPrice())));
        System.out.println("java书籍和非java书籍的平均价格： " + avgPrice);

        //分类的数量
        Map<Boolean, Long> count = books.stream().collect(Collectors.partitioningBy(item -> item.getCategory().equals("Java"), Collectors.counting()));
        System.out.println("java书籍和非java书籍的书籍数量： " + count);

    }

    /**
     * 求最小值
     */
    private static void testMaxMin() {
        Book book = books.stream().min(Comparator.comparing(Book::getInventory)).get();
        System.out.println("库存最小的书籍： " + book);

        Book book1 = books.stream().collect(Collectors.minBy(Comparator.comparing(Book::getInventory))).get();
        System.out.println("库存最小的书籍： " + book1);
    }

    /**
     * 求最大值
     */

    private static void testMaxBy() {
        //求单价最贵的书籍
        Book book1 = books.stream().collect(Collectors.maxBy((o1, o2) -> o1.getPrice() == null ? 0 : o1.getPrice()
                .compareTo(o2.getPrice() == null ? 0 : o2.getPrice()))).get();
        System.out.println("单价最贵的书籍： " + book1);

        Book book = books.stream().max((left, right) -> left.getPrice() == null ? 0 : left.getPrice()
                .compareTo(right.getPrice() == null ? 0 : right.getPrice())).get();
        System.out.println("单价最贵的书籍： " + book);
    }

    /**
     * 获取指定的字段或数据
     */
    private static void testMapping() {
        List<String> authors = books.stream().collect(Collectors.mapping(Book::getAuthor, Collectors.toList()));
        System.out.println("获取指定字段的集合： " + authors);//[张三, 李四, 王五, 张三, 李四, 王五, 张三, 李四, 王五, 张三, 张三, 张三]

        List<String> authorList = books.stream().map(Book::getAuthor).collect(Collectors.toList());
        System.out.println("获取指定字段的集合： " + authorList);//[张三, 李四, 王五, 张三, 李四, 王五, 张三, 李四, 王五, 张三, 张三, 张三]

        List<String> authorDistinct = books.stream().map(Book::getAuthor).distinct().collect(Collectors.toList());
        System.out.println("获取指定字段的集合： " + authorDistinct);//[张三, 李四, 王五]

    }

    /**
     * 将集合元素连接成字符串
     */
    private static void testJoining() {
        //直接相连
        String authors = books.stream().map(Book::getAuthor).collect(Collectors.joining());
        System.out.println("获取作者集合字符串： " + authors);//张三李四王五张三李四王五张三李四王五张三张三张三
        //增加连接符 - distinct去除重复元素
        String authorsJoin = books.stream().map(Book::getAuthor).distinct().collect(Collectors.joining(","));
        System.out.println("获取作者集合字符串-有连接符： " + authorsJoin);//张三,李四,王五
        String collect = books.stream().map(Book::getAuthor).distinct().collect(Collectors.joining(",", "[", "]"));
        System.out.println("获取作者集合字符串-有连接符-有开始结束符号： " + collect);//[张三,李四,王五]
    }

    /**
     * 分组能够保证线程安全
     */
    private static void testGroupByConcurrent() {
        ConcurrentMap<String, List<Book>> collect = books.stream().collect(Collectors.groupingByConcurrent(Book::getCategory));
        System.out.println("根据书籍种类进行分组： " + collect);
        ConcurrentMap<String, Long> collect1 = books.stream().collect(Collectors.groupingByConcurrent(Book::getCategory, Collectors.counting()));
        System.out.println("根据书籍种类进行分组并计算数量： " + collect1);
    }

    /**
     * 分组
     */
    private static void testGroupBy() {
        //根据书籍种类分组
        Map<String, List<Book>> collect = books.stream().collect(Collectors.groupingBy(Book::getCategory));
        //根据书籍种类分组计数
        Map<String, Long> collect1 = books.stream().collect(Collectors.groupingBy(Book::getCategory, Collectors.counting()));
        //获取书籍种类的平均值 -默认是hashMap是无序的
        Map<String, Double> sorted = books.stream().collect(Collectors.groupingBy(Book::getCategory, Collectors.averagingDouble(item -> item.getPrice() == null ? 0 : item.getPrice())));
        System.out.println("获取书籍种类的平均值： " + sorted);
        //倒序排列-key的倒序-自定义一个比较器
        Map<String, Double> sortedTreeMapComparator = books.stream().collect(Collectors.groupingBy(Book::getCategory, () -> new TreeMap<>(Comparator.reverseOrder()), Collectors.averagingDouble((item -> item.getPrice() == null ? 0 : item.getPrice()))));
        System.out.println("获取书籍种类的平均值-倒序： " + sortedTreeMapComparator);
        //倒序排列-key的倒序-treeMp指定是降序map，调用descendingMap
        Map<String, Double> sortedTreeMap = books.stream().collect(Collectors.groupingBy(Book::getCategory, () -> new TreeMap().descendingMap(), Collectors.averagingDouble((item -> item.getPrice() == null ? 0 : item.getPrice()))));
        System.out.println("获取书籍种类的平均值-倒序： " + sortedTreeMap);

    }

    /**
     * 获取计数
     */
    private static void testCounting() {
        //返回所有集合的元素数量
        Long countNum = books.stream().collect(Collectors.counting());
        System.out.println(countNum);

        //如果没有任何数据的话，会返回0
        Long collect = books.stream().filter(item -> item.getAuthor().equals("11")).collect(Collectors.counting());
        System.out.println(collect);
        Long count = books.stream().filter(item -> item.getAuthor().equals("11")).count();
        System.out.println(count);
    }

    /**
     * 现获取数据，然后再对数据进行操作
     */
    private static void testCollectingAndThen() {
        Map<String, List<Book>> upPrice = books.stream().collect(Collectors.collectingAndThen(Collectors.groupingBy(Book::getAuthor), items -> {
            items.get("张三").stream().forEach(item -> item.setPrice(item.getPrice() == null ? 0 : item.getPrice() + 10000));
            return items;
        }));

        System.out.println("按照作者分组并将张三的书籍涨价: " + upPrice);

        //根据作者分组--去除作者是张三的书籍

        Map<String, List<Book>> author = books.stream().collect(Collectors.collectingAndThen(Collectors.groupingBy(Book::getAuthor), items -> {
            items.remove("张三");
            return items;
        }));
        System.out.println("根据作者分组--去除作者是张三的书籍: " + author);

        //本次结果和上面的结果一致
        Map<String, List<Book>> collect = books.stream().filter(item -> "张三".equals(item.getAuthor())).collect(Collectors.groupingBy(Book::getAuthor));
        System.out.println(collect);
    }

    /**
     * 获取Integer/Long/Double类型的平均值
     */
    private static void testAvg() {
        //获取所有的图书的平均售价
        Double priceAvg = books.stream().collect(Collectors.averagingDouble(item -> item.getPrice() == null ? 0D : item.getPrice()));
        Optional.ofNullable(priceAvg).ifPresent(System.out::println);
        //所有图书的平均页数
        Double pageAvg = books.stream().collect(Collectors.averagingInt(item -> item.getPages() == null ? 0 : item.getPages()));
        Optional.ofNullable(pageAvg).ifPresent(System.out::println);
        //所有图书的平均库存
        Double inventoryAvg = books.stream().collect(Collectors.averagingLong(item -> item.getInventory() == null ? 0L : item.getInventory()));
        Optional.ofNullable(inventoryAvg).ifPresent(System.out::println);
    }

    private static void initBooks() {
        books.add(new Book("书名1", "Java", "张三", 53.2, 135, 52301L));
        books.add(new Book("书名2", "Java", "李四", 49.3D, 246, 525301L));
        books.add(new Book("书名3", "Java", "王五", 26.3D, 186, 523101L));
        books.add(new Book("书名4", "Python", "张三", 39.3D, 98, 523101L));
        books.add(new Book("书名5", "Python", "李四", 59.3D, 169, 523021L));
        books.add(new Book("书名6", "Python", "王五", 63.3D, 489, 523L));
        books.add(new Book("书名7", "C#", "张三", 72.3D, 984, 752301L));
        books.add(new Book("书名8", "C#", "李四", 48.3D, 532, 9301L));
        books.add(new Book("书名9", "C#", "王五", 47.3D, 587, 5801L));
        books.add(new Book("书名10", "Netty", "张三", 68.3D, 653, 501L));
        books.add(new Book("书名11", "Netty", "张三", 61.3D, 782, 562301L));
        books.add(new Book("书名12", "Netty", "张三", null, 698, 542301L));
    }


    @Data
    static class Book {
        /**
         * 书名
         */
        private String name;
        /**
         * 类型
         */
        private String category;
        /**
         * 作者
         */
        private String author;
        /**
         * 价格
         */
        private Double price;
        /**
         * 页数
         */
        private Integer pages;
        /**
         * 库存数量
         */
        private Long inventory;

        public Book(String name, String category, String author, Double price, Integer pages, Long inventory) {
            this.name = name;
            this.category = category;
            this.author = author;
            this.price = price;
            this.pages = pages;
            this.inventory = inventory;
        }
    }
}

