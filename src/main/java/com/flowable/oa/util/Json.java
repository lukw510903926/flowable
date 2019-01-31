package com.flowable.oa.util;

import java.io.Serializable;

/**
 * @author lukew
 * @eamil 13507615840@163.com
 * @create 2018-09-06 20:01
 **/

public class Json implements Serializable {

    private static final long serialVersionUID = -7678018341360619657L;

    private String msg;

    private Object object;

    private boolean success;

    public Json() {
    }

    public static Json fail(String msg){
        return  new Json(msg,false);
    }

    public static Json success(){
        return  new Json(null,true);
    }

    public Json(String msg, boolean success) {
        this.msg = msg;
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
