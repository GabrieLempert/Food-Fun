package com.example.foodfun.model;

import java.util.ArrayList;

public class Meal {


    private ArrayList<Ingredients> ingredients;
    private int numberOfPeople;
    private int countIngredientsArr=0;
    private ArrayList<Supplies> supplies;
    private int countSuppliesArr=0;
    private TypeOfMeal type;
    private String name;


    public Meal(String name, TypeOfMeal type){
        this.ingredients=new ArrayList<Ingredients>();
        this.supplies=new ArrayList<Supplies>();
        this.numberOfPeople=0;
        this.name=name;
        this.type=type;
    }

    public boolean addIngredient(Ingredients newIngredient){
        if (!ingredients.isEmpty()) {
            for (int i = 0; i < countIngredientsArr; i++) {
                if (ingredients.contains(newIngredient)) {
                    return false;
                }
            }
        }
        ingredients.add(newIngredient);
        countIngredientsArr++;
        return true;
    }
    public boolean addSupplies(Supplies newSupplies){

        if (!supplies.isEmpty()) {
            for (int i = 0; i < countSuppliesArr; i++) {
                if (supplies.contains(newSupplies))
                    return false;
            }
        }
        supplies.add(newSupplies);
        countSuppliesArr++;
        return true;
    }

    public ArrayList<Ingredients> getIngredients() {
        return ingredients;
    }

    public int getCountIngredientsArr() {
        return countIngredientsArr;
    }

    public ArrayList<Supplies> getSupplies() {
        return supplies;
    }

    public int getCountSuppliesArr() {
        return countSuppliesArr;
    }

    public TypeOfMeal getType() {
        return type;
    }

    public String getName() {
        return name;
    }




    @Override
    public boolean equals(Object o) {
        if(o instanceof Meal){
            if (this.name.equals(((Meal) o).name)&&this.type.equals(((Meal) o).type)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("The meal is: " + name);
        return sb.toString();
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }
}
