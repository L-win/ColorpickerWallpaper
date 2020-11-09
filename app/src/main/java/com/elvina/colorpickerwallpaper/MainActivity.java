package com.elvina.colorpickerwallpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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
    int defaultColorSolid, defaultColorGradientA, defaultColorGradientB;
    Button buttonColorPickerSolid, buttonSetWallpaper, buttonSwitchSolid, buttonSwitchGradient;
    Button buttonColorPickerGradientA, buttonColorPickerGradientB;
    ImageView imageViewSolid, imageViewGradientA, imageViewGradientB;

    Bitmap bitmapSolid = null;
    Bitmap bitmapGradientA = null;
    Bitmap bitmapGradientB = null;
    Bitmap bitmapGradientFinish = null;
    ColorDrawable colorDrawableSolid, colorDrawableGradientA, colorDrawableGradientB;
    GradientDrawable gradientDrawable;

    String layoutSwitchState = "solid";

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
        bitmapGradientFinish = Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888);

        // SET BUTTONS
        setButtons();

        // TEST
//        System.out.println("Test-1: "+defaultColorSolid);
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
        defaultColorSolid = ContextCompat.getColor(MainActivity.this, R.color.white);
        buttonSwitchSolid = findViewById(R.id.button_solid);
        buttonSwitchGradient = findViewById(R.id.button_gradient);
        buttonSetWallpaper = findViewById(R.id.set_wallpaper);

//        imageViewSolid = findViewById(R.id.image_solid);
        buttonColorPickerSolid = findViewById(R.id.open_colorpicker);

//        imageViewGradientA = findViewById(R.id.image_gradient_a);
//        imageViewGradientB = findViewById(R.id.image_gradient_b);
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
                buttonSwitchSolid.setBackgroundResource(R.drawable.button_switch_left_on);
                buttonSwitchGradient.setBackgroundResource(R.drawable.button_switch_right_off);
                layoutSolid.setVisibility(LinearLayout.VISIBLE);
                layoutGradient.setVisibility(LinearLayout.GONE);
                layoutSwitchState = "solid";
            }
        });
        buttonSwitchGradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSwitchSolid.setBackgroundResource(R.drawable.button_switch_left_off);
                buttonSwitchGradient.setBackgroundResource(R.drawable.button_switch_right_on);
                layoutSolid.setVisibility(LinearLayout.GONE);
                layoutGradient.setVisibility(LinearLayout.VISIBLE);
                layoutSwitchState = "gradient";
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
//                setGradientDrawableFinish();
//                createGradientBitmapFinish();
                setWallpaper();
//                setAppBackground();
            }
        });
    }

    private void openColorPicker(String type) {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, defaultColorSolid, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                if (type.equals("solid")) {
                    colorDrawableSolid = new ColorDrawable(color);
                    createBitmapSolid();
//                    imageViewSolid.setImageBitmap(bitmapSolid);
                    setAppBackground("solid");
                } else if (type.equals("gradient_a")) {
                    defaultColorGradientA = color;
                    colorDrawableGradientA = new ColorDrawable(color);
                    setBitmapGradient("gradient_a");
//                    imageViewGradientA.setImageBitmap(bitmapGradientA);
                    setGradientDrawableFinish();
                    setAppBackground("gradient");
                } else if (type.equals("gradient_b")) {
                    defaultColorGradientB = color;
                    colorDrawableGradientB = new ColorDrawable(color);
                    setBitmapGradient("gradient_b");
//                    imageViewGradientB.setImageBitmap(bitmapGradientB);
                    setGradientDrawableFinish();
                    createGradientBitmapFinish();
                    setAppBackground("gradient");
                }
            }
        });
        colorPicker.show();

    }

    private void createBitmapSolid() {
        Canvas canvas = new Canvas(bitmapSolid);
        colorDrawableSolid.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        colorDrawableSolid.draw(canvas);
    }

    private void setBitmapGradient(String type) {

        if (type.equals("gradient_a")) {
            Canvas canvas = new Canvas(bitmapGradientA);
            colorDrawableGradientA.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            colorDrawableGradientA.draw(canvas);
        } else if (type.equals("gradient_b")) {
            Canvas canvas = new Canvas(bitmapGradientB);
            colorDrawableGradientB.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            colorDrawableGradientB.draw(canvas);
        }
    }

    private void setGradientDrawableFinish() {
        // int colors[] = { 0xff255779 , 0xff3e7492, 0xffa6c0cd };
        int colors[] = {defaultColorGradientA, defaultColorGradientB};
        gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
    }

    private void createGradientBitmapFinish() {
        Canvas canvas = new Canvas(bitmapGradientFinish);
        gradientDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        gradientDrawable.draw(canvas);
    }

    private void setWallpaper() {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            if (layoutSwitchState.equals("solid")){
                wallpaperManager.setBitmap(bitmapSolid);
            }else{
                wallpaperManager.setBitmap(bitmapGradientFinish);
            }
            Toast.makeText(this, "Done!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error.", Toast.LENGTH_LONG).show();
        }
    }

    private void setAppBackground(String type){
        if(type.equals("gradient")){
            layout.setBackground(gradientDrawable);
        }else if (type.equals("solid")){
            layout.setBackground(colorDrawableSolid);
        }

    }
}