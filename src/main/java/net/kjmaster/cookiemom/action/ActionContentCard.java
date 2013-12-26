package net.kjmaster.cookiemom.action;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import it.gmariotti.cardslib.library.internal.Card;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.IAction;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/1/13
 * Time: 1:28 PM
 */

public abstract class ActionContentCard extends Card implements IAction {
    public ActionContentCard(Context context, int layout) {
        super(context, layout);
    }

    public ActionContentCard(Context context) {
        this(context, R.layout.action_content);
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
        TextView mActionText = (TextView) parent.findViewById(R.id.available_action_text);
        ListView mActionList = (ListView) parent.findViewById(R.id.action_list);

        if (mActionText != null) {
            try {
                String actionText = getActionTitle();
                if (actionText != null) {
                    mActionText.setText(actionText);
                }
            } catch (Exception e) {
                Log.w("cookieMom", "Missing field info");
            }

        }

        if (mActionList != null) {
            try {

                //noinspection unchecked
                mActionList.setAdapter(new ArrayAdapter(
                        getContext(),
                        R.layout.ui_simple_medium_text,
                        R.id.content,
                        getActionList()
                ));
            } catch (Exception e) {
                Log.w("cookieMom", "Missing field info");
            }

        }
    }


}

