package edu.upc.eetac.dsa.walka;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.upc.eetac.dsa.walka.client.WalkaClient;
import edu.upc.eetac.dsa.walka.client.WalkaClientException;
import edu.upc.eetac.dsa.walka.client.entity.Event;
import edu.upc.eetac.dsa.walka.client.entity.EventCollection;
import edu.upc.eetac.dsa.walka.client.entity.User;
import edu.upc.eetac.dsa.walka.client.entity.UserCollection;

public class EventDetailActivity extends AppCompatActivity {
    private final static String TAG = CalendarActivity.class.toString();
    private getEventTask mEventTask = null;
    private DelEventTask mDelEventTask = null;
    private String id = null;
    private String uri = null;
    TextView textViewSubject;
    TextView textViewDate;
    TextView textViewHour;
    TextView textViewUbicacion;
    TextView textViewTag;
    TextView textViewNotes;
    TextView textViewParticipantes;
    View barra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "entro en el EventDetailActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        FloatingActionButton fabeditar = (FloatingActionButton) findViewById(R.id.fabeditar);
        fabeditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        Log.d(TAG, "fab ok");
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null) {
            id = (String) b.get("id");
        }
        Log.d(TAG, "id getExtras"+id);
        if (id != null) {
            //Execute CalendarTask
            mEventTask = new getEventTask(uri, id);
            mEventTask.execute((Void) null);
        } else {
        }
        //TODO: si uri es diferente de null no pasa nada
        FloatingActionButton fabeliminar = (FloatingActionButton) findViewById(R.id.fabdelete);
        fabeliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "id "+id);
                mDelEventTask = new DelEventTask(id);
                mDelEventTask.execute((Void) null);
            }
        });
    }

    public void rellenar_layout(Event evento) {
        Event event = new Event();
        event = evento;
        Log.d(TAG, "entro en el getView");
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        textViewSubject=(TextView)findViewById(R.id.Subject);
        textViewDate=(TextView)findViewById(R.id.day);
        textViewHour=(TextView)findViewById(R.id.hour);
        textViewUbicacion=(TextView)findViewById(R.id.ubicacion);
        textViewTag=(TextView)findViewById(R.id.tag);
        textViewNotes=(TextView)findViewById(R.id.notes);
        textViewParticipantes=(TextView)findViewById(R.id.participantes);
        LinearLayout bar = (LinearLayout) findViewById(R.id.bar);
        Log.d(TAG, "inicializado ok");
        if(event.getColor() != null){
            bar.setBackgroundColor(Color.parseColor(event.getColor()));
        }
        String title = event.getTitle();
        textViewSubject.setText(title);
        Log.d(TAG, title);
        Log.d(TAG, "subject ok");
        String ds = null;
        String de = null;
        String hs = null;
        String he = null;

        String date1 = event.getStart();
        String[] parts = date1.split(" ");
        ds = parts[0];
        hs = parts[1];
        Log.d(TAG, "if con espacio: "+ds + hs);

        String date2 = event.getEnd();
        String[] parts2 = date2.split(" ");
        de = parts2[0];
        he = parts2[1];
        Log.d(TAG, "if con espacio: "+de + he);

        if(ds.equals(de)){
            String[] partes = ds.split("-");
            String data = partes[2]+"/"+partes[1]+"/"+partes[0];
            Log.d(TAG, "de y ds iguales: "+data);
            textViewDate.setText(data);
            String hora = null;
            hora = hs +("  -  ")+he;
            textViewHour.setText(hora);
            Log.d(TAG, "iguales: " +data + hora);
        }
        else {
            Log.d(TAG, "diferentes ");
            String[] partes = ds.split("-");
            String data1 = partes[2]+"/"+partes[1]+"/"+partes[0];
            String[] partes2 = de.split("-");
            String data2 = partes2[2]+"/"+partes2[1]+"/"+partes2[0];
            String inicio = ("Inicio: " +data1 +"    a las: "+hs);
            textViewDate.setText(inicio);
            Log.d(TAG, "inicio: "+inicio);
            String fin = ("Fin: " +data2 +"    a las: "+he);
            textViewHour.setText(fin);
            Log.d(TAG, "fin: " + fin);

        }

        if (event.getLocation() != null)
            textViewUbicacion.setText(event.getLocation());
        else
            textViewUbicacion.setText("Ubicación no determinada");

        Log.d(TAG, "location");

        if (event.getTag() != null)
            textViewTag.setText(event.getTag());
        else
            textViewTag.setText("No hay tags");

        Log.d(TAG, "tag");

        if(event.getNotes() != null){
            textViewNotes.setText(Html.fromHtml(event.getNotes()));
        }
        else
            textViewNotes.setText("Este evento no tiene descripción");

        UserCollection users;
        users = event.getParticipants();
        if(users != null) {
            List<String> nameuser = new ArrayList<String>();
            for (User user : users.getUsers()) {
                nameuser.add(user.getLoginid());
                Log.d(TAG, "user:" + nameuser);
            }
            Iterator<String> i = nameuser.iterator();
            String participante;
            String part = null;
            while (i.hasNext()) {
                participante = i.next();
                part = (participante + ("   "));
            }
            textViewParticipantes.setText(part);
            Log.d(TAG, "usuarios finales: " + part);
        }
        else
            textViewParticipantes.setText("Eres el único participante");

    }

    public class getEventTask extends AsyncTask<Void, Void, String> {

        private String uri;
        private String id;

        public getEventTask(String uri, String id) {
            this.uri = uri;
            this.id = id;
        }

        @Override
        protected String doInBackground(Void... params){
            Log.d(TAG, "DO IN BACKGROUND");
            String jsonEvent = null;
            try {
                jsonEvent = WalkaClient.getInstance().getEvent(uri, id);
            } catch (WalkaClientException e) {
                // TODO: Handle gracefully
                Log.d(TAG, e.getMessage());
            }
            Log.d(TAG, "no se que del json:" + jsonEvent);
            return jsonEvent;

        }

        @Override
        protected void onPostExecute(String jsonEvent) {
            Log.d(TAG, "onPostExecute");
            Log.d(TAG, jsonEvent);
            Event event = (new Gson()).fromJson(jsonEvent, Event.class);

            if(event != null)
                rellenar_layout(event);
            else{}
            Log.d(TAG, "event ok");

            Log.d(TAG, "llamo a la función para que rellene todos los campos");


        }

        @Override
        protected void onCancelled() {
            mEventTask = null;
        }
    }
    public class DelEventTask extends AsyncTask<Void, Void, Boolean> {
        String id;
        public DelEventTask(String id) {
            this.id = id;
        }

        @Override
        protected Boolean doInBackground(Void... params){
            Log.d(TAG, "DO IN BACKGROUND");
            Boolean ok = false;
            try {
                ok = WalkaClient.getInstance().Eliminar(id);
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
                Intent intent = new Intent(EventDetailActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getBaseContext(), "No se ha podido eliminar",
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mDelEventTask = null;
        }
    }
}
