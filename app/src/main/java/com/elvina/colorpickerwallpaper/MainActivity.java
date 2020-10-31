package com.elvina.colorpickerwallpaper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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
    ColorDrawable cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SET TOOLBAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Colorpicker Wallpaper");

        // PREPARE VIEWS
        layout = findViewById(R.id.layout);
        defaultColor = ContextCompat.getColor(MainActivity.this, R.color.design_default_color_primary);
        imageView = findViewById(R.id.image);
        buttonColorPicker = findViewById(R.id.open_colorpicker);
        buttonSetWallpaper = findViewById(R.id.set_wallpaper);

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
    }

    public void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor = color;
//                layout.setBackgroundColor(defaultColor);
                setColorDrawable();
                setbitmap();
                imageView.setImageBitmap(bitmap);
            }
        });
        colorPicker.show();
    }

    public void setbitmap() {
//        bitmap = Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        cd.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        cd.draw(canvas);
    }

    public void setColorDrawable() {
        cd = new ColorDrawable(defaultColor);
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
}