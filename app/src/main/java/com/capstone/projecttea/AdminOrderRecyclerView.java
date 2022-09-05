package com.capstone.projecttea;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class AdminOrderRecyclerView extends RecyclerView.Adapter<AdminOrderRecyclerView.ViewHolder> {
 public enum Page {main,history}
    private ArrayList<ProductModel> productModelArrayList;
    private Context context;
    AdminOrderRecyclerView.Page page;

    public AdminOrderRecyclerView(Context context, ArrayList<ProductModel> productModels,AdminOrderRecyclerView.Page page ){
        this.page = page;
        productModelArrayList = productModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater =  LayoutInflater.from(viewGroup.getContext());
        View listItem = layoutInflater.inflate(R.layout.layout_adminorders, viewGroup, false);
        AdminOrderRecyclerView.ViewHolder viewHolder =  new AdminOrderRecyclerView.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ProductModel productModel = productModelArrayList.get(position);
        OrderModel orderModel = productModel.getOrderModel();
        UserModel userModel = productModel.getUserModel();

        viewHolder.txtID.setText(orderModel.getOrderID());
        viewHolder.txtOrderNo.setText(orderModel.getOrderNumber());
        viewHolder.txtName.setText(userModel.getName());
        viewHolder.txtContact.setText(userModel.getContact());
        viewHolder.txtAddress.setText(userModel.getAddress());
        viewHolder.txtSubtotal.setText(orderModel.getSubtotal());
        viewHolder.txtShippingfee.setText(orderModel.getShippingFee());
        viewHolder.txtTotal.setText(orderModel.getGrandTotal());

       ArrayList<ProductModel> productModels  =  new ArrayList<>();
        productModels.add(new ProductModel("1",R.drawable.product,"Honey Tea","54.2",new String[]{"Hot","Cold"},54));
        productModels.add(new ProductModel("1",R.drawable.product,"Honey Tea","54.2",new String[]{"Hot","Cold"},54));
        productModels.add(new ProductModel("1",R.drawable.product,"Honey Tea","54.2",new String[]{"Hot","Cold"},54));
        productModels.add(new ProductModel("1",R.drawable.product,"Honey Tea","54.2",new String[]{"Hot","Cold"},54));
        productModels.add(new ProductModel("1",R.drawable.product,"Honey Tea","54.2",new String[]{"Hot","Cold"},54));
        productModels.add(new ProductModel("1",R.drawable.product,"Honey Tea","54.2",new String[]{"Hot","Cold"},54));
        productModels.add(new ProductModel("1",R.drawable.product,"Honey Tea","54.2",new String[]{"Hot","Cold"},54));
        productModels.add(new ProductModel("1",R.drawable.product,"Honey Tea","54.2",new String[]{"Hot","Cold"},54));
        productModels.add(new ProductModel("1",R.drawable.product,"Honey Tea","54.2",new String[]{"Hot","Cold"},54));
        productModels.add(new ProductModel("1",R.drawable.product,"Honey Tea","54.2",new String[]{"Hot","Cold"},54));
        productModels.add(new ProductModel("1",R.drawable.product,"Honey Tea","54.2",new String[]{"Hot","Cold"},54));
        productModels.add(new ProductModel("1",R.drawable.product,"Honey Tea","54.2",new String[]{"Hot","Cold"},54));

        RecyclerView productRecyclerView = viewHolder.adminOrderRecyclerView;


        CartRecyclerViewAdapter adapter = new CartRecyclerViewAdapter(CartRecyclerViewAdapter.CartVariation.Checkout,productModels,context);
        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        productRecyclerView.setAdapter(adapter);

        viewHolder.btnInProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.topLevel.setBackgroundColor(R.id.btn_inprogress);
                viewHolder.bottomLevel.setBackgroundColor(R.id.btn_inprogress);
            }
        });

        viewHolder.btnOrderReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.topLevel.setBackgroundColor(R.id.btn_orderready);
                viewHolder.bottomLevel.setBackgroundColor(R.id.btn_orderready);
            }
        });

        viewHolder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.topLevel.setBackgroundColor(R.id.btn_cancel);
                viewHolder.bottomLevel.setBackgroundColor(R.id.btn_cancel);
            }
        });


    }

    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtID,txtOrderNo,txtName,txtContact,txtAddress,txtSubtotal,txtShippingfee,txtTotal;
        RecyclerView adminOrderRecyclerView;
        Button btnInProgress,btnOrderReady,btnCancel;
        ConstraintLayout topLevel,bottomLevel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.txtID = (TextView)itemView.findViewById(R.id.adminOrdersID);
            this.txtOrderNo = (TextView)itemView.findViewById(R.id.adminOrderNumber);
            this.txtName = (TextView)itemView.findViewById(R.id.adminOrderName);
            this.txtContact = (TextView)itemView.findViewById(R.id.adminOrderContact);
            this.txtAddress = (TextView)itemView.findViewById(R.id.adminOrderAddress);
            this.txtSubtotal = (TextView)itemView.findViewById(R.id.adminOrderSubtotal);
            this.txtShippingfee = (TextView)itemView.findViewById(R.id.adminOrderShippingFee);
            this.txtTotal = (TextView)itemView.findViewById(R.id.adminOrderTotal);

            this.adminOrderRecyclerView = (RecyclerView) itemView.findViewById(R.id.adminOrderRecyclerView);
            this.btnInProgress = (Button) itemView.findViewById(R.id.btn_inprogress);
            this.btnOrderReady = (Button) itemView.findViewById(R.id.btn_orderready);
            this.btnCancel = (Button) itemView.findViewById(R.id.btn_cancel);

            this.topLevel = (ConstraintLayout)  itemView.findViewById(R.id.topLevel);
            this.bottomLevel = (ConstraintLayout) itemView.findViewById(R.id.bottomLevel);
        }
    }
}
