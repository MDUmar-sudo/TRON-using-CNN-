package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton about,start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        transparentStatusBarAndNavigation();

        about = findViewById(R.id.about_btn);
        start = findViewById(R.id.start_btn);

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Instance for opening about page when about btn is clicked
                Intent intent = new Intent(MainActivity.this,AboutPage.class);
                startActivity(intent);
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Instance for opening button page
                Intent intent = new Intent(MainActivity.this,ButtonPage.class);
                startActivity(intent);

            }
        });

    }



    // Function to make status bar transparent
    private void transparentStatusBarAndNavigation(){
        if(Build.VERSION.SDK_INT>=19 || Build.VERSION.SDK_INT<21) {

            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }
        if(Build.VERSION.SDK_INT>=19){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View. SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            );
        }
        //make fully transparent status bar
        if(Build.VERSION.SDK_INT>=21){
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);


        }

    }
    private  void setWindowFlag(int i, boolean b){
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (b){
            winParams.flags |= i;
        }else{
            winParams.flags &= ~i;
        }
        win.setAttributes(winParams);
    }

}