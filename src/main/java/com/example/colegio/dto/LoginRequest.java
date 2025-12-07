package com.example.colegio.dto;

//información sensible de logeo
public class LoginRequest {
    private String email;
    private String password;

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    

    /**
     * @return 
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password 
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
