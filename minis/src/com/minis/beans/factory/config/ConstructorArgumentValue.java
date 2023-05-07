package com.minis.beans;

public class ArgumentValue {
    private String name;
    private Object value;
    private String type;
    private String ref;
    
    public ArgumentValue(String name, String value, String type, String ref) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.ref = ref;
    }

    public ArgumentValue(String name, String value, String type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public ArgumentValue(Object value, String type) {
        this.type = type;
        this.value = value;
    }   
    
    public String getName() {
        return name;
    }
    public Object getValue() {
        return value;
    }
    public String getType() {
        return type;
    }
    public String getRef() {
        return ref;
    }
}
