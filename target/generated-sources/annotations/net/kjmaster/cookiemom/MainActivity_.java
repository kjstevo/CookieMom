//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package net.kjmaster.cookiemom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.googlecode.androidannotations.api.SdkVersionHelper;
import net.kjmaster.cookiemom.R.layout;

public final class MainActivity_
        extends MainActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
    }

    private void init_(Bundle savedInstanceState) {
    }

    private void afterSetContentView_() {
        afterViews();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        afterSetContentView_();
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        afterSetContentView_();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        afterSetContentView_();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (((SdkVersionHelper.getSdkInt() < 5) && (keyCode == KeyEvent.KEYCODE_BACK)) && (event.getRepeatCount() == 0)) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    public static MainActivity_.IntentBuilder_ intent(Context context) {
        return new MainActivity_.IntentBuilder_(context);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(net.kjmaster.cookiemom.R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        MainActivity_.super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 4343:
                MainActivity_.this.scoutResult(resultCode);
                break;
            case 888:
                MainActivity_.this.onBoothResult(resultCode, data);
                break;
            case 1111:
                MainActivity_.this.onRemoveScout(resultCode, data);
                break;
            case 444:
                MainActivity_.this.placeCupboardOrder(resultCode, data);
                break;
            case 9876:
                MainActivity_.this.onBoothOrder(resultCode, data);
                break;
            case 111:
                MainActivity_.this.onScoutAssign(resultCode, data);
                break;
        }
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, MainActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public MainActivity_.IntentBuilder_ flags(int flags) {
            intent_.setFlags(flags);
            return this;
        }

        public void start() {
            context_.startActivity(intent_);
        }

        public void startForResult(int requestCode) {
            if (context_ instanceof Activity) {
                ((Activity) context_).startActivityForResult(intent_, requestCode);
            } else {
                context_.startActivity(intent_);
            }
        }

    }

}
