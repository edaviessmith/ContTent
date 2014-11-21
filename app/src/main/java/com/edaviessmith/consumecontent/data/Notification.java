package com.edaviessmith.consumecontent.data;


import com.edaviessmith.consumecontent.util.Var;

import java.util.ArrayList;
import java.util.List;

public class Notification {

    private int id = -1;
    private String name;
    private int type;

    private List<Alarm> alarms;

    public Notification() {
        type = Var.NOTIFICATION_ALARM;
        alarms = new ArrayList<Alarm>();
    }

    public Notification(String name, int type, List<Alarm> alarms) {
        this.name = name;
        this.type = type;
        this.alarms = alarms;
    }

    public Notification(int id, String name, int type) {
        this.id = id;
        this.name = name;
        this.type = type;
        alarms = new ArrayList<Alarm>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Alarm> getAlarms() {
        return alarms;
    }

    public void setAlarms(List<Alarm> alarms) {
        this.alarms = alarms;
    }
}
