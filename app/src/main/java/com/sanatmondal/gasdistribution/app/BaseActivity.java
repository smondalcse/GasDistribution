package com.sanatmondal.gasdistribution.app;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.sanatmondal.gasdistribution.R;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Edge-to-edge enabled globally
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);

        WindowInsetsControllerCompat controller =
                new WindowInsetsControllerCompat(window, window.getDecorView());
        controller.setAppearanceLightStatusBars(false); // white icons
    }

    protected void initToolbar(Toolbar toolbar, String title) {

        // Apply status bar inset ONLY to toolbar
        ViewCompat.setOnApplyWindowInsetsListener(toolbar, (v, insets) -> {
            Insets statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars());

            v.setPadding(
                    v.getPaddingLeft(),
                    statusBar.top,
                    v.getPaddingRight(),
                    v.getPaddingBottom()
            );
            return insets;
        });

        TextView tvTitle = toolbar.findViewById(R.id.toolbar_title);
        View btnBack = toolbar.findViewById(R.id.btnBack);

        if (tvTitle != null) tvTitle.setText(title);
        if (btnBack != null) btnBack.setOnClickListener(v -> onBackPressed());
    }
    /**
     * Call this if your activity has NO toolbar but you still want status bar
     */
    protected void applyStatusBarPadding(View contentRoot) {
        ViewCompat.setOnApplyWindowInsetsListener(contentRoot, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    v.getPaddingLeft(),
                    systemBars.top,   // top padding for status bar
                    v.getPaddingRight(),
                    v.getPaddingBottom()
            );
            return insets;
        });
    }
}

