package com.sanatmondal.gasdistribution.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanatmondal.gasdistribution.R;
import com.sanatmondal.gasdistribution.adapter.CustomerModelAdapter;
import com.sanatmondal.gasdistribution.model.CustomerModel;
import com.sanatmondal.gasdistribution.model.CustomerResponse;
import com.sanatmondal.gasdistribution.model.ResponseDefault;
import com.sanatmondal.gasdistribution.model.ResponseFinallySave;
import com.sanatmondal.gasdistribution.model.UserModel;
import com.sanatmondal.gasdistribution.network.ApiURL;
import com.sanatmondal.gasdistribution.others.HideKeyboard;
import com.sanatmondal.gasdistribution.pref.PrefServiceURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends AppCompatActivity {
    private static final String TAG = "CollectionActivity";

    private ActionBar toolbar;
    private Button btnCustomer, btnCreateCustomer;
    private TextView txtCustID, txtCustName, txtCustMobile, txtDueAmount, txtCurrentDue;
    private EditText etPayAmt;
    private CustomerModel selectedCustomerModel;
    BottomSheetDialog bottomSheetDialogCustomer;
    CustomerModelAdapter adapterCustomer;
    List<CustomerModel> mCustomerModels = new ArrayList<>();
    ApiURL apiURL = new ApiURL();
    String userID = "";
    UserModel userModel = new UserModel();
    private LinearLayout container;
    HideKeyboard hideKeyboard;
    public static final String KEY_ORDER_ID = "ORDER_ID";
    PrefServiceURL prefServiceURL;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        setupToolbar();
        getURL();
        getparams();
        initWidget();
        cutstomerBottomSheet();
    }

    private void getURL() {
        Log.i(TAG, "getURL: ");
        prefServiceURL = new PrefServiceURL(getApplicationContext());
        url = prefServiceURL.getURL();
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

    private void initWidget() {
        Log.i(TAG, "initWidget: ");

        container = findViewById(R.id.container);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: ");
                hideKeyboard.hideSoftKeyboard(CollectionActivity.this);
            }
        });

        btnCustomer = findViewById(R.id.btnCustomer);
        etPayAmt = findViewById(R.id.etPayAmt);
        etPayAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                calculateCurrentDue(txtDueAmount.getText().toString(), etPayAmt.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllCustomerList();
            }
        });

        txtCustID = findViewById(R.id.txtCustID);
        txtCustName = findViewById(R.id.txtCustName);
        txtCustMobile = findViewById(R.id.txtCustMobile);
        txtDueAmount = findViewById(R.id.txtDueAmount);
        txtCurrentDue = findViewById(R.id.txtCurrentDue);

        btnCreateCustomer = findViewById(R.id.btnCreateCustomer);
        btnCreateCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String custID = txtCustID.getText().toString();
                String payAmt = etPayAmt.getText().toString().trim();

                if (custID.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), "Select Customer.", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                if (payAmt.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), "Enter Pay Amt.", Toast.LENGTH_SHORT).show();
                    return;
                }

                showSaveCollectionAlert(custID, txtDueAmount.getText().toString(), etPayAmt.getText().toString());
            }
        });

    }

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar: ");
        toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Collection");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void cutstomerBottomSheet() {
        Log.i(TAG, "cutstomerBottomSheet: ");
        bottomSheetDialogCustomer = new BottomSheetDialog(CollectionActivity.this, R.style.Theme_Design_BottomSheetDialog);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottom_sheet_customer, (LinearLayout)findViewById(R.id.bottom_sheet_item));
        RecyclerView recycleview_customer = bottomSheetView.findViewById(R.id.recycleview_customer);
        recycleview_customer.setLayoutManager(new LinearLayoutManager(this));

        ImageView btn_customer_sheet_close = bottomSheetView.findViewById(R.id.btn_customer_sheet_close);
        btn_customer_sheet_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogCustomer.dismiss();
            }
        });

        adapterCustomer = new CustomerModelAdapter(this, mCustomerModels);
        recycleview_customer.setAdapter(adapterCustomer);
        bottomSheetDialogCustomer.setContentView(bottomSheetView);

        adapterCustomer.setOnItemClickListener(new CustomerModelAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(int position, List<CustomerModel> mData) {
                Log.i(TAG, "Customer: " + mData.get(position).getCustomerName());
                selectedCustomerModel = mData.get(position);
                CustomerModel custModel = mData.get(position);
                txtCustID.setText(custModel.getCustomerID());
                txtCustName.setText(custModel.getCustomerName());
                txtCustMobile.setText(custModel.getMobile1());
                txtDueAmount.setText(String.valueOf(custModel.getDueAmount()));
                bottomSheetDialogCustomer.dismiss();
            }
        });

        setupFullHeight(bottomSheetDialogCustomer);

        EditText et_search_customer = bottomSheetView.findViewById(R.id.et_search_customer);
        et_search_customer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateCurrentDue(String.valueOf(txtDueAmount.getText()), String.valueOf(etPayAmt.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterListCustomer(s.toString());
            }
        });

        et_search_customer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (et_search_customer.getRight() - et_search_customer.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        et_search_customer.setText("");
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void filterListCustomer(String text) {
        ArrayList<CustomerModel> filteredList = new ArrayList<>();

        for (CustomerModel item : mCustomerModels) {
            if (item.getCustomerName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapterCustomer.filterListCustomer(filteredList);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }


    private void getAllCustomerList(){


        final ProgressDialog dialog = ProgressDialog.show(this, "", "Please wait...", false, false);
        String URL = apiURL.getAllCustomerList();
        Log.i(TAG, "getAllCustomerList: " + URL);
        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: ");
                Log.i(TAG, "onResponse: " + response);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();

                CustomerResponse customerResponse = gson.fromJson(response, CustomerResponse.class);
                Log.i(TAG, "onResponse: " +customerResponse.getMsg());

                mCustomerModels.clear();
                mCustomerModels.addAll(customerResponse.getData());
                adapterCustomer.notifyDataSetChanged();
                bottomSheetDialogCustomer.show();
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

    private void savePayment(String custID, String payAmt, String userID){

        final ProgressDialog dialog = ProgressDialog.show(this, "", "Please wait...", false, false);
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = apiURL.customerPaymentAdd();
            Log.i(TAG, "savePayment: " + URL);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("CustomerId", custID);
            jsonBody.put("PayAmt", payAmt);
            jsonBody.put("CreateBy", userID);
            jsonBody.put("OrderNo", userModel.getOrderNo());
            final String requestBody = jsonBody.toString();
            Log.i(TAG, "savePayment: " + requestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResponse(String response) {
                    try {
                        Log.i(TAG, "onResponse: " + response);
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        ResponseFinallySave res = gson.fromJson(response, ResponseFinallySave.class);
                        if(res.getSuccess()) {
                            Toast.makeText(CollectionActivity.this, "Collection Save successfull.", Toast.LENGTH_SHORT).show();
                            resetAllField();
                            Intent intent = new Intent();
                            intent.putExtra(KEY_ORDER_ID, res.getData());
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            Toast.makeText(CollectionActivity.this, "Collection Save failed.", Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    } catch (Exception e) {
                        dialog.dismiss();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return super.parseNetworkResponse(response);
                }
            };

            requestQueue.add(stringRequest);
            stringRequest.setRetryPolicy(new RetryPolicy() {
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
            dialog.dismiss();
        } catch (JSONException e) {
            dialog.dismiss();
            e.printStackTrace();
        }
    }

    private void resetAllField() {
        Log.i(TAG, "resetAllField: ");
        txtCustID.setText("");
        txtCustName.setText("");
        txtCustMobile.setText("");
        txtDueAmount.setText("");
        etPayAmt.setText("");
    }

    private String calculateCurrentDue(String preDueAmt, String payAmt){
        try {
            if (preDueAmt.equalsIgnoreCase(""))
                preDueAmt = "0";
            if (payAmt.equalsIgnoreCase(""))
                payAmt = "0";

            Double dPreDueAmt = Double.valueOf(preDueAmt);
            Double dPayAmt = Double.valueOf(payAmt);

            Double dCurrentDue = dPreDueAmt - dPayAmt;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtCurrentDue.setText(String.valueOf(dCurrentDue));
                }
            });

            return "0.0";
        } catch (Exception ex){
            return String.valueOf("0.0");
        }

    }

    private void showSaveCollectionAlert(String CustID, String PreDue, String payAmt){
        String msgDefault = "Are you sure you want to save?";
        String message = "PreDue: " + PreDue + ", \n" + "PayAmt: " + payAmt;

        new AlertDialog.Builder(CollectionActivity.this)
                .setTitle(msgDefault)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        savePayment(CustID, payAmt, userID);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setIcon(R.drawable.ic_save)
                .show();
    }


}