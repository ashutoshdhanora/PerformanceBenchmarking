package com.performance.thrift;

public class AdditionServiceHandler implements AdditionService.Iface {

    public int add(int n1, int n2) throws org.apache.thrift.TException {

        return n1+n2;
    }
}
