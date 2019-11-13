package com.zzy.stream;

import com.alibaba.fastjson.JSON;
import com.zzy.sku.CartService;
import com.zzy.sku.Sku;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 常见预定义收集器使用
 */
public class StreamCollector {

    /**
     * 集合收集器
     */
    @Test
    public void toList() {
        List<Sku> list = CartService.getCartSkuList();
        List<Sku> result = list.stream()
                .filter(sku -> sku.getTotalPrice() > 100)
                .collect(Collectors.toList());
        result.stream()
                .forEach(System.out::println);
    }

    /**
     * 分组
     */
    @Test
    public void group() {
        List<Sku> list = CartService.getCartSkuList();
        Map<Object, List<Sku>> group = list.stream()
                .collect(Collectors.groupingBy(
                                sku -> sku.getSkuCategory()));  //以类别为条件, 为list分组
        for(Map.Entry entry: group.entrySet()){
            System.out.println(entry.getKey());
            ((List)entry.getValue()).stream()
                    .forEach(System.out::println);
        }
    }

    /**
     * 分区
     */
    @Test
    public void partition() {
        List<Sku> list = CartService.getCartSkuList();

        Map<Boolean, List<Sku>> partition = list.stream()
                .collect(Collectors.partitioningBy(
                        sku -> sku.getTotalPrice() > 100));

        System.out.println(
                JSON.toJSONString(partition, true));
    }

}
