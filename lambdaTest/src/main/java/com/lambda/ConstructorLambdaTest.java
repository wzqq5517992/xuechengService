package com.lambda;

import java.util.function.Supplier;

/**
 * lambda的构造器引用模式
 *
 * @author wzq.Jolin
 * @company none
 * @create 2019-03-16 19:54
 */
public class ConstructorLambdaTest {
    public static void main(String[] args) {

        /**
         * 构造引用:构造器的参数列表，需要与函数式接口中参数列表保持一致！
         * 类名 :: new
         * 注意:接口的抽象方法的参数和返回值必须和实例方法的参数和返回值类型保持一致
         */
        Supplier<Employee> sup = () -> new Employee();        //普通的lambda表达式
        System.out.println(sup.get());
        //-----------------------------------------------------------------------
        Supplier<Employee> sup2 = Employee::new;     //lambda构造器引用方式
        System.out.println(sup2.get());
    }
}
