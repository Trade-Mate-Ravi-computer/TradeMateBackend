package com.trademate.project.Model;

import lombok.Builder;

@Builder
public class JwtResponse {
    private String jwtToken;
    private String userNAme;

    public JwtResponse() {
    }

    public JwtResponse(String jwtToken, String userNAme) {
        this.jwtToken = jwtToken;
        this.userNAme = userNAme;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getUserNAme() {
        return userNAme;
    }

    public void setUserNAme(String userNAme) {
        this.userNAme = userNAme;
    }
}
