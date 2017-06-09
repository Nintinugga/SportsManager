package com.comp.ninti.general.core;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedList;
import java.util.List;

public class Event implements Parcelable{
    private long id;
    private String name;
    private LinkedList<Long> disciplines = new LinkedList<>();
    private String date;

    public Event(String name, List<Long> disciplines, String date) {
        this.name = name;
        this.disciplines.addAll(disciplines);
        this.date = date;
    }

    public Event(long id, String name, List<Long> disciplines, String date) {
        this.id = id;
        this.name = name;
        this.disciplines.addAll(disciplines);
        this.date = date;
    }

    protected Event(Parcel in) {
        id = in.readLong();
        name = in.readString();
        date = in.readString();
        disciplines = new LinkedList<>();
        in.readList(disciplines, null);
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(date);
        dest.writeList(disciplines);
    }
}
