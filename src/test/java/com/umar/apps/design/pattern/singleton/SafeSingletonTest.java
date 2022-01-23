package com.umar.apps.design.pattern.singleton;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SafeSingletonTest {

    @Test
    void when_constructor_called_then_IllegalAccessException() {
        assertThatThrownBy(() -> {
            var constructors = SafeSingleton.class.getDeclaredConstructors();
            var theConstructor = constructors[0];
            theConstructor.setAccessible(true);
            theConstructor.newInstance();
        }).hasCause(new IllegalAccessException("""
    Single constructor was called illegally. Calling from outside is forbidden."""));
    }

    @Test
    void when_singleton_deserialized_then_same_instance() {
        var singleton = SafeSingleton.getInstance();
        try(ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream("file.ser"))){
            writer.writeObject(singleton);
        }catch (IOException ex) {
            throw new RuntimeException("Some exception occurred", ex);
        }

        try(ObjectInputStream reader = new ObjectInputStream(new FileInputStream("file.ser"))) {
            var obj = reader.readObject();
            assertThat(obj.hashCode()).isEqualTo(singleton.hashCode());
            assertThat(obj).isInstanceOf(SafeSingleton.class);
            assertThat(((SafeSingleton)obj).display()).isNotBlank();
        }catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException("Some exception occurred", ex);
        }
    }

    @Test
    void when_singleton_cloned_then_CloneNotSupportedException() {
        assertThatThrownBy(() -> SafeSingleton.getInstance().clone())
                .hasMessage("Cloning of SafeSingleton is not supported.");
    }
    
    @Test
    void when_multiple_threads_access_getInstance_Of_Singleton_then_single_instances() throws InterruptedException, ExecutionException {

        Callable<SafeSingleton> c  = SafeSingleton::getInstance;
        var es = Executors.newFixedThreadPool(2);
        int count = 0;
        for(int i= 0; i<50;i++) {
            Future<SafeSingleton> f1 = es.submit(c);
            Future<SafeSingleton> f2 = es.submit(c);
            if(f1.get() != f2.get()) {
                System.out.println("Multiple Instances!!!");
                ++count;
                assertThat(f1.get()).isNotEqualTo(f2.get());
            }
        }
        assertThat(count).isEqualTo(0);
    }
}
