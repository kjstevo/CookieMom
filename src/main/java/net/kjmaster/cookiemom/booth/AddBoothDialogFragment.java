package net.kjmaster.cookiemom.booth;


import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.ui.DateTimePicker;

import java.sql.Date;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


@EFragment(R.layout.add_booth_dialog)
public class AddBoothDialogFragment extends DialogFragment {
    @ViewById(R.id.editText)
    EditText editText;
    @ViewById(R.id.add_booth_address)
    EditText addressText;

    @ViewById(R.id.text_date_time_hidden)
    TextView hiddenDateTime;
    @ViewById(R.id.Date)
    TextView dateText;

    @ViewById(R.id.Time)
    TextView timeText;


//    DatePicker datePicker;
//    @ViewById(R.id.booth_timePicker)
//    TimePicker timePicker;

    @Click(R.id.date_time_picker_button)
    void onPickClick() {
        showDateTimeDialog();
    }

    private void showDateTimeDialog() {
        // Create the dialog
        final Dialog mDateTimeDialog = new Dialog(getActivity());
        // Inflate the root layout
        final RelativeLayout mDateTimeDialogView = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.date_time_dialog, null);
        // Grab widget instance
        final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView.findViewById(R.id.DateTimePicker);
        // Check is system is set to use 24h time (this doesn't seem to work as expected though)
        final String timeS = android.provider.Settings.System.getString(getActivity().getContentResolver(), android.provider.Settings.System.TIME_12_24);
        final boolean is24h = !(timeS == null || timeS.equals("12"));

        // Update demo TextViews when the "OK" button is clicked
        mDateTimeDialogView.findViewById(R.id.SetDateTime).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                mDateTimePicker.clearFocus();
                // TODO Auto-generated method stub

                String eventDate = SimpleDateFormat.getDateInstance(DateFormat.LONG).format(new Date(mDateTimePicker.getDateTimeMillis()));
                ((TextView) getActivity().findViewById(R.id.Date)).setText(eventDate);
                if (mDateTimePicker.is24HourView()) {
                    ((TextView) getActivity().findViewById(R.id.Time)).setText(mDateTimePicker.get(Calendar.HOUR_OF_DAY) + ":" + mDateTimePicker.get(Calendar.MINUTE));
                } else {
                    NumberFormat fmt = NumberFormat.getNumberInstance();
                    fmt.setMinimumIntegerDigits(2);

                    ((TextView) getActivity().findViewById(R.id.Time)).setText(fmt.format(mDateTimePicker.get(Calendar.HOUR)) +
                            ":" + fmt.format(mDateTimePicker.get(Calendar.MINUTE)) + " "
                            + (mDateTimePicker.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM"));

                }


                hiddenDateTime.setText(String.valueOf(mDateTimePicker.getDateTimeMillis()));
                mDateTimeDialog.dismiss();
            }
        });

        // Cancel the dialog when the "Cancel" button is clicked
        mDateTimeDialogView.findViewById(R.id.CancelDialog).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                mDateTimeDialog.cancel();
            }
        });

        // Reset Date and Time pickers when the "Reset" button is clicked
        mDateTimeDialogView.findViewById(R.id.ResetDateTime).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                mDateTimePicker.reset();
            }
        });

        // Setup TimePicker
        mDateTimePicker.setIs24HourView(is24h);
        // No title on the dialog window
        mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Set the dialog content view
        mDateTimeDialog.setContentView(mDateTimeDialogView);
        // Display the dialog
        mDateTimeDialog.show();
    }

//    @Click(R.id.btn_ok)
//   void onOKClick()   {
//
//      if (editText != null) {
//          Intent intent= getActivity().getIntent().putExtra("add_booth",editText.getText());
//
//          getActivity().setResult(Constants.ADD_BOOTH_RESULT_OK,intent);
//      }


}

