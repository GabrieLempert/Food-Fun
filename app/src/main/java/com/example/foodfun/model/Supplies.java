package com.example.foodfun.model;

import java.util.Objects;

public class Supplies implements Comparable {
    private String name;


    private int howMuch;

    public Supplies(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Object o) {
        return this.name.compareTo((String) o);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Supplies) {
            return this.name.equals(((Supplies) o).name);
        } else {
            return false;
        }
    }

    public void setName(String name) {
        this.name=name;
    }
}
