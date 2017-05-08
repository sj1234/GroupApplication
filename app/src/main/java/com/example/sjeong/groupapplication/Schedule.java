package com.example.sjeong.groupapplication;

/**
 * Created by SJeong on 2017-05-04.
 */

public class Schedule {

    private String name;
    private String org_ring;
    private String chg_ring;
    private String start;
    private String end;
    private Boolean sun;
    private Boolean mon;
    private Boolean tue;
    private Boolean wed;
    private Boolean thu;
    private Boolean fri;
    private Boolean sat;

    public String getName() {
        return name;
    }

    public String getOrg_ring() {
        return org_ring;
    }

    public String getChg_ring_ring() {
        return chg_ring;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public Boolean getSun() {
        return sun;
    }

    public Boolean getMon() {
        return mon;
    }

    public Boolean getTue() {
        return tue;
    }

    public Boolean getWed() {
        return wed;
    }

    public Boolean getThu() {
        return thu;
    }

    public Boolean getFri() {
        return fri;
    }

    public Boolean getSat() {
        return sat;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrg_ring(String org_ring){
        this.org_ring = org_ring;
    }

    public void setChg_ring(String chg_ring){
        this.chg_ring = chg_ring;
    }

    public void setStart(String start){
        this.start = start;
    }

    public void setEnd(String end){
        this.end = end;
    }

    public void setSun(Boolean sun){
        this.sun = sun;
    }

    public void setMon(Boolean mon){
        this.mon = mon;
    }

    public void setTue(Boolean tue){
        this.tue = tue;
    }

    public void setWed(Boolean wed){
        this.wed = wed;
    }

    public void setThu(Boolean thu){
        this.thu = thu;
    }

    public void setFri(Boolean fri){
        this.fri = fri;
    }

    public void setSat(Boolean sat){
        this.sat = sat;
    }
}
