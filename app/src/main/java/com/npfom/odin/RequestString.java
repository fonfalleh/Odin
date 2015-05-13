package com.npfom.odin;

/**
 * Created by Bjorn on 15-05-06.
 */
public class RequestString implements RequestInterface {
    private String var;

    public RequestString(String str){
        var = str;
    }
    @Override
    public void process(String str) {
        var = str;
    }

    public String getVar() {
        return var;
    }
}
