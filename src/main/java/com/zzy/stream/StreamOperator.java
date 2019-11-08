package com.zzy.stream;

import com.alibaba.fastjson.JSON;
import com.zzy.sku.CartService;
import com.zzy.sku.Sku;
import com.zzy.sku.SkuCategoryEnum;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * 演示流的各种操作
 * 有状态和无状态: 有状态操作需要知道流的整体, 比如filter, skip, limit
 * 无状态操作针对流的单个元素 比如peek
 * 有状态操作会执行完流, 再继续下去
 */
public class StreamOperator {

    List<Sku> list;

    @Before
    public void init() {
        list = CartService.getCartSkuList();
    }

    /**
     * 按照 skuId 从小到大排序
     * sorted 是串行操作
     */
    @Test
    public void all(){
        list.stream()
                .sorted(Comparator.comparing(sku -> sku.getSkuId()))
                .forEach(System.out::println);
    }

    /**
     * filter使用：保留符合断言的元素
     * 串行执行
     */
    @Test
    public void filterTest() {
        list.stream()
                .filter(sku -> SkuCategoryEnum.BOOKS.equals(sku.getSkuCategory()))
                .forEach(System.out::println);
    }

    /**
     * map使用：将一个元素转换成另一个元素
     * 并行操作
     */
    @Test
    public void mapTest() {
        list.stream()
                .peek(sku -> System.out.println(sku.getSkuName()))
                .map(sku -> sku.getSkuName())
                .forEach(System.out::println);
    }

    /**
     * flatMap使用：将一个对象转换成流
     */
    @Test
    public void flatMapTest() {
        list.stream()
                .flatMap(sku -> Arrays.stream(sku.getSkuName().split("")))
                .forEach(System.out::println);
    }

    /**
     * peek使用：对流中元素进行遍历操作，与forEach类似，但不会销毁流元素
     */
    @Test
    public void peek() {
        list.stream()
                .peek(sku -> System.out.println(sku.getSkuName()))
                .forEach(System.out::println);
    }

    /**
     * sort使用：对流中元素进行排序，可选则自然排序或指定排序规则。有状态操作
     */
    @Test
    public void sortTest() {
        list.stream()
                .peek(sku -> System.out.println(sku.getSkuName()))
                .sorted(Comparator.comparing(Sku::getTotalPrice))
                .forEach(System.out::println);
    }

    /**
     * distinct使用：对流元素进行去重。有状态操作
     */
    @Test
    public void distinctTest() {
        list.stream()
                .map(sku -> sku.getSkuCategory())
                .distinct()
                .forEach(System.out::println);
    }

    /**
     * skip使用：跳过前N条记录。有状态操作
     */
    @Test
    public void skipTest() {
        list.stream()
                .sorted(Comparator.comparing(sku -> sku.getTotalPrice()))
                .skip(3)
                .forEach(System.out::println);
    }

    /**
     * limit使用：截断前N条记录。有状态操作
     * 模拟分页效果
     */
    @Test
    public void limitTest() {
        list.stream()
                .sorted(Comparator.comparing(Sku::getTotalPrice))
                .skip(2 * 3)
                // limit
                .limit(3)
                .forEach(System.out::println);
    }

    /**
     * allMatch使用：终端操作，短路操作。所有元素匹配，返回true
     */
    @Test
    public void allMatchTest() {
        boolean match = list.stream()
                .peek(System.out::println)
                // allMatch
                .allMatch(sku -> sku.getTotalPrice() > 100);
        System.out.println(match);
    }

    /**
     * anyMatch使用：任何元素匹配，返回true
     */
    @Test
    public void anyMatchTest() {
        boolean match = list.stream()
                .peek(System.out::println)
                .anyMatch(sku -> sku.getTotalPrice() > 100);
        System.out.println(match);
    }

    /**
     * noneMatch使用：任何元素都不匹配，返回true
     */
    @Test
    public void noneMatchTest() {
        boolean match = list.stream()
                .peek(System.out::println)
                .noneMatch(sku -> sku.getTotalPrice() > 3000);
        System.out.println(match);
    }

    /**
     * 找到第一个, 短路操作
     */
    @Test
    public void findFirstTest() {
        Optional<Sku> optional = list.stream()
                .peek(sku -> System.out.println(sku.getSkuName()))
                .findFirst();
        System.out.println(
                JSON.toJSONString(optional.get(), true));
    }

    /**
     * 找任意一个, 短路操作
     */
    @Test
    public void findAnyTest() {
        Optional<Sku> optional = list.stream()
                .peek(sku -> System.out.println(sku.getSkuName()))
                .findAny();
        System.out.println(
                JSON.toJSONString(optional.get(), true));
    }

    /**
     * max使用：
     */
    @Test
    public void maxTest() {
        OptionalDouble optionalDouble = list.stream()
                .mapToDouble(Sku::getTotalPrice)
                .max();
        System.out.println(optionalDouble.getAsDouble());
    }

    /**
     * min使用
     */
    @Test
    public void minTest() {
        OptionalDouble optionalDouble = list.stream()
                .mapToDouble(Sku::getTotalPrice)
                .min();
        System.out.println(optionalDouble.getAsDouble());
    }

    /**
     * count使用
     */
    @Test
    public void countTest() {
        long count = list.stream()
                .count();
        System.out.println(count);
    }

}
