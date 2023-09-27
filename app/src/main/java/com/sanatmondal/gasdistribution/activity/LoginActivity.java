package com.sanatmondal.gasdistribution.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanatmondal.gasdistribution.R;
import com.sanatmondal.gasdistribution.model.LoginModel;
import com.sanatmondal.gasdistribution.model.UserModel;
import com.sanatmondal.gasdistribution.network.ApiURL;
import com.sanatmondal.gasdistribution.others.HideKeyboard;
import com.sanatmondal.gasdistribution.pref.PrefServiceURL;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Button btnLogin;
    private EditText etEmpID, etPasswrod;
    private ProgressBar progressBar;
    ApiURL apiURL = new ApiURL();
    private LinearLayout container;
    HideKeyboard hideKeyboard;
    PrefServiceURL prefServiceURL;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        etEmpID = findViewById(R.id.etEmpID);
        etPasswrod = findViewById(R.id.etPasswrod);

        container = findViewById(R.id.container);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: ");
                hideKeyboard.hideSoftKeyboard(LoginActivity.this);
            }
        });

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String EmpID = etEmpID.getText().toString().trim();
                String Pass = etPasswrod.getText().toString().trim();
                hideKeyboard.hideSoftKeyboard(LoginActivity.this);

                if(EmpID.equalsIgnoreCase("")) {
                    Toast.makeText(LoginActivity.this, "Enter UserID", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(Pass.equalsIgnoreCase("")) {
                    Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                login(EmpID, Pass);
            }
        });

    }

    private void login(String EmpID, String Pass){

        String URL = apiURL.getLoginURL(EmpID, Pass);
        Log.i(TAG, "login: " + URL);
        final ProgressDialog dialog = ProgressDialog.show(this, "", "Please wait...", false, false);
        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: ");
                Log.i(TAG, "onResponse: " + response);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                LoginModel loginModel = gson.fromJson(response, LoginModel.class);

                if (loginModel.getMsg().equalsIgnoreCase("Success")){
                    UserModel userModel = loginModel.getUser();
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    intent.putExtra("userModel", userModel);
                    intent.putExtra("userID", EmpID);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
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

    private void showDialog(){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this.getApplicationContext());
        edittext.setBackgroundResource(R.drawable.rectangle);
        edittext.setText(url);
        alert.setMessage("");
        alert.setTitle("Enter app url");
        alert.setView(edittext);
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Editable YouEditTextValue = edittext.getText();
                String test = edittext.getText().toString();
                Log.i(TAG, "onClick: " + test);
                prefServiceURL.createPrefServiceURL(test);
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }

    public static int convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int dp = (int) (px / (metrics.densityDpi / 160f));
        return dp;
    }
}