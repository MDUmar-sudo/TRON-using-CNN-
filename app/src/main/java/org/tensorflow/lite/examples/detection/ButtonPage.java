package org.tensorflow.lite.examples.detection;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;


public class ButtonPage extends AppCompatActivity {

    ImageButton select, capture, live_detection;
    static final int  REQUEST_FILE = 100;
    static final int REQUEST_STORAGE = 101;
    static final int REQUEST_CAMERA = 102;
    public final String LOG_TAG = "ButtonPage";
    Uri uri_1,uri_2;
    File photoFile;
    ImageView Image;
    Bitmap bitmap;
    Uri fileProvider;
    private String imgPath = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_page);
        transparentStatusBarAndNavigation();

        select = findViewById(R.id.select_btn);
        capture = findViewById(R.id.capture_btn);
        live_detection = findViewById(R.id.lv_dct_btn);
        Image = findViewById(R.id.imgView);

        // Live detection functionality initiated
        live_detection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ButtonPage.this,DetectorActivity.class);
                startActivity(intent);



            }
        });


         //Camera functionality initiated
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //checks for camera permission
                if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED){
                    // if not then asks for permission
                    ActivityCompat.requestPermissions(ButtonPage.this,new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA);
                }else{
                    // if permission granted goes to capture image
                    captureImage();
                }


            }
        });



        // Image selection functionality initiated
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checks storage permission
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    // if not then asks for permission
                    ActivityCompat.requestPermissions(ButtonPage.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE);
                } else {
                    // if permission granted goes to select image
                    SelectImage();
                }
            }
        });


    }

    // Camera function
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_CAMERA);
    }





    // Gallery function
    private void SelectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int code;
        code = requestCode;
        switch (code){

            // Camera
            case 102: if( resultCode==RESULT_OK){
                if(data!=null){
                    Bitmap captured = (Bitmap) data.getExtras().get("data");
                    Intent capturedImage = new Intent(ButtonPage.this,SelectorPage.class);
                    capturedImage.putExtra("capture",captured);
                    startActivity(capturedImage);

                }

            }
                break;

            // Gallery
            case 100: if ( resultCode == RESULT_OK) {
                if (data != null) {
                    uri_1 = data.getData();
                    Intent sendImage = new Intent(this,SelectorPage.class);
                    sendImage.putExtra("image",uri_1.toString());
                    startActivity(sendImage);
                }
            }
                break;


            default:
                Toast.makeText(getApplicationContext(),
                        "Wrong Option Selected",
                        Toast.LENGTH_SHORT);
        }


    }

    // Function to make status bar transparent
    private void transparentStatusBarAndNavigation(){
        if (Build.VERSION.SDK_INT >= 19 || Build.VERSION.SDK_INT < 21) {

            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            );
        }
        //make fully transparent status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);


        }

    }
    private void setWindowFlag ( int i, boolean b){
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (b) {
            winParams.flags |= i;
        } else {
            winParams.flags &= ~i;
        }
        win.setAttributes(winParams);
    }
}
