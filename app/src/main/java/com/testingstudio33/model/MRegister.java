
package com.testingstudio33.model;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class MRegister {

    @Expose
    private Long code;
    @Expose
    private Register register;
    @Expose
    private String status;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
