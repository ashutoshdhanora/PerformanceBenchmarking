package com.performance;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.performance.thrift.AddMethodCallback;
import com.performance.thrift.AdditionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TNonblockingSocket;
import org.slf4j.LoggerFactory;


@Slf4j
public class RequestMaker {

    public static final long nanoToMicroSec = 1000;
    public static final long nanoToMiliSec = nanoToMicroSec * 1000;
    public static final long nanoToSec = nanoToMiliSec * 1000;
    //Initial delay of 2 millisecond : It will serve the QPS of 500
    public static final long startWithDelay = 2 * nanoToMiliSec;
    //Reduction factor is 100 Micro seconds
    public static final long reductionFactor = nanoToMicroSec * 100;
    //Lowest value of delay should be 400 Micro second, With this we can achieve 2,500/- QPS from one client
    public static final long breakPoint = nanoToMicroSec * 400;
    public static int successCount = 0, failureCount = 0, totalCount = 0;

    public static void main(String[] args) {
        RequestMaker requestMaker = new RequestMaker();
        requestMaker.makeRequest("10.14.118.71", "8080", startWithDelay, "/tmp/temp.txt");
    }

    // totalRequests supported is not hard bound
    public void makeRequest(final String hostIP, final String port, long delay, final String inputFile) {
        disableLogging();
        String urlPath = "http://" + hostIP + ":" + port + "/phoenix/network";

        AddMethodCallback addMethodCallback = new AddMethodCallback() {

            public void onComplete(AdditionService.AsyncClient.add_call add_call) {
                successCount++;
                totalCount++;
                System.out.println("Success = " + successCount);
                System.out.println("Failure =" + failureCount);
            }

            public void onError(Exception e) {
                failureCount++;
                totalCount++;
            }
        };
        AdditionService.AsyncClient asyncClient = null;
        try {
            TAsyncClientManager tAsyncClientManager = new TAsyncClientManager();
            asyncClient = new AdditionService.AsyncClient(new TBinaryProtocol.Factory(),
                    tAsyncClientManager, new TNonblockingSocket("localhost", 9090));
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (delay > breakPoint) {
            totalCount = 0;
            //Goal is to send the requexsts for one minute with same delay value, After one minute
            //We will change the delay
            long numberOfRequestsIn2Minutes = (nanoToSec / delay) * 60 * 2;
            while (totalCount < numberOfRequestsIn2Minutes) {
                 try {

                        if (asyncClient != null) {
                            asyncClient.add(40, 30, addMethodCallback);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                waitTime(delay);
            }
            delay -= reductionFactor;
        }

    }

    private void waitTime(long delay) {
        long lastTime = System.nanoTime();
        long thisTime = System.nanoTime();
        while ((thisTime - lastTime) < delay) {
            thisTime = System.nanoTime();
        }
    }

    private void disableLogging() {
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.OFF);
    }
}
