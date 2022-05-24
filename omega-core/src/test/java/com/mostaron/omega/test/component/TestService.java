package com.mostaron.omega.test.component;

import com.mostaron.omega.core.annotation.Autowired;
import com.mostaron.omega.core.annotation.Service;

/**
 * description: TestService <br>
 * date: 2022/5/9 16:19 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
@Service("testService")
public class TestService {
    @Autowired
    private TestComponent testComponent;
}
