package edu.upc.eetac.dsa.walka;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Calendar;

import edu.upc.eetac.dsa.walka.client.WalkaClient;
import edu.upc.eetac.dsa.walka.client.WalkaClientException;
import edu.upc.eetac.dsa.walka.client.entity.Event;
import edu.upc.eetac.dsa.walka.client.entity.EventCollection;
import edu.upc.eetac.dsa.walka.client.entity.EventCollectionAdapter;

public class CalendarWeekActivity extends AppCompatActivity {
    private final static String TAG = CalendarActivity.class.toString();

    private LogOutTask mLogOutTask = null;
    private WeekTask mWeekTask = null;
    private EventCollectionAdapter adapter;
    private EventCollection events = new EventCollection();
    private String date = null;
    private Calendar cal = Calendar.getInstance();
    private TextView mEventos;
    private ImageButton mCalendar;
    private ImageButton mWeek;
    private ImageButton mUser;
    private ImageButton mExit;
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "calendar activity: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_week);
        mWeek = (ImageButton) findViewById(R.id.week);
        mUser = (ImageButton) findViewById(R.id.user);
        mExit = (ImageButton) findViewById(R.id.logout);
        mCalendar = (ImageButton) findViewById(R.id.actual);
        //TODO:logout
        mEventos = (TextView) findViewById(R.id.tveventos);
        //Obtener la fecha actual
        date = obtener_fecha();
        Log.d(TAG, "holi esta es la data " + date);
        //Inicializar el adapter
        adapter = new EventCollectionAdapter(this, events);
        ListView list = (ListView)findViewById(R.id.listdays);
        list.setAdapter(adapter);
        Log.d(TAG, "adapter holi ");

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CalendarWeekActivity.this, EventDetailActivity.class);
                intent.putExtra("id", events.getEvents().get(position).getId());
                startActivity(intent);
            }
        });

        //fab añadir evento
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarWeekActivity.this, NewEventActivity.class);
                startActivity(intent);
            }
        });

        //button user
        mUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarWeekActivity.this, UserDetailsActivity.class);
                startActivity(intent);
            }
        });

        //button logout
        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                    String uri = null;
                    mLogOutTask = new LogOutTask(uri);
                    mLogOutTask.execute((Void)null);
                }
        });
        //button calendar
        mCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarWeekActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        //Execute CalendarTask
        mWeekTask = new WeekTask(null, date);
        mWeekTask.execute((Void) null);
    }
    public String obtener_fecha(){
        String fecha = null;
        String year = Integer.toString(cal.get(Calendar.YEAR));
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String dia;
        String month2 = Integer.toString(month+1);
        if(day <10)
            dia = ("0"+day);
        else
            dia = Integer.toString(day);
        fecha = (year + "-0" + month2 + "-"+day);
        Log.d(TAG, "Fecha con date: "+fecha);
        return fecha;
    }

    public class WeekTask extends AsyncTask<Void, Void, String> {

        private String uri;
        private String fecha;

        public WeekTask(String uri, String date) {
            this.uri = uri;
            fecha = date;
        }

        @Override
        protected String doInBackground(Void... params){
            Log.d(TAG, "DO IN BACKGROUND");
            String jsonEventCollection = null;
            try {
                jsonEventCollection = WalkaClient.getInstance().getWeekEvents(uri, date);
            } catch (WalkaClientException e) {
                // TODO: Handle gracefully
                Log.d(TAG, e.getMessage());
            }
            Log.d(TAG, "no se que del json:" + jsonEventCollection);
            return jsonEventCollection;

        }

        @Override
        protected void onPostExecute(String jsonEventCollection) {
            Log.d(TAG, "onPostExecute");
            Log.d(TAG, jsonEventCollection);
            EventCollection eventCollection = (new Gson()).fromJson(jsonEventCollection, EventCollection.class);
            Log.d(TAG, "event collection ok");
            int i = 0;
            for(Event event : eventCollection.getEvents()){
                Log.d(TAG, "entro en el for del sting: "+event.getTitle());
                events.getEvents().add(events.getEvents().size(), event);
            }
            if(events.getEvents().size() == 0){
                mEventos.setText("No hay eventos este mes");
            }

            adapter.notifyDataSetChanged();

        }

        @Override
        protected void onCancelled() {
            mWeekTask = null;
        }
    }
    public class LogOutTask extends AsyncTask<Void, Void, Boolean> {
        String uri;
        public LogOutTask(String uri) {
            this.uri = uri;
        }

        @Override
        protected Boolean doInBackground(Void... params){
            Log.d(TAG, "DO IN BACKGROUND");
            Boolean ok = false;
            try {
                ok = WalkaClient.getInstance().LogOut(uri);
            } catch (WalkaClientException e) {
                // TODO: Handle gracefully
                Log.d(TAG, e.getMessage());
            }
            return ok;
        }

        @Override
        protected void onPostExecute(Boolean ok) {
            if(ok)
            {
                Intent intent = new Intent(CalendarWeekActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getBaseContext(), "No se ha podido cerrar sesión",
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mLogOutTask = null;
        }
    }

}


