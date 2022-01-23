package dev.eliorba.insulinbook1.Models;

public class Food {
    private String title="";
    private  int Sugarbefore = 0;
    private  int Sugarafter = 0;
    private  int InsulinDose = 0;
    private  int Wight = 0;
    private  int Hight = 0;


    public Food() {
    }

    public String getTitle() {
        return title;
    }

    public Food setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getSugarBefore() {
        return Sugarbefore;
    }

    public Food setSugarBefore(int sugarBefore) {
        this.Sugarbefore = sugarBefore;
        return this;
    }

    public int getSugarAfter() {
        return Sugarafter;
    }

    public Food setSugarAfter(int Sugarafter) {
        this.Sugarafter = Sugarafter;
        return this;
    }

    public int getInsulinDose() {
        return InsulinDose;
    }

    public Food setInsulinDose(int insulinDose) {
        InsulinDose = insulinDose;
        return this;
    }

    public int getWight() {
        return Wight;
    }

    public Food setWight(int wight) {
        this.Wight = wight;
        return this;
    }

    public int getHight() {
        return Hight;
    }

    public Food setHight(int hight) {
        this.Hight = hight;
        return this;
    }
}
