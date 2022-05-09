package com.mostaron.omega.test;

import com.mostaron.omega.core.scanner.PackageComponentScanner;

/**
 * description: TestPackageComponentScanner <br>
 * date: 2022/5/9 16:03 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
public class TestPackageComponentScanner {
    public static void main(String... args) {
        PackageComponentScanner.doScan(new String[]{"com.mostaron"});
    }
}
