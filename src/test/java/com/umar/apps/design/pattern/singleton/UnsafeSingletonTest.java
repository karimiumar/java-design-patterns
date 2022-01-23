package com.umar.apps.design.pattern.singleton;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class UnsafeSingletonTest {
    
    @Test
    void when_UnsafeSinglton_object_created_using_reflection_then_a_new_instance_is_created() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        var unsafeSingletonInstance = UnsafeSingleton.getInstance();
        var constructor = UnsafeSingleton.class.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        var otherInstance = constructor.newInstance();
        assertThat(unsafeSingletonInstance.hashCode()).isNotEqualTo(otherInstance.hashCode());
    }
    
    @Test
    void when_UnsafeSingleton_object_deserialized_then_new_instance() {
        var unsafeSingletonInstance = UnsafeSingleton.getInstance();
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("unsafe.ser"))) {
            oos.writeObject(unsafeSingletonInstance);
        }catch(IOException ex) {
            throw new RuntimeException(ex);
        }
        
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("unsafe.ser"))) {
            var inst = ois.readObject();
            var unsafeInst = (UnsafeSingleton)inst;
            assertThat(unsafeSingletonInstance.hashCode()).isNotEqualTo(unsafeInst.hashCode());
        }catch(IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Test
    void when_UnsafeSingleton_object_cloned_then_new_instance() throws CloneNotSupportedException {
        var unsafeSingletonInstance = UnsafeSingleton.getInstance();
        var clone = unsafeSingletonInstance.clone();
        assertThat(unsafeSingletonInstance.hashCode()).isNotEqualTo(clone.hashCode());
    }
    
    
    @Test
    void when_multiple_threads_access_getInstance_Of_UnsafeSingleton_then_multiple_instances() throws InterruptedException, ExecutionException {
        Callable<UnsafeSingleton> c  = UnsafeSingleton::getInstance;
        var es = Executors.newFixedThreadPool(2);
        int count = 0;
        for(int i= 0; i<50;i++) {
            Future<UnsafeSingleton> f1 = es.submit(c);
            Future<UnsafeSingleton> f2 = es.submit(c);
            if(f1.get() != f2.get()) {
                System.out.println("Multiple Instances!!!");
                ++count;
                assertThat(f1.get()).isNotEqualTo(f2.get());
            }
            UnsafeSingleton.instance = null;
        }
        assertThat(count).isNotEqualTo(0);
    }
}
