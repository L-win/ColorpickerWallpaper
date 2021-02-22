package com.elvina.colorpickerwallpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout layout;
    LinearLayout layoutSolid, layoutGradient;
    int defaultColorSolid, defaultColorGradientA, defaultColorGradientB;
    Button buttonColorPickerSolid, buttonSetWallpaper, buttonSwitchSolid, buttonSwitchGradient, buttonSaveImage;
    Button buttonColorPickerGradientA, buttonColorPickerGradientB;

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
        bitmapSolid = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        bitmapGradientA = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        bitmapGradientB = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        bitmapGradientFinish = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);

        // SET BUTTONS
        setButtons();

        // TEST
        System.out.println("TEST-0: --- Running App ----");

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
        buttonSaveImage = findViewById(R.id.save_image);

        buttonColorPickerSolid = findViewById(R.id.open_colorpicker);

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
                buttonSwitchSolid.setText(R.string.buttonSwitchSolidBold);
                buttonSwitchGradient.setBackgroundResource(R.drawable.button_switch_right_off);
                buttonSwitchGradient.setText(R.string.buttonSwitchGradientNormal);

                layoutSolid.setVisibility(LinearLayout.VISIBLE);
                layoutGradient.setVisibility(LinearLayout.GONE);
                layoutSwitchState = "solid";
            }
        });
        buttonSwitchGradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSwitchSolid.setBackgroundResource(R.drawable.button_switch_left_off);
                buttonSwitchSolid.setText(R.string.buttonSwitchSolidNormal);
                buttonSwitchGradient.setBackgroundResource(R.drawable.button_switch_right_on);
                buttonSwitchGradient.setText(R.string.buttonSwitchGradientBold);

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

        buttonSaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImageNew();
            }
        });

        buttonSetWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Set wallpaper?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                setWallpaper();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).create().show();
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
                    setAppBackground("solid");
                } else if (type.equals("gradient_a")) {
                    defaultColorGradientA = color;
                    colorDrawableGradientA = new ColorDrawable(color);
                    setBitmapGradient("gradient_a");
                    setGradientDrawableFinish();
                    // TODO: DOES NOT WORK SOMETIMES
                    createGradientBitmapFinish();
                    setAppBackground("gradient");
                } else if (type.equals("gradient_b")) {
                    defaultColorGradientB = color;
                    colorDrawableGradientB = new ColorDrawable(color);
                    setBitmapGradient("gradient_b");
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
                //TODO: SET LOCK SCREEN WALLPAPER
//                wallpaperManager.setBitmap(bitmapSolid,null,false, WallpaperManager.FLAG_LOCK);
            }else{
                wallpaperManager.setBitmap(bitmapGradientFinish);
            }
            Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setAppBackground(String type){
        if(type.equals("gradient")){
            layout.setBackground(gradientDrawable);
        }else if (type.equals("solid")){
            layout.setBackground(colorDrawableSolid);
        }
    }

    private void saveImageOld() {
        File filePath = Environment.getExternalStorageDirectory();
        File dir = new File(filePath.getAbsolutePath() + "/ColorpickerWallpaper/");

        dir.mkdir();

        File file = new File(dir, System.currentTimeMillis() + ".jpg");
        OutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
        } catch (Exception e) {
            System.out.println("TEST-0: Output exception: " + e);
        }

        if (layoutSwitchState.equals("solid")){
            bitmapSolid.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        }else{
            bitmapGradientFinish.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        }

        try {
            outputStream.flush();
        } catch (Exception e) {

        }
        try {
            outputStream.close();
        } catch (Exception e) {

        }
        Toast.makeText(this, "Saved to External Storage", Toast.LENGTH_SHORT).show();
    }

    private void saveImageNew(){

        OutputStream fos = null;
        boolean picturesDirExists = true;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // ANDROID 10 AND ABOVE
                ContentResolver resolver = getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis() + ".jpg");
                contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
                contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

                Uri url;
                Uri ImageUri = resolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                fos = resolver.openOutputStream(Objects.requireNonNull(ImageUri));
            } else {
                // ANDROID 9 AND BELOW
                if (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).exists()
                        && Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).isDirectory()){
                    System.out.println("TEST-0: Directory exists");
                }else {
                    System.out.println("TEST-0: Directory does not exists");
                    picturesDirExists = false;
                }

                String ImagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();

                File image = new File(ImagesDir, System.currentTimeMillis() + ".jpg");
                fos = new FileOutputStream(image);
            }


        } catch (Exception e) {
            System.out.println(e);
        }

        if (picturesDirExists){
            if (layoutSwitchState.equals("solid")){
                bitmapSolid.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            }else{
                bitmapGradientFinish.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            }
            Toast.makeText(this, "Saved to External Storage", Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(this, "Error! Pictures directory does not exist", Toast.LENGTH_LONG).show();
        }

    }
}