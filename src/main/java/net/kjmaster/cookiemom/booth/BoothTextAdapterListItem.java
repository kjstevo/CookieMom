package net.kjmaster.cookiemom.booth;

import android.content.Context;
import android.widget.ArrayAdapter;
import net.kmaster.cookiemom.dao.Booth;

import java.text.DateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 12/2/13
 * Time: 9:41 AM
 */
public class BoothTextAdapterListItem extends ArrayAdapter<Object> {
    public BoothTextAdapterListItem(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(Object object) {
        //net.kjmaster.cookiemom.booth.BoothTextAdapterListItem.add returns void
        Booth booth = (Booth) object;
        String boothText;
        DateFormat fmt = DateFormat.getDateInstance();
        if (booth != null) {
            boothText = fmt.format(booth.getBoothDate()) + "      " + booth.getBoothLocation();
            super.add(boothText);
        }
        //12/2/13

    }
}
