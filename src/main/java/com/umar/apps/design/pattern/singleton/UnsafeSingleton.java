package com.umar.apps.design.pattern.singleton;

import com.umar.apps.util.ThrowingConsumer;

import java.io.Serializable;

public final class UnsafeSingleton extends Clone implements Serializable {
    
    public static int count = 0;
    
    private UnsafeSingleton() {
        
    }
    
    private static volatile UnsafeSingleton instance;
    
    /**
     * Multiple threads can create more than one instance.
     * There's no null check again inside the synchronized block.
     *
     * @return Returns an {@link UnsafeSingleton} instance
     */
    public static UnsafeSingleton getInstance() {
        if(null == instance) {
            ThrowingConsumer.unchecked(throwable -> Thread.sleep(10)).accept(UnsafeSingleton.class);
            synchronized(UnsafeSingleton.class) {
                ++count;
                instance = new UnsafeSingleton();
            }
        }
        return instance;
    }
   
    /**
     * Methods are always serialized in .class file
     * 
     * @return Returns a String 
     */
    public String display() {
        return "Unsafe Singleton";
    }
}
