package dev.sleep.scorelib.common;

import java.util.HashMap;

public abstract class AbstractManager<K, O> {

    private final HashMap<K, O> registeredObjectsList = new HashMap<>();

    public abstract AbstractManager<K, O> getInstance();

    public void registerObject(K key, O object) {
        this.getInstance().registeredObjectsList.putIfAbsent(key, object);
    }

    public HashMap<K, O> getRegisteredObjectList() {
        return this.getInstance().registeredObjectsList;
    }

    public O getObjectByKey(K key) {
        return this.getInstance().registeredObjectsList.get(key);
    }
}
