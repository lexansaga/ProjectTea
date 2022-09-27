package com.capstone.projecttea;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSeries#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSeries extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button btnAdd,btnUpdate,btnDelete,btnSave;
    CardView cardSelectVariations;
    EditText edtSeriesName;
    Spinner spinSelectSeries;
    String selectedFunction;
    FirebaseFirestore firestore;
    FirebaseHandler firebaseHandler;
    Context context;
    public FragmentSeries() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentVariations.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSeries newInstance(String param1, String param2) {
        FragmentSeries fragment = new FragmentSeries();
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
        firestore = FirebaseFirestore.getInstance();
        context = container.getContext();
        firebaseHandler = new FirebaseHandler(context);
        selectedFunction = "Add";
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_series, container, false);
        btnAdd = view.findViewById(R.id.btnVariationsAdd);
        btnUpdate = view.findViewById(R.id.btnVariatonsUpdate);
        btnDelete = view.findViewById(R.id.btnVariatonsDelete);
        btnSave = view.findViewById(R.id.btnSeriesSave);

        spinSelectSeries = view.findViewById(R.id.spinnerSeriesItemSelect);

        cardSelectVariations = view.findViewById(R.id.cardSelectSeries);
        edtSeriesName = view.findViewById(R.id.edtSeriesName);

        setSelected(btnAdd,new Button[]{btnUpdate,btnDelete},"Add");
        setSelected(btnUpdate,new Button[]{btnAdd,btnDelete},"Update");
        setSelected(btnDelete,new Button[]{btnAdd,btnUpdate},"Delete");
        firebaseHandler.FillSpinner(spinSelectSeries, firestore.collection("Series"));
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedFunction.equals("Delete")) {
                    if (spinSelectSeries.getSelectedItem().toString().contains("Choose")) {
                        Toast.makeText(context, "Select " + spinSelectSeries.getContentDescription() + " first!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure you want to delete this Add Ons?");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog

                            firestore.collection("Series").document(edtSeriesName.getText().toString().trim()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "Series deleted Sucessfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                    alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.font_color));
                    alert.getButton(alert.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.font_color));
                    return;
                }
                if(edtSeriesName.getText().toString().isEmpty()){

                    Toast.makeText(getContext(), "Please fill up AddOns Name and Price", Toast.LENGTH_SHORT).show();

                    return;
                }
                //Default for Add and Update
                Map<String, Object> data = new HashMap<>();
                data.put("Name", edtSeriesName.getText().toString().trim());

                firestore.collection("Series").document(edtSeriesName.getText().toString()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        firebaseHandler.FillSpinner(spinSelectSeries, firestore.collection("Series"));
                        Toast.makeText(context, "Series Added Successfully", Toast.LENGTH_SHORT).show();
                        ResetDefault();

                    }
                });

            }

        });
        return view;
    }
    void ResetDefault(){
        spinSelectSeries.setSelection(0);
        edtSeriesName.setText("");
    }
    void setSelected(Button button,Button[] arr,String selected){

        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                selectedFunction = selected;

                button.setBackground(getResources().getDrawable(R.drawable.btn_radioselected,null));
                button.setTextColor(getResources().getColor(R.color.white,null));
                for (Button btn: arr
                ) {
                    btn.setBackground(getResources().getDrawable(R.drawable.btn_radiodefault,null));
                    btn.setTextColor(getResources().getColor(R.color.primary,null));
                }
                btnSave.setText(selectedFunction + " Series");
                if(selectedFunction.equals("Add")){
                    cardSelectVariations.setVisibility(View.GONE);
                }
                else{
                    cardSelectVariations.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}