package com.whiteclaw.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author whiteclaw
 * created on 2019-01-04
 */
class ObservableThreadTest {

    @Test
    public void test() throws Exception{
        EmptyTaskLifeCycle<String> lifeCycle = new EmptyTaskLifeCycle<String>() {
            @Override
            public void onFinished(Thread thread, String result) {
                System.out.println("the result is: " + result);
            }
        };
        ObservableThread<String> observableThread = new ObservableThread<>(lifeCycle, () -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println("finished...");
            return "Observer Finished";
        });
        observableThread.start();

        TimeUnit.SECONDS.sleep(1);
        System.out.println(observableThread.getCycle().name());
        TimeUnit.SECONDS.sleep(5);
        System.out.println(observableThread.getCycle().name());
    }
}
