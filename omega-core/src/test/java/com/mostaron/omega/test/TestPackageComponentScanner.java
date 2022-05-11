package com.mostaron.omega.test;

import com.mostaron.omega.core.annotation.Service;
import com.mostaron.omega.core.beans.BeanDefinition;
import com.mostaron.omega.core.scanner.PackageComponentScanner;
import com.mostaron.omega.test.component.TestComponent;
import com.mostaron.omega.test.component.TestService;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * description: TestPackageComponentScanner <br>
 * date: 2022/5/9 16:03 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
public class TestPackageComponentScanner {
    public static void main(String... args) {
        Set<BeanDefinition> beanDefinitionSet = PackageComponentScanner.doScan(new String[]{"com.mostaron"});

        beanDefinitionSet.forEach(System.out::println);

    }
}
