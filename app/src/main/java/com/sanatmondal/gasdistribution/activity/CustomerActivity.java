package com.sanatmondal.gasdistribution.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanatmondal.gasdistribution.R;
import com.sanatmondal.gasdistribution.adapter.AutoCompleteAllItemModelAdapter;
import com.sanatmondal.gasdistribution.adapter.ContactModelAdapter;
import com.sanatmondal.gasdistribution.adapter.CustomerModelAdapter;
import com.sanatmondal.gasdistribution.adapter.WarehouseModelAdapter;
import com.sanatmondal.gasdistribution.model.ContactsModal;
import com.sanatmondal.gasdistribution.model.CustomerModel;
import com.sanatmondal.gasdistribution.model.CustomerResponse;
import com.sanatmondal.gasdistribution.model.ItemResponse;
import com.sanatmondal.gasdistribution.model.ResponseDefault;
import com.sanatmondal.gasdistribution.model.ResponseTempOrderSale;
import com.sanatmondal.gasdistribution.model.ResponseWarehouse;
import com.sanatmondal.gasdistribution.model.TEMPSalesFromCustomerOrderViewModel;
import com.sanatmondal.gasdistribution.model.WarehouseModel;
import com.sanatmondal.gasdistribution.network.ApiURL;
import com.sanatmondal.gasdistribution.others.HideKeyboard;
import com.sanatmondal.gasdistribution.pref.PrefServiceURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends AppCompatActivity {
    private static final String TAG = "CustomerActivity";
    private ActionBar toolbar;
    private TextView txtSelectCustomer, txtWHNo, txtWHName;
    private EditText etCName,etCMobile,etCAddress;
    private Button btnCreateCustomer, btnWarehouse;
    private ImageView imgContact;
    ApiURL apiURL = new ApiURL();
    private LinearLayout container;
    HideKeyboard hideKeyboard;
    PrefServiceURL prefServiceURL;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        setupToolbar();
        getURL();
        initWidget();
        WarehouseBottomSheet();
    }

    private void getURL() {
        Log.i(TAG, "getURL: ");
        prefServiceURL = new PrefServiceURL(getApplicationContext());
        url = prefServiceURL.getURL();
    }

    private void initWidget() {
        Log.i(TAG, "initWidget: ");

        container = findViewById(R.id.container);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: ");
                hideKeyboard.hideSoftKeyboard(CustomerActivity.this);
            }
        });

        txtWHNo = findViewById(R.id.txtWHNo);
        txtWHName = findViewById(R.id.txtWHName);

        txtSelectCustomer = findViewById(R.id.txtSelectCustomer);
        txtSelectCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        imgContact = findViewById(R.id.imgContact);
        imgContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestContactPermission();
            }
        });
        etCName = findViewById(R.id.etCName);
        etCMobile = findViewById(R.id.etCMobile);
        etCAddress = findViewById(R.id.etCAddress);
        btnWarehouse = findViewById(R.id.btnWarehouse);
        btnWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWarehouse();
            }
        });
        btnCreateCustomer = findViewById(R.id.btnCreateCustomer);
        btnCreateCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etCName.getText().toString().trim();
                String mobile = etCMobile.getText().toString().trim();
                String add = etCAddress.getText().toString().trim();

                if(name.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), "Enter Customer Name.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(mobile.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), "Enter Customer Mobile Number.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(add.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), "Enter Customer Address.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (txtWHNo.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(getApplicationContext(), "Select warehouse.", Toast.LENGTH_SHORT).show();
                    return;
                }

                saveCustomer(name, mobile, add);
            }
        });
        contactBottomSheet();
    }

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar: ");
        toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Customer");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void saveCustomer(String name, String mobile, String add){

        final ProgressDialog dialog = ProgressDialog.show(this, "", "Please wait...", false, false);
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = apiURL.customerEntry();
            Log.i(TAG, "saveCustomer: " + URL);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("CustomerName", name);
            jsonBody.put("PhoneNumber", mobile);
            jsonBody.put("Address", add);
            jsonBody.put("WarehouseID", selectedWarehouseModel.getWHNo());
            final String requestBody = jsonBody.toString();
            Log.i(TAG, "saveCustomer: " + requestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResponse(String response) {
                    try {
                        Log.i(TAG, "onResponse: " + response);

                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        ResponseDefault res = gson.fromJson(response, ResponseDefault.class);
                        if (res.getSuccess()) {
                            Toast.makeText(CustomerActivity.this, "Customer Save successfull.", Toast.LENGTH_SHORT).show();
                            resetAllField();
                        } else {
                            Toast.makeText(CustomerActivity.this, "Customer Save failed.", Toast.LENGTH_SHORT).show();
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
                    //return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
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
        etCName.setText("");
        etCMobile.setText("");
        etCAddress.setText("");
    }

    BottomSheetDialog bottomSheetDialogContact;
    ContactModelAdapter adapterContact;
    List<ContactsModal> mContactsModals = new ArrayList<>();
    private void contactBottomSheet() {
        Log.i(TAG, "cutstomerBottomSheet: ");
        bottomSheetDialogContact = new BottomSheetDialog(CustomerActivity.this, R.style.Theme_Design_BottomSheetDialog);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottom_sheet_contact, (LinearLayout)findViewById(R.id.bottom_sheet_item));
        RecyclerView recycleview_customer = bottomSheetView.findViewById(R.id.recycleview_contact);
        recycleview_customer.setLayoutManager(new LinearLayoutManager(this));

        ImageView btn_contact_sheet_close = bottomSheetView.findViewById(R.id.btn_contact_sheet_close);
        btn_contact_sheet_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogContact.dismiss();
            }
        });

        adapterContact = new ContactModelAdapter(this, mContactsModals);
        recycleview_customer.setAdapter(adapterContact);
        bottomSheetDialogContact.setContentView(bottomSheetView);

        adapterContact.setOnItemClickListener(new ContactModelAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(int position, List<ContactsModal> mData) {
                Log.i(TAG, "Customer: " + mData.get(position).getUserName());

                etCName.setText(mData.get(position).getUserName());
                etCMobile.setText(mData.get(position).getContactNumber());
                bottomSheetDialogContact.dismiss();
            }
        });

        setupFullHeight(bottomSheetDialogContact);

        EditText et_search_contact = bottomSheetView.findViewById(R.id.et_search_contact);
        et_search_contact.addTextChangedListener(new TextWatcher() {
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

        et_search_contact.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (et_search_contact.getRight() - et_search_contact.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        et_search_contact.setText("");
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void filterListCustomer(String text) {
        ArrayList<ContactsModal> filteredList = new ArrayList<>();

        for (ContactsModal item : mContactsModals) {
            if (item.getUserName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapterContact.filterListCustomer(filteredList);
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


    public static final int CONTACT_PERMISSION_CODE = 11234;

    private void requestContactPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_CONTACTS)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to show your current location on map.")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(CustomerActivity.this,
                                    new String[] {Manifest.permission.READ_CONTACTS}, CONTACT_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_CONTACTS}, CONTACT_PERMISSION_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CONTACT_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getAllPhoneContacts();
            } else {
                Log.d(TAG, "onRequestPermissionsResult: " + "Permission DENIED");
            }
        }
    }


    public ArrayList<ContactsModal> getAllPhoneContacts() {
        Log.d("START","Getting all Contacts");
        ArrayList<ContactsModal> listContactsModal = new ArrayList<ContactsModal>();

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone._ID}, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false)
        {
            String contactNumber = cursor.getString(cursor.getColumnIndexOrThrow("DISPLAY_NAME"));
            Log.i(TAG, "getAllPhoneContacts: " + contactNumber);
            ContactsModal modal = new ContactsModal(cursor.getString(cursor.getColumnIndexOrThrow("DISPLAY_NAME")),
                    cursor.getString(cursor.getColumnIndexOrThrow("data1")));
            listContactsModal.add(modal);
            cursor.moveToNext();
        }
        cursor.close();
        cursor = null;
        Log.d("END","Got all Contacts");
        mContactsModals.clear();
        mContactsModals.addAll(listContactsModal);
        adapterContact.notifyDataSetChanged();
        bottomSheetDialogContact.show();
        return listContactsModal;
    }


    private WarehouseModel selectedWarehouseModel;
    BottomSheetDialog bottomSheetDialogWarehouse;
    WarehouseModelAdapter adapterWarehouse;
    List<WarehouseModel> mWarehouse = new ArrayList<>();

    private void WarehouseBottomSheet() {
        Log.i(TAG, "WarehouseBottomSheet: ");
        bottomSheetDialogWarehouse = new BottomSheetDialog(CustomerActivity.this, R.style.Theme_Design_BottomSheetDialog);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottom_sheet_warehouse, (LinearLayout)findViewById(R.id.bottom_sheet_item));
        RecyclerView recycleview_customer = bottomSheetView.findViewById(R.id.recycleview_customer);
        recycleview_customer.setLayoutManager(new LinearLayoutManager(this));

        ImageView btn_customer_sheet_close = bottomSheetView.findViewById(R.id.btn_customer_sheet_close);
        btn_customer_sheet_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogWarehouse.dismiss();
            }
        });

        adapterWarehouse = new WarehouseModelAdapter(this, mWarehouse);
        recycleview_customer.setAdapter(adapterWarehouse);
        bottomSheetDialogWarehouse.setContentView(bottomSheetView);

        adapterWarehouse.setOnItemClickListener(new WarehouseModelAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(int position, List<WarehouseModel> mData) {
                Log.i(TAG, "Warehouse: " + mData.get(position).getWarehouseName());
                txtWHNo.setText(mData.get(position).getWHNo());
                txtWHName.setText(mData.get(position).getWarehouseName());
                selectedWarehouseModel = mData.get(position);
                bottomSheetDialogWarehouse.dismiss();
            }
        });

        setupFullHeight(bottomSheetDialogWarehouse);

        EditText et_search_wh = bottomSheetView.findViewById(R.id.et_search_wh);
        et_search_wh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterListWarehouse(s.toString());
            }
        });

        et_search_wh.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (et_search_wh.getRight() - et_search_wh.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        et_search_wh.setText("");
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void filterListWarehouse(String text) {
        ArrayList<WarehouseModel> filteredList = new ArrayList<>();

        for (WarehouseModel item : mWarehouse) {
            if (item.getWarehouseName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapterWarehouse.filterListCustomer(filteredList);
    }

    private void getWarehouse() {
        Log.i(TAG, "getWarehouse: ");

        final ProgressDialog dialog = ProgressDialog.show(this, "", "Please wait...", false, false);
        String URL = apiURL.getWarehouseList();
        Log.i(TAG, "getWarehouse: " + URL);
        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: ");
                Log.i(TAG, "onResponse: " + response);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                ResponseWarehouse res = gson.fromJson(response, ResponseWarehouse.class);

                if (res.getData() != null) {
                    mWarehouse.clear();
                    mWarehouse.addAll(res.getData());
                    adapterWarehouse.notifyDataSetChanged();
                    bottomSheetDialogWarehouse.show();
                    dialog.dismiss();
                }

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

}