package com.example.secondhandmarket;

public class UserAccount {
    public UserAccount(){}
    public String getIdToken(){return idToken;}
    public void setIdToken(String idToken){
        this.idToken = idToken;
    }
    private String idToken;
    public String getEmailId(){return emailId;}
    public void setEmailId(String emailId){this.emailId = emailId;}
    private String emailId;

    public String getPassword(){return password;}
    public void setPassword(String emailId){this.emailId = password;}
    private String password;

    public String getName(){return name;}
    public void setName(String emailId){this.emailId = name;}
    private String name;

    public String getBirth(){return Birth;}
    public void setBirth(String emailId){this.emailId = Birth;}
    private String Birth;


}
