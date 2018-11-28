package project.coursera.dailyselfie;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * This activity is for viewing our selfie when we tap on it.
 */
public class SelfieViewerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selfie_viewer);
        ImageView imageView = (ImageView) findViewById(R.id.selfie_viewer_image);

        byte[] imageByteArray = new byte[]{0x00};

        String imagePath = getIntent().getStringExtra("imagePath");
        Bitmap imageBmp = BitmapFactory.decodeFile(imagePath);
        imageBmp = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
        imageView.setImageBitmap(imageBmp);
    }

}
