package com.minis.beans;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class ArgumentValues {
    private final Map<Integer, ArgumentValue> indexedArgumentValues = new HashMap<Integer, ArgumentValue>();
    private final List<ArgumentValue> genericArgumentValues = new LinkedList<>();

    private void addArgumentValue(Integer key, ArgumentValue newValue) {
        this.indexedArgumentValues.put(key, newValue);
    }
    public boolean hasIndexedArgumentValue(Integer key) {
        return this.indexedArgumentValues.containsKey(key);
    }
    public ArgumentValue getIndexedArgumentValue(Integer key) {
        return this.indexedArgumentValues.get(key);
    }
    public void addGenericArgumentValue(Object value, String type) {
        this.genericArgumentValues.add(new ArgumentValue(value, type));
    }
    private void addGenericArgumentValue(ArgumentValue newValue){
        if (newValue.getName() != null) {            
            for (Iterator<ArgumentValue> it =                 
            this.genericArgumentValues.iterator(); it.hasNext(); ) {                
                ArgumentValue currentValue = it.next();                
                if (newValue.getName().equals(currentValue.getName())) {                    
                    it.remove();                
                }            
            }        
        }        
        this.genericArgumentValues.add(newValue);
    }
    public ArgumentValue getGeneriArgumentValue(String requireName) {
        for (ArgumentValue value : this.genericArgumentValues) {
            if (requireName.equals(value.getName())) {
                return value;
            }
        }
        return null;
    }
    public int getArgumentCount() {
        return this.genericArgumentValues.size();
    }
    public boolean isEmpty() {
        return this.genericArgumentValues.isEmpty();
    }
}
