package com.lambda;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wzq.Jolin
 * @company none
 * @create 2019-03-28 14:25
 */
public class StreamTest {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
         // 对所有元素进行操作
        List<String> collect = list.stream().map(item -> item + "1").collect(Collectors.toList());
        System.out.println(collect);
    }
}
