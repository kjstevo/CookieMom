//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package net.kjmaster.cookiemom.booth.checking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.googlecode.androidannotations.api.SdkVersionHelper;
import net.kjmaster.cookiemom.R.layout;
import net.kjmaster.cookiemom.R.string;

public final class BoothCheckInActivity_
    extends BoothCheckInActivity
{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(layout.scout_order_layout);
    }

    private void init_(Bundle savedInstanceState) {
        Resources resources_ = this.getResources();
        booth_checkin_order = resources_.getString(string.booth_checkin_order);
        checkin = resources_.getString(string.checkin);
        injectExtras_();
    }

    private void afterSetContentView_() {
        afterViewFrag();
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
        if (((SdkVersionHelper.getSdkInt()< 5)&&(keyCode == KeyEvent.KEYCODE_BACK))&&(event.getRepeatCount() == 0)) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    public static BoothCheckInActivity_.IntentBuilder_ intent(Context context) {
        return new BoothCheckInActivity_.IntentBuilder_(context);
    }

    private void injectExtras_() {
        Intent intent_ = getIntent();
        Bundle extras_ = intent_.getExtras();
        if (extras_!= null) {
            if (extras_.containsKey("BoothId")) {
                try {
                    BoothId = ((Long) extras_.get("BoothId"));
                } catch (ClassCastException e) {
                    Log.e("BoothCheckInActivity_", "Could not cast extra to expected type, the field is left to its default value", e);
                }
            }
        }
    }

    @Override
    public void setIntent(Intent newIntent) {
        super.setIntent(newIntent);
        injectExtras_();
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, BoothCheckInActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public BoothCheckInActivity_.IntentBuilder_ flags(int flags) {
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

        public BoothCheckInActivity_.IntentBuilder_ BoothId(long BoothId) {
            intent_.putExtra("BoothId", BoothId);
            return this;
        }

    }

}
