package dev.sleep.scorelib.common;

import com.google.common.collect.HashBasedTable;

import java.util.Set;

public abstract class AbstractAdvancedManager<K, SK, O> {

    private final HashBasedTable<K, SK, O> registeredObjectsList = HashBasedTable.create();

    public abstract AbstractAdvancedManager<K, SK, O> getInstance();

    public void registerObject(K key, SK subKey, O object) {
        this.getInstance().registeredObjectsList.put(key, subKey, object);
    }

    public HashBasedTable<K, SK, O> getRegisteredObjectList() {
        return this.getInstance().registeredObjectsList;
    }

    public Set<SK> getSubKeyList() {
        return registeredObjectsList.columnKeySet();
    }

    public O getObjectByKey(K key) {
        return registeredObjectsList.row(key).values().stream().findFirst().get();
    }

    public SK getSubkeyByKey(K key) {
        return registeredObjectsList.row(key).keySet().stream().findFirst().get();
    }

    public K getKeyBySubKey(SK subKey) {
        return registeredObjectsList.column(subKey).keySet().stream().findFirst().get();
    }
}
