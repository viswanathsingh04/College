
package com.testingstudio33.model;

import com.google.gson.annotations.Expose;

import java.util.List;

@SuppressWarnings("unused")
public class Error {

    @Expose
    private List<String> email;
    @Expose
    private Error error;
    @Expose
    private List<String> phone;

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }

}
