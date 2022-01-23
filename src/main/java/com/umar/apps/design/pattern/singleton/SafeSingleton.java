package com.umar.apps.design.pattern.singleton;

import com.umar.apps.util.ThrowingConsumer;
import com.umar.apps.util.ThrowingFunction;

import java.io.Serial;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SafeSingleton extends Clone implements Serializable {
    
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
            ThrowingConsumer.unchecked(throwable -> Thread.sleep(10)).accept(SafeSingleton.class);
            synchronized (SafeSingleton.class) {
                if(null == instance) {
                    ThrowingFunction.unchecked(throwable -> instance = new SafeSingleton())
                            .apply(SafeSingleton.class);
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
    private Object readResolve() {
        return instance;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning of SafeSingleton is not supported.");
    }
}
