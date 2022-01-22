package org.tensorflow.lite.examples.detection;


import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;


public class SelectorPage extends AppCompatActivity{


    ImageView Image;
    Bitmap captured;
    Uri uri_1;
    Bitmap bitmap;
    File photoFile;
    private ObjectDetector objectDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector_page);
        transparentStatusBarAndNavigation();
        // Multiple object detection in static images
        ObjectDetectorOptions options =
                new ObjectDetectorOptions.Builder()
                        .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
                        .enableMultipleObjects()
                        .enableClassification()
                        .build();

        objectDetector = ObjectDetection.getClient(options);

        Image = findViewById(R.id.imgView);

        // Shows image in the image view from camera or image selector
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            /*Two url doesn't seem to be working*/
            String imagePath =bundle.getString("capture");

             captured = BitmapFactory.decodeFile(imagePath);
            //captured = bundle.getParcelable("capture");
            uri_1 = Uri.parse(bundle.getString("image"));
            if(uri_1!=null){
                try {
                    bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri_1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Image.setImageBitmap(bitmap);
                runDetection(bitmap);

            }else{
                    Image.setImageBitmap(captured);

            }
        }

    }

    private void rotateIfRequired(Bitmap bitmap) {
        try {

            ExifInterface exifInterface = new ExifInterface(photoFile.getAbsolutePath());
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED
            );

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                rotateImage(bitmap, 90f);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                rotateImage(bitmap, 180f);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                rotateImage(bitmap, 270f);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(
                source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true
        );
    }





    protected void runDetection(Bitmap bitmap) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        // Task failed with an exception
        // ...
        objectDetector.process(image)
                .addOnSuccessListener(
                        detectedObjects -> {
                            // Task completed successfully
                            StringBuilder sb = new StringBuilder();
                            List<BoxWithText> list = new ArrayList<>();
                            for (DetectedObject object : detectedObjects) {
                                for (DetectedObject.Label label : object.getLabels()) {
                                    sb.append(label.getText()).append(" : ")
                                            .append(label.getConfidence()).append("\n");
                                }
                                if (!object.getLabels().isEmpty()) {
                                    list.add(new BoxWithText(object.getLabels().get(0).getText(), object.getBoundingBox()));
                                }
                            }
                            getInputImageView().setImageBitmap(drawDetectionResult(bitmap, list));
                        })
                .addOnFailureListener(
                        Throwable::printStackTrace);
    }

    protected ImageView getInputImageView() {
        return Image;
    }

    protected Bitmap drawDetectionResult(
            Bitmap bitmap,
            List<BoxWithText> detectionResults
    ) {
        Bitmap outputBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(outputBitmap);
        Paint pen = new Paint();
        pen.setTextAlign(Paint.Align.LEFT);

        for (BoxWithText box : detectionResults) {
            // draw bounding box

            pen.setColor(Color.BLUE);
            pen.setStrokeWidth(8F);
            pen.setStyle(Paint.Style.STROKE);
            final RectF trackedPos = new RectF(box.rect);
            float cornerSize = Math.min(trackedPos.width(), trackedPos.height()) / 8.0f;
            canvas.drawRoundRect(trackedPos, cornerSize, cornerSize, pen);

            Rect tagSize = new Rect(0, 0, 0, 0);

            // calculate the right font size
            pen.setStyle(Paint.Style.FILL_AND_STROKE);
            pen.setColor(Color.GRAY);
            pen.setStrokeWidth(2F);

            pen.setTextSize(66F);
            pen.getTextBounds(box.text, 0, box.text.length(), tagSize);
            float fontSize = pen.getTextSize() * box.rect.width() / tagSize.width();

            // adjust the font size so texts are inside the bounding box
            if (fontSize < pen.getTextSize()) pen.setTextSize(fontSize);

            float margin = (box.rect.width() - tagSize.width()) / 2.0F;
            if (margin < 0F) margin = 0F;
            canvas.drawText(
                    box.text, box.rect.left + margin,
                    box.rect.top + tagSize.height(), pen
            );
        }
        return outputBitmap;
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