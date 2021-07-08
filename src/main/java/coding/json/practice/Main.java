package coding.json.practice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws IOException {
        // BlockingQueue<Runnable> ArrayBlockingQueue
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(3, 100, 10, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        // 데몬이 아니므로 shutdown() 으로 종료시켜야함 또는 - allowCoreThreadTimeOut을 true로 해서 core thread 종료
        threadPool.allowCoreThreadTimeOut(true);

        Runnable r1 = new Multi();

        for(int i=0; i<100; i++){
            threadPool.execute(r1);
        }
        System.out.println("쓰레드 콜 종료");

        /*
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r1);
        Thread t3 = new Thread(r1);

        t1.setName("1");
        t2.setName("2");
        t3.setName("3");

        t1.start();
        t2.start();
        t3.start();
        */


        /*
        OriginBasic ob = new OriginBasic();
        ob.basic();
        ob.basicGetList();
        ob.basicPutList();


        Origin2 o2 = new Origin2();
        o2.basicGet();
        o2.basicPost();
        */
    }


    public static void numberRepeat(String name) {
        for(int i=0; i<5; i++) System.out.println(i);
        System.out.println(name + " 메서드가 종료되었습니다");
    }

}
