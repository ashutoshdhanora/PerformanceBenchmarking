package com.performance.thrift;


import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;

public class MyServer {

    private void startServer() {
        try {
            TNonblockingServerTransport nonblockingServerTransport = new TNonblockingServerSocket(9090);
            AdditionService.Processor processor = new AdditionService.Processor(new AdditionServiceHandler());

            TServer tServer = new TNonblockingServer(new TNonblockingServer.Args(nonblockingServerTransport).processor(processor));
            tServer.serve();
        } catch (Exception e) {
            System.out.println("Exception = " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        MyServer myServer = new MyServer();
        myServer.startServer();
    }

}
