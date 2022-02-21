package com.example.foodfun;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.foodfun.model.Ingredients;
import com.example.foodfun.model.Meal;
import com.example.foodfun.model.Supplies;
import com.example.foodfun.model.TypeOfMeal;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomizeTrip#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomizeTrip extends Fragment {
    Button cancel;
    Button moveOn;
    Dialog dialog;
    TextInputLayout addMealLayout;
    TextInputLayout addPeopleLayout;
    TextInputEditText addIngredientName;
    TextInputEditText addMealName;
    TextInputLayout ingredientLayout;
    TextInputLayout generalIngredientLayout;
    TextInputLayout suppliesLayout;
    TextInputEditText numberOfPeople;
    TextInputEditText nameOfSupplies;
    TextInputEditText numberOfIngredients;
    TextInputEditText addGeneralIngredient;
    RadioButton breakfast;
    RadioButton lunch;
    RadioButton dinner;
    FragmentCustomizeTripListener listener;
    FirebaseDatabase fireBase=FirebaseDatabase.getInstance();
    DatabaseReference ref= fireBase.getReference("Trips");
    ListView ls;
    ArrayAdapter adapter ;
    Button addMeal;
    Button createMeal;
    Button goToFinal;
    LinearLayout mealLayout;
    ArrayList<Meal> meals;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public interface FragmentCustomizeTripListener{
        void getMeal(Meal addMeal);
        void addMeal(Meal newMeal);
        void changeTab();
    }
    public CustomizeTrip() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditTrip.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomizeTrip newInstance(String param1, String param2) {
        CustomizeTrip fragment = new CustomizeTrip();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        this.meals=new ArrayList<Meal>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_coustomize_trip, container, false);
        addMeal=(Button) v.findViewById(R.id.add_meal_button);
        mealLayout=v.findViewById(R.id.create_new_meal);
        ls=v.findViewById(R.id.list_view);
        addToArray();
        this.adapter=new ArrayAdapter(this.getActivity().getApplicationContext(), android.R.layout.simple_selectable_list_item,android.R.id.text1,meals);
        ls.setAdapter(adapter);
        createNewMealLayer();
        setAddMeal();
        addMealLayout=v.findViewById(R.id.meal_name_layout);
        addPeopleLayout=v.findViewById(R.id.meal_people_layout);
        breakfast=v.findViewById(R.id.Breakfast);
        dinner=v.findViewById(R.id.Dinner);
        lunch=v.findViewById(R.id.Lunch);
        addMealName=v.findViewById(R.id.meal_name);
        addIngredientName=v.findViewById(R.id.ingredient_name);
        ingredientLayout =v.findViewById(R.id.ingredient_name_layout);
        numberOfIngredients=v.findViewById(R.id.number_ingredients);
        numberOfPeople=v.findViewById(R.id.meal_people);
        generalIngredientLayout=v.findViewById(R.id.general_ingredient_layer);
        suppliesLayout=v.findViewById(R.id.supplies_name_layout);
        addGeneralIngredient=v.findViewById(R.id.general_ingredient);
        createMeal=v.findViewById(R.id.finish_new_meal);
        dialog=new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(false);
        cancel=dialog.findViewById(R.id.cancel_meal_dialog);
        moveOn=dialog.findViewById(R.id.add_meal_dialog);
        goToFinal=v.findViewById(R.id.finish_customize_trip);
        createMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addMealName.getText().toString().isEmpty()){
                    if(numberOfPeople.getText().toString().isEmpty())
                        addPeopleLayout.setError("Forgot it");
                    addMealLayout.setError("Forgot it");
                    Toast.makeText(getActivity(), "Forgot Something", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (checkIfMealFull(createMeal()))
                        listener.addMeal(createMeal());
                    else {
                        dialog.show();
                        cancel.setOnClickListener(e -> {
                            Toast.makeText(getActivity(), "Canceled new meal", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        });
                        moveOn.setOnClickListener(e -> {
                            listener.addMeal(createMeal());
                            mealLayout.setVisibility(View.GONE);
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Created new meal", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            }
        });



        finishedCustomizeTrip();
        return v;
    }

    public void createNewMealLayer(){
        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mealLayout.getVisibility()==View.VISIBLE)
                    mealLayout.setVisibility(View.GONE);
                else
                    mealLayout.setVisibility(View.VISIBLE);
            }
        });

    }

    public void addToArray(){
        //Ingredients
        Ingredients salt=new Ingredients("salt");
        Ingredients pepper=new Ingredients("pepper");
        Ingredients canolaOil=new Ingredients("canola oil");
        Ingredients oliveOil=new Ingredients("olive oil");
        Ingredients eggs=new Ingredients("eggs");
        Ingredients tomato=new Ingredients("tomato");
        Ingredients sugar=new Ingredients("sugar");
        Ingredients pasta=new Ingredients("pasta");
        Ingredients chickenBreast=new Ingredients("chicken breast");
        Ingredients rice=new Ingredients("rice");
        Ingredients potato=new Ingredients("potato");

        sugar.setGeneral(true);
        canolaOil.setGeneral(true);
        pepper.setGeneral(true);
        salt.setGeneral(true);



        //supplies
        Supplies pan=new Supplies("pan");
        Supplies pot=new Supplies("pot");
        Supplies fork=new Supplies("fork");
        Supplies plate=new Supplies("plate");
        Supplies knife=new Supplies("knife");
        Supplies cup=new Supplies("cup");
        Supplies trashBag=new Supplies("trash bag");
        Supplies teaPot=new Supplies("tea pot");
        Supplies gas=new Supplies("gas");


        Meal shakshuka=new Meal("shakshuka", TypeOfMeal.BREAKFAST);
        Meal chickenWithRice=new Meal("chicken with rice",TypeOfMeal.DINNER);
        Meal pastaWithTomatoes=new Meal("pasta italian",TypeOfMeal.LUNCH);
        Meal potatoInFire=new Meal("potato in fire",TypeOfMeal.DINNER);

        shakshuka.addIngredient(salt);
        shakshuka.addIngredient(pepper);
        shakshuka.addIngredient(tomato);
        shakshuka.addIngredient(eggs);
        shakshuka.addIngredient(canolaOil);

        chickenWithRice.addIngredient(salt);
        chickenWithRice.addIngredient(pepper);
        chickenWithRice.addIngredient(chickenBreast);
        chickenWithRice.addIngredient(rice);

        pastaWithTomatoes.addIngredient(salt);
        pastaWithTomatoes.addIngredient(pepper);
        pastaWithTomatoes.addIngredient(tomato);
        pastaWithTomatoes.addIngredient(oliveOil);
        pastaWithTomatoes.addIngredient(pasta);

        potatoInFire.addIngredient(potato);

        potatoInFire.addSupplies(pot);

        shakshuka.addSupplies(fork);
        shakshuka.addSupplies(plate);
        shakshuka.addSupplies(knife);

        pastaWithTomatoes.addSupplies(plate);

        chickenWithRice.addSupplies(pan);

        meals.add(potatoInFire);
        meals.add(shakshuka);
        meals.add(chickenWithRice);
        meals.add(pastaWithTomatoes);


    }

    public void setAddMeal(){

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Meal newMeal= (Meal) ls.getAdapter().getItem(position);
                listener.getMeal(newMeal);
            }
        });
    }

    public Ingredients addMealOfCustomer(){
        Ingredients ingredients=new Ingredients(null);
        try {
            ingredientLayout.setStartIconOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    CharSequence name = addIngredientName.getText();
                    if (numberOfIngredients.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "You didn't give a number", Toast.LENGTH_SHORT).show();
                    } else {
                        if (name.toString().equals("")) {
                            Toast.makeText(getActivity(), "Didn't enter a name", Toast.LENGTH_SHORT).show();
                        } else {
                            ingredients.setName(name.toString());
                            ingredients.setHowMuch(Integer.parseInt(numberOfIngredients.getText().toString()));
                            Toast.makeText(getActivity(), "Added a new Ingredient", Toast.LENGTH_SHORT).show();
                            addIngredientName.setText("");
                            numberOfIngredients.setText("");
                        }
                    }
                }

            });
        }catch (Exception exception){
            if(ingredients.getName().isEmpty())
                throw exception;
        }
        return ingredients;
    }

    public  Meal createMeal(){
        TypeOfMeal type = null;
        if(breakfast.isChecked())
            type=TypeOfMeal.BREAKFAST;
        if (lunch.isChecked())
            type=TypeOfMeal.LUNCH;
        if (dinner.isChecked())
            type=TypeOfMeal.DINNER;
        while(addMeal.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),"Didn't add a name",Toast.LENGTH_SHORT).show();
        }
        Meal newMeal=new Meal(addMeal.getText().toString(),type);
        if(!newMeal.addIngredient(addMealOfCustomer()))
            Toast.makeText(getActivity(),"This ingredient already in the meal",Toast.LENGTH_SHORT).show();
        if(!newMeal.addSupplies(addSupplies()))
            Toast.makeText(getActivity(),"This supplies already in the meal",Toast.LENGTH_SHORT).show();
        if(!newMeal.addIngredient(addGeneralIngredient()))
            Toast.makeText(getActivity(),"This ingredient already in the meal",Toast.LENGTH_SHORT).show();

        return newMeal;
    }


    public boolean checkIfMealFull(Meal check){
        int counter=0;
        if(check.getIngredients().isEmpty()||check.getSupplies().isEmpty())
            return false;

        for (int i = 0; i <check.getCountIngredientsArr() ; i++) {
            if(check.getIngredients().get(i).isGeneral())
                counter++;
        }
        if(counter==0)
            return false;

        return true;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof CustomizeTrip.FragmentCustomizeTripListener) {
            listener = (CustomizeTrip.FragmentCustomizeTripListener) context;
        }else{
            throw new RuntimeException(context.toString()+" must implement FragmentCustomizeTripListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }

    public Supplies  addSupplies(){
        Supplies newSupplies=new Supplies("");
        suppliesLayout.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameOfSupplies.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Didn't add a name", Toast.LENGTH_SHORT).show();
                }
                else {
                    newSupplies.setName(nameOfSupplies.getText().toString());
                    Toast.makeText(getActivity(), "added new supplies", Toast.LENGTH_SHORT).show();
                }
            }

        });
        return newSupplies;
    }

    public Ingredients addGeneralIngredient(){
        Ingredients newIngredient=new Ingredients("");
        generalIngredientLayout.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addGeneralIngredient.getText().toString().isEmpty())
                    Toast.makeText(getActivity(), "No name to ingredient", Toast.LENGTH_SHORT).show();
                else{
                    newIngredient.setName(addGeneralIngredient.getText().toString());
                    newIngredient.setGeneral(true);
                    Toast.makeText(getActivity(), "Added general ingredient", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return newIngredient;
    }

    public void finishedCustomizeTrip(){
        goToFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.changeTab();
            }
        });

    }



}

