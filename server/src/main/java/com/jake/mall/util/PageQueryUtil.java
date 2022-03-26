package com.jake.mall.util;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Page query parameter
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageQueryUtil extends LinkedHashMap<String, Object> {
    //current page
    private int page;
    //limit per page
    private int limit;

    public PageQueryUtil(Map<String, Object> params) {
        this.putAll(params);

        //Page query parameter
        this.page = Integer.parseInt(params.get("page").toString());
        this.limit = Integer.parseInt(params.get("limit").toString());
        this.put("start", (page - 1) * limit);
        this.put("page", page);
        this.put("limit", limit);
    }
}
