package com.example.camera_demo;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import static com.otaliastudios.cameraview.controls.Flash.AUTO;
import static com.otaliastudios.cameraview.controls.Flash.OFF;
import static com.otaliastudios.cameraview.controls.Flash.TORCH;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBar;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.VideoResult;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Mode;


import java.io.File;
import java.io.FileOutputStream;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ImageView flipCamera, flashCameraButton, captureImage, captureVideo;

    CameraView cameraView;
    private boolean isOpen = false;
    private boolean useOrientationListener = false;
    VerticalSeekBar brightness, zoom_seekbar;
    Button video, photo;
    Button creation;
    Handler timerUpdateHandler;
    TextView time_txt;
    private static final int REQUEST_BLUETOOTH_PERMISSION_NEW = 100;
    String[] OLD_PERMISSION = new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, RECORD_AUDIO};
    long min = 00;
    long sec = 00;
    Runnable timerUpdateRunnable = new Runnable() {

        @Override
        public void run() {
            if (sec == 60) {
                min = min + 1;
                sec = 0;
            } else {
                sec = sec + 1;
            }
            time_txt.setText(min + ":" + sec);
            timerUpdateHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        cameraView = findViewById(R.id.camera_view);
        flipCamera = findViewById(R.id.flipCamera);
        flashCameraButton = findViewById(R.id.flash);
        captureImage = findViewById(R.id.captureImage);
        brightness = findViewById(R.id.brightness);
        zoom_seekbar = findViewById(R.id.zoom);
        video = findViewById(R.id.video);
        photo = findViewById(R.id.photo);
        creation = findViewById(R.id.creation);
        captureVideo = findViewById(R.id.captureVideo);

        time_txt = findViewById(R.id.time);
        timerUpdateHandler = new Handler(Looper.getMainLooper());


        requestPermissionsNew();

        creation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CreationActivity.class);
                startActivity(intent);
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                captureImage.setBackgroundResource(R.drawable.play);
                cameraView.setMode(Mode.VIDEO);
                captureImage.setVisibility(View.GONE);
                captureVideo.setVisibility(View.VISIBLE);
                time_txt.setVisibility(View.VISIBLE);
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraView.setMode(Mode.PICTURE);
                captureVideo.setVisibility(View.GONE);
                captureImage.setVisibility(View.VISIBLE);
                time_txt.setVisibility(View.GONE);
            }
        });

        captureVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (cameraView.isTakingVideo()) {
                    cameraView.stopVideo();
                    flipCamera.setVisibility(View.VISIBLE);
                    timerUpdateHandler.removeCallbacks(timerUpdateRunnable);
                    min = 0;
                    sec = 0;
                    time_txt.setText(sec + ":" + min);
                    captureVideo.setImageResource(R.drawable.play);

                    Log.d("TAG1010", "isTakingVideo");
                } else {
                    Log.d("TAG1010", "ELSE");
                    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + getResources().getString(R.string.app_name) + "/" + "Videos";
                    File videofile = null;
                    File videoFolder = new File(path);
                    videofile = new File(path, "Video_"
                            + System.currentTimeMillis()
                            + ".mp4");
                    boolean success = true;
                    if (!videoFolder.exists()) {
                        success = videoFolder.mkdirs();
                    }
                    cameraView.takeVideoSnapshot(videofile);
                    captureVideo.setImageResource(R.drawable.stop);
                    flipCamera.setVisibility(View.INVISIBLE);
                    timerUpdateHandler.postDelayed(timerUpdateRunnable, 1000);
                }
            }
        });

        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                screenBrightness(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureShutter() {

            }

            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                super.onPictureTaken(result);

                File imageFile;
                Bitmap loadedImage = null;
                loadedImage = BitmapFactory.decodeByteArray(result.getData(), 0, result.getData().length);

                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + getResources().getString(R.string.app_name) + "/" + "Photos";

                File folder = new File(path);

                imageFile = new File(path, "Image_"
                        + System.currentTimeMillis()
                        + ".jpg");
                boolean success = true;
                if (!folder.exists()) {
                    success = folder.mkdirs();
                }
                if (success) {

                    FileOutputStream fout = null;

                    try {
                        fout = new FileOutputStream(imageFile);
                        loadedImage.compress(Bitmap.CompressFormat.JPEG, 99, fout);
                        fout.flush();
                        fout.close();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    MediaScannerConnection.scanFile(MainActivity.this,
                            new String[]{imageFile.toString()}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> uri=" + uri);
                                }
                            });

                    Toast.makeText(getBaseContext(), "Image  saved",
                            Toast.LENGTH_SHORT).show();


                    Intent i = new Intent(MainActivity.this, ImageShowActivity.class);
                    i.putExtra("imageFile", imageFile.getAbsolutePath());
                    startActivity(i);
                } else {
                    Toast.makeText(getBaseContext(), "Image Not saved", Toast.LENGTH_SHORT).show();
                    return;
                }


            }

            @Override
            public void onVideoTaken(@NonNull VideoResult result) {
                File file = result.getFile();
                MediaScannerConnection.scanFile(MainActivity.this,
                        new String[]{file.toString()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> uri=" + uri);
                            }
                        });
            }

            @Override
            public void onVideoRecordingStart() {

            }

            @Override
            public void onVideoRecordingEnd() {
                // Notifies that the actual video recording has ended.
                // Can be used to remove UI indicators added in onVideoRecordingStart.


            }
        });
        zoom_seekbar.setMax(100);
        zoom_seekbar.setProgress(0);
        zoom_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                float newprogress = progress / 100f;
                cameraView.setZoom(newprogress);
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        flashCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(flashCameraButton.isEnabled()){
                    cameraView.setFlash(TORCH);
                }else  {
                    cameraView.setFlash(OFF);
                }*/
                if (cameraView.getFlash() == TORCH) {
                    cameraView.setFlash(OFF);
                    flashCameraButton.setBackgroundResource(R.drawable.baseline_flash_on_24);

                } else {
                    cameraView.setFlash(TORCH);
                    flashCameraButton.setBackgroundResource(R.drawable.baseline_flash_off_24);
                }
            }
        });
        flipCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cameraView.getFacing() == Facing.BACK) {
                    cameraView.setFacing(Facing.FRONT);
                } else {
                    cameraView.setFacing(Facing.BACK);
                }

            }
        });
        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                cameraView.setMode(Mode.PICTURE);
                cameraView.takePicture();
                cameraView.setFlash(AUTO);

            }
        });
    }


    private void screenBrightness(double newBrightnessValue) {
        /*
         * WindowManager.LayoutParams settings = getWindow().getAttributes();
         * settings.screenBrightness = newBrightnessValue;
         * getWindow().setAttributes(settings);
         */
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        float newBrightness = (float) newBrightnessValue;
        lp.screenBrightness = newBrightness / (float) 255;
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.close();
    }

    public static boolean setAutoFocus(Camera.Parameters parameters) {
        // best for taking pictures, API >= ICE_CREAM_SANDWICH
        String continuousPicture =
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE;
        // less aggressive than CONTINUOUS_PICTURE, API >= GINGERBREAD
        String continuousVideo =
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO;
        // last resort
        String autoFocus = Camera.Parameters.FOCUS_MODE_AUTO;

        // prefer feature detection instead of checking BUILD.VERSION
        List<String> focusModes = parameters.getSupportedFocusModes();

        if (focusModes.contains(continuousPicture)) {
            parameters.setFocusMode(continuousPicture);
        } else if (focusModes.contains(continuousVideo)) {
            parameters.setFocusMode(continuousVideo);
        } else if (focusModes.contains(autoFocus)) {
            parameters.setFocusMode(autoFocus);
        } else {
            return false;
        }

        return true;
    }

    private boolean checkPermission() {
        // in this method we are checking if the permissions are granted or not and returning the result.
        for (String s : OLD_PERMISSION) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, s) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissionsNew() {
        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

        } else {
            requestPermissionBluetooth(OLD_PERMISSION, REQUEST_BLUETOOTH_PERMISSION_NEW);
        }
    }

    private void requestPermissionBluetooth(String[] PERMISSIONS, int requestCode) {
        ActivityCompat.requestPermissions(this, PERMISSIONS, requestCode);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d("TAG", "onRequestPermissionsResult: " + requestCode);


        switch (requestCode) {
            case REQUEST_BLUETOOTH_PERMISSION_NEW:
                if (grantResults.length > 0 && checkPermission()) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permissions denied, Permissions are required to use the app..", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }


}