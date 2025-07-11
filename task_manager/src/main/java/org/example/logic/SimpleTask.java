package org.example.logic;

public final class SimpleTask extends Task {
    private Integer startHour;
    private Integer endHour;
    public SimpleTask(int id,String nume,int startHour, int endHour){
        super(id,nume);
        this.startHour = startHour;
        this.endHour = endHour;
    }
    public int getStartHour(){
        return this.startHour;
    }
    public int getEndHour(){
        return this.endHour;
    }
    public int estimateDuration(){
        return this.endHour-this.startHour;
    }

}
