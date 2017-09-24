package rebeccaansems.diabetestracker;

/**
 * Created by SriramHariharan on 9/23/17.
 */


import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by SriramHariharan on 3/11/16.
 */
public class SearchAdapter extends ArrayAdapter<Food> {

    private final Context mContext;
    private final ArrayList<Food> mItem;

    public SearchAdapter(Context context, ArrayList<Food> itemsArrayList) {
        super(context, R.layout.food_row, itemsArrayList);
        this.mContext = context;
        this.mItem = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.food_row, parent, false);
        TextView mFoodName = (TextView) v.findViewById(R.id.food_name);
        TextView mBrand = (TextView) v.findViewById(R.id.food_brand);
        TextView mFoodDescription = (TextView) v.findViewById(R.id.food_description);
        mFoodName.setText(mItem.get(position).getTitle());
        mFoodDescription.setText(mItem.get(position).getFoodDescription());
        mBrand.setText(mItem.get(position).getBrand());
        return v;
    }

}

