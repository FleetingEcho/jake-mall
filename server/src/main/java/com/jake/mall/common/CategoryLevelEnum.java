
package com.jake.mall.common;


public enum CategoryLevelEnum {
    /*0*/
    DEFAULT(0, "ERROR"),
    LEVEL_ONE(1, "Level 1 Classification"),
    LEVEL_TWO(2, "Level 2 Classification"),
    LEVEL_THREE(3, "Level 3 Classification");

    private int level;

    private String name;

    CategoryLevelEnum(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public static CategoryLevelEnum getOrderStatusEnumByLevel(int level) {
        for (CategoryLevelEnum categoryLevelEnum : CategoryLevelEnum.values()) {
            if (categoryLevelEnum.getLevel() == level) {
                return categoryLevelEnum;
            }
        }
        return DEFAULT;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
