package com.seris.bassein.enums;

public enum Gender {
    male("Эрэгтэй"), female("Эмэгтэй");

    private final String mon;

    Gender(String mon) {
        this.mon = mon;
    }

    public String getMon() {
        return mon;
    }
}
