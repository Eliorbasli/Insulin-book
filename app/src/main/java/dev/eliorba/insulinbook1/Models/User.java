package dev.eliorba.insulinbook1.Models;

public class User {
    String UserId;
    int Age;
    int Height;
    int Wight ;
    int LongInsulin;
    String Gender;

    public User(){
    }


    public User(String UserId ,int Age , int Height , int Wight , int LongInsulin , String Gender){
        this.UserId = UserId;
        this.Age = Age;
        this.Height = Height;
        this.Wight = Wight;
        this.LongInsulin = LongInsulin;
        this.Gender = Gender;
    }


    public String getUserId() {
        return UserId;
    }

    public User setId(String id) {
        this.UserId = id;
        return this;
    }

    public int getAge() {
        return Age;
    }

    public User setAge(int Age) {
        this.Age = Age;
        return this;
    }

    public int getHeight() {
        return Height;
    }

    public User setHeight(int height) {
        this.Height = Height;
        return this;
    }

    public int getWight() {
        return Wight;
    }

    public User setWight(int Wight) {
        this.Wight = Wight;
        return this;
    }

    public int getLongInsulin() {
        return LongInsulin;
    }

    public User setLongInsulin(int LongInsulin) {
        this.LongInsulin = LongInsulin;
        return this;
    }

    public String getGender() {
        return Gender;
    }

    public User setGender(String Gender) {
        this.Gender = Gender;
        return this;
    }
}
