package project.coursera.dailyselfie;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {

    private ArrayList<SelfieImage> list;
    private static LayoutInflater inflater = null;
    private Context mContext;

    public ImageAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        list = new ArrayList<>();
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View newView = convertView;
        SelfieHolder holder;

        SelfieImage curr = list.get(position);

        if (null == convertView) {
            holder = new SelfieHolder();
            newView = inflater.inflate(R.layout.selfie_list_item, parent, false);
            holder.selfieImage = (ImageView) newView.findViewById(R.id.selfie_list_item_image);
            holder.selfieDate = (TextView) newView.findViewById(R.id.selfie_list_item_date);
            holder.selfieText = (TextView) newView.findViewById(R.id.selfie_list_item_text);
            newView.setTag(holder);

        } else {
            holder = (SelfieHolder) newView.getTag();
        }

        holder.selfieImage.setImageBitmap(curr.getBitmap());
        holder.selfieDate.setText("");
        holder.selfieText.setText("\n" + curr.getmText());

        return newView;
    }

    static class SelfieHolder {
        ImageView selfieImage;
        TextView selfieDate;
        TextView selfieText;
    }

    public void add(SelfieImage listItem) {
        list.add(listItem);
        notifyDataSetChanged();
    }

    public ArrayList<SelfieImage> getList() {
        return list;
    }

    public void removeAll() {
        list = new ArrayList<>();
        this.notifyDataSetChanged();
    }
}

