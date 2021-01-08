package com.ljy.page.list;

import org.json.JSONObject;

public class HttpStatus<T> {
    public static final String ERR_NETOWRK = "-1";
    public static final String ERR_SHOP_VERIFY = "-2";
    public boolean success = false;
    public long startTime = 0;
    public long endTime = 0;
    public T obj;
    public JSONObject rspJSONObject;
    public String msg;
    public int code = 0;
    public int hasNext = 0;
    public int httpCode = -1;
    public String tag;
    private boolean timeOut;

    public HttpStatus() {
        super();
        success = false;
        msg = "请求失败";
    }

    public HttpStatus(int code) {
        this.code = code;
    }

    public HttpStatus(int code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public void setTimeOut(boolean timeOut) {
        this.timeOut = timeOut;
    }

    /***
     * 是否为请求超时
     * @return
     */
    public boolean isTimeOut() {
        return timeOut;
    }
}
