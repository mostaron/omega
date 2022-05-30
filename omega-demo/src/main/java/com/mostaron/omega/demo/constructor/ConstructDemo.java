package com.mostaron.omega.demo.constructor;

import java.lang.reflect.Constructor;

/**
 * description: ConstructDemo <br>
 * date: 2022/5/23:023 17:17:17 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
public class ConstructDemo {

    class A {
        public A() {

        }
    }

    class B {
        public B(A a) {

        }
    }

    public static void main(String... args) throws NoSuchMethodException {
        Class<A> aClass = A.class;
        Class<B> bClass = B.class;
        Class<ConstructDemo> demoClass = ConstructDemo.class;

        Constructor<?>[] aConstructors = aClass.getDeclaredConstructors();
        Constructor<?>[] bConstructors = bClass.getDeclaredConstructors();
        Constructor<?>[] demoConstructors = demoClass.getDeclaredConstructors();

        System.out.println(aClass.getDeclaredConstructor());


        return;
    }

}
