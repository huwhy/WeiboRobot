package cn.huwhy.weibo.robot.service;

import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class TaskService {

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public void submit(Runnable runnable, long delaySeconds) {
        executorService.scheduleWithFixedDelay(runnable, 0, delaySeconds, TimeUnit.SECONDS);
    }

    public void submit(Runnable runnable) {
        executorService.schedule(runnable, 1, TimeUnit.SECONDS);
    }

}
