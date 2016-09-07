package com.zx.stlife.tools;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by micheal on 16/1/3.
 */
public class LockUtils {

    public static void lock(Map<String, Lock> lockMap, String key){
        Lock lock = lockMap.get(key);
        if(lock == null){
            lock = new ReentrantLock();
            lockMap.put(key, lock);
        }
        try {
            lock.lock();
        }catch (Exception ex){
            lock = new ReentrantLock();
            lock.lock();
            lockMap.put(key, lock);
        }
    }

    public static void unlock(Map<String, Lock> lockMap, String key){
        Lock lock = lockMap.get(key);
        lock.unlock();
        lockMap.remove(key);
        lock = null;
    }
}
