package com.sanatmondal.gasdistribution.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanatmondal.gasdistribution.R;
import com.sanatmondal.gasdistribution.adapter.ReportItemAdapter;
import com.sanatmondal.gasdistribution.adapter.SalesCollectionItemAdapter;
import com.sanatmondal.gasdistribution.model.LoginModel;
import com.sanatmondal.gasdistribution.model.ReportModel;
import com.sanatmondal.gasdistribution.model.ResponseReport;
import com.sanatmondal.gasdistribution.model.TempOrderSale;
import com.sanatmondal.gasdistribution.model.UserModel;
import com.sanatmondal.gasdistribution.network.ApiURL;
import com.sanatmondal.gasdistribution.pref.PrefServiceURL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    private static final String TAG = "ReportActivity";

    private ActionBar toolbar;
    String userID = "";
    UserModel userModel = new UserModel();
    ApiURL apiURL = new ApiURL();
    ResponseReport responseReport;
    private TextView txtOrderNo, txtDPName, txtDate, txtTotalBillAmt, txtTotalCollectionAmt, txtTotalExpenseAmt, txtTotAssQty, txtTotCollAmt, txtTotReFil, txtTotPkg, txtRemainingAmt;
    private RecyclerView rvItems;
    Double totlSalesPrice = 0.0;
    private RecyclerView.LayoutManager mLayoutManager;
    private ReportItemAdapter reportItemAdapter;
    List<ReportModel> reportModelList = new ArrayList<>();

    PrefServiceURL prefServiceURL;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setupToolbar();
        getparams();
        getURL();
        initWidget();
        buildItemRecycleView();
        getOrderWiseReportList(userModel.getOrderNo());
    }

    private void getURL() {
        Log.i(TAG, "getURL: ");
        prefServiceURL = new PrefServiceURL(getApplicationContext());
        url = prefServiceURL.getURL();
    }

    private void buildItemRecycleView() {
        Log.i(TAG, "buildItemRecycleView: ");
        rvItems = findViewById(R.id.rvItems);
        rvItems.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        reportItemAdapter = new ReportItemAdapter(getApplicationContext(), reportModelList);
        rvItems.setLayoutManager(mLayoutManager);
        rvItems.setAdapter(reportItemAdapter);
        reportItemAdapter.setOnItemClickListener(new ReportItemAdapter.OnItemClickListener() {
            @Override
            public void onRemoveItemClick(int position, List<ReportModel> mData) {
                Log.i(TAG, "onRemoveItemClick: " + mData.get(position).getItemId());
            }

        });
    }

    private void initWidget() {
        Log.i(TAG, "initWidget: ");

        txtOrderNo = findViewById(R.id.txtOrderNo);
        txtDPName = findViewById(R.id.txtDPName);
        txtDate = findViewById(R.id.txtDate);
        txtTotalBillAmt = findViewById(R.id.txtTotalBillAmt);
        txtTotalCollectionAmt = findViewById(R.id.txtTotalCollectionAmt);
        txtTotalExpenseAmt = findViewById(R.id.txtTotalExpenseAmt);
        txtTotAssQty = findViewById(R.id.txtTotAssQty);
        txtTotCollAmt = findViewById(R.id.txtTotCollAmt);
        txtTotReFil = findViewById(R.id.txtTotReFil);
        txtTotPkg = findViewById(R.id.txtTotPkg);
        txtRemainingAmt = findViewById(R.id.txtRemainingAmt);
    }

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar: ");
        toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Report");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getparams() {
        Log.i(TAG, "getparams: ");
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        userModel = (UserModel) bundle.getSerializable("userModel");
        userID = bundle.getString("userID", "");
        //   orderNo = bundle.getString("orderNo", "");
        Log.i(TAG, "getparams: " + userModel.getOrderNo());
    }
    
    private void getOrderWiseReportList(String OrderNo){
        String URL = apiURL.getOrderWiseReportList(OrderNo);
        Log.i(TAG, "getOrderWiseReportList: " + URL);
        final ProgressDialog dialog = ProgressDialog.show(this, "", "Please wait...", false, false);
        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: ");
                Log.i(TAG, "onResponse: " + response);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                responseReport = gson.fromJson(response, ResponseReport.class);

                if(responseReport.getMsg().equalsIgnoreCase("Success")) {
                    if (responseReport.getData().getCollectionHistory().size() > 0){
                        reportModelList.addAll(responseReport.getData().getCollectionHistory());
                        for (ReportModel model: responseReport.getData().getCollectionHistory()) {
                            totlSalesPrice = totlSalesPrice + model.getTotalSPrice();
                            tolAssQty = tolAssQty + model.getAssignQty();
                            totColQty = totColQty + model.getCylinder();
                            totReFil = totReFil + model.getGasOnly();
                            totPkg = totPkg + model.getGasCylinderBoth();
                        }
                        setModelValue();
                        if (responseReport.getData().getCollectionExpense() != null) {
                            setCollectionExpenseAmt();
                        }
                    } else if (responseReport.getData().getCollectionExpense() != null) {
                        setCollectionExpenseAmt();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "No Data Found.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                reportItemAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.i(TAG, "onErrorResponse: " + error.getMessage());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
    }

    Double tolAssQty = 0.0, totColQty = 0.0, totReFil = 0.0, totPkg = 0.0;
    private void setModelValue() {
        Log.i(TAG, "setModelValue: ");
        txtOrderNo.setText(responseReport.getData().getCollectionHistory().get(0).getOrderNo());
        txtDPName.setText(responseReport.getData().getCollectionHistory().get(0).getDeliveryPersonName());
        txtTotalBillAmt.setText(String.valueOf(totlSalesPrice));

        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        Date date = new Date();
        txtDate.setText(String.valueOf(dateFormat.format(date)));

        /*
        int a =  Integer.parseInt((tolAssQty.toString()));
        int b =  Integer.parseInt((totColQty.toString()));
        int c =  Integer.parseInt((totReFil.toString()));
        int d =  Integer.parseInt((totPkg.toString()));
        * */

        txtTotAssQty.setText(String.valueOf(tolAssQty.intValue()));
        txtTotCollAmt.setText(String.valueOf(totColQty.intValue()));
        txtTotReFil.setText(String.valueOf(totReFil.intValue()));
        txtTotPkg.setText(String.valueOf(totPkg.intValue()));
    }

    private void setCollectionExpenseAmt(){
        txtTotalCollectionAmt.setText(String.valueOf(responseReport.getData().getCollectionExpense().getCollectAmount()));
        txtTotalExpenseAmt.setText(String.valueOf(responseReport.getData().getCollectionExpense().getExpenceAmt()));
        txtRemainingAmt.setText(String.valueOf(responseReport.getData().getCollectionExpense().getCollectAmount() - responseReport.getData().getCollectionExpense().getExpenceAmt()));
    }

}