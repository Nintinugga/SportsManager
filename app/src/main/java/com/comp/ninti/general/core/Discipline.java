package com.comp.ninti.general.core;

import android.os.Parcel;
import android.os.Parcelable;



public class Discipline implements Parcelable {
    private String name;
    private Rule rule;
    private int attempts;
    private long id;
    private long ruleId;

    public Discipline(String name, Rule rule, int attempts) {
        this.name = name;
        this.rule = rule;
        this.attempts = attempts;
    }

    public Discipline(String name, Rule rule, int attempts, long id) {
        this.name = name;
        this.rule = rule;
        this.attempts = attempts;
        this.id = id;
    }

    public Discipline(String name, long ruleId, int attempts, long id) {
        this.name = name;
        this.attempts = attempts;
        this.id = id;
        this.ruleId = ruleId;
    }

    protected Discipline(Parcel in) {
        name = in.readString();
        attempts = in.readInt();
        id = in.readLong();
        ruleId = in.readLong();
    }

    public static final Creator<Discipline> CREATOR = new Creator<Discipline>() {
        @Override
        public Discipline createFromParcel(Parcel in) {
            return new Discipline(in);
        }

        @Override
        public Discipline[] newArray(int size) {
            return new Discipline[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRuleId() {
        return ruleId;
    }

    public void setRuleId(long ruleId) {
        this.ruleId = ruleId;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Discipline){
            Discipline toCompare = (Discipline)obj;
            return this.id == toCompare.getId();
        }
            return super.equals(obj);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(attempts);
        dest.writeLong(id);
        dest.writeLong(ruleId);
    }
}
