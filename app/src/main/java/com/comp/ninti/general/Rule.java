package com.comp.ninti.general;


import java.io.Serializable;

public class Rule implements Serializable{

    private RuleType ruleType;
    private String name;

    public Rule(String name, RuleType ruleType){
        this.name = name;
        this.ruleType = ruleType;
    }

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
}
