package com.zzy.stream.predicate;

import com.zzy.sku.Sku;

/**
 * 断言接口
 */
public interface SkuPredicate {

    /**
     * 选择判断标准
     * @param sku
     * @return
     */
    boolean test(Sku sku);

}
