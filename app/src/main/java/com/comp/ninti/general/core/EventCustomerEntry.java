package com.comp.ninti.general.core;



public class EventCustomerEntry {
    private long evId;
    private long custId;
    private long discId;
    private int attempt;
    private int points;

    public EventCustomerEntry(long evId, long custId, long discId, int attempt, int points) {
        this.evId = evId;
        this.custId = custId;
        this.discId = discId;
        this.attempt = attempt;
        this.points = points;
    }

    public EventCustomerEntry(long evId, long custId, long discId, int attempt) {
        this.evId = evId;
        this.custId = custId;
        this.discId = discId;
        this.attempt = attempt;
    }

    public long getEvId() {
        return evId;
    }

    public void setEvId(long evId) {
        this.evId = evId;
    }

    public long getCustId() {
        return custId;
    }

    public void setCustId(long custId) {
        this.custId = custId;
    }

    public long getDiscId() {
        return discId;
    }

    public void setDiscId(long discId) {
        this.discId = discId;
    }

    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "EventCustomerEntry{" +
                "evId=" + evId +
                ", custId=" + custId +
                ", discId=" + discId +
                ", attempt=" + attempt +
                ", points=" + points +
                '}';
    }
}
