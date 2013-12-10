//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package net.kjmaster.cookiemom.scout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R.layout;

@SuppressWarnings("UnusedParameters")
public final class SelectScoutListActivity_
        extends SelectScoutListActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(layout.select_scout);
    }

    private void init_(Bundle savedInstanceState) {
        injectExtras_();
        main = ((Main) this.getApplication());
    }

    private void afterSetContentView_() {
        {
            AdapterView<?> view = ((AdapterView<?>) findViewById(android.R.id.list));
            if (view != null) {
                view.setOnItemClickListener(new OnItemClickListener() {


                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        listItemClicked(position);
                    }

                }
                );
            }
        }
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

    public static SelectScoutListActivity_.IntentBuilder_ intent(Context context) {
        return new SelectScoutListActivity_.IntentBuilder_(context);
    }

    private void injectExtras_() {
        Intent intent_ = getIntent();
        Bundle extras_ = intent_.getExtras();
        if (extras_ != null) {
            if (extras_.containsKey("targetId")) {
                try {
                    targetId = ((Long) extras_.get("targetId"));
                } catch (ClassCastException e) {
                    Log.e("SelectScoutListActivity_", "Could not cast extra to expected type, the field is left to its default value", e);
                }
            }
            if (extras_.containsKey("requestCode")) {
                try {
                    requestCode = ((Integer) extras_.get("requestCode"));
                } catch (ClassCastException e) {
                    Log.e("SelectScoutListActivity_", "Could not cast extra to expected type, the field is left to its default value", e);
                }
            }
            if (extras_.containsKey("FragmentTag")) {
                try {
                    FragmentTag = cast_(extras_.get("FragmentTag"));
                } catch (ClassCastException e) {
                    Log.e("SelectScoutListActivity_", "Could not cast extra to expected type, the field is left to its default value", e);
                }
            }
        }
    }

    @Override
    public void setIntent(Intent newIntent) {
        super.setIntent(newIntent);
        injectExtras_();
    }

    @SuppressWarnings("unchecked")
    private <T> T cast_(Object object) {
        return ((T) object);
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, SelectScoutListActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public SelectScoutListActivity_.IntentBuilder_ flags(int flags) {
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

        public SelectScoutListActivity_.IntentBuilder_ targetId(long targetId) {
            intent_.putExtra("targetId", targetId);
            return this;
        }

        public SelectScoutListActivity_.IntentBuilder_ requestCode(int requestCode) {
            intent_.putExtra("requestCode", requestCode);
            return this;
        }

        public SelectScoutListActivity_.IntentBuilder_ FragmentTag(String FragmentTag) {
            intent_.putExtra("FragmentTag", FragmentTag);
            return this;
        }

    }

}
