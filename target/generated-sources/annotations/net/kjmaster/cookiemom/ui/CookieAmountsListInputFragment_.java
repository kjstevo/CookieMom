//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package net.kjmaster.cookiemom.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import it.gmariotti.cardslib.library.view.CardListView;
import net.kjmaster.cookiemom.R.layout;

public final class CookieAmountsListInputFragment_
    extends CookieAmountsListInputFragment
{

    private View contentView_;

    private void init_(Bundle savedInstanceState) {
        injectFragmentArguments_();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    private void afterSetContentView_() {
        cardListView = ((CardListView) findViewById(net.kjmaster.cookiemom.R.id.carddemo_list_base1));
        afterViews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView_ = super.onCreateView(inflater, container, savedInstanceState);
        if (contentView_ == null) {
            contentView_ = inflater.inflate(layout.scout_order_dialog, container, false);
        }
        return contentView_;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        afterSetContentView_();
    }

    public View findViewById(int id) {
        if (contentView_ == null) {
            return null;
        }
        return contentView_.findViewById(id);
    }

    public static CookieAmountsListInputFragment_.FragmentBuilder_ builder() {
        return new CookieAmountsListInputFragment_.FragmentBuilder_();
    }

    private void injectFragmentArguments_() {
        Bundle args_ = getArguments();
        if (args_!= null) {
            if (args_.containsKey("isEditable")) {
                try {
                    isEditable = args_.getBoolean("isEditable");
                } catch (ClassCastException e) {
                    Log.e("CookieAmountsListInputFragment_", "Could not cast argument to the expected type, the field is left to its default value", e);
                }
            }
            if (args_.containsKey("isBoxes")) {
                try {
                    isBoxes = args_.getBoolean("isBoxes");
                } catch (ClassCastException e) {
                    Log.e("CookieAmountsListInputFragment_", "Could not cast argument to the expected type, the field is left to its default value", e);
                }
            }
        }
    }

    public static class FragmentBuilder_ {

        private Bundle args_;

        private FragmentBuilder_() {
            args_ = new Bundle();
        }

        public CookieAmountsListInputFragment build() {
            CookieAmountsListInputFragment_ fragment_ = new CookieAmountsListInputFragment_();
            fragment_.setArguments(args_);
            return fragment_;
        }

        public CookieAmountsListInputFragment_.FragmentBuilder_ isEditable(boolean isEditable) {
            args_.putBoolean("isEditable", isEditable);
            return this;
        }

        public CookieAmountsListInputFragment_.FragmentBuilder_ isBoxes(boolean isBoxes) {
            args_.putBoolean("isBoxes", isBoxes);
            return this;
        }

    }

}
