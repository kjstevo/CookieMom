package net.kjmaster.cookiemom.scout.add;


import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.ICookieActionFragment;

import java.util.HashMap;


@EFragment(R.layout.add_scout_dialog)
public class AddScoutDialogFragment extends DialogFragment implements ICookieActionFragment {
    @ViewById(R.id.editText)
    EditText editText;

    @Override
    public HashMap<String, String> valuesMap() {
        HashMap<String, String> valueMap = new HashMap<String, String>();
        valueMap.put(getString(R.string.add_scout), editText.getText().toString());
        return valueMap;
    }


    @Override
    public void refreshView() {

    }

    @Override
    public boolean isRefresh() {
        return false;
    }

//  @Click(R.id.btn_ok)
//   void onOKClick()   {
//
//      if (editText != null) {
//          Intent intent= getActivity().getIntent().putExtra("add_scout",editText.getText());
//
//          getActivity().setResult(Constants.ADD_SCOUT_RESULT_OK,intent);
//      }
//
//  }


}

