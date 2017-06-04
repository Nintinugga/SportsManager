package com.comp.ninti.general;

import java.util.LinkedList;
import java.util.List;

public class Event {
    private long id;
    private String name;
    private LinkedList<Long> disciplines = new LinkedList<>();
    private String date;

    public Event(String name, List<Long> disciplines, String date) {
        this.name = name;
        this.disciplines.addAll(disciplines);
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(LinkedList<Long> disciplines) {
        this.disciplines = disciplines;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        String disc = "";
        for(Long disciLong: disciplines){
            disc = disc + ", " +disciLong;
        }
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", disciplines=" + disc +
                ", date='" + date + '\'' +
                '}';
    }
}
