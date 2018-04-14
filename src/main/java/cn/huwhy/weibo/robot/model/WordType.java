package cn.huwhy.weibo.robot.model;

import cn.huwhy.interfaces.EnumValue;

public enum WordType implements EnumValue<Integer> {
    BLACK("黑粉", 0),
    IRON("铁粉", 1),
    MASS("群众", 2),
    PASSERBY("过客", 3);

    WordType(String name, Integer value) {
       this.name = name;
       this.value = value;
    }

    private String name;
    private Integer value;

    @Override
    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
