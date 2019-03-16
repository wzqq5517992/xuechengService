package com.lambda;


/**
 * 普通lambda表达式 返回值接口类型demo
 */

import com.alibaba.fastjson.JSON;

import javax.swing.Timer;
import java.util.*;
import java.util.Comparator.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LambdaTest {

    public static void main(String[] args) {


        String[] planets = new String[]{"Mercuy", "Venus", "Earths", "Adfsfg"};
        // System.out.println(Arrays.toString(planets));
        //  System.out.println("按照首字母顺序排序:");
        Arrays.sort(planets);//按照首字母顺序排序
        // System.out.println(Arrays.toString(planets));

        //  System.out.println("按照长度从大到小排序:");
        Arrays.sort(planets, (x, y) -> -(x.length() - y.length()));  //按照字符串长度从大到小排序
        //System.out.println(Arrays.toString(planets));

        Timer timer = new Timer(1000, event -> System.out.println("the timer is:" + new Date()));
        timer.start();


        /**
         * 比较对象的薪资属性进行倒序排序
         */
        List<Employee> employeeList = Arrays.asList(
                new Employee(1, "小花", 22, 3500),
                new Employee(2, "小明虎", 18, 5500),
                new Employee(3, "小明虎", 18, 6500),
                new Employee(4, "小白", 20, 8500));
        // employeeList.stream().sorted((x, y) -> -Double.compare(x.getSalary(), y.getSalary())).forEach(System.out::println);
        Stream<Employee> employeeStream = employeeList.stream().sorted((x, y) -> -Double.compare(x.getSalary(), y.getSalary()));
        //List<Object> asList = Arrays.asList(employeeStream.toArray());
        /**
         * 员工属性多级排序
         */
        Employee[] objectArray = employeeStream.toArray(Employee[]::new);//转化为对象数组
        Arrays.sort(objectArray,  Comparator.comparing(Employee::getAge));//根据年龄正序排序
        Arrays.sort(objectArray,Comparator.comparing(Employee::getName,(x,y)->Integer.compare(x.length(),y.length())));//按照名字长度进行正序排序的第一种方式
        Arrays.sort(objectArray,Comparator.comparingInt(p->p.getName().length()));//按照名字长度进行正序排序的第二种方式
        Arrays.sort(objectArray,Comparator.comparing(Employee::getName).thenComparingDouble(Employee::getSalary));//按照名字和薪资正序排序
        Arrays.sort(objectArray,  Comparator.comparing(Employee::getRemark,Comparator.nullsFirst(null)));//根据
        Arrays.sort(objectArray,  Comparator.comparing(Employee::getRemark,Comparator.nullsFirst(null).reversed()));//根据
        System.out.println("对象数组比较年龄:"+Arrays.toString(objectArray));



        List<Employee> list1 = Arrays.asList(objectArray);//对线数组转化为list套对象
        //  System.out.println("wzq:" + list1);

        /**
         * 按照姓名进行分组排序
         */
        List<Employee> employeeLists = Arrays.asList(
                new Employee(1, "小花", 22, 3500),
                new Employee(2, "小明", 18, 5500),
                new Employee(3, "小白", 20, 8500),
                new Employee(4, "小白", 22, 8000));

        Map<String, List<Employee>> map = employeeLists.stream().collect(Collectors.groupingBy(Employee::getName));  //按照姓名进行分组
        String jsonString = JSON.toJSONString(map);
        //   System.out.println(jsonString);
        // employeeLists.stream().filter((e)->e.getSalary()>5500).forEach(System.out::println); //薪资大于5500的员工

        List<String> names = Arrays.asList("wzzq", "asdasdsd");
        Stream<Employee> employeestream = names.stream().map(Employee::new);
        List<Employee> list = employeestream.collect(Collectors.toList());
        System.out.println("list:" + list);


        /**
         * 消费式接口 Consumer
         */
        //1.accept输入什么参数  son输出什么  无返回类型
        Consumer<String> con = (fff) -> System.out.println(fff);//普通的lambda表达式
        con.accept("李晨渣渣!");


        /**
         * Supplier 供给型接口,无形参 即无入参
         */
        Employee employee = new Employee();
        employee.setName("haha");
        Supplier<String> sup = () -> employee.getName();
        //  Supplier<Employee> sups = ()-> new Employee();
        System.out.println(sup.get());

        /**
         * 断定型接口  返回类型为boolean  形参可多个
         */
        BiPredicate<String, String> predicate = (e, y) -> e.equals(y);
        boolean test1 = predicate.test("syx", "wzq");
        System.out.println(test1);

        /**
         * Comparator   Predicate的子接口
         */
        Comparator<Integer> com = (e1, e2) -> e1.compareTo(e2);
        int result = com.compare(2, 1);
        System.out.println("result:" + result);

        /**
         * 函数型接口     形参和返回值都存在   形参支持多个
         */
        Function<String, String> fun = (e) -> e.toUpperCase();
        String apply = fun.apply("s");
        System.out.println(apply);

        //*****************************************************



        /*-----------------------------------------*/

        /**
         * 方法引用的第二种方式
         * 类名::静态方法名
         * 注意:接口的抽象方法的参数和返回值必须和实例方法的参数和返回值类型保持一致
         */
        //  Comparator<Integer> com = (x, y)->Integer.compare(x,y) //普通的lambda表达式
        Comparator<Integer> cosm = Integer::compare; //lambda方法引用方式
        int compare = cosm.compare(1, 2);
        System.out.println(compare);


        /*-----------------------------------------*/

        /**
         * 方法引用第三种方式
         * 类名::实例方法名
         * 注意:若Lambda 参数列表中的第一个参数是实例方法的调用者,而第二个参数是实例方法的参数时才可以使用
         * 这种方式
         */
        //  BiPredicate<String, String> bp =(x, y)->x.equals(y);  //普通的lambda表达
        BiPredicate<String, String> bp = String::equals;   //lambda方法引用方式
        boolean test = bp.test("哈哈", "哈哈");
        System.out.println(test);


    }
}