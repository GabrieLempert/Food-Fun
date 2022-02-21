package com.example.foodfun.model;

import java.util.ArrayList;

public class Trip {
    private String name;

    public int getNumberOfDays() {
        return numberOfDays;
    }

    private int numberOfDays=0;

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    private int numberOfPeople=0;

    public ArrayList<Meal> getMeals() {
        return meals;
    }

    private ArrayList<Meal> meals;
    private int numberOfMeals=0;
    private int numberOfGeneralIngredients =0;

    public int getWaterBottles() {
        return waterBottles;
    }

    private int waterBottles;
    private static int  LITTER_PER_PERSON=2;

    public ArrayList<Ingredients> getGeneralIngredients() {
        return generalIngredients;
    }

    private ArrayList<Ingredients> generalIngredients;

    public ArrayList<Supplies> getSupplies() {
        return supplies;
    }

    private ArrayList<Supplies> supplies;

    public Trip() {
        this.numberOfDays=0;
        this.numberOfPeople=0;
        this.name="NEW TRIP";
        this.waterBottles=(numberOfPeople*LITTER_PER_PERSON)*numberOfDays;
        this.meals=new ArrayList<Meal>();
        this.generalIngredients=new ArrayList<Ingredients>();
        this.supplies=new ArrayList<Supplies>();

    }

    public boolean addMeal(Meal newMeal){
        meals.add(newMeal);
        numberOfMeals++;
        return true;
    }

    public String addGeneralIngredients(Ingredients newIngredient){
        int count=0;
        for (int i = 0; i < numberOfMeals; i++) {
            if (meals.get(i).getIngredients().contains(newIngredient)) {
                int index = meals.get(i).getIngredients().indexOf(newIngredient);
                if (meals.get(i).getIngredients().get(index).isGeneral()){
                    count++;
                    if(!generalIngredients.contains(newIngredient))
                        generalIngredients.add(newIngredient);
                }
            }
        }
        int howMuch=(numberOfPeople*count)/30+1;
        String outPutString="The general ingredient is "+newIngredient.getName()+"and you need this much: "+howMuch;
        return outPutString;
    }


    public void setSupplies(Supplies newSupplies){
        for (int i = 0; i < meals.size(); i++) {
            if(!supplies.contains(newSupplies))
                supplies.add(newSupplies);
        }
    }
    public String calculateIngredients(Ingredients ingredients){
        int howMuchForTrip=0;
        for (int i = 0; i <numberOfMeals ; i++) {
            if (meals.get(i).getIngredients().contains(ingredients)){
                int index= meals.get(i).getIngredients().indexOf(ingredients);
                if(!meals.get(i).getIngredients().get(index).isGeneral())
                    howMuchForTrip= meals.get(i).getIngredients().get(index).getHowMuch()*meals.get(i).getNumberOfPeople()+howMuchForTrip;
            }
        }
        String ingredientOutput="The "+ingredients.getName()+" need for trip "+howMuchForTrip;
        return ingredientOutput;
    }

    public ArrayList<Ingredients> allIngredients(){
        ArrayList<Ingredients> arrayList=new ArrayList<Ingredients>();
        for (int i = 0; i <numberOfMeals ; i++) {
            for (int j = 0; j < meals.get(i).getCountIngredientsArr(); j++) {
                if(!arrayList.contains(meals.get(i).getIngredients().get(j)))
                    arrayList.add(meals.get(i).getIngredients().get(j));
            }
        }
        return arrayList;
    }


    public void setName(String name) {
        this.name = name;
    }
    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }
    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "Trip" + getName();
    }
}

