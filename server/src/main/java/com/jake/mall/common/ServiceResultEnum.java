
package com.jake.mall.common;

public enum ServiceResultEnum {
    /*error*/
    ERROR("error"),

    SUCCESS("success"),

    DATA_NOT_EXIST("No records consulted !"),

    PARAM_ERROR("Parameter error !"),

    SAME_CATEGORY_EXIST("Classification of the same level and name already exists!"),

    SAME_LOGIN_NAME_EXIST("Username already exists!"),

    LOGIN_NAME_NULL("Please enter your login name!"),

    LOGIN_NAME_IS_NOT_PHONE("Please enter the correct mobile phone number"),

    LOGIN_PASSWORD_NULL("Please enter your password!"),

    LOGIN_VERIFY_CODE_NULL("Please enter the verification code!"),

    LOGIN_VERIFY_CODE_ERROR("Captcha error!"),

    SAME_INDEX_CONFIG_EXIST("The same home page configuration item already exists!"),

    GOODS_CATEGORY_ERROR("Classification data exceptions!"),

    SAME_GOODS_EXIST("Identical product information already exists!"),

    GOODS_NOT_EXIST("Product does not exist!"),

    GOODS_PUT_DOWN("Product has been removed from the shelves!"),

    SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR("Exceeding the maximum number of individual items to be purchased!"),

    SHOPPING_CART_ITEM_NUMBER_ERROR(" The number of items must not be less than 1 !"),

    SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR("Exceeds maximum shopping cart capacity!"),

    SHOPPING_CART_ITEM_EXIST_ERROR("Already exists! No need to add it again and again!"),

    LOGIN_ERROR("Login failed!"),

    NOT_LOGIN_ERROR("Not logged in!"),

    ADMIN_NOT_LOGIN_ERROR("Administrator not logged in!"),

    TOKEN_EXPIRE_ERROR("Invalid certification! Please log in again!"),

    ADMIN_TOKEN_EXPIRE_ERROR("Administrator login expired! Please log in again!"),

    USER_NULL_ERROR("Invalid user! Please login again!"),

    LOGIN_USER_LOCKED_ERROR("User has been banned from logging in!"),

    ORDER_NOT_EXIST_ERROR("Orders do not exist!"),

    ORDER_ITEM_NOT_EXIST_ERROR("Order item does not exist!"),

    NULL_ADDRESS_ERROR("Address cannot be empty!"),

    ORDER_PRICE_ERROR("Order price anomalies!"),

    ORDER_ITEM_NULL_ERROR("Order item exceptions!"),

    ORDER_GENERATE_ERROR("Generating order exceptions!"),

    SHOPPING_ITEM_ERROR("Shopping cart data exceptions!"),

    SHOPPING_ITEM_COUNT_ERROR("Insufficient stock!"),

    ORDER_STATUS_ERROR("Order status exceptions!"),

    OPERATE_ERROR("Operation failed!"),

    REQUEST_FORBIDEN_ERROR("Disable this operation!"),

    NO_PERMISSION_ERROR("No permission!"),

    DB_ERROR("database error");

    private String result;

    ServiceResultEnum(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
