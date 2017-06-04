package com.comp.ninti.general;

import java.io.Serializable;


public class Discipline implements Serializable {
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
}
