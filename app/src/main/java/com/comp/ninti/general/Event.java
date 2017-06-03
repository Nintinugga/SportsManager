package com.comp.ninti.general;


import java.util.Date;
import java.util.List;

public class Event {
    private long id;
    private String name;
    private List<String> disciplines;
    private Date date;

    public Event(String name, List<String> disciplines, Date date){
        this.date = date;
    }
}
