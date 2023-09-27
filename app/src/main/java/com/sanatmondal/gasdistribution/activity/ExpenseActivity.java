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
import com.sanatmondal.gasdistribution.adapter.ExpenseModelAdapter;
import com.sanatmondal.gasdistribution.model.CustomerModel;
import com.sanatmondal.gasdistribution.model.ExpenceModel;
import com.sanatmondal.gasdistribution.model.LoginModel;
import com.sanatmondal.gasdistribution.model.ResponseDefault;
import com.sanatmondal.gasdistribution.model.ResponseExpense;
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

public class ExpenseActivity extends AppCompatActivity {
    private static final String TAG = "ExpenseActivity";

    private ActionBar toolbar;
    UserModel userModel = new UserModel();
    String userID = "";
    private TextView txtExpenseName;
    private EditText etExpenseAmt, etComments;
    private Button btnExpenseSave, btnShowExpenseHead;
    ApiURL apiURL = new ApiURL();
    private LinearLayout container;
    HideKeyboard hideKeyboard;
    public static final String KEY_ORDER_ID = "ORDER_ID";
    PrefServiceURL prefServiceURL;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        setupToolbar();
        getURL();
        getparams();
        initWidget();
        expenseBottomSheet();

    }

    private void getURL() {
        Log.i(TAG, "getURL: ");
        prefServiceURL = new PrefServiceURL(getApplicationContext());
        url = prefServiceURL.getURL();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getAllExpenseHeadList() {
        Log.i(TAG, "getAllExpenseHeadList: ");

        String URL = apiURL.getALLExpenseHEADList();
        Log.i(TAG, "login: " + URL);
        final ProgressDialog dialog = ProgressDialog.show(this, "", "Please wait...", false, false);
        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: " + response);

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                ResponseExpense res = gson.fromJson(response, ResponseExpense.class);

                mExpenceModel.clear();
                mExpenceModel.addAll(res.getData());
                adapter.notifyDataSetChanged();
                bottomSheetDialogExpenseHead.show();
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

    private void initWidget() {
        Log.i(TAG, "initWidget: ");
        container = findViewById(R.id.container);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: ");
                hideKeyboard.hideSoftKeyboard(ExpenseActivity.this);
            }
        });
        etExpenseAmt = findViewById(R.id.etExpenseAmt);
        etComments = findViewById(R.id.etComments);
        txtExpenseName = findViewById(R.id.txtExpenseName);
        btnShowExpenseHead = findViewById(R.id.btnShowExpenseHead);
        btnShowExpenseHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllExpenseHeadList();
            }
        });
        btnExpenseSave = findViewById(R.id.btnExpenseSave);
        btnExpenseSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String expenseAmt = etExpenseAmt.getText().toString();
                String comments = etComments.getText().toString();

                if(expenseAmt.equalsIgnoreCase("")){
                    Toast.makeText(ExpenseActivity.this, "Enter Expense Amt.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(comments.equalsIgnoreCase("")){
                    Toast.makeText(ExpenseActivity.this, "Enter Comments.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(txtExpenseName.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(ExpenseActivity.this, "Select Expense Head.", Toast.LENGTH_SHORT).show();
                    return;
                }

                showSaveExpenseAlert(expenseAmt, userModel.getOrderNo(), txtExpenseName.getText().toString(), selectedDailyExpenceTypeID, etComments.getText().toString());

            }
        });
    }

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar: ");
        toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Expense");
    }

    private void getparams() {
        Log.i(TAG, "getparams: ");
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        userModel = (UserModel) bundle.getSerializable("userModel");
        userID = bundle.getString("userID", "");
        Log.i(TAG, "getparams: " + userModel.getOrderNo());
    }

    BottomSheetDialog bottomSheetDialogExpenseHead;
    ExpenseModelAdapter adapter;
    List<ExpenceModel> mExpenceModel = new ArrayList<>();
    String selectedDailyExpenceTypeID = "0";
    private void expenseBottomSheet() {
        Log.i(TAG, "expenseBottomSheet: ");
        bottomSheetDialogExpenseHead = new BottomSheetDialog(ExpenseActivity.this, R.style.Theme_Design_BottomSheetDialog);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottom_sheet_expense, (LinearLayout)findViewById(R.id.bottom_sheet_item));
        RecyclerView recycleview_customer = bottomSheetView.findViewById(R.id.recycleview_customer);
        recycleview_customer.setLayoutManager(new LinearLayoutManager(this));

        ImageView btn_expense_sheet_close = bottomSheetView.findViewById(R.id.btn_expense_sheet_close);
        btn_expense_sheet_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogExpenseHead.dismiss();
            }
        });

        adapter = new ExpenseModelAdapter(this, mExpenceModel);
        recycleview_customer.setAdapter(adapter);
        bottomSheetDialogExpenseHead.setContentView(bottomSheetView);

        adapter.setOnItemClickListener(new ExpenseModelAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(int position, List<ExpenceModel> mData) {
                Log.i(TAG, "DailyExpenceTypeName: " + mData.get(position).getDailyExpenceTypeName());

                ExpenceModel model = mData.get(position);
                selectedDailyExpenceTypeID = String.valueOf(model.getId());
                txtExpenseName.setText(model.getDailyExpenceTypeName());
                bottomSheetDialogExpenseHead.dismiss();
            }
        });

        setupFullHeight(bottomSheetDialogExpenseHead);

        EditText et_search_customer = bottomSheetView.findViewById(R.id.et_search_customer);
        et_search_customer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

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

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private void filterListCustomer(String text) {
        ArrayList<ExpenceModel> filteredList = new ArrayList<>();

        for (ExpenceModel item : mExpenceModel) {
            if (item.getDailyExpenceTypeName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterListCustomer(filteredList);
    }

    private void dailyExpenseInsert(String ExpenceAmt, String OrderNo, String ExpenceTypeName, String DailyExpenceTypeID, String Comment){

        final ProgressDialog dialog = ProgressDialog.show(this, "", "Please wait...", false, false);
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = apiURL.dailyExpenseInsert();
            Log.i(TAG, "dailyExpenseInsert: " + URL);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("ExpenceAmt", ExpenceAmt);
            jsonBody.put("OrderNo", OrderNo);
            jsonBody.put("ExpenceTypeName", ExpenceTypeName);
            jsonBody.put("DailyExpenceTypeID", DailyExpenceTypeID);
            jsonBody.put("Comment", Comment);
            jsonBody.put("CreateBy", userID);
            final String requestBody = jsonBody.toString();
            Log.i(TAG, "dailyExpenseInsert: " + requestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResponse(String response) {
                    try {
                        Log.i(TAG, "onResponse: " + response);
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        ResponseFinallySave res = gson.fromJson(response, ResponseFinallySave.class);
                        if (res.getSuccess()) {
                            Toast.makeText(ExpenseActivity.this, "Expense Save successfull.", Toast.LENGTH_SHORT).show();
                            resetAllValue();
                            Intent intent = new Intent();
                            intent.putExtra(KEY_ORDER_ID, res.getData());
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            Toast.makeText(ExpenseActivity.this, "Expense Save failed.", Toast.LENGTH_SHORT).show();
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

    private void resetAllValue() {
        Log.i(TAG, "resetAllValue: ");
        txtExpenseName.setText("");
        etExpenseAmt.setText("");
        etComments.setText("");
        selectedDailyExpenceTypeID ="0";
    }


    private void showSaveExpenseAlert(String ExpenceAmt, String OrderNo, String ExpenceTypeName, String DailyExpenceTypeID, String Comment){
        String msgDefault = "Expense Save";
        String message = "Are you sure you want to save?";

        new AlertDialog.Builder(ExpenseActivity.this)
                .setTitle(msgDefault)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dailyExpenseInsert(ExpenceAmt, OrderNo, ExpenceTypeName, DailyExpenceTypeID, Comment);
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