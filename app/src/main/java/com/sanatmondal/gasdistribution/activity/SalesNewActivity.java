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
import android.graphics.Typeface;
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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.sanatmondal.gasdistribution.adapter.CollectionItemAdapter;
import com.sanatmondal.gasdistribution.adapter.CustomerModelAdapter;
import com.sanatmondal.gasdistribution.adapter.ItemModelAdapter;
import com.sanatmondal.gasdistribution.adapter.SalesCollectionItemAdapter;
import com.sanatmondal.gasdistribution.model.CustomerModel;
import com.sanatmondal.gasdistribution.model.CustomerResponse;
import com.sanatmondal.gasdistribution.model.ItemModel;
import com.sanatmondal.gasdistribution.model.ItemResponse;
import com.sanatmondal.gasdistribution.model.ResponseDefault;
import com.sanatmondal.gasdistribution.model.ResponseFinallySave;
import com.sanatmondal.gasdistribution.model.ResponseTempOrderSale;
import com.sanatmondal.gasdistribution.model.TEMPSalesFromCustomerOrderViewModel;
import com.sanatmondal.gasdistribution.model.TempOrderSale;
import com.sanatmondal.gasdistribution.model.UserModel;
import com.sanatmondal.gasdistribution.network.ApiURL;
import com.sanatmondal.gasdistribution.others.HideKeyboard;
import com.sanatmondal.gasdistribution.pref.PrefServiceURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SalesNewActivity extends AppCompatActivity
        implements View.OnClickListener {
    private static final String TAG = "SalesNewActivity";

    private ActionBar toolbar;
    String userID = "";
    UserModel userModel = new UserModel();
    Button btnCustomer, btnItem, btnAllItem;
    private TextView txtCustID, txtCustName, txtCustMobile, txtCustStock, txtCustAddress, txtItemID, txtItemName, txtItemQtyLeft,
            txtItemBill, txtNetAmt, txtPreDue, txtTotalBill, txtCurrentDue, txtItemSalesCount, txtItemCollectionCount, txtSlenderSalesPrice, txtGasSalesPrice;
    private Button btnSales, btnCollection, btnAddItem, btnCustomerRemove, btnCancel, btnSave;
    private ArrayList<Button> buttons = new ArrayList<Button>();
    String orderType = "0";
    private LinearLayout linSalesOnly;
    RadioGroup rgItemtype;
    RadioButton rbGas, rbGasCylinder;
    private EditText etQty, etPayAmt, etDiscount;
    private ItemModel selectedSalesItemModel, selectedCollectionItemModel;
    private CustomerModel selectedCustomerModel;
    private LinearLayout linTitleCollection, linTitleSales, linPriceSec;
    ApiURL apiURL = new ApiURL();
    Double itemToltalPriceFromDB = 0.0;
    private LinearLayout container;
    HideKeyboard hideKeyboard;
    public static final String KEY_ORDER_ID = "ORDER_ID";
    PrefServiceURL prefServiceURL;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_new);
        setupToolbar();
        getparams();
        getURL();
        initWidget();
        itemBottomSheetItem();
        cutstomerBottomSheet();
        buildRecycleView();
        buildRecycleViewCollection();
        getTempOrderSales();
    }

    private void getURL() {
        Log.i(TAG, "getURL: ");
        prefServiceURL = new PrefServiceURL(getApplicationContext());
        url = prefServiceURL.getURL();
    }

    ProgressDialog dialog;
    private void initWidget() {
        Log.i(TAG, "initWidget: ");

        container = findViewById(R.id.container);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: ");
                hideKeyboard.hideSoftKeyboard(SalesNewActivity.this);
            }
        });

        linTitleSales = findViewById(R.id.linTitleSales);
        linTitleCollection = findViewById(R.id.linTitleCollection);
        linTitleSales.setVisibility(View.VISIBLE);
        linTitleCollection.setVisibility(View.GONE);

        linPriceSec = findViewById(R.id.linPriceSec);
        linPriceSec.setVisibility(View.GONE);

        btnCustomer = findViewById(R.id.btnCustomer);
        btnItem = findViewById(R.id.btnItem);
        btnAllItem = findViewById(R.id.btnAllItem);
        btnCustomerRemove = findViewById(R.id.btnCustomerRemove);
        linSalesOnly = findViewById(R.id.linSalesOnly);

        txtCustID = findViewById(R.id.txtCustID);
        txtCustName = findViewById(R.id.txtCustName);
        txtCustMobile = findViewById(R.id.txtCustMobile);
        txtCustStock = findViewById(R.id.txtCustStock);
        txtCustAddress = findViewById(R.id.txtCustAddress);

        txtPreDue = findViewById(R.id.txtPreDue);
        txtItemBill = findViewById(R.id.txtItemBill);
        txtNetAmt = findViewById(R.id.txtNetAmt);
        txtTotalBill = findViewById(R.id.txtTotalBill);
        txtCurrentDue = findViewById(R.id.txtCurrentDue);
        txtItemSalesCount = findViewById(R.id.txtItemSalesCount);
        txtItemCollectionCount = findViewById(R.id.txtItemCollectionCount);

        txtSlenderSalesPrice = findViewById(R.id.txtSlenderSalesPrice);
        txtGasSalesPrice = findViewById(R.id.txtGasSalesPrice);

        btnCustomer.setOnClickListener(this);
        btnItem.setOnClickListener(this);
        btnAllItem.setOnClickListener(this);

        txtItemID = findViewById(R.id.txtItemID);
        txtItemName = findViewById(R.id.txtItemName);
        txtItemQtyLeft = findViewById(R.id.txtItemQtyLeft);
        etQty = findViewById(R.id.etQty);
        etDiscount = findViewById(R.id.etDiscount);
        etDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateTotalPrice(String.valueOf(txtPreDue.getText().toString()), txtItemBill.getText().toString(), etDiscount.getText().toString(), etPayAmt.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rgItemtype = findViewById(R.id.rgItemtype);
        rbGas = findViewById(R.id.rbGas);
        rbGasCylinder = findViewById(R.id.rbGasCylinder);
        rgItemtype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                hideKeyboard.hideSoftKeyboard(SalesNewActivity.this);
            }
        });

        btnAddItem = findViewById(R.id.btnAddItem);
        etPayAmt = findViewById(R.id.etPayAmt);
        etPayAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateTotalPrice(String.valueOf(txtPreDue.getText().toString()), txtItemBill.getText().toString(), etDiscount.getText().toString(), etPayAmt.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard.hideSoftKeyboard(SalesNewActivity.this);
                dialog = ProgressDialog.show(SalesNewActivity.this, "", "Please wait...", false, false);
                prepareForSaveItem();

            }
        });
        orderType = "1";

        btnSales = findViewById(R.id.btnSales);
        btnCollection = findViewById(R.id.btnCollection);
        btnSales.setOnClickListener(this);
        btnCollection.setOnClickListener(this);
        btnCustomerRemove.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        buttons.add(btnSales);
        buttons.add(btnCollection);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            enableDisableButtonLikeTab(0);
        }

    }

    private void prepareForSaveItem() {
        Log.i(TAG, "prepareForSaveItem: ");

        if (txtCustID.getText().toString().equalsIgnoreCase("")){
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Select Customer First", Toast.LENGTH_SHORT).show();
            return;
        }

//        int selectedID = rgItemtype.getCheckedRadioButtonId();
//        RadioButton selecteSalesType = (RadioButton) findViewById(selectedID);

        if (etQty.getText().toString().equalsIgnoreCase("")) {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Enter Qty.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedSalesItemModel == null && selectedCollectionItemModel == null) {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Please add item", Toast.LENGTH_SHORT).show();
            return;
        }

        String salesType = "";
        if (orderType == "1") {

            if (rbGas.isChecked()) {
                salesType = "Gas_Only";
            } else if (rbGasCylinder.isChecked()){
                salesType = "Gas_Cylinder_Both";
            } else {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Select Item Type", Toast.LENGTH_SHORT).show();
                return;
            }

            /*
            if (selecteSalesType != null) {
                if (selecteSalesType.getText().toString().equalsIgnoreCase("Refill")) {
                    salesType = "Gas_Only";
                } else {
                    salesType = "Gas_Cylinder_Both";
                }
            } else {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Select Item Type", Toast.LENGTH_SHORT).show();
                return;
            }*/
        }

        String OrderNo = userModel.getOrderNo();
        String ItemInfo = txtItemName.getText().toString();
        String CustomerID = txtCustID.getText().toString();
        String Item_Id = txtItemID.getText().toString();
        String SalesQty = etQty.getText().toString();

        TEMPSalesFromCustomerOrderViewModel tempItem = new TEMPSalesFromCustomerOrderViewModel();
        tempItem.setOrderNo(OrderNo);
        tempItem.setItemInfo(ItemInfo);
        tempItem.setCustomerID(CustomerID);
        tempItem.setItem_Id(Item_Id);
        tempItem.setSalesType(salesType);
        tempItem.setSalesQty(SalesQty);
        if (orderType == "1") {
            if (Double.valueOf(SalesQty) > selectedSalesItemModel.getInvQty()) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Max Qty is " + selectedSalesItemModel.getInvQty(), Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (orderType == "1")
            saveSalesItemTemp(tempItem);
        else
            saveCollectionItemTemp(tempItem);

    }

    private void resetItemValue() {
        Log.i(TAG, "resetItemValue: ");
        selectedSalesItemModel = null;
        selectedCollectionItemModel = null;
        etQty.setText("");
        txtItemQtyLeft.setText("");
        txtItemID.setText("");
        txtItemName.setText("");
        rbGas.setChecked(false);
        rbGasCylinder.setChecked(false);
        txtSlenderSalesPrice.setText("");
        txtGasSalesPrice.setText("");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void resetCustomerValue(){
        txtCustID.setText("");
        txtCustName.setText("");
        txtCustMobile.setText("");
        txtCustAddress.setText("");
        txtCustStock.setText("");
        txtPreDue.setText("0.0");
        txtNetAmt.setText(txtItemBill.getText().toString());
        txtItemBill.setText(String.valueOf(itemToltalPriceFromDB));
        selectedCustomerModel = null;
        btnCustomer.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
        btnCustomer.setClickable(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void resetAllValue(String type) {
        Log.i(TAG, "resetAllValue: ");
        selectedSalesItemModel = null;
        selectedCollectionItemModel = null;
        etQty.setText("");
        txtItemQtyLeft.setText("");
        txtItemID.setText("");
        txtItemName.setText("");
        txtCustID.setText("");
        txtCustName.setText("");
        txtCustMobile.setText("");
        txtCustStock.setText("");

        txtPreDue.setText("0.0");
        etDiscount.setText("0.0");
        txtNetAmt.setText("0.0");
        txtItemBill.setText("0.0");
        txtTotalBill.setText("0.0");
        txtCurrentDue.setText("0.0");
        etPayAmt.setText("0.0");
        txtSlenderSalesPrice.setText("");
        txtGasSalesPrice.setText("");
        selectedCustomerModel = null;
        btnCustomer.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
        btnCustomer.setClickable(true);
        if (type.equalsIgnoreCase("saveFinally"))
            btnCustomerRemove.setVisibility(View.VISIBLE);
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

                    btnItem.setVisibility(View.GONE);
                    linPriceSec.setVisibility(View.GONE);
                    btnAllItem.setVisibility(View.VISIBLE);
                    btnAddItem.setText("Add Collection Item");

                } else {
                    btn.setTextColor(getResources().getColor(R.color.black));
                    btn.setTypeface(null, Typeface.NORMAL);
                    btn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));

                    btnItem.setVisibility(View.VISIBLE);
                    linPriceSec.setVisibility(View.VISIBLE);
                    txtSlenderSalesPrice.setText("");
                    txtGasSalesPrice.setText("");
                    btnAllItem.setVisibility(View.GONE);
                    btnAddItem.setText("Add Sales Item");
                }
            }
        }
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
        userID = bundle.getString("userID", "");
        Log.i(TAG, "getparams: " + userModel.getOrderNo());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    BottomSheetDialog bottomSheetDialogCustomer;
    CustomerModelAdapter adapterCustomer;
    List<CustomerModel> mCustomerModels = new ArrayList<>();
    private void cutstomerBottomSheet() {
        Log.i(TAG, "cutstomerBottomSheet: ");
        bottomSheetDialogCustomer = new BottomSheetDialog(SalesNewActivity.this, R.style.Theme_Design_BottomSheetDialog);
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
                txtCustAddress.setText(custModel.getAddress());
                txtCustStock.setText(String.valueOf(custModel.getStockQty()));
                txtPreDue.setText(String.valueOf(custModel.getDueAmount()));
                calculateTotalPrice(String.valueOf(txtPreDue.getText().toString()), txtItemBill.getText().toString(), etDiscount.getText().toString(), etPayAmt.getText().toString());
//                if(!txtItemBill.getText().toString().equalsIgnoreCase("")){
//                    String price = calculateTotalPrice(String.valueOf(custModel.getDueAmt()), txtItemBill.getText().toString(), etDiscount.getText().toString(), etPayAmt.getText().toString());
//                    txtTotalBill.setText(price);
//                }

                btnCustomer.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
                btnCustomer.setClickable(false);

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

    BottomSheetDialog bottomSheetDialogItem;
    ItemModelAdapter adapterItem;
    List<ItemModel> mItemModels = new ArrayList<>();

    private void itemBottomSheetItem() {
        Log.i(TAG, "initBottomSheetItem: ");

        bottomSheetDialogItem = new BottomSheetDialog(SalesNewActivity.this, R.style.Theme_Design_BottomSheetDialog);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottom_sheet_item, (LinearLayout)findViewById(R.id.bottom_sheet_item));
        RecyclerView recycleview_recipents = bottomSheetView.findViewById(R.id.recycleview_item);
        recycleview_recipents.setLayoutManager(new LinearLayoutManager(this));

        ImageView btn_customer_item_close = bottomSheetView.findViewById(R.id.btn_item_sheet_close);
        btn_customer_item_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogItem.dismiss();
            }
        });

        adapterItem = new ItemModelAdapter(this, mItemModels);
        recycleview_recipents.setAdapter(adapterItem);
        bottomSheetDialogItem.setContentView(bottomSheetView);

        adapterItem.setOnItemClickListener(new ItemModelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, List<ItemModel> mData) {
                Log.i(TAG, "ProductInfo: " + mData.get(position).getItemName());
                if (orderType.equalsIgnoreCase("1")){
                    selectedSalesItemModel = mData.get(position);
                } else if (orderType.equalsIgnoreCase("2")){
                    selectedCollectionItemModel = mData.get(position);
                }
                txtItemID.setText(mData.get(position).getItemId());
                txtItemName.setText(mData.get(position).getItemName());
                txtItemQtyLeft.setText(String.valueOf(mData.get(position).getInvQty()));

                txtSlenderSalesPrice.setText(String.valueOf(mData.get(position).getSlenderSalesPrice()));
                txtGasSalesPrice.setText(String.valueOf(mData.get(position).getGasSalesPrice()));

                bottomSheetDialogItem.dismiss();
            }
        });

        setupFullHeight(bottomSheetDialogItem);

        EditText et_search_item = bottomSheetView.findViewById(R.id.et_search_item);
        et_search_item.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterItem(s.toString());
            }
        });

        et_search_item.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (et_search_item.getRight() - et_search_item.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        et_search_item.setText("");
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void filterItem(String text) {
        ArrayList<ItemModel> filteredList = new ArrayList<>();

        for (ItemModel item : mItemModels) {
            if (item.getItemName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapterItem.filterList(filteredList);
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSales:
                enableDisableButtonLikeTab(0);
                orderType = "1";
                rvItems.setVisibility(View.VISIBLE);
                rvItemsCollection.setVisibility(View.GONE);
                linTitleSales.setVisibility(View.VISIBLE);
                linTitleCollection.setVisibility(View.GONE);
                linSalesOnly.setVisibility(View.VISIBLE);
                txtItemName.setText("");
                txtItemID.setText("");
                etQty.setText("");
                break;
            case R.id.btnCollection:
                enableDisableButtonLikeTab(1);
                orderType = "2";
                rvItems.setVisibility(View.GONE);
                rvItemsCollection.setVisibility(View.VISIBLE);
                linTitleSales.setVisibility(View.GONE);
                linTitleCollection.setVisibility(View.VISIBLE);
                linSalesOnly.setVisibility(View.GONE);
                txtItemName.setText("");
                txtItemID.setText("");
                etQty.setText("");
                break;
            case R.id.btnCustomer:
                getAllCustomerList(userModel.getOrderNo());
                break;
            case R.id.btnItem:
                getOrderItemList(userModel.getOrderNo());
                break;
            case R.id.btnAllItem:
                getAllItemList(userModel.getOrderNo());
                break;
            case R.id.btnCustomerRemove:
                resetCustomerValue();
                break;
            case R.id.btnCancel:
                if(mTempOrderSale.size() <= 0 && mTempOrderCollection.size() <= 0){
                    Toast.makeText(this, "No Item or collection found for cancel.", Toast.LENGTH_SHORT).show();
                    return;
                }
                deleteTempDataUserWise(userID);
                break;
            case R.id.btnSave:

                if (mTempOrderCollection.size() <= 0 && mTempOrderSale.size() <= 0)
                {
                    Toast.makeText(getApplicationContext(), "No item found for save.", Toast.LENGTH_SHORT).show();
                    return;
                }
                showOrderSaveAlert(txtTotalBill.getText().toString(), etPayAmt.getText().toString());

                break;
        }
    }

    private void getAllCustomerList(String Orderno){

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

    private void getOrderItemList(String Orderno){

        final ProgressDialog dialog = ProgressDialog.show(this, "", "Please wait...", false, false);
        String URL = apiURL.getOrderItemList(userModel.getOrderNo());
        Log.i(TAG, "getOrderItemList: " + URL);
        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: ");
                Log.i(TAG, "onResponse: " + response);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                ItemResponse itemResponse = gson.fromJson(response, ItemResponse.class);
                mItemModels.clear();
                mItemModels.addAll(itemResponse.getData());
                adapterItem.notifyDataSetChanged();
                bottomSheetDialogItem.show();
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

    private void getAllItemList(String Orderno){

        final ProgressDialog dialog = ProgressDialog.show(this, "", "Please wait...", false, false);
        String URL = apiURL.getAllItemList();
        Log.i(TAG, "getAllItemList: " + URL);
        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: ");
                Log.i(TAG, "onResponse: " + response);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                ItemResponse itemResponse = gson.fromJson(response, ItemResponse.class);

                mItemModels.clear();
                mItemModels.addAll(itemResponse.getData());
                adapterItem.notifyDataSetChanged();
                bottomSheetDialogItem.show();
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

    private void filterListCustomer(String text) {
        ArrayList<CustomerModel> filteredList = new ArrayList<>();

        for (CustomerModel item : mCustomerModels) {
            if (item.getCustomerName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapterCustomer.filterListCustomer(filteredList);
    }

    private RecyclerView rvItems, rvItemsCollection;
    private RecyclerView.LayoutManager mLayoutManager;
    List<TempOrderSale> mTempOrderSale = new ArrayList<>();
    private SalesCollectionItemAdapter salesCollectionItemAdapter;

    private void buildRecycleView() {
        Log.i(TAG, "buildRecycleView: ");
        rvItems = findViewById(R.id.rvItems);
        rvItems.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        salesCollectionItemAdapter = new SalesCollectionItemAdapter(getApplicationContext(), mTempOrderSale);
        rvItems.setLayoutManager(mLayoutManager);
        rvItems.setAdapter(salesCollectionItemAdapter);

        salesCollectionItemAdapter.setOnItemClickListener(new SalesCollectionItemAdapter.OnItemClickListener() {

            @Override
            public void onRemoveItemClick(int position, List<TempOrderSale> mData) {
                Log.i(TAG, "onRemoveItemClick: " + mData.get(position).getItemId());
                Log.i(TAG, "onRemoveItemClick: " + mData.get(position).getItemInfo());
                Log.i(TAG, "onRemoveItemClick: " + mData.get(position).getSalesType());

                DeleteTempDataUserWiseSINGLE(userID, mData.get(position).getItemId(), mData.get(position).getSalesType());
            }

        });
    }

    List<TempOrderSale> mTempOrderCollection = new ArrayList<>();
    private CollectionItemAdapter mCollectionItemAdapter;
    private void buildRecycleViewCollection() {
        Log.i(TAG, "buildRecycleView: ");
        rvItemsCollection = findViewById(R.id.rvItemsCollection);
        rvItemsCollection.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        mCollectionItemAdapter = new CollectionItemAdapter(getApplicationContext(), mTempOrderCollection);
        rvItemsCollection.setLayoutManager(mLayoutManager);
        rvItemsCollection.setAdapter(mCollectionItemAdapter);

        mCollectionItemAdapter.setOnItemClickListener(new CollectionItemAdapter.OnItemClickListener() {

            @Override
            public void onRemoveItemClick(int position, List<TempOrderSale> mData) {
                Log.i(TAG, "onRemoveItemClick: " + mData.get(position).getItemId());
                Log.i(TAG, "onRemoveItemClick: " + mData.get(position).getItemInfo());
                Log.i(TAG, "onRemoveItemClick: " + mData.get(position).getSalesType());

                DeleteTempDataUserWiseSINGLE(userID, mData.get(position).getItemId(), mData.get(position).getSalesType());
            }

        });
    }

    private void saveSalesItemTemp(TEMPSalesFromCustomerOrderViewModel itemModel){

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = apiURL.addToTempTableOrderSales();
            Log.i(TAG, "saveSalesItemTemp: " + URL);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("OrderNo", itemModel.getOrderNo());
            jsonBody.put("ItemInfo", itemModel.getItemInfo());
            jsonBody.put("CustomerID", itemModel.getCustomerID());
            jsonBody.put("Item_Id", itemModel.getItem_Id());
            jsonBody.put("SalesType", itemModel.getSalesType());
            jsonBody.put("SalesQty", itemModel.getSalesQty());
            jsonBody.put("CreateBy", userID);
            final String requestBody = jsonBody.toString();
            Log.i(TAG, "saveItemTemp: " + URL);
            Log.i(TAG, "save: " + requestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.i(TAG, "onResponse:saveItemTemp: " + response);

                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        ResponseDefault res = gson.fromJson(response, ResponseDefault.class);

                        if (res.getSuccess()){
                            Toast.makeText(SalesNewActivity.this, "Sales Item added successfully.", Toast.LENGTH_SHORT).show();
                            resetItemValue();
                            getTempOrderSales();
                        } else {
                            Toast.makeText(SalesNewActivity.this, "Item added failed.", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    } catch (Exception e) {
                        dialog.dismiss();
                        Toast.makeText(SalesNewActivity.this, "Item added failed.", Toast.LENGTH_SHORT).show();
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
        } catch (JSONException e) {
            dialog.dismiss();
            e.printStackTrace();
        }
    }

    Double price = 0.0, totalBill = 0.0;
    Double saleCount = 0.0, collectionCount = 0.0;
    private void getTempOrderSales(){
        final ProgressDialog dialog = ProgressDialog.show(this, "", "Please wait...", false, false);
        String URL = apiURL.getTempOrderSales(userModel.getOrderNo().equalsIgnoreCase("") ? userID : userModel.getOrderNo());
        Log.i(TAG, "getTempOrderSales: " + URL);
        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: ");
                Log.i(TAG, "onResponse: getTempOrderSales: " + response);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                ResponseTempOrderSale res = gson.fromJson(response, ResponseTempOrderSale.class);
                price = 0.0;
                totalBill = 0.0;
                mTempOrderSale.clear();
                mTempOrderCollection.clear();

                saleCount = 0.0;
                collectionCount = 0.0;

                for (int i = 0; i < res.getData().size(); i++){
                    price += res.getData().get(i).getNetAmount();
                    if (res.getData().get(i).getSalesQty() > 0)
                    {
                        saleCount = saleCount + res.getData().get(i).getSalesQty();
                        mTempOrderSale.add(res.getData().get(i));
                        selectedCustomerModel = res.getData().get(i).getCustomer();
                    } else if (res.getData().get(i).getCollectionQty() > 0){
                        collectionCount = collectionCount + res.getData().get(i).getCollectionQty();
                        mTempOrderCollection.add(res.getData().get(i));
                        selectedCustomerModel = res.getData().get(i).getCustomer();
                    }

                }

                txtItemSalesCount.setText(String.valueOf(saleCount));
                txtItemCollectionCount.setText(String.valueOf(collectionCount));

                txtItemBill.setText(String.valueOf(price));
                if(res.getData().size() > 0){
                    setCustomerValue(selectedCustomerModel);
                }
                calculateTotalPrice(String.valueOf(txtPreDue.getText().toString()), txtItemBill.getText().toString(), etDiscount.getText().toString(), etPayAmt.getText().toString());

                salesCollectionItemAdapter.notifyDataSetChanged();
                mCollectionItemAdapter.notifyDataSetChanged();
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setCustomerValue(CustomerModel selectedCustomerModel){
        Log.i(TAG, "setCustomerValue: ");
        if (selectedCustomerModel == null){
            return;
        }
        txtCustID.setText(selectedCustomerModel.getCustomerID());
        txtCustName.setText(selectedCustomerModel.getCustomerName());
        txtCustMobile.setText(selectedCustomerModel.getMobile1());
        txtCustStock.setText(String.valueOf(selectedCustomerModel.getStockQty()));
        txtCustAddress.setText(selectedCustomerModel.getAddress());
        txtPreDue.setText(String.valueOf(selectedCustomerModel.getDueAmount()));
        btnCustomer.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
        btnCustomer.setClickable(false);
        btnCustomerRemove.setVisibility(View.GONE);

    }

    private void saveCollectionItemTemp(TEMPSalesFromCustomerOrderViewModel itemModel){

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = apiURL.addToTempTableOrderCollect();
            Log.i(TAG, "saveCollectionItemTemp: " + URL);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("OrderNo", itemModel.getOrderNo());
            jsonBody.put("ItemInfo", itemModel.getItemInfo());
            jsonBody.put("CustomerID", itemModel.getCustomerID());
            jsonBody.put("Item_Id", itemModel.getItem_Id());
            jsonBody.put("SalesQty", itemModel.getSalesQty());
            jsonBody.put("CreateBy", userID);
            final String requestBody = jsonBody.toString();
            Log.i(TAG, "saveItemTemp: " + URL);
            Log.i(TAG, "save: " + requestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResponse(String response) {
                    try {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        ResponseDefault res = gson.fromJson(response, ResponseDefault.class);
                        if (res.getSuccess()){
                            Toast.makeText(SalesNewActivity.this, "Collection Item added successfully.", Toast.LENGTH_SHORT).show();
                            resetAllValue("saveCollectionItemTemp");
                            getTempOrderSales();
                        } else {
                            Toast.makeText(SalesNewActivity.this, "Item added failed.", Toast.LENGTH_SHORT).show();
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

    private void saveFinally(String orderNo, String custID, String whID,  String itemPrice, String discount,
                             String netAmt, String preDueAmt, String totalBillAmt, String payAmt, String currentDueAmt){

        final ProgressDialog dialog = ProgressDialog.show(this, "", "Please wait...", false, false);
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = apiURL.saveFinally();
            Log.i(TAG, "saveFinally: " + URL);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("OrderNo", orderNo);
            jsonBody.put("CustomerID", custID);

            jsonBody.put("TotalItemPrice", itemPrice);
            jsonBody.put("AddCharge", "0");
            jsonBody.put("DiscountAmt", discount);
            jsonBody.put("NetTotalAmt", netAmt);
            jsonBody.put("CustomerPreviousAmount", preDueAmt);
            jsonBody.put("CusTotalBillAmount", totalBillAmt);
            jsonBody.put("CollectAmount", payAmt);
            jsonBody.put("CustCurrentDueAmount", currentDueAmt);

            jsonBody.put("WarehouseID", whID);
            jsonBody.put("CreateBy", userID);
            final String requestBody = jsonBody.toString();
            Log.i(TAG, "saveFinally: " + URL);
            Log.i(TAG, "saveFinally: " + requestBody);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResponse(String response) {
                    try {
                        Log.i(TAG, "onResponse:saveFinally: " + response);
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        ResponseFinallySave res = gson.fromJson(response, ResponseFinallySave.class);
                        if(res.getSuccess()) {
                            Toast.makeText(SalesNewActivity.this, "Order save successfully.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra(KEY_ORDER_ID, res.getData());
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            Toast.makeText(SalesNewActivity.this, "Order save failed.", Toast.LENGTH_SHORT).show();
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
        } catch (JSONException e) {
            dialog.dismiss();
            e.printStackTrace();
        }
    }

    private void deleteTempDataUserWise(String UserID){

        final ProgressDialog dialog = ProgressDialog.show(this, "", "Please wait...", false, false);
        String URL = apiURL.deleteTempDataUserWise(userModel.getOrderNo());
        Log.i(TAG, "deleteTempDataUserWise: " + URL);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(String response) {
                try {
                    Log.i(TAG, "onResponse: " + response);
                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    ResponseTempOrderSale res = gson.fromJson(response, ResponseTempOrderSale.class);

                    if(res.getSuccess()){
                        Toast.makeText(SalesNewActivity.this, "Order Cancel success.", Toast.LENGTH_SHORT).show();
                        mTempOrderSale.clear();
                        mTempOrderCollection.clear();
                        salesCollectionItemAdapter.notifyDataSetChanged();
                        mCollectionItemAdapter.notifyDataSetChanged();
                        btnCustomerRemove.setVisibility(View.VISIBLE);
                        resetAllValue("deleteTempDataUserWise");

                    } else {
                        Toast.makeText(SalesNewActivity.this, "Order Cancel failed.", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                } catch (Exception exception){
                    dialog.dismiss();
                }

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

    private String calculateTotalPrice(String preDueAmt, String itemPrice, String discount, String payAmt){
        try {
            if (preDueAmt.equalsIgnoreCase(""))
                preDueAmt = "0";
            if (itemPrice.equalsIgnoreCase(""))
                itemPrice = "0";
            if (discount.equalsIgnoreCase(""))
                discount = "0";
            if (payAmt.equalsIgnoreCase(""))
                payAmt = "0";

            Double dPreDueAmt = Double.valueOf(preDueAmt);
            Double dItemPrice = Double.valueOf(itemPrice);
            Double dDiscount = Double.valueOf(discount);
            Double dPayAmt = Double.valueOf(payAmt);

            Double dNetAmt = dItemPrice - dDiscount;
            Double dTotalBill = dNetAmt + dPreDueAmt;
            Double dCurrentDue = dTotalBill - dPayAmt;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtNetAmt.setText(String.valueOf(dNetAmt));
                    txtTotalBill.setText(String.valueOf(dTotalBill));
                    txtCurrentDue.setText(String.valueOf(dCurrentDue));
                }
            });

            return String.valueOf(dTotalBill);
        } catch (Exception ex){
            return String.valueOf("0.0");
        }

    }

    private void showOrderSaveAlert(String totalBillAmt, String payAmt){
        String msgDefault = "Are you sure you want to save the order?";
        String message = "TotalBill: " + totalBillAmt + ", \n" + "PayAmt: " + payAmt;

        new AlertDialog.Builder(SalesNewActivity.this)
                .setTitle("Do you want to save the order?")
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (mTempOrderCollection.size() <= 0 && mTempOrderSale.size() <= 0)
                        {
                            Toast.makeText(getApplicationContext(), "No item found for save.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (txtCustID.getText().toString().equalsIgnoreCase("")){
                            Toast.makeText(getApplicationContext(), "Select Customer First", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        saveFinally(userModel.getOrderNo(), txtCustID.getText().toString(), "", checkBlank(txtItemBill.getText().toString()),
                                checkBlank(etDiscount.getText().toString()), checkBlank(txtNetAmt.getText().toString()), checkBlank(txtPreDue.getText().toString()),
                                checkBlank(txtTotalBill.getText().toString()), checkBlank(etPayAmt.getText().toString()), checkBlank(txtCurrentDue.getText().toString()));
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

    private String checkBlank(String value){
        if(value.equalsIgnoreCase(""))
            return "0";
        else {
            return value;
        }
    }

    private void DeleteTempDataUserWiseSINGLE(String UserID, String ItemID,String SalesType){

        final ProgressDialog dialog = ProgressDialog.show(this, "", "Please wait...", false, false);
        try {
            String URL = apiURL.DeleteTempDataUserWiseSINGLE(UserID, ItemID,SalesType);
//            JSONObject jsonBody = new JSONObject();
//            jsonBody.put("UserID", UserID);
//            jsonBody.put("ItemID", ItemID);
//            jsonBody.put("SalesType", SalesType);
//            final String requestBody = jsonBody.toString();
            Log.i(TAG, "DeleteTempDataUserWiseSINGLE: " + URL);
//            Log.i(TAG, "DeleteTempDataUserWiseSINGLE: " + requestBody);
            StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResponse(String response) {
                    try {
                        Log.i(TAG, "onResponse: " + response);
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        ResponseDefault res = gson.fromJson(response, ResponseDefault.class);
                        if (res.getSuccess()){
                            Toast.makeText(SalesNewActivity.this, "Item delete successful.", Toast.LENGTH_SHORT).show();
                            resetItemValue();
                            getTempOrderSales();
                        } else {
                            Toast.makeText(SalesNewActivity.this, "Item delete failed.", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    } catch (Exception exception){
                        dialog.dismiss();
                    }

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
        } catch (Exception ex) {
            dialog.dismiss();
            ex.printStackTrace();
        }

    }

    
}