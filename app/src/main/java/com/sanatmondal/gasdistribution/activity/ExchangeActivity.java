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
import com.sanatmondal.gasdistribution.adapter.ExchangerItemAdapter;
import com.sanatmondal.gasdistribution.adapter.ItemModelAdapter;
import com.sanatmondal.gasdistribution.adapter.SalesCollectionItemAdapter;
import com.sanatmondal.gasdistribution.adapter.WarehouseModelAdapter;
import com.sanatmondal.gasdistribution.model.CustomerModel;
import com.sanatmondal.gasdistribution.model.CustomerResponse;
import com.sanatmondal.gasdistribution.model.ExchangerItemListModel;
import com.sanatmondal.gasdistribution.model.ItemModel;
import com.sanatmondal.gasdistribution.model.ItemResponse;
import com.sanatmondal.gasdistribution.model.ResponseDefault;
import com.sanatmondal.gasdistribution.model.ResponseExchanger;
import com.sanatmondal.gasdistribution.model.ResponseExchangerItemListModel;
import com.sanatmondal.gasdistribution.model.ResponseFinallySave;
import com.sanatmondal.gasdistribution.model.ResponseTempOrderSale;
import com.sanatmondal.gasdistribution.model.ResponseWarehouse;
import com.sanatmondal.gasdistribution.model.TEMPReplacementModel;
import com.sanatmondal.gasdistribution.model.TEMPSalesFromCustomerOrderViewModel;
import com.sanatmondal.gasdistribution.model.TempOrderSale;
import com.sanatmondal.gasdistribution.model.UserModel;
import com.sanatmondal.gasdistribution.model.WarehouseModel;
import com.sanatmondal.gasdistribution.network.ApiURL;
import com.sanatmondal.gasdistribution.others.HideKeyboard;
import com.sanatmondal.gasdistribution.pref.PrefServiceURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeActivity extends AppCompatActivity
        implements View.OnClickListener{
    private static final String TAG = "ExchangeActivity";

    private ActionBar toolbar;
    UserModel userModel = new UserModel();
    String userID = "";

    Button btnCustomer, btnItem, btnAllItem;
    private TextView txtCustID, txtCustName, txtCustMobile, txtCustStock, txtCustAddress, txtItemID, txtItemName, txtTotalGiveQty, txtTotRecQty, txtWHNo, txtWHName;
    private Button btnGive, btnReceive, btnAddItem, btnCustomerRemove, btnCancel, btnSave, btnWarehouse;
    private ArrayList<Button> buttons = new ArrayList<Button>();
    String orderType = "0";
    private EditText etQty, etAddCost, etLessCost;
    private ItemModel selectedSalesItemModel, selectedCollectionItemModel;
    private CustomerModel selectedCustomerModel;
    private WarehouseModel selectedWarehouseModel;
    private LinearLayout linTitleCollection, linTitleSales;
    ApiURL apiURL = new ApiURL();
    Double itemToltalPriceFromDB = 0.0;
    private LinearLayout container;
    HideKeyboard hideKeyboard;

    public static final String KEY_EXCHANGE_ID = "EXCHANGE_ID";
    PrefServiceURL prefServiceURL;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        setupToolbar();
        getURL();
        getparams();
        initWidget();
        itemBottomSheetItem();
        cutstomerBottomSheet();
        WarehouseBottomSheet();
        buildRecycleView();
      //  buildRecycleViewCollection();
        //getTempOrderSales();

        deleteTempDataUserWise(userID, "first");
    }

    private void getURL() {
        Log.i(TAG, "getURL: ");
        prefServiceURL = new PrefServiceURL(getApplicationContext());
        url = prefServiceURL.getURL();
    }

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar: ");
        toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Exchange");
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

    ProgressDialog dialog;
    private void initWidget() {
        Log.i(TAG, "initWidget: ");

        container = findViewById(R.id.container);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: ");
                hideKeyboard.hideSoftKeyboard(ExchangeActivity.this);
            }
        });

        linTitleSales = findViewById(R.id.linTitleSales);
        linTitleCollection = findViewById(R.id.linTitleCollection);
        linTitleSales.setVisibility(View.VISIBLE);
        linTitleCollection.setVisibility(View.GONE);

        btnWarehouse = findViewById(R.id.btnWarehouse);
        btnCustomer = findViewById(R.id.btnCustomer);
        btnItem = findViewById(R.id.btnItem);
        btnAllItem = findViewById(R.id.btnAllItem);
        btnCustomerRemove = findViewById(R.id.btnCustomerRemove);

        txtCustID = findViewById(R.id.txtCustID);
        txtCustName = findViewById(R.id.txtCustName);
        txtCustMobile = findViewById(R.id.txtCustMobile);
        txtCustStock = findViewById(R.id.txtCustStock);
        txtCustAddress = findViewById(R.id.txtCustAddress);

        txtTotalGiveQty = findViewById(R.id.txtTotalGiveQty);
        txtTotRecQty = findViewById(R.id.txtTotRecQty);

        txtWHNo = findViewById(R.id.txtWHNo);
        txtWHName = findViewById(R.id.txtWHName);

        btnWarehouse.setOnClickListener(this);
        btnCustomer.setOnClickListener(this);
        btnItem.setOnClickListener(this);
        btnAllItem.setOnClickListener(this);

        txtItemID = findViewById(R.id.txtItemID);
        txtItemName = findViewById(R.id.txtItemName);
        etQty = findViewById(R.id.etQty);
        etAddCost = findViewById(R.id.etAddCost);
        etLessCost = findViewById(R.id.etLessCost);

        btnAddItem = findViewById(R.id.btnAddItem);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard.hideSoftKeyboard(ExchangeActivity.this);
                dialog = ProgressDialog.show(ExchangeActivity.this, "", "Please wait...", false, false);
                prepareForSaveItem();

            }
        });
        orderType = "1";

        btnGive = findViewById(R.id.btnGive);
        btnReceive = findViewById(R.id.btnReceive);
        btnGive.setOnClickListener(this);
        btnReceive.setOnClickListener(this);
        btnCustomerRemove.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        buttons.add(btnGive);
        buttons.add(btnReceive);
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

        TEMPReplacementModel tempItem = new TEMPReplacementModel();
        tempItem.setItemID(txtItemID.getText().toString());
        tempItem.setItemInfo(txtItemName.getText().toString());
        tempItem.setInvType("Cylinder");
        tempItem.setReturnDate("");
        if (orderType == "1") {
            tempItem.setType("give");
            tempItem.setGQty(etQty.getText().toString());
            tempItem.setRQty("0");
        } else {
            tempItem.setType("get");
            tempItem.setRQty(etQty.getText().toString());
            tempItem.setGQty("0");
        }

        saveExchangeItemTemp(tempItem);
 /*

        String salesType = "";

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
            saveExchangeItemTemp(tempItem);
        else
            saveCollectionItemTemp(tempItem);
*/
    }

    private void resetItemValue() {
        Log.i(TAG, "resetItemValue: ");
        selectedSalesItemModel = null;
        selectedCollectionItemModel = null;
        etQty.setText("");
        txtItemID.setText("");
        txtItemName.setText("");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void resetCustomerValue(){
        txtCustID.setText("");
        txtCustName.setText("");
        txtCustMobile.setText("");
        txtCustAddress.setText("");
        txtCustStock.setText("");
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
        txtItemID.setText("");
        txtItemName.setText("");
        txtCustID.setText("");
        txtCustName.setText("");
        txtCustMobile.setText("");
        txtCustStock.setText("");
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
                    btnAllItem.setVisibility(View.VISIBLE);
                    btnAddItem.setText("Add Collection Item");

                } else {
                    btn.setTextColor(getResources().getColor(R.color.black));
                    btn.setTypeface(null, Typeface.NORMAL);
                    btn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));

                    btnItem.setVisibility(View.VISIBLE);
                    btnAllItem.setVisibility(View.GONE);
                    btnAddItem.setText("Add Sales Item");
                }
            }
        }
    }

    BottomSheetDialog bottomSheetDialogWarehouse;
    WarehouseModelAdapter adapterWarehouse;
    List<WarehouseModel> mWarehouse = new ArrayList<>();
    private void WarehouseBottomSheet() {
        Log.i(TAG, "WarehouseBottomSheet: ");
        bottomSheetDialogWarehouse = new BottomSheetDialog(ExchangeActivity.this, R.style.Theme_Design_BottomSheetDialog);
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
                resetItemValue();
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

    BottomSheetDialog bottomSheetDialogCustomer;
    CustomerModelAdapter adapterCustomer;
    List<CustomerModel> mCustomerModels = new ArrayList<>();
    private void cutstomerBottomSheet() {
        Log.i(TAG, "cutstomerBottomSheet: ");
        bottomSheetDialogCustomer = new BottomSheetDialog(ExchangeActivity.this, R.style.Theme_Design_BottomSheetDialog);
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
                //  calculateTotalPrice(String.valueOf(txtPreDue.getText().toString()), txtItemBill.getText().toString(), etDiscount.getText().toString(), etPayAmt.getText().toString());

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

        bottomSheetDialogItem = new BottomSheetDialog(ExchangeActivity.this, R.style.Theme_Design_BottomSheetDialog);
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
            case R.id.btnGive:
                enableDisableButtonLikeTab(0);
                orderType = "1";
                rvItems.setVisibility(View.VISIBLE);
             //  rvItemsCollection.setVisibility(View.GONE);
                linTitleSales.setVisibility(View.VISIBLE);
              //  linTitleCollection.setVisibility(View.GONE);
                txtItemName.setText("");
                txtItemID.setText("");
                etQty.setText("");
                break;
            case R.id.btnReceive:
                enableDisableButtonLikeTab(1);
                orderType = "2";
             //   rvItems.setVisibility(View.GONE);
             //   rvItemsCollection.setVisibility(View.VISIBLE);
            //    linTitleSales.setVisibility(View.GONE);
               // linTitleCollection.setVisibility(View.VISIBLE);
                txtItemName.setText("");
                txtItemID.setText("");
                etQty.setText("");
                break;
            case R.id.btnCustomer:
                getALLExchangerList(userModel.getOrderNo());
                break;
            case R.id.btnWarehouse:
                getWarehouse();
                break;
            case R.id.btnItem:
                //getOrderItemList(userModel.getOrderNo());
                if (selectedWarehouseModel != null) {
                    getALLItemListForExchange(selectedWarehouseModel.getWHNo());
                } else {
                    Toast.makeText(this, "Select Warehouse first.", Toast.LENGTH_SHORT).show();
                }
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
                deleteTempDataUserWise(userID, "cancel");
                break;
            case R.id.btnSave:
                if (mTempOrderCollection.size() <= 0 && mTempOrderSale.size() <= 0) {
                    Toast.makeText(getApplicationContext(), "No item found for save.", Toast.LENGTH_SHORT).show();
                    return;
                }
                showOrderSaveAlert();
                break;
        }
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

    private void getALLExchangerList(String Orderno){

        final ProgressDialog dialog = ProgressDialog.show(this, "", "Please wait...", false, false);
        String URL = apiURL.getALLExchangerList();
        Log.i(TAG, "getALLExchangerList: " + URL);
        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: ");
                Log.i(TAG, "onResponse: " + response);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();

                ResponseExchanger customerResponse = gson.fromJson(response, ResponseExchanger.class);
                Log.i(TAG, "onResponse: " +customerResponse.getMsg());

                mCustomerModels.clear();
                List<CustomerModel> listCust = new ArrayList<>();
                if (customerResponse.getData() != null) {
                    for (int i = 0; i < customerResponse.getData().size(); i++) {
                        listCust.add(new CustomerModel(customerResponse.getData().get(i).getId(), customerResponse.getData().get(i).getExchengerName(),
                                customerResponse.getData().get(i).getMobileNo(), customerResponse.getData().get(i).getMobileNo(), customerResponse.getData().get(i).getAddress(), customerResponse.getData().get(i).getStockQty()));
                    }
                    mCustomerModels.addAll(listCust);
                }
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
/*
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
*/
    private void getAllItemList(String Orderno){

        final ProgressDialog dialog = ProgressDialog.show(this, "", "Please wait...", false, false);
        String URL = apiURL.getAllItemList();
        Log.i(TAG, "ALLItemListForExchange: " + URL);
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

    private void getALLItemListForExchange(String WHID){

        final ProgressDialog dialog = ProgressDialog.show(this, "", "Please wait...", false, false);
        String URL = apiURL.WhWiseEmptyCylenderItemList(WHID);
        Log.i(TAG, "ALLItemListForExchange: " + URL);
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

    private void filterListWarehouse(String text) {
        ArrayList<WarehouseModel> filteredList = new ArrayList<>();

        for (WarehouseModel item : mWarehouse) {
            if (item.getWarehouseName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapterWarehouse.filterListCustomer(filteredList);
    }

    private RecyclerView rvItems;
    private RecyclerView.LayoutManager mLayoutManager;
    List<ExchangerItemListModel> mTempOrderSale = new ArrayList<>();
    private ExchangerItemAdapter salesCollectionItemAdapter;

    private void buildRecycleView() {
        Log.i(TAG, "buildRecycleView: ");
        rvItems = findViewById(R.id.rvItems);
        rvItems.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        salesCollectionItemAdapter = new ExchangerItemAdapter(getApplicationContext(), mTempOrderSale);
        rvItems.setLayoutManager(mLayoutManager);
        rvItems.setAdapter(salesCollectionItemAdapter);

        salesCollectionItemAdapter.setOnItemClickListener(new ExchangerItemAdapter.OnItemClickListener() {

            @Override
            public void onRemoveItemClick(int position, List<ExchangerItemListModel> mData) {
                Log.i(TAG, "onRemoveItemClick: " + mData.get(position).getItemInfo());
                DeleteTempDataUserWiseSINGLEExchange(userID, mData.get(position).getItemID(), mData.get(position).getType());
            }

        });
    }

    List<TempOrderSale> mTempOrderCollection = new ArrayList<>();
    private CollectionItemAdapter mCollectionItemAdapter;
    /*
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
    }*/

    private void saveExchangeItemTemp(TEMPReplacementModel itemModel){

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = apiURL.AddToTEMPReplacement();
            Log.i(TAG, "saveExchangeItemTemp: " + URL);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("ItemID", itemModel.getItemID());
            jsonBody.put("ItemInfo", itemModel.getItemInfo());
            jsonBody.put("InvType", itemModel.getInvType());
            jsonBody.put("RQty", itemModel.getRQty());
            jsonBody.put("GQty", itemModel.getGQty());
            jsonBody.put("Type", itemModel.getType());
          //  jsonBody.put("ReturnDate", itemModel.getReturnDate());
            jsonBody.put("CreateBy", userID);
            final String requestBody = jsonBody.toString();
            Log.i(TAG, "saveExchangeItemTemp: " + URL);
            Log.i(TAG, "save: " + requestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.i(TAG, "onResponse:saveItemTemp: " + response);

                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        ResponseExchangerItemListModel res = gson.fromJson(response, ResponseExchangerItemListModel.class);

                        if (res.getSuccess()){
                            Toast.makeText(ExchangeActivity.this, "Exchange Item added successfully.", Toast.LENGTH_SHORT).show();
                            resetItemValue();
                            mTempOrderSale.clear();
                            mTempOrderSale.addAll(res.getData());
                            salesCollectionItemAdapter.notifyDataSetChanged();

                            if (res.getData().size() > 0)
                                CalculateItemQty(res.getData());

                            //getTempOrderSales();
                        } else {
                            Toast.makeText(ExchangeActivity.this, "Exchange Item added failed.", Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    } catch (Exception e) {
                        dialog.dismiss();
                        Toast.makeText(ExchangeActivity.this, "Exchange Item added failed.", Toast.LENGTH_SHORT).show();
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

    private void CalculateItemQty(List<ExchangerItemListModel> tempExchangeList) {
        Log.i(TAG, "CalculateItemQty: ");

        Double totGiveQty = 0.0, totRecQty = 0.0;
        for (int i = 0; i < tempExchangeList.size(); i++){
            totGiveQty = totGiveQty + tempExchangeList.get(i).getGQty();
            totRecQty = totRecQty + tempExchangeList.get(i).getRQty();
        }

        String finalTotGiveQty = String.valueOf(totGiveQty.intValue());
        String finalTotRecQty = String.valueOf(totRecQty.intValue());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtTotalGiveQty.setText(finalTotGiveQty);
                txtTotRecQty.setText(finalTotRecQty);
            }
        });

    }

    Double price = 0.0, totalBill = 0.0;
    Double saleCount = 0.0, collectionCount = 0.0;
    /*
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
*/
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
        btnCustomer.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
        btnCustomer.setClickable(false);
        btnCustomerRemove.setVisibility(View.GONE);

    }

    private void saveCollectionItemTemp(TEMPSalesFromCustomerOrderViewModel itemModel){
        //  final ProgressDialog dialog = ProgressDialog.show(this, "", "Please wait...", false, false);
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
                            Toast.makeText(ExchangeActivity.this, "Collection Item added successfully.", Toast.LENGTH_SHORT).show();
                            resetAllValue("saveCollectionItemTemp");
                           // getTempOrderSales();
                        } else {
                            Toast.makeText(ExchangeActivity.this, "Item added failed.", Toast.LENGTH_SHORT).show();
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

    private void FinallySaveExchange(String custID, String AddAmount,  String LessAmount){

        final ProgressDialog dialog = ProgressDialog.show(this, "", "Please wait...", false, false);
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = apiURL.FinallySaveExchange();
            Log.i(TAG, "FinallySaveExchange: " + URL);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("ExchangerID", custID);
            jsonBody.put("AddAmount", AddAmount);
            jsonBody.put("LessAmount", LessAmount);
            jsonBody.put("WarehouseID", selectedWarehouseModel.getWHNo());
            jsonBody.put("CreateBy", userID);

            final String requestBody = jsonBody.toString();
            Log.i(TAG, "FinallySaveExchange: " + URL);
            Log.i(TAG, "FinallySaveExchange: " + requestBody);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResponse(String response) {
                    try {
                        Log.i(TAG, "onResponse:saveFinally: " + response);
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();

                        ResponseDefault res = gson.fromJson(response, ResponseDefault.class);
                        if (res.getMsg().equalsIgnoreCase("Success")){
                            Toast.makeText(ExchangeActivity.this, "Exchange successful.", Toast.LENGTH_SHORT).show();
                            mTempOrderSale.clear();
                            finish();
                        } else {
                            Toast.makeText(ExchangeActivity.this, "Exchange failed.", Toast.LENGTH_SHORT).show();
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

    private void deleteTempDataUserWise(String UserID, String callingTime){

        final ProgressDialog dialog = ProgressDialog.show(this, "", "Please wait...", false, false);
        String URL = apiURL.deleteTempDataUserWise(UserID);
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
                        if(!callingTime.equalsIgnoreCase("first"))
                            Toast.makeText(ExchangeActivity.this, "Clear success.", Toast.LENGTH_SHORT).show();
                        mTempOrderSale.clear();
                        mTempOrderCollection.clear();
                        salesCollectionItemAdapter.notifyDataSetChanged();
                        mCollectionItemAdapter.notifyDataSetChanged();
                        btnCustomerRemove.setVisibility(View.VISIBLE);
                        resetAllValue("deleteTempDataUserWise");

                    } else {
                        Toast.makeText(ExchangeActivity.this, "Order Cancel failed.", Toast.LENGTH_SHORT).show();
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
    /*
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
    */
    private void showOrderSaveAlert(){
        String msgDefault = "Are you sure you want to Exchange?";
       // String message = "TotalBill: " + totalBillAmt + ", \n" + "PayAmt: " + payAmt;

        new AlertDialog.Builder(ExchangeActivity.this)
                .setTitle("Exchange Item")
                .setMessage(msgDefault)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (mTempOrderSale.size() <= 0) {
                            Toast.makeText(getApplicationContext(), "No item found for save.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (txtCustID.getText().toString().equalsIgnoreCase("")){
                            Toast.makeText(getApplicationContext(), "Select Customer First", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (txtWHNo.getText().toString().equalsIgnoreCase("")){
                            Toast.makeText(getApplicationContext(), "Select Warehouse First", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        FinallySaveExchange(checkBlank(txtCustID.getText().toString()), checkBlank(etAddCost.getText().toString()), checkBlank(etLessCost.getText().toString()));

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

    private void DeleteTempDataUserWiseSINGLEExchange(String UserID, String ItemID,String Type){

        final ProgressDialog dialog = ProgressDialog.show(this, "", "Please wait...", false, false);
        try {
            String URL = apiURL.DeleteTempDataUserWiseSINGLEExchange(UserID, ItemID, Type);
            Log.i(TAG, "DeleteTempDataUserWiseSINGLEExchange: " + URL);
            StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResponse(String response) {
                    try {
                        Log.i(TAG, "onResponse: " + response);
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        ResponseExchangerItemListModel res = gson.fromJson(response, ResponseExchangerItemListModel.class);

                        if (res.getSuccess()){
                            Toast.makeText(ExchangeActivity.this, "Item delete successful.", Toast.LENGTH_SHORT).show();
                            mTempOrderSale.clear();
                            mTempOrderSale.addAll(res.getData());
                            salesCollectionItemAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(ExchangeActivity.this, "Item delete failed.", Toast.LENGTH_SHORT).show();
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