package coding.json.practice.origin;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MultiThread implements Runnable{

    @Override
    public void run() {
        for(int i=0; i<5; i++) System.out.println(Thread.currentThread().getName() + " i = " + i);
        System.out.println(Thread.currentThread().getName() + " 쓰레드가 끝났습니다.");
    }

    // java 비동기 처리 > Thread class start(), Runnable interface run()
    // priority 또는 시간 설정해서 조정






}
