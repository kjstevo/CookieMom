package net.kjmaster.cookiemom.booth;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import it.gmariotti.cardslib.library.internal.Card;
import net.kjmaster.cookiemom.R;
import net.kmaster.cookiemom.dao.Booth;
import net.kmaster.cookiemom.dao.Scout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/1/13
 * Time: 1:28 PM
 */

public class BoothContentCard extends Card {
    public BoothContentCard(Context context, Booth booth) {
        this(context, R.layout.booth_content, booth);
    }

    public Booth getBooth() {
        return mBooth;
    }

    @SuppressWarnings("WeakerAccess")
    public BoothContentCard(Context context, int innerLayout, Booth booth) {
        super(context, innerLayout);
        mBooth = booth;

    }

    private final Booth mBooth;

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
        TextView mDate = (TextView) parent.findViewById(R.id.booth_date);
        TextView mTime = (TextView) parent.findViewById(R.id.booth_time);
        TextView mAddress = (TextView) parent.findViewById(R.id.booth_address);
        ListView mScoutList = (ListView) parent.findViewById(R.id.booth_scout_list);


        if (mDate != null) {
            try {
                String date = new SimpleDateFormat("EEE, MMM d").format(mBooth.getBoothDate());
                if (date != null) {
                    mDate.setText(date);
                }
            } catch (Exception e) {
                Log.w("cookiemom", "Missing field info");
            }

        }
        if (mTime != null) {
            try {
                String time = DateFormat.getTimeFormat(getContext()).format(mBooth.getBoothDate());

                //String time=new SimpleDateFormat("HH:mm PM").format(mBooth.getBoothDate());
                if (time != null) {
                    mTime.setText(time);
                }
            } catch (Exception e) {
                Log.w("cookiemom", "Missing field info");
            }
        }

        if (mAddress != null) {
            try {
                String address = mBooth.getBoothAddress();
                if (address != null) {
                    mAddress.setText(address);
                }
            } catch (Exception e) {
                Log.w("cookiemom", "Missing field info");
            }
        }

        if (mScoutList != null) {
            try {
                mScoutList.setAdapter(new ArrayAdapter<Scout>(
                        getContext(),
                        R.layout.simple_text,
                        R.id.content,
                        getScoutList()
                ));
            } catch (Exception e) {
                Log.w("cookiemom", "Missing field info");
            }

        }
    }

    private List<Scout> getScoutList() {
        List<Scout> arrayList = new ArrayList<Scout>();
        for (int j = 0; j < mBooth.getScoutsAssigned().size(); j++) {
            arrayList.add(mBooth.getScoutsAssigned().get(j).getScout());
        }

        return arrayList;
    }

}

