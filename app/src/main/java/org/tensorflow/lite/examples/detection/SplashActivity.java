package org.tensorflow.lite.examples.detection;

import static android.os.Build.VERSION.SDK_INT;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.view.Window;

import android.view.WindowManager;


public class SplashActivity extends AppCompatActivity {


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        transparentStatusBarAndNavigation();

        Thread splashTread = new Thread(){


            @Override

            public void run() {

                try {

                    sleep(2000);

                    startActivity(new Intent(getApplicationContext(),MainActivity.class));

                    finish();

                } catch (InterruptedException e) {

                    e.printStackTrace();

                }


                super.run();

            }

        };


        splashTread.start();





    }
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
