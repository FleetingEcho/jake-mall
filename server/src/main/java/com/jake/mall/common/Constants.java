
package com.jake.mall.common;

public class Constants {
    // Default url prefix for uploaded files, change according to deployment
    // settings
    public final static String FILE_UPLOAD_DIC = "E:\\Github-repo-Java\\jake-mall\\Server\\src\\main\\resources\\images\\";

    public final static int INDEX_CAROUSEL_NUMBER = 5;// Number of rotating images on the home page (can be modified
                                                      // according to your needs)

    public final static int INDEX_CATEGORY_NUMBER = 10;// Maximum number of first level categories on the home page

    public final static int INDEX_GOODS_HOT_NUMBER = 4;// Number of hot items on the home page

    public final static int INDEX_GOODS_NEW_NUMBER = 5;// Number of new products on the home page

    public final static int INDEX_GOODS_RECOMMEND_NUMBER = 10;// Number of recommended products on the home page

    public final static int SHOPPING_CART_ITEM_TOTAL_NUMBER = 20;// Maximum number of items in the shopping cart (can be
                                                                 // modified according to your needs)

    public final static int SHOPPING_CART_ITEM_LIMIT_NUMBER = 5;// Maximum number of individual items to be purchased in
                                                                // the shopping cart (can be modified to suit your
                                                                // needs)

    public final static int GOODS_SEARCH_PAGE_LIMIT = 10;// Default number of search pagination items (10 per page)

    public final static int SHOPPING_CART_PAGE_LIMIT = 5;// Default number of items for shopping cart paging (5 per
                                                         // page)

    public final static int ORDER_SEARCH_PAGE_LIMIT = 5;// Default number of entries for my order list pagination (5
                                                        // entries per page)

    public final static int SELL_STATUS_UP = 0;// Product Status
    public final static int SELL_STATUS_DOWN = 1;// Status of products off the shelf

    public final static int TOKEN_LENGTH = 32;// token length

    public final static String USER_INTRO = "Live a colorful life";// Default Profile
}
