package com.minis.beans;


public class PropertyValue {
    private final String name;
    private final Object value;
    private final String type;
    private final boolean isRef;

    public PropertyValue(String name, Object value, String type) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.isRef = false;
    }

    public PropertyValue(String name, Object value, String type, Boolean isRef) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.isRef = isRef;
    }
    
    public String getName() {
        return this.name;
    }
    public Object getValue() {
        return this.value;
    }
    public String getType() {
        return this.type;
    }
    public boolean getIsRef() {
        return this.isRef;
    }
}