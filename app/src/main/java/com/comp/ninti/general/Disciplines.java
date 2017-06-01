package com.comp.ninti.general;

import java.io.Serializable;


public class Disciplines implements Serializable {
    String name;
    Rule rule;
    int attempts;
    long id;

    public Disciplines(String name, Rule rule, int attempts) {
        this.name = name;
        this.rule = rule;
        this.attempts = attempts;
    }

    public Disciplines(String name, Rule rule, int attempts, long id) {
        this.name = name;
        this.rule = rule;
        this.attempts = attempts;
        this.id = id;
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
}
