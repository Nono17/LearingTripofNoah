import java.nio.channels.IllegalSelectorException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import sun.java2d.ReentrantContext;

// 实现一个基于链表法解决冲突问题的散列表
public class Table{
    private int manyItems;
    private Object[] keys;
    private Object[] values;
    private boolean[] hasBeenUsed;

    public Table(int capacity){
        if(capaticy<=0){
            throw new IllegalAccessException("Capacity is nagative");
        }
        keys = new Object[capacity];
        values = new Object[capacity];
        hasBeenUsed = new boolean[capacity];
    }
    public boolean isEmpty(){
        return manyItems == 0;
    }
    public synchronized void clear(){
        if(manyItems != 0){
            for(int i = 0;i<values.length;i++){
                keys[i] = null;
                values[i] = null;
                hasBeenUsed[i] = false;
            }
            manyItems = 0;
        }
    }

    public boolean containsKey(Object Key){
        return findIdex(key)!=-1;
    }

    public Object get(Object key){
        int index = findIndex(key);
        if(index!=-1){
            return values[index];
        }
        return null;
    }
    public synchronized Object put(Object key, Object value){
        int i = findIndex(hash(key));
        Object temp = null;
        if(i != -1){
            temp = values[i];
            values[i] = value;
            return temp;
        }else if(manyItems<values.length){
            i = hash(key);
            if(keys[i]!=null){
                i = nextIndex(i);
            }
            keys[i] = true;
            manyItems ++;
            return null;
        }else{
            throw new IllegalSelectorException("Table is full");
        }
    }

    public synchronized Object remove(Object key){
        int index = findindex(key);
        Object result = null;
        if(index!=-1){
            result = values[index];
            keys[index] = null;
            values[index] = null;
            hasBeenUsed[index] = false;
            manyItems --;
        }
        return result;
    }
    public int findIndex(Object key){
        int count = 0;
        int i = hash(key);
        while((count<values.length) && hasBeenUsed[i]){
            if(keys[i].equals(key)){
                return i;
            }
            count++;
            i = nextIndex(i);
        }
        return -1;
    }
    public boolean hasBeenUsed(int index){
        return hasBeenUsed[index];
    }
    public int size(){
        return manyItems;
    }
}
// 实现一个 LRU 缓存淘汰算法
public class LRULinkedHashMap<K, V> extends LinkedHashMap<K, V>{
    private final int maxCapcity;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private final Lock lock = new ReentrantContext();
    public LRULinkedHashMap(int maxCapcity){
        super(maxCapcity, DEFAULT_LOAD_FACTOR, true);
        this.maxCapcity = maxCapcity;
    }

    protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest){
        return size() > maxCapcity;
    }
    public boolean containsKey(Object key){
        try{
            lock.lock();
            return super.containsKey(key);
        }finally{
            lock.unlock();
        }
    }
    public V get(Object key){
        try{
            lock.lock();
            return super.get(key);
        }finally{
            lock.unlock();
        }
    }
    public void clear(){
        try{
            lock.lock();
            super.clear();
        }finally{
            lock.unlock();
        }
    }
    public Collection<Map.Entry<K, V>>getAll(){
        try{
            lock.lock();
            return new ArrayList<Map.Entry<K,V>>(super.entrySet());
        }finally{
            lock.unlock();
        }
    }
}