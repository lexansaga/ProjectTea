package com.capstone.projecttea;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class FirebaseHandler {
    Context context;
    FirebaseFirestore firestore;
    ArrayList<String> arrayList;
    StorageReference storageReference;
    String ImageLink;
    public FirebaseHandler(Context context) {
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

    }

    public int GetItemPosition(Query path, String name) {

        Log.e("data",arrayList.toString());
        return arrayList.indexOf(name);
    }

    public void FillSpinner(Spinner spinner, Query path) {

        path.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<String> data = new ArrayList<>();
                    String appSpinner = (String) spinner.getContentDescription();
                    data.add(0, "Choose " + appSpinner);
                    for (QueryDocumentSnapshot snapshot : task.getResult()
                    ) {
                        if (appSpinner.equals("Item")) {

                            data.add(snapshot.get("Name").toString().trim() + "|" + snapshot.get("Variation".toString().trim()));
                        }
                        else if(appSpinner.equals("Variation")){
                            data.add(snapshot.get("Variation").toString().trim());
                        }else {

                            data.add(snapshot.get("Name").toString().trim());
                        }

                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, data);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(spinnerArrayAdapter);
                    spinnerArrayAdapter.notifyDataSetChanged();

                    arrayList = data;


                }
            }
        });

    }

    public String GetImageLink(){

        return ImageLink;
    }


    public FirebaseHandler UploadImage(Uri filePath,String databasePath) {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                           databasePath);

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
                                          ImageLink = link;
                                          Toast
                                                  .makeText(context,
                                                          ""+ImageLink,
                                                          Toast.LENGTH_SHORT)
                                                  .show();
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

        return this;
    }
}
