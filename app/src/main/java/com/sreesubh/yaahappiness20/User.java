package com.sreesubh.yaahappiness20;

import java.util.List;

public class User {
    String UserUuid,Dob;
    Double AggHappinessScore;

    public User() {
    }

    public String getUserUuid() {
        return UserUuid;
    }

    public void setUserUuid(String userUuid) {
        UserUuid = userUuid;
    }

    public String getDob() {
        return Dob;
    }

    public void setDob(String dob) {
        Dob = dob;
    }

    public Double getAggHappinessScore() {
        return AggHappinessScore;
    }

    public void setAggHappinessScore(Double aggHappinessScore) {
        AggHappinessScore = aggHappinessScore;
    }


    public User(String userUuid, String dob, Double aggHappinessScore) {
        UserUuid = userUuid;
        Dob = dob;
        AggHappinessScore = aggHappinessScore;
    }
}
