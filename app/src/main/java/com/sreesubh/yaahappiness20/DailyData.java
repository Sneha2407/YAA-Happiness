package com.sreesubh.yaahappiness20;

public class DailyData {
    String Date,Day;
    int Score;

    public DailyData() {
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    public DailyData(String date, String day, int score) {
        Date = date;
        Day = day;
        Score = score;
    }
}
