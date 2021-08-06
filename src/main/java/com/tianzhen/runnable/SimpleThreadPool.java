package com.tianzhen.runnable;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class SimpleThreadPool extends ThreadPoolTaskExecutor {

    private static final int CORE_POLL_SIZE = 5;

    private static final int MAX_POOL_SIZE = 100;


    public SimpleThreadPool(){
        super.setCorePoolSize(CORE_POLL_SIZE);
        super.setMaxPoolSize(MAX_POOL_SIZE);
        super.initialize();
    }

}
