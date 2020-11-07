package com.elvina.colorpickerwallpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout layout;
    LinearLayout layoutSolid, layoutGradient;
    int defaultColor;
    Button buttonColorPickerSolid, buttonSetWallpaper, buttonSwitchSolid, buttonSwitchGradient;
    Button buttonColorPickerGradientA, buttonColorPickerGradientB;
    ImageView imageViewSolid, imageViewGradientA, imageViewGradientB;

    Bitmap bitmapSolid = null;
    Bitmap bitmapGradientA = null;
    Bitmap bitmapGradientB = null;
    ColorDrawable colorDrawable;
    GradientDrawable gradientDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SET TOOLBAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setTitle("Colorpicker Wallpaper");

        // PREPARE VIEWS
        prepareViews();

        // CREATE BITMAP
        bitmapSolid = Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888);
        bitmapGradientA = Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888);
        bitmapGradientB = Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888);


        // SET BUTTONS
        setButtons();

        // TEST
//        System.out.println("Test-1: "+defaultColor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_app:
                Intent intentA = new Intent(this, AboutActivity.class);
                startActivityForResult(intentA, 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void prepareViews() {
        // PREPARE VIEWS
        layout = findViewById(R.id.layout);
        defaultColor = ContextCompat.getColor(MainActivity.this, R.color.white);
        buttonSwitchSolid = findViewById(R.id.button_solid);
        buttonSwitchGradient = findViewById(R.id.button_gradient);
        buttonSetWallpaper = findViewById(R.id.set_wallpaper);

        imageViewSolid = findViewById(R.id.image_solid);
        buttonColorPickerSolid = findViewById(R.id.open_colorpicker);

        imageViewGradientA = findViewById(R.id.image_gradient_a);
        imageViewGradientB = findViewById(R.id.image_gradient_b);
        buttonColorPickerGradientA = findViewById(R.id.open_gradient_colorpicker_a);
        buttonColorPickerGradientB = findViewById(R.id.open_gradient_colorpicker_b);

        // PREPARE CONTAINER LAYOUT
        layoutSolid = findViewById(R.id.solid_layout);
        layoutGradient = findViewById(R.id.gradient_layout);
    }

    private void setButtons() {

        buttonSwitchSolid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSwitchSolid.setBackgroundResource(R.drawable.button_active_background);
                buttonSwitchGradient.setBackgroundResource(R.drawable.button_background);
                layoutSolid.setVisibility(LinearLayout.VISIBLE);
                layoutGradient.setVisibility(LinearLayout.GONE);
            }
        });
        buttonSwitchGradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSwitchSolid.setBackgroundResource(R.drawable.button_background);
                buttonSwitchGradient.setBackgroundResource(R.drawable.button_active_background);
                layoutSolid.setVisibility(LinearLayout.GONE);
                layoutGradient.setVisibility(LinearLayout.VISIBLE);
            }
        });

        buttonColorPickerSolid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker("solid");
            }
        });

        buttonColorPickerGradientA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker("gradient_a");
            }
        });

        buttonColorPickerGradientB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker("gradient_b");
            }
        });


        buttonSetWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWallpaper();
            }
        });

    }

    private void openColorPicker(String type) {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor = color;
                if (type.equals("solid")) {
                    setColorDrawable();
                    createBitmapSolid();
                    imageViewSolid.setImageBitmap(bitmapSolid);
                } else if (type.equals("gradient_a")) {
                    setGradientDrawable();
                    setBitmapGradient("gradient_a");
                    imageViewGradientA.setImageBitmap(bitmapGradientA);
                } else if (type.equals("gradient_b")) {
                    setGradientDrawable();
                    setBitmapGradient("gradient_b");
                    imageViewGradientB.setImageBitmap(bitmapGradientB);
                }

//                setGradientDrawable();
//                setBitmapGradient();

            }
        });
        colorPicker.show();
    }


    public void setColorDrawable() {
        colorDrawable = new ColorDrawable(defaultColor);
    }

    public void createBitmapSolid() {
        Canvas canvas = new Canvas(bitmapSolid);
        colorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        colorDrawable.draw(canvas);
    }

    public void setGradientDrawable() {
//        int colors[] = { 0xff255779 , 0xff3e7492, 0xffa6c0cd };
        int colors[] = {0xff255779, 0xFFABDFFF};

        gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
    }

    public void setBitmapGradient(String type) {
        Canvas canvas = new Canvas(bitmapGradientA);
        if (type.equals("gradient_b")) {
            canvas = new Canvas(bitmapGradientB);
        }
        gradientDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        gradientDrawable.draw(canvas);
    }


    public void setWallpaper() {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            wallpaperManager.setBitmap(bitmapSolid);
            Toast.makeText(this, "Done!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error.", Toast.LENGTH_LONG).show();
        }
    }

}