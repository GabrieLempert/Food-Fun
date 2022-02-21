package com.example.foodfun;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.foodfun.model.Ingredients;
import com.example.foodfun.model.Meal;
import com.example.foodfun.model.Trip;
import com.example.foodfun.view.MainActivity;
import com.example.foodfun.view.NewTrip;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Final#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Final extends Fragment {
    Trip newTrip;
    FinalFragmentListener listener;
    ArrayAdapter adapterForIngredient;
    ArrayAdapter adapterForSupplies;
    ArrayAdapter getAdapterForIngredientGeneral;
    ListView getLsIngredientsGeneral;
    ListView lsIngredients;
    ListView lsSupplies;
    Button save;


    public interface FinalFragmentListener{
        public Trip getTrip();
        public void changeTab(int position);
        public void saveToFireBase();
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Final() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Final.
     */
    // TODO: Rename and change types and number of parameters
    public static Final newInstance(String param1, String param2) {
        Final fragment = new Final();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_final, container, false);
        getLsIngredientsGeneral=v.findViewById(R.id.general_ingredient_list);
        lsIngredients=v.findViewById(R.id.ingredient_list);
        lsSupplies=v.findViewById(R.id.supplies_list);
        setLsIngredients();
        setLsSupplies();
        setLsIngredientsGeneral();

        save=v.findViewById(R.id.save);
        finishAndSave();
        return v;
    }




    public void setLsIngredients(){
        ArrayList<String> listIngredients=new ArrayList<String>();
        if(!listIngredients.contains("Water bottles "+listener.getTrip().getWaterBottles()))
            listIngredients.add("Water bottles "+listener.getTrip().getWaterBottles());
        if(!listener.getTrip().getMeals().isEmpty())
        {
            ArrayList<Ingredients> ingredientsArrayList=listener.getTrip().allIngredients();
            for (int i = 0; i <ingredientsArrayList.size() ; i++) {
                listIngredients.add(listener.getTrip().calculateIngredients(ingredientsArrayList.get(i)));
            }
            adapterForIngredient=new ArrayAdapter(this.getActivity().getApplicationContext(), android.R.layout.simple_list_item_2,android.R.id.text1,listIngredients);
        }

    }

    public  void setLsIngredientsGeneral(){
        ArrayList<String> listIngredients=new ArrayList<String>();
        if(!listener.getTrip().getMeals().isEmpty()) {
            for (int i = 0; i < listener.getTrip().getMeals().size(); i++) {
                for (int j = 0; j < listener.getTrip().getMeals().get(i).getIngredients().size(); j++) {
                    listener.getTrip().addGeneralIngredients(listener.getTrip().getMeals().get(i).getIngredients().get(i));
                }
            }
        }

        for (int i = 0; i < listener.getTrip().getGeneralIngredients().size(); i++) {
            String outPut="The general is: "+listener.getTrip().getGeneralIngredients().get(i).getName();
            listIngredients.add(outPut);
        }
        getAdapterForIngredientGeneral=new ArrayAdapter(this.getActivity().getApplicationContext(), android.R.layout.simple_gallery_item,android.R.id.text1,listIngredients);
    }

    public void setLsSupplies() {
        ArrayList<String> listSupplies=new ArrayList<String>();

        for (int i = 0; i <listener.getTrip().getMeals().size(); i++) {
            for (int j = 0; j < listener.getTrip().getMeals().get(i).getSupplies().size();j++) {
                listener.getTrip().setSupplies(listener.getTrip().getMeals().get(i).getSupplies().get(j));
            }
        }
        for (int i = 0; i <listener.getTrip().getSupplies().size() ; i++) {
            String outPut=listener.getTrip().getSupplies().get(i).getName();
            listSupplies.add(outPut);
        }
        this.adapterForSupplies=new ArrayAdapter(this.getActivity().getApplicationContext(), android.R.layout.simple_gallery_item,android.R.id.text1,listSupplies);

    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Final.FinalFragmentListener) {
            listener = (Final.FinalFragmentListener) context;
        }else{
            throw new RuntimeException(context.toString()+" must implement FragmentCustomizeTripListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }

    @Override
    public void onResume() {
        super.onResume();
        setLsIngredients();
        lsIngredients.setAdapter(this.adapterForIngredient);
        setLsIngredientsGeneral();
        getLsIngredientsGeneral.setAdapter(this.getAdapterForIngredientGeneral);
        setLsSupplies();
        lsSupplies.setAdapter(this.adapterForSupplies);
    }

    public void  finishAndSave(){
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAll()) {
                    listener.saveToFireBase();
                    startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
                    getActivity().finish();
                }
            }
        });
    }

    public boolean checkAll(){
        if(listener.getTrip().getSupplies().isEmpty()||listener.getTrip().getMeals().isEmpty()||listener.getTrip().getGeneralIngredients().isEmpty()){
            listener.changeTab(1);
            Toast.makeText(getActivity(),"check again maybe you didnt add somthing",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(listener.getTrip().getNumberOfPeople()==0||listener.getTrip().getNumberOfDays()==0) {
            Toast.makeText(getActivity(),"check again maybe you didnt add somthing",Toast.LENGTH_SHORT).show();
            listener.changeTab(0);
            return false;
        }
        return true;
    }



}