package com.umar.apps.design.pattern.singleton;

import java.io.Serial;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SafeSingleton extends Clone implements Serializable {
    
    public static int count = 0;
    
    //We can use class check to make sure, the private constructor is not accessed from any other class.
    private SafeSingleton() throws IllegalAccessException {
        StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
        if(SafeSingleton.class != walker.getCallerClass())
            throw new IllegalAccessException(
        """
        Single constructor was called illegally. Calling from outside is forbidden.""");
    }

    private static volatile SafeSingleton instance;

    public static SafeSingleton getInstance() {
        if(null == instance) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(SafeSingleton.class.getName()).log(Level.SEVERE, null, ex);
            }
            synchronized (SafeSingleton.class) {
                if(null == instance) {
                    ++count;
                    try {
                        instance = new SafeSingleton();
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
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
        return "Safe Singleton";
    }

    @Serial
    protected Object readResolve() {
        return instance;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning of SafeSingleton is not supported.");
    }
}
