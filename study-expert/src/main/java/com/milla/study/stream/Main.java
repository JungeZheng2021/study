package com.milla.study.stream;

import com.milla.study.stream.vo.StudentVO;
import com.milla.study.stream.vo.TeacherVO;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @Package: com.milla.study.stream
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/6/23 11:19
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/23 11:19
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class Main {
    static Stream<StudentVO> stream = Stream.of(
//            new StudentVO("AA", "上海", "女", 22),
//            new StudentVO("BB", "南京", "男", 20),
            new StudentVO("BB", "杭州", "女", 19),
            new StudentVO("CC", "杭州", "男", 20),
            new StudentVO("DD", "南京", "女", 22),
            new StudentVO("DD", "上海", "女", 23),
            new StudentVO("DD", "北京", "男", 22),
            new StudentVO("EE", "杭州", "男", 23),
            new StudentVO("FF", "上海", "女", 24));

    public static void main(String[] args) {
//        new Main().test();
        new Main().demo();
    }

    private void demo() {

        CopyOnWriteArrayList<Object> objects = new CopyOnWriteArrayList<>();
        CopyOnWriteArraySet<Object> objects1 = new CopyOnWriteArraySet<>();
        objects1.add("1");

        int[] src = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] desc = new int[5];
        System.arraycopy(src, 3, desc, 1, desc.length);
        System.out.println(Arrays.toString(desc));


        //
//        List<StudentVO> list = stream.collect(Collectors.toList());
//
//        StudentVO studentVO = list.stream().findAny().get();
//
//        StudentVO studentVO1 = list.stream().findFirst().get();
//        System.out.println(studentVO1);
//        System.out.println(studentVO);


    }

    private void test() {
        //将学生对象变成集合
        List<StudentVO> list = stream.collect(Collectors.toList());
        System.out.println("学生集合： " + list);
        //获取所有学生的姓名-集合
        List<String> usernameList = list.stream().map(StudentVO::getUsername).collect(Collectors.toList());
        List<String> usernameList2 = list.stream().collect(Collectors.mapping(studentVO -> studentVO.getUsername(), Collectors.toList()));
        System.out.println("学生姓名集合：" + usernameList);
        System.out.println("学生姓名集合：" + usernameList2);
        //合并学生的姓名-连接程字符串
        String usernameJoin = list.stream().map(StudentVO::getUsername).collect(Collectors.joining("-"));
        System.out.println("学生姓名连接： " + usernameJoin);
        //地址在杭州的学生数量
        Stream<StudentVO> voStream = list.stream().filter(stu -> stu.getCity().equals("杭州")).distinct().limit(5).skip(2);


        long countHz = list.stream().filter(stu -> stu.getCity().equals("杭州")).count();
        System.out.println("杭州学生的个数：" + countHz);
        //按照所在城市分组
        Map<String, List<StudentVO>> groupByList = list.stream().collect(Collectors.groupingBy(studentVO -> studentVO.getCity()));
        System.out.println("按照城市分组：" + groupByList);
        //按照年龄和姓名排序-正序
        List<StudentVO> sortedList = list.stream().sorted(Comparator.comparingInt(StudentVO::getAge).thenComparing(StudentVO::getUsername).reversed()).collect(Collectors.toList());
        System.out.println("按照年龄排序：" + sortedList);
        //学生姓名作为key转变成map->如果有有重复的key，需要做个取舍-否则会报错
        Map<String, StudentVO> usernameKey = list.stream().collect(Collectors.toMap(studentVO -> studentVO.getUsername(), studentVO -> studentVO, (one, two) -> {
            //可以根据业务保留第一个元素或者是第二个元素
            return one;
        }));
        Map<String, String> usernameKey2 = list.stream().collect(Collectors.mapping(stu -> stu.getUsername(), Collectors.toMap(stu -> stu, stu -> stu, (one, two) -> one)));
        System.out.println("将学生姓名作为key: " + usernameKey);
        System.out.println("将学生姓名作为key: " + usernameKey2);
        //根据城市分组，然后再按照学生名变成map-所有变成map的操作都需要考虑重复元素的问题
        Map<String, Map<String, StudentVO>> collect = list.stream().collect(Collectors.groupingBy(stu -> stu.getCity(), Collectors.toMap(stu -> stu.getUsername(), studentVO -> studentVO)));
        System.out.println(collect);

        //数据转换
        List<TeacherVO> collect1 = list.stream().flatMap((e) -> {
            TeacherVO teacher = new TeacherVO();
            teacher.setUsername(e.getUsername());
            teacher.setCity(e.getCity());
            return Stream.of(teacher);
        }).collect(Collectors.toList());
        System.out.println(collect1);


        list.stream().map((o) -> {
            return null;
        }).collect(Collectors.toList());
        StudentVO studentVO = list.parallelStream().findAny().get();
        System.out.println(studentVO);


        //中间操作不执行，只有终端操作的时候才会执行打印
        list.stream().peek(stu -> {
            System.out.println("学生对象：  " + stu);
        }).count();


        Map<Boolean, List<StudentVO>> collect2 = list.stream().collect(Collectors.partitioningBy(o -> o.getAge() > 20));
        System.out.println(collect2);

        StudentVO studentVO1 = list.stream().reduce((one, two) -> {
            System.out.println("one: " + one);
            System.out.println("two: " + two);
            return one;
        }).get();
        System.out.println(studentVO1);
    }
}
