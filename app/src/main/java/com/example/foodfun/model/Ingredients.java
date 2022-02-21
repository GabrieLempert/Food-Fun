package com.example.foodfun.model;

import java.util.Objects;

public class Ingredients implements Comparable{

   public void setName(String name) {
      this.name = name;
   }

   public void setGeneral(boolean general) {
      this.general = general;
   }

   private boolean general;
   private String name;
   private int howMuch;

   public Ingredients(String name){
      this.name=name;
      this.howMuch=0;
      this.general=false;
   }

   public String getName() {
      return name;
   }

   public void setHowMuch(int howMuch) {
      this.howMuch = howMuch;
   }

   public int getHowMuch() {
      return howMuch;
   }


   @Override
   public int compareTo(Object o) {
     return this.name.compareTo((String) o);
   }

   @Override
   public boolean equals(Object o) {
      if(o instanceof Ingredients){
         return this.name.equals(((Ingredients) o).name);
      }else{
         return  false;
      }
   }


   public boolean isGeneral() {
      return general;
   }


}
