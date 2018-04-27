package com.zzt.demo.alpha.service.google;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class GoogleAuthenticator  extends Authenticator {
    private String user = "";
    private String password = "";
    public GoogleAuthenticator(String user, String password) {
        this.user = user;
        this.password = password;
    }
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password.toCharArray());
    }
}