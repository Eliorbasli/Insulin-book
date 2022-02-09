package dev.eliorba.insulinbook1.Models;

public class User {
    double id;
    int age;
    int height;
    int wight ;
    int longInsulin;
    String gender;

    public User(){
    }


    public User(double id ,int age , int height , int wight , int longInsulin , String gender){
        this.id = id;
        this.age = age;
        this.height = height;
        this.wight = wight;
        this.longInsulin = longInsulin;
        this.gender = gender;
    }


    public double getId() {
        return id;
    }

    public User setId(double id) {
        this.id = id;
        return this;
    }

    public int getAge() {
        return age;
    }

    public User setAge(int age) {
        this.age = age;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public User setHeight(int height) {
        this.height = height;
        return this;
    }

    public int getWight() {
        return wight;
    }

    public User setWight(int wight) {
        this.wight = wight;
        return this;
    }

    public int getLongInsulin() {
        return longInsulin;
    }

    public User setLongInsulin(int longInsulin) {
        this.longInsulin = longInsulin;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public User setGender(String gender) {
        this.gender = gender;
        return this;
    }
}
