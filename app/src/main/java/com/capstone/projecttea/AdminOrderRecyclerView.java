package com.capstone.projecttea;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AdminOrderRecyclerView extends RecyclerView.Adapter < AdminOrderRecyclerView.ViewHolder > {
    public enum Page {
        main,
        history
    }
    private ArrayList < ProductModel > productModelArrayList;
    private Context context;
    AdminOrderRecyclerView.Page page;

    FirebaseFirestore firestore;

    public AdminOrderRecyclerView(Context context, ArrayList < ProductModel > productModels, AdminOrderRecyclerView.Page page) {
        this.page = page;
        productModelArrayList = productModels;
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem = layoutInflater.inflate(R.layout.layout_adminorders, viewGroup, false);
        AdminOrderRecyclerView.ViewHolder viewHolder = new AdminOrderRecyclerView.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        ProductModel productModel = productModelArrayList.get(position);
        OrderModel orderModel = productModel.getOrderModel();
        UserModel userModel = productModel.getUserModel();

        viewHolder.txtOrderNo.setText(orderModel.getOrderID());
        viewHolder.txtID.setText(orderModel.getOrderNumber().substring(1,6).toUpperCase(Locale.ROOT));
        viewHolder.txtName.setText(userModel.getName());
        viewHolder.txtContact.setText(userModel.getContact());
        viewHolder.txtAddress.setText(userModel.getAddress());
        viewHolder.txtSubtotal.setText(orderModel.getSubtotal());
        viewHolder.txtShippingfee.setText(orderModel.getShippingFee());
        viewHolder.txtTotal.setText(orderModel.getGrandTotal());
        viewHolder.txtDate.setText(orderModel.getDate());


        Query queryProduct = firestore.collection("Orders").document(userModel.getEmail()).collection("Product").whereEqualTo("OrderID",orderModel.getOrderNumber());
      //  Toast.makeText(context, orderModel.getOrderNumber(), Toast.LENGTH_SHORT).show();
        queryProduct.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot snapshot) {
                ArrayList<ProductModel> productModels  =  new ArrayList<>();

                for (QueryDocumentSnapshot productSnapshot: snapshot
                ) {
                    String productOrderID = productSnapshot.get("ID").toString();
                    String refOrderID = productSnapshot.get("OrderID").toString();
                    String productName = productSnapshot.get("Name").toString();
                    String price = productSnapshot.get("Price").toString();
                    String quantity = productSnapshot.get("Quantity").toString();
                    String variation = productSnapshot.get("Variation").toString();
                    String addOns = Utils.CheckTextIfNull(productSnapshot.get("AddOns"),"No AddOns");
                    String timeOrder = productSnapshot.get("TimeOrder").toString();
                    String productTotal = productSnapshot.get("Total_Price").toString();
                    String imageLink = productSnapshot.get("ImageLink").toString();


                    ProductModel model = new ProductModel();
                    model.setID(productModel.getID());
                    model.setImageLink(imageLink);
                    model.setProductName(productName);

                    model.setVariation(variation);
                    model.setPrice(price);
                    model.setQuantity(Integer.parseInt(quantity));
                    model.setAddOns(addOns);
                    productModels.add(model);

                    //       productModels.add(new ProductModel(productModel.getID(), imageLink, productName, price, variation, Integer.parseInt(quantity)));

                }
                RecyclerView productRecyclerView = viewHolder.adminOrderRecyclerView;
                CartRecyclerViewAdapter adapter = new CartRecyclerViewAdapter(CartRecyclerViewAdapter.CartVariation.Checkout, productModels, context);
                productRecyclerView.setHasFixedSize(true);
                productRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                productRecyclerView.setAdapter(adapter);
              //  adapter.notifyDataSetChanged();
            }
        });

        Query query = firestore.collection("Orders").document(userModel.getEmail()).collection("Orders").whereEqualTo("ID",orderModel.getOrderNumber());
        //  Toast.makeText(context, orderModel.getOrderNumber().toString(), Toast.LENGTH_SHORT).show();
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot snapshot) {
                if(snapshot.isEmpty()){
                    Toast.makeText(context,"Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                //     Toast.makeText(context,"Status", Toast.LENGTH_SHORT).show();
                String status = orderModel.getStatus();

                changeOrderStatus(status,viewHolder);
            }
        });

        if (page == Page.main) {




            viewHolder.btnInProgress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Map<String,Object> data = new HashMap<>();
                    data.put("Status","InProgress");
                    firestore.collection("Orders").document(userModel.getEmail()).collection("Orders").document(orderModel.getOrderNumber()).set(data, SetOptions.merge());
                    changeOrderStatus("InProgress",viewHolder);

                }
            });

            viewHolder.btnOrderReady.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Map<String,Object> data = new HashMap<>();
                    data.put("Status","OrderReady");
                    firestore.collection("Orders").document(userModel.getEmail()).collection("Orders").document(orderModel.getOrderNumber()).set(data, SetOptions.merge());

                    changeOrderStatus("OrderReady",viewHolder);


                }
            });

            viewHolder.btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Map<String,Object> data = new HashMap<>();
                    data.put("Status","Cancel");
                    firestore.collection("Orders").document(userModel.getEmail()).collection("Orders").document(orderModel.getOrderNumber()).set(data, SetOptions.merge());
                    changeOrderStatus("Cancel",viewHolder);


                }
            });

        } else if (page == Page.history) {

            viewHolder.btnInProgress.setVisibility(View.GONE);
            viewHolder.btnOrderReady.setVisibility(View.GONE);
            viewHolder.btnCancel.setVisibility(View.GONE);

            viewHolder.txtStatus.setVisibility(View.VISIBLE);
            viewHolder.txtStatus.setText(orderModel.getStatus());
        }

    }

    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }

    public void changeOrderStatus (String status, ViewHolder viewHolder){

        if(status.contains("InProgress")){
         //   Toast.makeText(context,status, Toast.LENGTH_SHORT).show();
            viewHolder.topLevel.setBackgroundColor(context.getResources().getColor(R.color.yellow_accent));
            viewHolder.bottomLevel.setBackgroundColor(context.getResources().getColor(R.color.yellow_accent));

        }
        if(status.contains("OrderReady")){
           // Toast.makeText(context,status, Toast.LENGTH_SHORT).show();
            viewHolder.topLevel.setBackgroundColor(context.getResources().getColor(R.color.blue_accent));
            viewHolder.bottomLevel.setBackgroundColor(context.getResources().getColor(R.color.blue_accent));
        }
        if(status.contains("Cancel")){
            //Toast.makeText(context,status, Toast.LENGTH_SHORT).show();
            viewHolder.topLevel.setBackgroundColor(context.getResources().getColor(R.color.red_accent));
            viewHolder.bottomLevel.setBackgroundColor(context.getResources().getColor(R.color.red_accent));
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtID, txtOrderNo, txtName, txtContact, txtAddress, txtSubtotal, txtShippingfee, txtTotal, txtStatus, txtDate;
        RecyclerView adminOrderRecyclerView;
        Button btnInProgress, btnOrderReady, btnCancel;
        ConstraintLayout topLevel, bottomLevel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.txtID = (TextView) itemView.findViewById(R.id.adminOrdersID);
            this.txtOrderNo = (TextView) itemView.findViewById(R.id.adminOrderNumber);
            this.txtName = (TextView) itemView.findViewById(R.id.adminOrderName);
            this.txtContact = (TextView) itemView.findViewById(R.id.adminOrderContact);
            this.txtAddress = (TextView) itemView.findViewById(R.id.adminOrderAddress);
            this.txtSubtotal = (TextView) itemView.findViewById(R.id.adminOrderSubtotal);
            this.txtShippingfee = (TextView) itemView.findViewById(R.id.adminOrderShippingFee);
            this.txtTotal = (TextView) itemView.findViewById(R.id.adminOrderTotal);
            this.txtStatus = (TextView) itemView.findViewById(R.id.adminOrderStatus);

            this.txtDate = (TextView) itemView.findViewById(R.id.txtAdminOrderDate);

            this.topLevel = (ConstraintLayout) itemView.findViewById(R.id.topLevel);
            this.bottomLevel = (ConstraintLayout) itemView.findViewById(R.id.bottomLevel);
            this.adminOrderRecyclerView = (RecyclerView) itemView.findViewById(R.id.adminOrderRecyclerView);

                this.btnInProgress = (Button) itemView.findViewById(R.id.btn_inprogress);
                this.btnOrderReady = (Button) itemView.findViewById(R.id.btn_orderready);
                this.btnCancel = (Button) itemView.findViewById(R.id.btn_cancel);



        }
    }
}