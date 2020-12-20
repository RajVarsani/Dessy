
package com.example.dessy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.transition.Transition;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        init();
    }

    private static final int REQUEST_PERMISSIONS = 111;
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int PERMISSIONS_COUNT = 2;

    private boolean notPermissions() {
        for (int i = 0; i < PERMISSIONS_COUNT; i++) {
            if (checkSelfPermission(PERMISSIONS[i]) != getPackageManager().PERMISSION_GRANTED)
                return true;
        }
        return false;
    }

    static {
        System.loadLibrary("dessy");
    }

//    public static native void BlackAndWhite (int[] pixals, int height, int width);

    public static native void blackAndWhite(int[] pixals, int height, int width, int hardnessOfFilter, int extarRed, int extraGreen, int extraBlue, int choiceOfFilter, int extraBrightnessToAdd);

    @Override
    protected void onResume() {
        super.onResume();
        if (notPermissions()) {
            requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS && grantResults.length > 0) {
            if (notPermissions()) {
                ((ActivityManager) this.getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData();
                recreate();
                return;
            }
        }
    }

    private int extraBrightnessToAdd = 0;

    private int stateOfApp = 0;

    private static final int REQUEST_PICK_IMAGE = 1111;

    private ImageView imageView;

    private static final int REQUEST_IMAGE_CAPTURE = 11111;

    private static final String appID = "photo editor";

    private Uri imageUri;

    private int valueOfHardnessOfFilter = 1;

    private int extarRed = 0;

    private int extraGreen = 0;

    private int extraBlue = 0;

    private int choiceOfFilter = 0;


    @Override
    public void onBackPressed() {
        if (stateOfApp == 1) {
            findViewById(R.id.popupone).setVisibility(View.GONE);
            stateOfApp = 0;
        } else if (stateOfApp == 2) {
            findViewById(R.id.editScreen).setVisibility(View.GONE);
            findViewById(R.id.welcomeScreen).setVisibility(View.VISIBLE);
            findViewById(R.id.popupone).setVisibility(View.VISIBLE);
            stateOfApp = 1;
        } else if (stateOfApp == 3) {
            findViewById(R.id.popuptwo).setVisibility(View.GONE);
            stateOfApp = 2;
        } else if (stateOfApp == 4) {
            findViewById(R.id.popupThree).setVisibility(View.GONE);
            stateOfApp = 2;
        } else if (stateOfApp == 5) {
            findViewById(R.id.aboutSec).setVisibility(View.GONE);
            findViewById(R.id.welcomeScreen).setVisibility(View.VISIBLE);
            stateOfApp=0;
        } else {
            super.onBackPressed();
        }
    }

    private void init() {


        imageView = findViewById(R.id.imageView);

        final ImageButton selectPhotoButton = findViewById(R.id.selectImagebtn);
        selectPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stateOfApp==0) {
                    stateOfApp = 1;
                    final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    final Intent pickIntent = new Intent(Intent.ACTION_PICK);
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    final Intent chooseIntent = Intent.createChooser(intent, "Select Image");
                    startActivityForResult(chooseIntent, REQUEST_PICK_IMAGE);
                }
            }
        });


        final ImageButton saveImageButton = findViewById(R.id.saveImage);
        saveImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            final File outFile = createImageFile();
                            try (FileOutputStream out = new FileOutputStream(outFile)) {
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                imageUri = Uri.parse("file://" + outFile.getAbsolutePath());
                                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri));
                                MediaScannerConnection.scanFile(MainActivity.this, new String[]{outFile.getPath()}, new String[]{"image/jpeg"}, null);
                                Toast.makeText(MainActivity.this, "The image was saved", Toast.LENGTH_SHORT).show();
                                findViewById(R.id.popuptwo).setVisibility(View.GONE);
                                findViewById(R.id.popupThree).setVisibility(View.VISIBLE);
                                stateOfApp = 4;
                            } catch (IOException e) {
                                Toast.makeText(MainActivity.this, "image was not saved the error was :" + e.getLocalizedMessage() + "please report", Toast.LENGTH_SHORT).show();


                            }
                        }
                    }
                };

                builder.setMessage("Save current photo to gallery ?").
                        setPositiveButton("yes", dialogClickListener).
                        setNegativeButton("no", dialogClickListener).show();

            }
        });

        final ImageButton openAboutSecButton = findViewById(R.id.aboutSecButtoSn);
        openAboutSecButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stateOfApp==0) {
                    stateOfApp = 5;
                    findViewById(R.id.welcomeScreen).setVisibility(View.GONE);
                    findViewById(R.id.aboutSec).setVisibility(View.VISIBLE);
                }
            }
        });

        final ImageButton backFromAboutSecButton = findViewById(R.id.backfromaboutsec);
        backFromAboutSecButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.aboutSec).setVisibility(View.GONE);
                findViewById(R.id.welcomeScreen).setVisibility(View.VISIBLE);
                stateOfApp=0;
            }
        });

        final ImageButton colouredProcessButton = findViewById(R.id.colouredProcessButton);
        colouredProcessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceOfFilter = 0;
                stateOfApp = 2;
                runChnages(bitmap, imageView, pixals, height, width, valueOfHardnessOfFilter, extarRed, extraGreen, extraBlue, choiceOfFilter, extraBrightnessToAdd);
                findViewById(R.id.popupone).setVisibility(View.GONE);
                findViewById(R.id.welcomeScreen).setVisibility(View.GONE);
                findViewById(R.id.editScreen).setVisibility(View.VISIBLE);
                findViewById(R.id.textOfExtraBrightness).setVisibility(View.GONE);
                findViewById(R.id.valueOfExtraBrightness).setVisibility(View.GONE);
                findViewById(R.id.valueOfExtraRed).setVisibility(View.VISIBLE);
                findViewById(R.id.valueOfExtraGreen).setVisibility(View.VISIBLE);
                findViewById(R.id.valueOfExtraBlue).setVisibility(View.VISIBLE);
                findViewById(R.id.textOfExtraRed).setVisibility(View.VISIBLE);
                findViewById(R.id.textOfExtraGreen).setVisibility(View.VISIBLE);
                findViewById(R.id.textOfExtrablue).setVisibility(View.VISIBLE);
            }
        });

        final ImageButton greyscaleProcessButton = findViewById(R.id.greyscaleProcessButton);
        greyscaleProcessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceOfFilter = 1;
                stateOfApp = 2;
                runChnages(bitmap, imageView, pixals, height, width, valueOfHardnessOfFilter, extarRed, extraGreen, extraBlue, choiceOfFilter, extraBrightnessToAdd);
                findViewById(R.id.popupone).setVisibility(View.GONE);
                findViewById(R.id.welcomeScreen).setVisibility(View.GONE);
                findViewById(R.id.editScreen).setVisibility(View.VISIBLE);
                findViewById(R.id.textOfExtraBrightness).setVisibility(View.VISIBLE);
                findViewById(R.id.valueOfExtraBrightness).setVisibility(View.VISIBLE);
                findViewById(R.id.valueOfExtraRed).setVisibility(View.GONE);
                findViewById(R.id.valueOfExtraGreen).setVisibility(View.GONE);
                findViewById(R.id.valueOfExtraBlue).setVisibility(View.GONE);
                findViewById(R.id.textOfExtraRed).setVisibility(View.GONE);
                findViewById(R.id.textOfExtraGreen).setVisibility(View.GONE);
                findViewById(R.id.textOfExtrablue).setVisibility(View.GONE);


            }
        });

        final SeekBar seekBarforHardnessOfFilter = findViewById(R.id.seekbarbtnStrngthoffilter);
        seekBarforHardnessOfFilter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (stateOfApp==2) {
                    if (progress != 0) {
                        valueOfHardnessOfFilter = progress;
                    } else {
                        valueOfHardnessOfFilter = 1;
                    }
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (stateOfApp == 2) {
                    Toast.makeText(MainActivity.this, "Hardness of filter:" + valueOfHardnessOfFilter,
                            Toast.LENGTH_SHORT).show();
                    runChnages(bitmap, imageView, pixals, height, width, valueOfHardnessOfFilter, extarRed, extraGreen, extraBlue, choiceOfFilter, extraBrightnessToAdd);
                }
            }
        });
        final SeekBar seekbarextraRed = findViewById(R.id.valueOfExtraRed);
        seekbarextraRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (stateOfApp==2) {
                    extarRed = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (stateOfApp == 2) {
                    Toast.makeText(MainActivity.this, "Extra red:" + extarRed,
                            Toast.LENGTH_SHORT).show();
                    runChnages(bitmap, imageView, pixals, height, width, valueOfHardnessOfFilter, extarRed, extraGreen, extraBlue, choiceOfFilter, extraBrightnessToAdd);
                }
            }
        });

        final SeekBar seekbarextraGreen = findViewById(R.id.valueOfExtraGreen);
        seekbarextraGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (stateOfApp==2) {
                    extraGreen = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (stateOfApp == 2) {
                    Toast.makeText(MainActivity.this, "Extra green:" + extraGreen,
                            Toast.LENGTH_SHORT).show();
                    runChnages(bitmap, imageView, pixals, height, width, valueOfHardnessOfFilter, extarRed, extraGreen, extraBlue, choiceOfFilter, extraBrightnessToAdd);
                }
            }
        });

        final SeekBar seekbarextraBlue = findViewById(R.id.valueOfExtraBlue);
        seekbarextraBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (stateOfApp==2) {
                    extraBlue = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (stateOfApp == 2) {
                    Toast.makeText(MainActivity.this, "Extra blue:" + extraBlue,
                            Toast.LENGTH_SHORT).show();
                    runChnages(bitmap, imageView, pixals, height, width, valueOfHardnessOfFilter, extarRed, extraGreen, extraBlue, choiceOfFilter, extraBrightnessToAdd);
                }
            }
        });

        final SeekBar seekbarextraBrightness = findViewById(R.id.valueOfExtraBrightness);
        seekbarextraBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {



            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               if (stateOfApp==2) {
                   extraBrightnessToAdd = progress;
               }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (stateOfApp == 2) {
                Toast.makeText(MainActivity.this, "Extra Brightness:" + extraBrightnessToAdd,
                        Toast.LENGTH_SHORT).show();

                    runChnages(bitmap, imageView, pixals, height, width, valueOfHardnessOfFilter, extarRed, extraGreen, extraBlue, choiceOfFilter, extraBrightnessToAdd);
                }
            }
        });

        final ImageButton backToChoiceButton = findViewById(R.id.cancelButtonToChoice);
        backToChoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (stateOfApp==2) {
                    findViewById(R.id.popupone).setVisibility(View.VISIBLE);
                    findViewById(R.id.welcomeScreen).setVisibility(View.VISIBLE);
                    findViewById(R.id.editScreen).setVisibility(View.GONE);
                    stateOfApp = 1;
                }else if (stateOfApp==3){
                    findViewById(R.id.popuptwo).setVisibility(View.GONE);
                    stateOfApp=2;
                }else if (stateOfApp==4){
                    findViewById(R.id.popupThree).setVisibility(View.GONE);
                    stateOfApp=2;
                }
            }
        });

        final ImageButton processDoneBtn = findViewById(R.id.processDoneBtn);
        processDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.popuptwo).setVisibility(View.VISIBLE);
                stateOfApp = 3;
            }
        });

        final ImageButton remindmeLater = findViewById(R.id.remindMeLater);
        remindmeLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.popupThree).setVisibility(View.GONE);
                stateOfApp=2;
            }
        });

        final ImageButton ratingButton = findViewById(R.id.goToRaitng);
        ratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.popupThree).setVisibility(View.GONE);
                stateOfApp=2;
            }
        });

    }


    private void runChnages(Bitmap bitmap, ImageView imageView, int[] pixals, int height, int width, int valueOfHardnessOfFilter, int extarRed, int extraGreen, int extraBlue, int choiceOfFilter, int extraBrightnessToAdd) {
        new Thread() {
            public void run() {

                final int[] cpyPixelArray = pixals.clone();

                blackAndWhite(cpyPixelArray, height, width, valueOfHardnessOfFilter, extarRed, extraGreen, extraBlue, choiceOfFilter,extraBrightnessToAdd);

                bitmap.setPixels(cpyPixelArray, 0, width, 0, 0, width, height);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });

            }
        }.start();
    }

    private File createImageFile() {
        @SuppressLint("SimpleDateFormat") final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        final String imageFileName = "/jPEG" + timeStamp + ".jpg";
        final File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(storageDir + imageFileName);
    }


    private Bitmap bitmap;
    private int height = 0;
    private int width = 0;
    public static final int MAX_PIXEL_COUNT = 2048;

    private int[] pixals;
    private int pixelcount = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE) {
            if (data != null) {
                imageUri = data.getData();
            }
        }

        final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "Loding", "Please wait", true);
//        edtmod = true;
//        findViewById(R.id.welcomeScreen).setVisibility(View.GONE);
        findViewById(R.id.popupone).setVisibility(View.VISIBLE);
//        findViewById(R.id.aboutSec).setVisibility(View.GONE);
//        findViewById(R.id.editScreen).setVisibility(View.VISIBLE);

        new Thread() {
            public void run() {
                bitmap = null;
                final BitmapFactory.Options bmpOtions = new BitmapFactory.Options();
                bmpOtions.inBitmap = bitmap;
                try (InputStream input = getContentResolver().openInputStream(imageUri)) {

                    bitmap = BitmapFactory.decodeStream(input, null, bmpOtions);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bmpOtions.inJustDecodeBounds = false;
                width = bmpOtions.outWidth;
                height = bmpOtions.outHeight;
                int resizeScale = 1;
                if (width > MAX_PIXEL_COUNT) {
                    resizeScale = width / MAX_PIXEL_COUNT;
                } else if (height > MAX_PIXEL_COUNT) {
                    resizeScale = height / MAX_PIXEL_COUNT;
                }
                if (width / resizeScale > MAX_PIXEL_COUNT || height / resizeScale > MAX_PIXEL_COUNT) {
                    resizeScale++;
                }
                bmpOtions.inSampleSize = resizeScale;
                InputStream input = null;
                try {
                    input = getContentResolver().openInputStream(imageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    recreate();
                    return;
                }
                bitmap = BitmapFactory.decodeStream(input, null, bmpOtions);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                        dialog.cancel();

                    }
                });

                width = bitmap.getWidth();
                height = bitmap.getHeight();
                bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

                pixelcount = width * height;
                pixals = new int[pixelcount];
                bitmap.getPixels(pixals, 0, width, 0, 0, width, height);

            }
        }.start();

    }
}


