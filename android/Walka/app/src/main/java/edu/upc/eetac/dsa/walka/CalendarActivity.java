package edu.upc.eetac.dsa.walka;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import edu.upc.eetac.dsa.walka.client.entity.User;

public class CalendarActivity extends AppCompatActivity {
    private final static String TAG = CalendarActivity.class.toString();

    private CalendarTask mCalendarTask = null;
    private LogOutTask mLogOutTask = null;
    private TextView mMonth;
    private EventCollectionAdapter adapter;
    private EventCollection events = new EventCollection();
    private String date = null;
    private Calendar cal = Calendar.getInstance();
    private ImageButton mPrev;
    private ImageButton mNext;
    private TextView mEventos;
    private ImageButton mWeek;
    private ImageButton mUser;
    private ImageButton mExit;
    private ImageButton mCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "calendar activity: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mMonth = (TextView)findViewById(R.id.tvmes);
        mPrev = (ImageButton)findViewById(R.id.prevmonth);
        mNext = (ImageButton)findViewById(R.id.nextmonth);
        mWeek = (ImageButton) findViewById(R.id.week);
        mUser = (ImageButton) findViewById(R.id.user);
        mExit = (ImageButton) findViewById(R.id.logout);
        //TODO:logout
        mEventos = (TextView) findViewById(R.id.tveventos);
        mEventos.setText("");
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null) {
            date = (String) b.get("fecha");
            mMonth.setText(nombre(date));
        }
        else {
            //Obtener la fecha actual
            date = obtener_fecha();
            mMonth.setText(nombre(date));
            Log.d(TAG, "holi esta es la data " + date);
        }

        //Inicializar el adapter
        adapter = new EventCollectionAdapter(this, events);
        ListView list = (ListView)findViewById(R.id.listdays);
        list.setAdapter(adapter);
        Log.d(TAG, "adapter holi ");

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CalendarActivity.this, EventDetailActivity.class);
                intent.putExtra("id", events.getEvents().get(position).getId());
                startActivity(intent);
            }
        });


        //fab añadir evento
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, NewEventActivity.class);
                startActivity(intent);
            }
        });

        //button user
        mUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, UserDetailsActivity.class);
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


        //button week events
        mWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, CalendarWeekActivity.class);
                startActivity(intent);
            }
        });

        //button ir al mes anterior
        mPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fecha = null;
                fecha = fecha_prev(date);
                date = fecha;
                Log.d(TAG, "Fecha prev en el fab: " + fecha);
                Log.d(TAG, "Fecha date actual: " + date);

                if (fecha != null) {
                    Intent intent = new Intent(CalendarActivity.this, CalendarActivity.class);
                    intent.putExtra("fecha", date);
                    startActivity(intent);
                }
            }
        });

        //button ir al mes siguiente
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fecha = null;
                Log.d(TAG, "date:" + date);
                fecha = fecha_next(date);
                date = fecha;
                Log.d(TAG, "Fecha next en el fab: " + fecha);
                Log.d(TAG, "Fecha date actual: " + date);

                if (fecha != null) {
                    finish();
                    /*Intent intent = new Intent(CalendarActivity.this, CalendarActivity.class);
                    intent.putExtra("fecha", date);
                    startActivity(intent);*/
                }
            }
        });

        //Execute CalendarTask
        mCalendarTask = new CalendarTask(null, date);
        mCalendarTask.execute((Void) null);
    }
    public class DialogoAlerta extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());

            builder.setMessage("Está seguro de que desea salir?")
                    .setTitle("Información")
                    .setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mLogOutTask = new LogOutTask(null);
                            mLogOutTask.execute((Void)null);
                            Intent intent = new Intent(CalendarActivity.this, LoginActivity.class);
                            intent.putExtra("fecha", date);
                            startActivity(intent);
                        }
                    });

            return builder.create();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            FragmentManager fragmentManager = getFragmentManager();
            DialogoAlerta dialogo = new DialogoAlerta();
            dialogo.show(fragmentManager, "tagAlerta");
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public String obtener_fecha(){
        String fecha = null;
        String year = Integer.toString(cal.get(Calendar.YEAR));
        int month = cal.get(Calendar.MONTH);
        String month2 = Integer.toString(month+1);
        fecha = (year + "-0" + month2 + "-01");
        Log.d(TAG, "Fecha con date: "+fecha);
        return fecha;
    }
    public String nombre(String data){
        String month = null;
        String monthname = null;
        String fecha = null;
        int mes = 0;
        String[] partes = data.split("-");
        month = partes[1];
        mes = Integer.parseInt(month);
        monthname = nombre_mes(mes);
        fecha = (monthname + ("  ")+partes[0]);
        return fecha;
    }
    public String fecha_prev(String data){
        String prev = null;
        String month = null;
        int mes = 0;
        int any = 0;
        String year = null;
        String[] partes = data.split("-");
        year = partes[0];
        month = partes[1];
        mes = Integer.parseInt(month);
        if (mes == 1) {
            month = ("12");
            any = Integer.parseInt(year);
            any = any - 1;
            year = Integer.toString(any);
            prev = (year+("-")+month+("-01"));
        }
        else {
            if(mes > 10){
                month = Integer.toString(mes - 1);
                prev = (year+("-")+month+("-01"));
            }
            else{
                month = Integer.toString(mes - 1);
                prev = (year+("-0")+month+("-01"));
            }
        }

        Log.d(TAG, "Fecha prev: "+prev);
        return prev;
    }
    public String fecha_next(String data){
        String next = null;
        String month = null;
        int mes = 0;
        int any = 0;
        String year = null;
        String[] partes = data.split("-");
        year = partes[0];
        month = partes[1];
        mes = Integer.parseInt(month);
        Log.d(TAG, "year: "+year);
        Log.d(TAG, "month: "+month);

        if (mes == 12) {
            month = ("1");
            any = Integer.parseInt(year);
            any = any + 1;
            year = Integer.toString(any);
            next = (year+("-0")+month+("-01"));
            Log.d(TAG, "Fecha next: "+next);

        }
        else {
            if(mes >= 9){
                month = Integer.toString(mes + 1);
                next = (year+("-")+month+("-01"));
                Log.d(TAG, "Fecha next: "+next);

            }
            else{
                month = Integer.toString(mes + 1);
                next = (year+("-0")+month+("-01"));
                Log.d(TAG, "Fecha next: "+next);

            }
        }

        Log.d(TAG, "Fecha next: "+next);
        return next;

    }
    public String nombre_mes(int numero){
        String nmes = null;
        if(numero == 1)
            nmes = "Enero";
        else if(numero == 2)
            nmes = "Febrero";
        else if(numero == 3)
            nmes ="Marzo";
        else if(numero == 4)
            nmes = "Abril";
        else if(numero == 5)
            nmes ="Mayo";
        else if(numero == 6)
            nmes ="Junio";
        else if(numero == 7)
            nmes = "Julio";
        else if(numero == 8)
            nmes = "Agosto";
        else if(numero == 9)
            nmes = "Septiembre";
        else if(numero == 10)
            nmes = "Octubre";
        else if(numero == 11)
            nmes = "Noviembre";
        else if(numero == 12)
            nmes = "Diciembre";

        return nmes;

    }

    public class CalendarTask extends AsyncTask<Void, Void, String> {

        private String uri;
        private String fecha;

        public CalendarTask(String uri, String date) {
            this.uri = uri;
            fecha = date;
        }

        @Override
        protected String doInBackground(Void... params){
            Log.d(TAG, "DO IN BACKGROUND");
            String jsonEventCollection = null;
            try {
                jsonEventCollection = WalkaClient.getInstance().getEvents(uri, date);
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
            mCalendarTask = null;
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
                Intent intent = new Intent(CalendarActivity.this, LoginActivity.class);
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
