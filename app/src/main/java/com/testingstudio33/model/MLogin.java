
package com.testingstudio33.model;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class MLogin {

    @Expose
    private Long code;
    @Expose
    private Login login;
    @Expose
    private String status;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
