package com.sanatmondal.gasdistribution.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanatmondal.gasdistribution.R;
import com.sanatmondal.gasdistribution.adapter.AutoCompleteAllItemModelAdapter;
import com.sanatmondal.gasdistribution.adapter.AutoCompleteCustomerAdapter;
import com.sanatmondal.gasdistribution.adapter.AutoCompleteItemModelAdapter;
import com.sanatmondal.gasdistribution.adapter.SalesCollectionItemAdapter;
import com.sanatmondal.gasdistribution.model.CustomerModel;
import com.sanatmondal.gasdistribution.model.CustomerResponse;
import com.sanatmondal.gasdistribution.model.ItemModel;
import com.sanatmondal.gasdistribution.model.ItemResponse;
import com.sanatmondal.gasdistribution.model.SalesCollectionItemModel;
import com.sanatmondal.gasdistribution.model.UserModel;
import com.sanatmondal.gasdistribution.pref.PrefServiceURL;

import java.util.ArrayList;
import java.util.List;

public class SalesActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "OrderActivity-29032022";

    private ActionBar toolbar;
    UserModel userModel = new UserModel();
    private Button btnSales, btnCollection, btnAddItem, btnCancel, btnSave;
    private ArrayList<Button> buttons = new ArrayList<Button>();

    AppCompatAutoCompleteTextView autoItemModel, autoAllItemModel, autoCustomer;
    List<ItemModel> itemList;
    List<ItemModel> allItemList;
    List<CustomerModel> customerModels;
    AutoCompleteItemModelAdapter adapter;
    AutoCompleteAllItemModelAdapter adapterAllItem;
    AutoCompleteCustomerAdapter adapterCustomer;
    ItemModel selectedItemModel, collectionItemModel;
    CustomerModel selectedCustomerModel;
    private ProgressBar progressBar;
    String orderType = "0";

    private RecyclerView rvItems;
    private RecyclerView.LayoutManager mLayoutManager;

    RadioGroup rgItemtype;
    RadioButton rbSales, rbCollection;
    EditText etQty;

    PrefServiceURL prefServiceURL;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        setupToolbar();
        getparams();
        getURL();
        initWidget();
        setParamsValue();
        buildRecycleView();
        progressBar.setVisibility(View.VISIBLE);
        getAllCustomerList(userModel.getOrderNo());
        getOrderItemList(userModel.getOrderNo());
        getAllItemList(userModel.getOrderNo());
    }

    private void getURL() {
        Log.i(TAG, "getURL: ");
        prefServiceURL = new PrefServiceURL(getApplicationContext());
        url = prefServiceURL.getURL();
    }

    private void setParamsValue() {
        Log.i(TAG, "setParamsValue: ");
    }

    private void initWidget() {
        Log.i(TAG, "initWidget: ");

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SalesActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        });
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SalesActivity.this, "btnSave", Toast.LENGTH_SHORT).show();
            }
        });

        autoItemModel = findViewById(R.id.autoItemModel);
        itemList = new ArrayList<>();
        autoItemModel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedItemModel = itemList.get(position);
            }
        });
        autoItemModel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    autoItemModel.showDropDown();
                }
            }
        });

        autoAllItemModel = findViewById(R.id.autoAllItemModel);
        allItemList = new ArrayList<>();
        autoAllItemModel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                collectionItemModel = allItemList.get(position);
            }
        });

        autoAllItemModel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    autoAllItemModel.showDropDown();
                }
            }
        });

        customerModels = new ArrayList<>();
        autoCustomer = findViewById(R.id.autoCustomer);
        autoCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedCustomerModel = customerModels.get(position);
            }
        });
        autoCustomer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    autoCustomer.showDropDown();
                }
            }
        });

        btnSales = findViewById(R.id.btnSales);
        btnCollection = findViewById(R.id.btnCollection);
        btnSales.setOnClickListener(this);
        btnCollection.setOnClickListener(this);

        buttons.add(btnSales);
        buttons.add(btnCollection);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            enableDisableButtonLikeTab(0);
        }

        rgItemtype = findViewById(R.id.rgItemtype);
        rbSales = (RadioButton) findViewById(R.id.rbSales);
        rbCollection = (RadioButton) findViewById(R.id.rbCollection);

        etQty = findViewById(R.id.etQty);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedID = rgItemtype.getCheckedRadioButtonId();
                RadioButton selectedGender = (RadioButton) findViewById(selectedID);

                SalesCollectionItemModel model = new SalesCollectionItemModel();
                model.setQty(etQty.getText().toString());
                if(orderType.equalsIgnoreCase("1")){
                    model.setItemModel(selectedItemModel);
                } else if(orderType.equalsIgnoreCase("2")){
                    model.setItemModel(collectionItemModel);
                } else {
                    Toast.makeText(SalesActivity.this, "Please select Type", Toast.LENGTH_SHORT).show();
                }

                model.setItemType(selectedGender.getText().toString());

                mSalesCollectionItemModels.add(model);
                salesCollectionItemAdapter.notifyDataSetChanged();
            }
        });
        orderType = "1";
    }

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar: ");
        toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Sales");
    }

    private void getparams() {
        Log.i(TAG, "getparams: ");
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        userModel = (UserModel) bundle.getSerializable("userModel");
        Log.i(TAG, "getparams: " + userModel.getOrderNo());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void enableDisableButtonLikeTab(int btn_position){
        if(buttons.size() > 1) {
            for (int i = 0; i < buttons.size(); i++){
                Button btn = buttons.get(i);
                if(i == btn_position){
                    btn.setTextColor(getResources().getColor(R.color.white));
                    btn.setTypeface(null, Typeface.BOLD);
                    btn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_500)));

                    autoItemModel.setVisibility(View.GONE);
                    autoAllItemModel.setVisibility(View.VISIBLE);
                } else {
                    btn.setTextColor(getResources().getColor(R.color.black));
                    btn.setTypeface(null, Typeface.NORMAL);
                    btn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));

                    autoItemModel.setVisibility(View.VISIBLE);
                    autoAllItemModel.setVisibility(View.GONE);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSales:
                enableDisableButtonLikeTab(0);
                orderType = "1";
                break;
            case R.id.btnCollection:
                enableDisableButtonLikeTab(1);
                orderType = "2";
                break;
        }
    }

    private void getOrderItemList(String Orderno){
        Log.i(TAG, "getOrderItemList: ");
        String URL_BASE = "http://delowarenterprise.rexsystemsbd.com/gasapi/version1/";
        String URL_METHOD = "orderitemlist?";
        String URL_PARAMS = "orderno=" + Orderno;
        String URL_LOGIN = URL_BASE + URL_METHOD + URL_PARAMS;
        Log.i(TAG, "login: " + URL_LOGIN);

        StringRequest request = new StringRequest(URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: ");
                Log.i(TAG, "onResponse: " + response);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();

                ItemResponse itemResponse = gson.fromJson(response, ItemResponse.class);


                itemList.addAll(itemResponse.getData());
                adapter = new AutoCompleteItemModelAdapter(getApplicationContext(), itemList);
                autoItemModel.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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

    private void getAllItemList(String Orderno){
        Log.i(TAG, "getAllItemList: ");
        String URL_BASE = "http://delowarenterprise.rexsystemsbd.com/gasapi/version1/";
        String URL_METHOD = "allitemlist?";
        String URL_LOGIN = URL_BASE + URL_METHOD;
        Log.i(TAG, "getAllItemList: " + URL_LOGIN);

        StringRequest request = new StringRequest(URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: ");
                Log.i(TAG, "onResponse: " + response);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();

                ItemResponse itemResponse = gson.fromJson(response, ItemResponse.class);

                allItemList.addAll(itemResponse.getData());
                adapterAllItem = new AutoCompleteAllItemModelAdapter(getApplicationContext(), allItemList);
                autoAllItemModel.setAdapter(adapterAllItem);
                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
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

    private void getAllCustomerList(String Orderno){
        Log.i(TAG, "getAllItemList: ");
        String URL_BASE = "http://delowarenterprise.rexsystemsbd.com/gasapi/version1/";
        String URL_METHOD = "allcustomerlist";
        String URL_CUSTOMER = URL_BASE + URL_METHOD;
        Log.i(TAG, "getAllCustomerList: " + URL_CUSTOMER);

        StringRequest request = new StringRequest(URL_CUSTOMER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: ");
                Log.i(TAG, "onResponse: " + response);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();

                CustomerResponse customerResponse = gson.fromJson(response, CustomerResponse.class);
                Log.i(TAG, "onResponse: " +customerResponse.getMsg());

                customerModels.addAll(customerResponse.getData());
                adapterCustomer = new AutoCompleteCustomerAdapter(getApplicationContext(), customerModels);
                autoCustomer.setAdapter(adapterCustomer);
                progressBar.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
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

    /*
    public void showQtyInputDialogBox(final int position, final List<ItemModel> mData){
        Log.i(TAG, "showQtyInputDialogBox: ");
        final AlertDialog.Builder alert = new AlertDialog.Builder(SalesActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog,null);
        final TextInputLayout txt_inputText = mView.findViewById(R.id.txt__qty_input);
        Button btn_cancel = mView.findViewById(R.id.btn_cancel);
        Button btn_okay = mView.findViewById(R.id.btn_okay);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                String qty = txt_inputText.getEditText().getText().toString();
                int intQty = Integer.parseInt(qty);
                if(intQty > 0 ) {
                    Log.i(TAG, "onClick: " + "saveItemTemp");
                    Log.i(TAG, "onClick: " + mData.get(position).getItemName());
                }
                else
                    Toast.makeText(getApplicationContext(), "Qty should be more than 0 always.", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
    }
    */

    List<SalesCollectionItemModel> mSalesCollectionItemModels = new ArrayList<>();
    private SalesCollectionItemAdapter salesCollectionItemAdapter;
    private void buildRecycleView() {
        Log.i(TAG, "buildRecycleView: ");
        rvItems = findViewById(R.id.rvItems);
        rvItems.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
    }
}