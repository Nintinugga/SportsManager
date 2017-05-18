package com.comp.ninti.general;

import java.io.Serializable;

/**
 * Created by ninti on 5/14/2017.
 */

public class Disciplines implements Serializable {
    String name;
    Rule rule;
    int attempts;

    public Disciplines(String name, Rule rule, int attempts) {
        this.name = name;
        this.rule = rule;
        this.attempts = attempts;
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
