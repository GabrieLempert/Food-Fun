package com.example.foodfun;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link create_trip#newInstance} factory method to
 * create an instance of this fragment.
 */
public class create_trip extends Fragment {
    FragmentAListener listener;
    Button submitName;
    Button submitPeople;
    Button submitDays;
    EditText enterName;
    EditText enterDays;
    EditText enterPeople;
    public interface FragmentAListener{
        public void onInputASent(CharSequence name);
        public void onInputDaysSent(int days);
        public void onInputPeopleSent(int people);

    }
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public create_trip() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment create_trip.
     */
    // TODO: Rename and change types and number of parameters
    public static create_trip newInstance(String param1, String param2) {
        create_trip fragment = new create_trip();
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

        View v=inflater.inflate(R.layout.fragment_create_trip, container, false);
        submitName=v.findViewById(R.id.Submit_Name);
        submitPeople=v.findViewById(R.id.Number_People);
        submitDays=v.findViewById(R.id.Submit_Number);
        enterDays=v.findViewById(R.id.Number_Days);
        enterName=v.findViewById(R.id.editTextTextPersonName);
        enterPeople=v.findViewById(R.id.editTextNumberSigned);
        submitName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(enterName.getText().toString().isEmpty())
                    Toast.makeText(getActivity(), "didn't enter a name", Toast.LENGTH_SHORT).show();
                else {
                    CharSequence name = enterName.getText();
                    listener.onInputASent(name);
                    Toast.makeText(getActivity(), "added new supplies", Toast.LENGTH_SHORT).show();
                    enterName.setEnabled(false);
                }
            }
        });

        submitDays.setOnClickListener(new View.OnClickListener() {
            int numberOfDays = 0;
            @Override
            public void onClick(View v) {
                if (enterDays.getText().toString().isEmpty() || Integer.parseInt(enterDays.getText().toString()) == 0) {
                    if (enterDays.getText().toString().isEmpty() ) {
                        Toast.makeText(getActivity(), "didn't enter a number", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Need a number higher than 0", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    numberOfDays=Integer.parseInt(enterDays.getText().toString());
                    listener.onInputDaysSent(numberOfDays);
                    enterDays.setEnabled(false);

                }
            }
        });

        submitPeople.setOnClickListener(new View.OnClickListener() {
            int numberOfPeople =0;
            @Override
            public void onClick(View v) {
                if(enterPeople.getText().toString().isEmpty()||Integer.parseInt(enterDays.getText().toString())==0) {
                    if (enterPeople.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "didn't enter a number", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Need a number higher than 0", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    numberOfPeople = Integer.parseInt(enterPeople.getText().toString());
                    listener.onInputPeopleSent(numberOfPeople);
                    enterPeople.setEnabled(false);
                }

            }

        });


        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof FragmentAListener) {
            listener = (FragmentAListener) context;
        }else{
            throw new RuntimeException(context.toString()+" must implement FragmentAListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }
}