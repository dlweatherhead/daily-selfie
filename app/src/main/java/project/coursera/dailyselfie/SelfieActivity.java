package project.coursera.dailyselfie;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SelfieActivity extends ListActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final File PICS_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/Daily_Selfies");
    private static final long ALARM_TIMER = 2 * 60 * 1000; // 2 minutes
    private final String TAG = "SelfieActivity";
    private ImageAdapter mAdapter;
    private SelfieImage tempSelfie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_selfie);

        mAdapter = new ImageAdapter(getApplicationContext());
        setListAdapter(mAdapter);
        loadExistingSelfies();

        // Setup Selfie Reminder Alarm
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent mNotificationReceiverIntent = new Intent(SelfieActivity.this, SelfieReceiver.class);
        PendingIntent mNotificationPendingReceiveIntent = PendingIntent.getBroadcast(SelfieActivity.this, 0, mNotificationReceiverIntent, 0);
        alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + ALARM_TIMER,
                ALARM_TIMER,
                mNotificationPendingReceiveIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_selfie, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete_all) {
            Log.d(TAG, "Deleting all selfies.");
            deleteAllSelfies();
            return true;
        }
        else if(id == R.id.action_camera) {
            Log.d(TAG, "Camera Activated.");
            dispatchTakePictureIntent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bitmap capturedImage;

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            capturedImage = BitmapFactory.decodeFile(tempSelfie.getFullPath(), bmOptions);
            //capturedImage = Bitmap.createScaledBitmap(capturedImage, 100, 100, true);

            tempSelfie.setBitmap(capturedImage);
            mAdapter.add(tempSelfie);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        SelfieImage image = (SelfieImage) mAdapter.getItem(position);

        Intent intent = new Intent(SelfieActivity.this, SelfieViewerActivity.class);
        intent.putExtra("imagePath", image.getFullPath());
        startActivity(intent);
    }

    private void loadExistingSelfies() {
        File[] filesArray = PICS_DIR.listFiles();
        if(filesArray != null && filesArray.length > 0) {
            for (File file : filesArray) {

                String fullPath = file.getAbsolutePath();
                Bitmap bmp = BitmapFactory.decodeFile(fullPath);
                String dateTime = null;

                try {
                    ExifInterface exif = new ExifInterface(fullPath);
                    dateTime = exif.getAttribute(ExifInterface.TAG_DATETIME);
                } catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                }

                SelfieImage image = new SelfieImage(dateTime, bmp, file.getName().split("&")[0], fullPath);
                mAdapter.add(image);
            }
        }
    }

    private void deleteAllSelfies() {
        File[] filesArray = PICS_DIR.listFiles();
        mAdapter.removeAll();
        tempSelfie = null;
        if(filesArray != null && filesArray.length > 0) {
            for (File file : filesArray) {
                file.delete();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = createImageFile();
            if(photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "&";

        PICS_DIR.mkdirs();

        File image = null;
        try {
            image = File.createTempFile(imageFileName,
                                        ".jpg",
                                        PICS_DIR);
        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        if(image == null) return null;
        try {
            ExifInterface exif = new ExifInterface(image.getAbsolutePath());
            exif.setAttribute(ExifInterface.TAG_DATETIME, timeStamp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        tempSelfie = new SelfieImage(timeStamp, null, image.getName().split("&")[0], image.getAbsolutePath());
        return image;
    }

}
