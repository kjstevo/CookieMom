//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package net.kjmaster.cookiemom.summary.stat.booth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import it.gmariotti.cardslib.library.view.CardView;
import net.kjmaster.cookiemom.R.layout;

public final class SummaryStatBoothCardFragment_
    extends SummaryStatBoothCardFragment
{

    private View contentView_;

    private void init_(Bundle savedInstanceState) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    private void afterSetContentView_() {
        mCardView = ((CardView) findViewById(net.kjmaster.cookiemom.R.id.summary_stat_booth_cardview));
        afterViews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView_ = super.onCreateView(inflater, container, savedInstanceState);
        if (contentView_ == null) {
            contentView_ = inflater.inflate(layout.summary_stat_booth_fragment, container, false);
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

    public static SummaryStatBoothCardFragment_.FragmentBuilder_ builder() {
        return new SummaryStatBoothCardFragment_.FragmentBuilder_();
    }

    public static class FragmentBuilder_ {

        private Bundle args_;

        private FragmentBuilder_() {
            args_ = new Bundle();
        }

        public SummaryStatBoothCardFragment build() {
            SummaryStatBoothCardFragment_ fragment_ = new SummaryStatBoothCardFragment_();
            fragment_.setArguments(args_);
            return fragment_;
        }

    }

}