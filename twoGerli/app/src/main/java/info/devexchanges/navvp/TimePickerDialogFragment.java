package info.devexchanges.navvp;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerDialogFragment extends DialogFragment {
    Calendar dt = Calendar.getInstance();
    static TimePickerDialogFragment newInstance() {
        TimePickerDialogFragment dlg = new TimePickerDialogFragment();
        return dlg;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TimePickerDialog tDialog = new TimePickerDialog(getActivity(),
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view,
                                      int hourOfDay, int minute) {
                    ((MPinputActivity) getActivity()).getTime(hourOfDay, minute);
                }
            }, dt.get(Calendar.HOUR),dt.get(Calendar.MINUTE),true);
        return tDialog;
    }
}
