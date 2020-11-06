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
    int defaultColor;
    Button buttonColorPicker;
    Button buttonSetWallpaper;
    ImageView imageView;

    Bitmap bitmap = null;
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
        layout = findViewById(R.id.layout);
        defaultColor = ContextCompat.getColor(MainActivity.this, R.color.white);
        imageView = findViewById(R.id.image);
        buttonColorPicker = findViewById(R.id.open_colorpicker);
        buttonSetWallpaper = findViewById(R.id.set_wallpaper);

        // CREATE BITMAP
        bitmap = Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888);

        // SET BUTTONS
        buttonColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });
        buttonSetWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWallpaper();
            }
        });

        // TEST
//        System.out.println("Test-1: "+defaultColor);
    }

    public void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor = color;
//                setColorDrawable();
//                setBitmapSolid();
                setGradientDrawable();
                setBitmapGradient();
                imageView.setImageBitmap(bitmap);
            }
        });
        colorPicker.show();
    }

    public void setBitmapSolid() {
        Canvas canvas = new Canvas(bitmap);
        colorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        colorDrawable.draw(canvas);
    }

    public void setBitmapGradient(){
        Canvas canvas = new Canvas(bitmap);
        gradientDrawable.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
        gradientDrawable.draw(canvas);
    }

    public void setColorDrawable() {
        colorDrawable = new ColorDrawable(defaultColor);
    }

    public void setGradientDrawable(){
//        int colors[] = { 0xff255779 , 0xff3e7492, 0xffa6c0cd };
        int colors[] = { 0xff255779 , 0xFFABDFFF };

        gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
    }

    public void setWallpaper() {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            wallpaperManager.setBitmap(bitmap);
            Toast.makeText(this, "Done!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error.", Toast.LENGTH_LONG).show();
        }
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
}