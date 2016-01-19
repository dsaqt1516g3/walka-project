package edu.upc.eetac.dsa.walka;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.wallpaper.WallpaperService;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Calendar;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import edu.upc.eetac.dsa.walka.client.WalkaClient;
import edu.upc.eetac.dsa.walka.client.WalkaClientException;
import edu.upc.eetac.dsa.walka.client.entity.Event;
import edu.upc.eetac.dsa.walka.client.entity.User;

public class NewEventActivity extends AppCompatActivity {
    private final static String TAG = WalkaClient.class.toString();
    private CreateEventTask mAuthTask = null;
    private EditText mSubject;
    private TextView mStartDate;
    private TextView mEndDate;
    private TextView mStartHour;
    private TextView mEndHour;
    private EditText mTag;
    private EditText mNotes;
    private EditText mLocation;

    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        mSubject = (EditText) findViewById(R.id.Subject);
        mTag = (EditText) findViewById(R.id.tag);
        mNotes = (EditText) findViewById(R.id.notes);
        mStartHour = (TextView) findViewById(R.id.StartHourText);
        mEndHour = (TextView) findViewById(R.id.EndHourText);
        mStartDate = (TextView) findViewById(R.id.StartDateText);
        mEndDate = (TextView) findViewById(R.id.EndDateText);
        mLocation = (EditText) findViewById(R.id.ubicacion);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        FloatingActionButton fabdel = (FloatingActionButton) findViewById(R.id.fabdel);
        fabdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewEventActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fabok = (FloatingActionButton) findViewById(R.id.fabok);
        fabok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptCreate();
            }
        });

    }
    public void showDatePickerDialogStart(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }
    public void showDatePickerDialogEnd(View v) {
        DialogFragment newFragment = new DatePickerFragment2();
        newFragment.show(getFragmentManager(), "datePicker");
    }
    public void showHourPickerDialogStart(View v){
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void showHourPickerDialogEnd(View v){
        DialogFragment newFragment = new TimePickerFragment2();
        newFragment.show(getFragmentManager(), "timePicker");

    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            if(hourOfDay<10 && minute<10)
                ((TextView) getActivity().findViewById(R.id.StartHourText)).setText("0"+hourOfDay + (":0") + minute);
            else if (hourOfDay<10)
                ((TextView) getActivity().findViewById(R.id.StartHourText)).setText("0"+hourOfDay + (":") + minute);
            else if (minute<10)
                ((TextView) getActivity().findViewById(R.id.StartHourText)).setText(hourOfDay + (":0") + minute);
            else
                ((TextView) getActivity().findViewById(R.id.StartHourText)).setText(hourOfDay + (":") + minute);
        }
    }
    public static class TimePickerFragment2 extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            if(hourOfDay<10 && minute<10)
                ((TextView) getActivity().findViewById(R.id.EndHourText)).setText("0"+hourOfDay + (":0") + minute);
            else if (hourOfDay<10)
                ((TextView) getActivity().findViewById(R.id.EndHourText)).setText("0"+hourOfDay + (":") + minute);
            else if (minute<10)
                ((TextView) getActivity().findViewById(R.id.EndHourText)).setText(hourOfDay + (":0") + minute);
            else
                ((TextView) getActivity().findViewById(R.id.EndHourText)).setText(hourOfDay + (":") + minute);
        }
    }
    public static class DatePickerFragment2 extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            month = month + 1;
            if(month < 10){
                ((TextView) getActivity().findViewById(R.id.EndDateText)).setText(year+("-0")+month+("-")+day);
            }
            else
                ((TextView) getActivity().findViewById(R.id.EndDateText)).setText(year+("-")+month+("-")+day);


        }
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            month = month + 1;
            if(month < 10){
                ((TextView) getActivity().findViewById(R.id.StartDateText)).setText(year+("-0")+month+("-")+day);
            }
            else
                ((TextView) getActivity().findViewById(R.id.StartDateText)).setText(year+("-")+month+("-")+day);


        }
    }


    private void attemptCreate(){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mSubject.setError(null);
        mStartDate.setError(null);
        mEndDate.setError(null);
        mStartHour.setError(null);
        mEndHour.setError(null);
        mTag.setError(null);
        mNotes.setError(null);
        mLocation.setError(null);

        //GetText
        String subject = mSubject.getText().toString();
        Log.d(TAG, "Subject: " + subject);
        String startdate = mStartDate.getText().toString();
        Log.d(TAG,"Startdate: "+startdate);
        String enddate = mEndDate.getText().toString();
        Log.d(TAG,"Enddate: "+enddate);
        String starthour = mStartHour.getText().toString();
        Log.d(TAG,"Starthour: "+starthour);
        String endhour = mEndHour.getText().toString();
        Log.d(TAG,"Endhour: "+endhour);
        String start = (startdate+("T")+starthour+(":00"));
        String end = (enddate+("T")+endhour+(":00"));
        String tag = mTag.getText().toString();
        Log.d(TAG, "tag: "+tag);
        String notes= mNotes.getText().toString();
        Log.d(TAG,"Notes: "+notes);
        String ubicacion = mLocation.getText().toString();

        boolean cancel = false;
        View focusView = null;
        //Comprobar que las fechas sean válidas
        String[] part = startdate.split("-");
        int dia = Integer.parseInt(part[2]);
        int mes = Integer.parseInt(part[1]);
        int any = Integer.parseInt(part[0]);
        String[] part2 = enddate.split("-");
        int dia2 = Integer.parseInt(part2[2]);
        int mes2 = Integer.parseInt(part2[1]);
        int any2 = Integer.parseInt(part2[0]);

        if(any > any2){
            mStartDate.setError(getString(R.string.error_format));
            focusView = mStartDate;
            cancel = true;
        }
        if(mes>mes2 && any == any2) {
            mStartDate.setError(getString(R.string.error_format));
            focusView = mStartDate;
            cancel = true;
        }
        if(dia2 < dia && mes2 == mes && any==any2)
        {
            mStartDate.setError(getString(R.string.error_format));
            focusView = mStartDate;
            cancel = true;
        }

        if(dia<dia2 && mes>mes2 && any==any2 )
        {
            mStartDate.setError(getString(R.string.error_format));
            focusView = mStartDate;
            cancel = true;
        }

        //Comprobar que no haya campos vacios
        if(TextUtils.isEmpty(subject)){
            mSubject.setError(getString(R.string.error_field_required));
            focusView = mSubject;
            cancel = true;
        }

        if(TextUtils.isEmpty(startdate)){
            mStartDate.setError(getString(R.string.error_field_required));
            focusView = mStartDate;
            cancel = true;
        }
        if(TextUtils.isEmpty(enddate)){
            mEndDate.setError(getString(R.string.error_field_required));
            focusView = mEndDate;
            cancel = true;
        }
        if(TextUtils.isEmpty(starthour)){
            mStartHour.setError(getString(R.string.error_field_required));
            focusView = mStartHour;
            cancel = true;
        }
        if(TextUtils.isEmpty(endhour)){
            mEndHour.setError(getString(R.string.error_field_required));
            focusView = mEndHour;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt register and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            Event event = new Event();
            event.setTitle(subject);
            event.setStart(start);
            event.setEnd(end);
            event.setLocation(ubicacion);
            event.setNotes(notes);
            event.setTag(tag);
            mAuthTask = new CreateEventTask(event);
            mAuthTask.execute((Void) null);
        }

    }
    public class CreateEventTask extends AsyncTask<Void, Void, Boolean> {
        String subject;
        String start;
        String end;
        String ubicacion;
        String notes;
        String tag;
        Event event = new Event();

        CreateEventTask(Event event) {
            this.event = event;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean ok=false;
            WalkaClient client = WalkaClient.getInstance();
            try {
                ok = client.newEvent(event);
            } catch (WalkaClientException e) {
                // TODO: Handle gracefully
                Log.d(TAG, e.getMessage());
            }
            return ok;

        }

        @Override
        protected void onPostExecute(final Boolean bool) {
        if(bool){
            startActivity(new Intent(NewEventActivity.this, CalendarActivity.class));
        }
        else
            Toast.makeText(getBaseContext(), "No se ha podido crear el evento",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onCancelled() {
            Log.d(TAG, "cancelled y peto");
            mAuthTask = null;

        }
    }
}
