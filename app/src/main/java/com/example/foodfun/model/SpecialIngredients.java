package com.example.foodfun.model;

public class SpecialIngredients extends Ingredients {

    private boolean isInBag;
    SpecialIngredients(String name,boolean isInBag) {
        super(name);
        this.isInBag=true;
    }




}
