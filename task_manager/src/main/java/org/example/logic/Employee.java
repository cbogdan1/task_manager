package org.example.logic;

import java.io.Serializable;
import java.util.Objects;

public class Employee implements Serializable {
    private Integer idEmployee;
    private String name;
    private Integer workTime;
    public Employee(Integer id,String name){
        this.idEmployee=id;
        this.name=name;
        this.workTime=0;
    }
    public int getId(){
        return this.idEmployee;
    }
    public String getName(){
        return this.name;
    }
    public int hashCode() {
        return Objects.hash(name, idEmployee);
    }
    public boolean equals(Object obj){
        if (obj==null || obj.getClass()!=this.getClass()) return false;
        return ((Employee)(obj)).idEmployee.equals(this.idEmployee);
    }
    public void setWorkTime(int x){
        this.workTime=x;
    }
    public int getWorkTime(){
        return this.workTime;
    }
}
//de suprascris hashCode() si equals()