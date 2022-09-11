package com.capstone.projecttea;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class AdminOrderRecyclerView extends RecyclerView.Adapter < AdminOrderRecyclerView.ViewHolder > {
    public enum Page {
        main,
        history
    }
    private ArrayList < ProductModel > productModelArrayList;
    private Context context;
    AdminOrderRecyclerView.Page page;

    public AdminOrderRecyclerView(Context context, ArrayList < ProductModel > productModels, AdminOrderRecyclerView.Page page) {
        this.page = page;
        productModelArrayList = productModels;
        this.context = context;
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

        viewHolder.txtID.setText(orderModel.getOrderID());
        viewHolder.txtOrderNo.setText(orderModel.getOrderNumber());
        viewHolder.txtName.setText(userModel.getName());
        viewHolder.txtContact.setText(userModel.getContact());
        viewHolder.txtAddress.setText(userModel.getAddress());
        viewHolder.txtSubtotal.setText(orderModel.getSubtotal());
        viewHolder.txtShippingfee.setText(orderModel.getShippingFee());
        viewHolder.txtTotal.setText(orderModel.getGrandTotal());

        ArrayList < ProductModel > productModels = new ArrayList < > ();
        productModels.add(new ProductModel("1", R.drawable.product, "Honey Tea", "54.2", new String[] {
                "Hot",
                "Cold"
        }, 54));
        productModels.add(new ProductModel("1", R.drawable.product, "Honey Tea", "54.2", new String[] {
                "Hot",
                "Cold"
        }, 54));
        productModels.add(new ProductModel("1", R.drawable.product, "Honey Tea", "54.2", new String[] {
                "Hot",
                "Cold"
        }, 54));
        productModels.add(new ProductModel("1", R.drawable.product, "Honey Tea", "54.2", new String[] {
                "Hot",
                "Cold"
        }, 54));
        productModels.add(new ProductModel("1", R.drawable.product, "Honey Tea", "54.2", new String[] {
                "Hot",
                "Cold"
        }, 54));
        productModels.add(new ProductModel("1", R.drawable.product, "Honey Tea", "54.2", new String[] {
                "Hot",
                "Cold"
        }, 54));
        productModels.add(new ProductModel("1", R.drawable.product, "Honey Tea", "54.2", new String[] {
                "Hot",
                "Cold"
        }, 54));
        productModels.add(new ProductModel("1", R.drawable.product, "Honey Tea", "54.2", new String[] {
                "Hot",
                "Cold"
        }, 54));
        productModels.add(new ProductModel("1", R.drawable.product, "Honey Tea", "54.2", new String[] {
                "Hot",
                "Cold"
        }, 54));
        productModels.add(new ProductModel("1", R.drawable.product, "Honey Tea", "54.2", new String[] {
                "Hot",
                "Cold"
        }, 54));
        productModels.add(new ProductModel("1", R.drawable.product, "Honey Tea", "54.2", new String[] {
                "Hot",
                "Cold"
        }, 54));
        productModels.add(new ProductModel("1", R.drawable.product, "Honey Tea", "54.2", new String[] {
                "Hot",
                "Cold"
        }, 54));

        RecyclerView productRecyclerView = viewHolder.adminOrderRecyclerView;
        CartRecyclerViewAdapter adapter = new CartRecyclerViewAdapter(CartRecyclerViewAdapter.CartVariation.Checkout, productModels, context);
        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        productRecyclerView.setAdapter(adapter);

        if (page == Page.main) {
            viewHolder.btnInProgress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.topLevel.setBackgroundColor(context.getResources().getColor(R.color.yellow_accent));
                    viewHolder.bottomLevel.setBackgroundColor(context.getResources().getColor(R.color.yellow_accent));
                }
            });

            viewHolder.btnOrderReady.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.topLevel.setBackgroundColor(context.getResources().getColor(R.color.blue_accent));
                    viewHolder.bottomLevel.setBackgroundColor(context.getResources().getColor(R.color.blue_accent));
                }
            });

            viewHolder.btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.topLevel.setBackgroundColor(context.getResources().getColor(R.color.red_accent));
                    viewHolder.bottomLevel.setBackgroundColor(context.getResources().getColor(R.color.red_accent));
                }
            });

        } else if (page == Page.history) {

            viewHolder.btnInProgress.setVisibility(View.GONE);
            viewHolder.btnOrderReady.setVisibility(View.GONE);
            viewHolder.btnCancel.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtID, txtOrderNo, txtName, txtContact, txtAddress, txtSubtotal, txtShippingfee, txtTotal;
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

            this.topLevel = (ConstraintLayout) itemView.findViewById(R.id.topLevel);
            this.bottomLevel = (ConstraintLayout) itemView.findViewById(R.id.bottomLevel);
            this.adminOrderRecyclerView = (RecyclerView) itemView.findViewById(R.id.adminOrderRecyclerView);

                this.btnInProgress = (Button) itemView.findViewById(R.id.btn_inprogress);
                this.btnOrderReady = (Button) itemView.findViewById(R.id.btn_orderready);
                this.btnCancel = (Button) itemView.findViewById(R.id.btn_cancel);


        }
    }
}