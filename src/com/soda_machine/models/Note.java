package com.soda_machine.models;

import java.util.List;

public class Note {
    private int id;
    public int getId(){
        return id;
    }

    private int value;

    public int getValue() {
        return value;
    }

    private int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Note(int value, int amount, int id){
        this.value = value;
        this.amount = amount;
        this.id = id;
    }

    public int getSum(){
        return this.value * this.amount;
    }

    public static int getSumNotes( List<Note> notes){
        return notes.stream().mapToInt(o -> o.getAmount() * o.getValue()).sum();
    }
}
