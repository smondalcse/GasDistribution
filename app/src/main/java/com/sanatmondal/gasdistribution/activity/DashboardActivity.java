package com.sanatmondal.gasdistribution.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sanatmondal.gasdistribution.R;
import com.sanatmondal.gasdistribution.model.UserModel;

public class DashboardActivity extends AppCompatActivity {
    private static final String TAG = "DashboardActivity";

    CardView btnCustomer, btnSales, btnExchange, btnPayment, btnExpense, btnReport;
    UserModel userModel = new UserModel();
    String userID = "";
    private TextView txtUserName, txtOrderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getparams();
        initWidget();
        setModelValue();
    }

    private void setModelValue() {
        Log.i(TAG, "setModelValue: ");
        txtUserName.setText("UserID: " + userModel.getEmployeeID());
        txtOrderID.setText("Order ID: " + userModel.getOrderNo());
    }

    private void getparams() {
        Log.i(TAG, "getparams: ");
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        userModel = (UserModel) bundle.getSerializable("userModel");
        userID = bundle.getString("userID", "");
        Log.i(TAG, "getparams: " + userModel.getOrderNo());
        Log.i(TAG, "userID: " + userID);
    }

    private void initWidget() {
        Log.i(TAG, "initWidget: ");

        getSupportActionBar().hide();

        txtUserName = findViewById(R.id.txtUserName);
        txtOrderID = findViewById(R.id.txtOrderID);

        btnCustomer = findViewById(R.id.btnCustomer);
        btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, CustomerActivity.class);
                intent.putExtra("userModel", userModel);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        btnSales = findViewById(R.id.btnSales);
        btnSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, SalesNewActivity.class);
                intent.putExtra("userModel", userModel);
                intent.putExtra("userID", userID);
                startForResult.launch(intent);
            }
        });

        btnExchange = findViewById(R.id.btnExchange);
        btnExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, ExchangeActivity.class);
                intent.putExtra("userModel", userModel);
                intent.putExtra("userID", userID);
              //  startForResultExchange.launch(intent);
                startActivity(intent);
            }
        });

        btnPayment  = findViewById(R.id.btnPayment);
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, CollectionActivity.class);
                intent.putExtra("userModel", userModel);
                intent.putExtra("userID", userID);
                //startActivity(intent);
                startForResultPaymentCollection.launch(intent);
            }
        });

        btnExpense  = findViewById(R.id.btnExpense);
        btnExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, ExpenseActivity.class);
                intent.putExtra("userModel", userModel);
                intent.putExtra("userID", userID);
                startForResultExpense.launch(intent);
            }
        });

        btnReport  = findViewById(R.id.btnReport);
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, ReportActivity.class);
                intent.putExtra("userModel", userModel);
                intent.putExtra("userID", userID);
                //    intent.putExtra("orderNo", txtOrderID.getText().toString());
                startActivity(intent);
            }
        });
    }

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if(result.getData() != null && result.getData().getStringExtra(SalesNewActivity.KEY_ORDER_ID) != null){
                            txtOrderID.setText("Order ID: " + result.getData().getStringExtra(SalesNewActivity.KEY_ORDER_ID));
                            userModel.setOrderNo(result.getData().getStringExtra(SalesNewActivity.KEY_ORDER_ID));
                            Log.i(TAG, "onActivityResult: " + userModel.getOrderNo());
                        } else {
                            txtOrderID.setText("");
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> startForResultExchange = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if(result.getData() != null && result.getData().getStringExtra(SalesNewActivity.KEY_ORDER_ID) != null){
                            txtOrderID.setText("Order ID: " + result.getData().getStringExtra(SalesNewActivity.KEY_ORDER_ID));
                            userModel.setOrderNo(result.getData().getStringExtra(SalesNewActivity.KEY_ORDER_ID));
                            Log.i(TAG, "onActivityResult: " + userModel.getOrderNo());
                        } else {
                            txtOrderID.setText("");
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> startForResultPaymentCollection = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if(result.getData() != null && result.getData().getStringExtra(CollectionActivity.KEY_ORDER_ID) != null){
                            txtOrderID.setText("Order ID: " + result.getData().getStringExtra(CollectionActivity.KEY_ORDER_ID));
                            userModel.setOrderNo(result.getData().getStringExtra(CollectionActivity.KEY_ORDER_ID));
                            Log.i(TAG, "onActivityResult: " + userModel.getOrderNo());
                        } else {
                            txtOrderID.setText("");
                        }
                    }
                }
            });


    ActivityResultLauncher<Intent> startForResultExpense = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if(result.getData() != null && result.getData().getStringExtra(ExpenseActivity.KEY_ORDER_ID) != null){
                            txtOrderID.setText("Order ID: " + result.getData().getStringExtra(ExpenseActivity.KEY_ORDER_ID));
                            userModel.setOrderNo(result.getData().getStringExtra(CollectionActivity.KEY_ORDER_ID));
                            Log.i(TAG, "onActivityResult: " + userModel.getOrderNo());
                        } else {
                            txtOrderID.setText("");
                        }
                    }
                }
            });

}