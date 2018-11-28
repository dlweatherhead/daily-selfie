package project.coursera.dailyselfie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class SelfieImage implements Parcelable{

    private String mDate;
    private Bitmap mBitmap;
    private String mText;
    private String fullPath;

    public SelfieImage(String date, Bitmap selfie, String mText, String path) {
        this.mDate = date;
        this.mBitmap = selfie;
        this.mText = mText;
        this.fullPath = path;
    }

    private SelfieImage(Parcel in) {
        List<String> list = new ArrayList<String>();
        in.readStringList(list);

        mDate = list.get(0);
        mText = list.get(1);
        fullPath = list.get(2);
        mBitmap = BitmapFactory.decodeFile(fullPath);
    }

    public String getDate() { return mDate; }
    
    public void setDate(String date) { this.mDate = date; }

    public Bitmap getBitmap() { return mBitmap; }

    public void setBitmap(Bitmap selfie) { mBitmap = selfie; }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        List<String> list = new ArrayList<String>();
        list.add(0, mDate);
        list.add(1, mText);
        list.add(2, fullPath);
        dest.writeStringList(list);
    }

    public static final Parcelable.Creator<SelfieImage> CREATOR
            = new Parcelable.Creator<SelfieImage>() {
        public SelfieImage createFromParcel(Parcel in) {
            return new SelfieImage(in);
        }

        public SelfieImage[] newArray(int size) {
            return new SelfieImage[size];
        }
    };
}