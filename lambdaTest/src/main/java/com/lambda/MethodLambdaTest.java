package com.lambda;

import java.util.Comparator;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**lambda的方法引用模式
 * @author wzq.Jolin
 * @company none
 * @create 2019-03-16 19:41
 */
public class MethodLambdaTest {
    public static void main(String[] args) {

        /**
         * 方法引用的第一种方式
         * 对象::实例方法
         * 注意:接口的抽象方法的参数和返回值必须和实例方法的参数和返回值类型保持一致
         */
       // Consumer<String> con =(e)-> System.out.println(e);  //普通的lambda表达式
        Consumer<String> con = System.out::println; //lambda方法引用方式
        con.accept("hello lambda");


        Employee employee = new Employee();
        employee.setAge(26);
//      Supplier<Integer> sup = () -> employee.getAge();   //普通的lambda表达式
        Supplier<Integer>  sup  = employee::getAge;   //lambda方法引用方式
        Integer age = sup.get();
        System.out.println(age);

        /*-----------------------------------------*/

        /**
         * 方法引用的第二种方式
         * 类名::静态方法名
         * 注意:接口的抽象方法的参数和返回值必须和实例方法的参数和返回值类型保持一致
         */
        //Comparator<Integer> com = (x, y)->Integer.compare(x,y); //普通的lambda表达式
        Comparator<Integer> com = Integer::compare; //lambda方法引用方式
        int compare = com.compare(1, 2);
        System.out.println(compare);

        /*-----------------------------------------*/

        /**
         * 方法引用第三种方式
         * 类名::实例方法名
         * 注意:若Lambda 参数列表中的第一个参数是实例方法的调用者,而第二个参数是实例方法的参数时才可以使用
         * 这种方式
         */
       // BiPredicate<String, String> bp =(x, y)->x.equals(y);  //普通的lambda表达
        BiPredicate<String, String> bp = String::equals;   //lambda方法引用方式
        boolean test = bp.test("哈哈", "哈哈");
        System.out.println(test);

    }
}
