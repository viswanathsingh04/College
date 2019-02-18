
package com.testingstudio33.model;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class Register {

    @Expose
    private String email;
    @Expose
    private String name;
    @Expose
    private String phone;
    @Expose
    private String token;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
