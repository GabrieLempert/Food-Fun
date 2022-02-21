package com.example.foodfun.model;

import java.util.ArrayList;

public class UserFoodFun {

    String userName;
    String email;
    String password;
    ArrayList<Trip> usersTrips;
    int userIDCounter=1000;

    public int getGetUserID() {
        return getUserID;
    }

    public void setGetUserID(int getUserID) {
        this.getUserID = getUserID;
    }

    int getUserID;


    public ArrayList<Trip> getUsersTrips() {
        return usersTrips;
    }

    public void setUsersTrips(ArrayList<Trip> usersTrips) {
        this.usersTrips = usersTrips;
    }

    public UserFoodFun(String userName, String email, String password){
        this.usersTrips=new ArrayList<Trip>();
        this.userName=userName;
        this.email=email;
        this.password=password;
        this.getUserID=userIDCounter++;

    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
