package com.umar.apps.design.pattern.singleton;

import java.io.Serializable;
import java.util.function.Supplier;

public final class UnsafeSingleton extends Clone implements Serializable {
	
    private UnsafeSingleton() {
        
    }
    
    static volatile UnsafeSingleton instance;
    
    /**
     * Multiple threads can create more than one instance.
     * There's no null check again inside the synchronized block.
     *
     * @return Returns an {@link UnsafeSingleton} instance
     */
    public static UnsafeSingleton getInstance() {
        if(null == instance) {
            synchronized(UnsafeSingleton.class) {
                System.out.println(Thread.currentThread().getName() + " is creating an instance.");
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
    
    public Supplier<Integer> sharedResourceSupplier(){
        return () -> 5;
    }
}
