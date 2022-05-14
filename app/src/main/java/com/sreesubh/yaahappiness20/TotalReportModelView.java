package com.sreesubh.yaahappiness20;

public class TotalReportModelView {
    double score;
    String day,date;

    public double getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public TotalReportModelView(double score, String day, String date) {
        this.score = score;
        this.day = day;
        this.date = date;
    }
}
