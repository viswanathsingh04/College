
package com.testingstudio33.model;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class MOtp {

    @Expose
    private String message;
    @Expose
    private String type;
    @Expose
    private String code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
