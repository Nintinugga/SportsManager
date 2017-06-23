package com.comp.ninti.general.core;


import android.os.Parcel;
import android.os.Parcelable;

import com.comp.ninti.general.RuleType;


public class Rule implements Parcelable {

    private RuleType ruleType;
    private String name;
    private double bestTime;
    private double worstTime;
    private int bestTimePoints;
    private int worstTimePoints;
    private long id;

    public Rule(String name, RuleType ruleType) {
        this.name = name;
        this.ruleType = ruleType;
    }

    public Rule(String name, RuleType ruleType, long id) {
        this.name = name;
        this.ruleType = ruleType;
        this.id = id;
    }


    public Rule(String name, RuleType ruleType, double bestTime, int bestTimePoints, double worstTime, int worstTimePoints) {
        this.name = name;
        this.ruleType = ruleType;
        this.bestTime = bestTime;
        this.bestTimePoints = bestTimePoints;
        this.worstTime = worstTime;
        this.worstTimePoints = worstTimePoints;
    }

    public Rule(String name, RuleType ruleType, double bestTime, int bestTimePoints, double worstTime, int worstTimePoints, long id) {
        this.name = name;
        this.ruleType = ruleType;
        this.bestTime = bestTime;
        this.bestTimePoints = bestTimePoints;
        this.worstTime = worstTime;
        this.worstTimePoints = worstTimePoints;
        this.id = id;
    }


    protected Rule(Parcel in) {
        name = in.readString();
        ruleType = Enum.valueOf(RuleType.class, in.readString());
        bestTime = in.readDouble();
        worstTime = in.readDouble();
        bestTimePoints = in.readInt();
        worstTimePoints = in.readInt();
        id = in.readLong();
    }

    public static final Creator<Rule> CREATOR = new Creator<Rule>() {
        @Override
        public Rule createFromParcel(Parcel in) {
            return new Rule(in);
        }

        @Override
        public Rule[] newArray(int size) {
            return new Rule[size];
        }
    };

    public RuleType getRuleType() {
        return ruleType;
    }

    public void setRuleType(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBestTime() {
        return bestTime;
    }

    public void setBestTime(double bestTime) {
        this.bestTime = bestTime;
    }

    public double getWorstTime() {
        return worstTime;
    }

    public void setWorstTime(double worstTime) {
        this.worstTime = worstTime;
    }

    public int getBestTimePoints() {
        return bestTimePoints;
    }

    public void setBestTimePoints(int bestTimePoints) {
        this.bestTimePoints = bestTimePoints;
    }

    public int getWorstTimePoints() {
        return worstTimePoints;
    }

    public void setWorstTimePoints(int worstTimePoints) {
        this.worstTimePoints = worstTimePoints;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(ruleType.name());
        dest.writeDouble(bestTime);
        dest.writeDouble(worstTime);
        dest.writeInt(bestTimePoints);
        dest.writeInt(worstTimePoints);
        dest.writeLong(id);
    }
}
