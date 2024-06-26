package com.capstone.projecttea;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageItem extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button btnAdd, btnUpdate, btnDelete, btnSave;
    String selectedFunction;
    Spinner spinItem, spinSeries, spinVariaton;
    ImageView imgItem;
    EditText itemName, itemPrice, itemDescription, itemVariations;
    CardView cardSelectItem;
    private Uri filePath;
    Context context;
    FirebaseStorage storage;
    FirebaseFirestore firestore;
    StorageReference storageReference;
    FirebaseHandler firebaseHandler;
    private final int PICK_IMAGE_REQUEST = 22;

    public ManageItem() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageItem.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageItem newInstance(String param1, String param2) {
        ManageItem fragment = new ManageItem();
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
        View view = inflater.inflate(R.layout.fragment_manage_item, container, false);
        context = container.getContext();
        selectedFunction = "Add";

        // Inflate the layout for this fragment
        btnAdd = view.findViewById(R.id.btnManageItemAdd);
        btnUpdate = view.findViewById(R.id.btnItemManageItemUpdate);
        btnDelete = view.findViewById(R.id.btnItemManageItemDelete);

        btnSave = view.findViewById(R.id.btnManageSave);
        cardSelectItem = view.findViewById(R.id.cardManageSelectItem);

        spinItem = view.findViewById(R.id.spinnerManageItemSelect);
        spinSeries = view.findViewById(R.id.spinManageItemSeries);

        imgItem = view.findViewById(R.id.imageManageItem);

        itemName = view.findViewById(R.id.edtManageItemName);
        itemPrice = view.findViewById(R.id.edtManageItemPrice);
        itemDescription = view.findViewById(R.id.edtManageItemDescription);
        itemVariations = view.findViewById(R.id.edtItemVariation);

        setSelected(btnAdd, new Button[]{
                btnUpdate,
                btnDelete
        }, "Add");
        setSelected(btnUpdate, new Button[]{
                btnAdd,
                btnDelete
        }, "Update");
        setSelected(btnDelete, new Button[]{
                btnAdd,
                btnUpdate
        }, "Delete");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        firestore = FirebaseFirestore.getInstance();
        firebaseHandler = new FirebaseHandler(context);
        firebaseHandler.FillSpinner(spinItem, firestore.collection("Items"));
        firebaseHandler.FillSpinner(spinSeries, firestore.collection("Series"));

        imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent IntentImagePicker = new Intent();
                IntentImagePicker.setType("image/*");
                IntentImagePicker.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(
                                IntentImagePicker,
                                "Select Image from here..."),
                        PICK_IMAGE_REQUEST);
            }
        });

        spinItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //  Log.e("Hello","Error");
                // Toast.makeText(context,spinItem.getSelectedItem()+"Hello World",Toast.LENGTH_LONG).show();
                firestore.collection("Items").document(spinItem.getSelectedItem().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                              //  Toast.makeText(context, firebaseHandler.GetItemPosition(firestore.collection("Series"), documentSnapshot.get("Series").toString()) + "", Toast.LENGTH_SHORT).show();
                                itemName.setText(documentSnapshot.get("Name").toString());
                                itemPrice.setText(documentSnapshot.get("Price").toString());
                                itemDescription.setText(documentSnapshot.get("Description").toString());
                                itemVariations.setText(documentSnapshot.get("Variation").toString());
                                spinSeries.setSelection(firebaseHandler.GetItemPosition(null,documentSnapshot.get("Series").toString()));
                                storageReference.child("Items/" + spinItem.getSelectedItem().toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        //    Toast.makeText(context,uri.toString(),Toast.LENGTH_SHORT).show();
                                        Glide.with(context).load(uri).into(imgItem);
                                    }
                                });

                            }
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {

            //This code handles the saving of the item
            @Override
            public void onClick(View view) {

                String id = spinItem.getSelectedItem().toString();
                if (selectedFunction.equals("Delete")) {
                    // If the selected function was delete , The function of this block will be delete
                    if (spinItem.getSelectedItem().toString().contains("Choose")) {
                        Toast.makeText(context, "Select " + spinItem.getContentDescription() + " first!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //We build an alert dialog to confirm the user if he/she's sure to delete the item
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure you want to delete this item?");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    //Once yes , we set our query on Items Collection in which directed to selected item , then if that item exists we delete the item
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            storageReference.child("Items/" + spinItem.getSelectedItem().toString()).delete();
                            firestore.collection("Items").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "Item deleted Sucessfully", Toast.LENGTH_SHORT).show();
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
                if(itemName.getText().toString() == "" || itemVariations.getText().toString() == "" || itemPrice.getText().toString() == ""){
                    Toast.makeText(getContext(), "Please fill up Product Name , Variations or Price", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Default for Add and Update
                // Adding and deleting has the same block of code, Due to firebase flexibility
                // First we need to set the item on the Hashmap
                String selectedSeries = spinSeries.getSelectedItem().toString().trim();
                Map<String, Object> data = new HashMap<>();
                String itemID = itemName.getText().toString().trim() + "|" + itemVariations.getText().toString();
                data.put("ID", itemID);
                data.put("Series", selectedSeries.contains("Choose") ? "Default" : selectedSeries);
                data.put("Name", itemName.getText().toString().trim());
                data.put("Description", itemDescription.getText().toString().trim());
                data.put("Variation", itemVariations.getText().toString().trim());
                data.put("Price", itemPrice.getText().toString().trim());
                data.put("ImageLink", "https://firebasestorage.googleapis.com/v0/b/projecttea-5d955.appspot.com/o/Items%2Fitem_placeholder.png?alt=media&token=cd69d117-5198-4fe1-9ba0-e8c2b0b0ce98");


               //Then get the image path before saviing it to the database
              if(filePath == null){
                  firestore.collection("Items").document(id).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void unused) {
                          firebaseHandler.FillSpinner(spinItem, firestore.collection("Items"));
                          ResetDefault();
                          Toast.makeText(context, "Item Added Successfully", Toast.LENGTH_SHORT).show();

                      }
                  });
                  return;
              }
                // Code for showing progressDialog while uploading
                //Hanlding Item Image Upload
                ProgressDialog progressDialog
                        = new ProgressDialog(context);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                // Defining the child of storageReference
                StorageReference ref
                        = storageReference
                        .child(
                                "Items/"+itemID);

                // adding listeners on upload
                // or failure of image
                ref.putFile(filePath)
                        .addOnSuccessListener(
                                new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                    @Override
                                    public void onSuccess(
                                            UploadTask.TaskSnapshot taskSnapshot) {

                                        // Image uploaded successfully
                                        // Dismiss dialog
                                        progressDialog.dismiss();
                                        Toast
                                                .makeText(context,
                                                        "Image Uploaded!!",
                                                        Toast.LENGTH_SHORT)
                                                .show();
                                        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                String link = task.getResult().toString();

                                                data.put("ImageLink",link);

                                                firestore.collection("Items").document(id).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        firebaseHandler.FillSpinner(spinItem, firestore.collection("Items"));
                                                        Toast.makeText(context, "Item Added Successfully", Toast.LENGTH_SHORT).show();
                                                        ResetDefault();
                                                    }
                                                });
                                            }
                                        });

                                    }
                                })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                // Error, Image not uploaded
                                progressDialog.dismiss();
                                Toast
                                        .makeText(context,
                                                "Failed " + e.getMessage(),
                                                Toast.LENGTH_SHORT)
                                        .show();
                            }
                        })
                        .addOnProgressListener(
                                new OnProgressListener<UploadTask.TaskSnapshot>() {

                                    // Progress Listener for loading
                                    // percentage on the dialog box
                                    @Override
                                    public void onProgress(
                                            UploadTask.TaskSnapshot taskSnapshot) {
                                        double progress
                                                = (100.0 *
                                                taskSnapshot.getBytesTransferred() /
                                                taskSnapshot.getTotalByteCount());
                                        progressDialog.setMessage(
                                                "Uploaded " +
                                                        (int) progress + "%");
                                    }
                                });




            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST &&
                resultCode == getActivity().RESULT_OK &&
                data != null &&
                data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getActivity().getContentResolver(),
                                filePath);
                imgItem.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }


    void ResetDefault(){
        spinItem.setSelection(0);
        spinSeries.setSelection(0);

        imgItem.setImageDrawable(getResources().getDrawable(R.drawable.product_placeholder,null));

        itemName.setText("");
        itemPrice.setText("");
        itemDescription.setText("");
        itemVariations.setText("");
    }

    void setSelected(Button button, Button[] arr, String selected) {

        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                selectedFunction = selected;
                button.setBackground(getResources().getDrawable(R.drawable.btn_radioselected, null));
                button.setTextColor(getResources().getColor(R.color.white, null));
                for (Button btn : arr) {
                    btn.setBackground(getResources().getDrawable(R.drawable.btn_radiodefault, null));
                    btn.setTextColor(getResources().getColor(R.color.primary, null));
                }
                btnSave.setText(selectedFunction + " Item");
                if (selectedFunction.equals("Add")) {
                    cardSelectItem.setVisibility(View.GONE);
                    ResetDefault();
                } else {
                    cardSelectItem.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}