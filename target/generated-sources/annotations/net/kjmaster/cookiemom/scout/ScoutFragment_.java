//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package net.kjmaster.cookiemom.scout;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import it.gmariotti.cardslib.library.view.CardListView;
import net.kjmaster.cookiemom.R.layout;

@SuppressWarnings("UnusedParameters")
public final class ScoutFragment_
        extends ScoutFragment {

    private View contentView_;

    private void init_(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    private void afterSetContentView_() {
        cardView = ((CardListView) findViewById(net.kjmaster.cookiemom.R.id.carddemo_list_base1));
        afterViews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView_ = super.onCreateView(inflater, container, savedInstanceState);
        if (contentView_ == null) {
            contentView_ = inflater.inflate(layout.fragment_sample, container, false);
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

    public static ScoutFragment_.FragmentBuilder_ builder() {
        return new ScoutFragment_.FragmentBuilder_();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(net.kjmaster.cookiemom.R.menu.scout_frag, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = super.onOptionsItemSelected(item);
        if (handled) {
            return true;
        }
        int itemId_ = item.getItemId();
        if (itemId_ == net.kjmaster.cookiemom.R.id.menu_add_scout) {
            addScout();
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ScoutFragment_.super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 4343:
                ScoutFragment_.this.scoutResullts(resultCode);
                break;
        }
    }

    public static class FragmentBuilder_ {

        private Bundle args_;

        private FragmentBuilder_() {
            args_ = new Bundle();
        }

        public ScoutFragment build() {
            ScoutFragment_ fragment_ = new ScoutFragment_();
            fragment_.setArguments(args_);
            return fragment_;
        }

    }

}
