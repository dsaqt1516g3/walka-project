package edu.upc.eetac.dsa.walka;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.upc.eetac.dsa.walka.client.WalkaClient;
import edu.upc.eetac.dsa.walka.client.WalkaClientException;
import edu.upc.eetac.dsa.walka.client.entity.User;

public class UserDetailsActivity extends AppCompatActivity {
    private UserTask mUserTask = null;
    private EditUserTask mEditTask = null;
    User usuario = new User();

    private final static String TAG = CalendarActivity.class.toString();
    EditText mName;
    TextView mUsername;
    EditText mEmail;
    EditText mCountry;
    EditText mCity;
    EditText mPhone;
    EditText mCumple;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        mUsername = (TextView) findViewById(R.id.header);
        mName = (EditText) findViewById(R.id.nombre);
        mEmail = (EditText) findViewById(R.id.email);
        mCountry = (EditText) findViewById(R.id.pais);
        mCity = (EditText) findViewById(R.id.ciudad);
        mPhone = (EditText) findViewById(R.id.phone);
        mCumple = (EditText) findViewById(R.id.cumple);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //Button cancel
        FloatingActionButton fabdel = (FloatingActionButton) findViewById(R.id.fabdel);
        fabdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserDetailsActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        //Botón ok
        FloatingActionButton fabok = (FloatingActionButton) findViewById(R.id.fabok);
        fabok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "le he dado al ok");
                Boolean cancel;
                cancel = obtener_editado();
                Log.d(TAG, "obtengo el editado");
                if(cancel == false)
                {
                    mEditTask = new EditUserTask(usuario);
                    mEditTask.execute((Void) null);
                }
                else{
                    Toast.makeText(getBaseContext(),"Alguno de los campos no es correcto",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        mUserTask = new UserTask(null);
        mUserTask.execute((Void) null);
    }

    public Boolean obtener_editado(){
        Boolean cancel = false;
        //comprobar que no haya campos vacios
        if(TextUtils.isEmpty(mName.getText().toString())){
            Log.d(TAG, "name is empty");
            mName.setError(getString(R.string.error_field_required));
            cancel = true;
        }
        else{
            String fullname = mName.getText().toString();
            usuario.setFullname(fullname);
        }

        if(TextUtils.isEmpty(mEmail.getText().toString())){
            Log.d(TAG, "email is empty");
            mEmail.setError(getString(R.string.error_field_required));
            cancel = true;
        }
        else
            usuario.setEmail(mEmail.getText().toString());

        //Comprobamos Email
        if(isEmailValid(mEmail.getText().toString())==false)
        {
            mEmail.setError(getString(R.string.error_field_required));
            cancel = true;
            Log.d(TAG, "email incorrecto");
        }

        usuario.setCountry(mCountry.getText().toString());
        usuario.setCity(mCity.getText().toString());
        usuario.setBirthdate(mCumple.getText().toString());
        usuario.setPhonenumber(mPhone.getText().toString());

        return cancel;

    }
    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
    public void user_details(User user){
        usuario.setId(user.getId());
        mUsername.setText(user.getLoginid());
        mEmail.setText(user.getEmail());
        mName.setText(user.getFullname());

        if(user.getBirthdate() != null){
            mCumple.setText(user.getBirthdate());
        }

        if(user.getCity() != null)
            mCity.setText(user.getCity());
        else
            mCity.setText("No está determinada");

        if(user.getCountry() != null)
            mCountry.setText(user.getCountry());
        else
            mCountry.setText("No está determinado");

        if(user.getPhonenumber() != null)
            mPhone.setText(user.getPhonenumber());
        else
            mPhone.setText("Introduce tu número");

    }

    public class UserTask extends AsyncTask<Void, Void, String> {

        private String uri;

        public UserTask(String uri) {
            this.uri = uri;
        }

        @Override
        protected String doInBackground(Void... params){
            Log.d(TAG, "DO IN BACKGROUND");
            String jsonEventCollection = null;
            try {
                jsonEventCollection = WalkaClient.getInstance().getUser(uri);
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
            User user = null;
            user = (new Gson()).fromJson(jsonEventCollection, User.class);
            usuario = user;
            Log.d(TAG, "event collection ok");
            user_details(user);
        }

        @Override
        protected void onCancelled() {
            mUserTask = null;
        }
    }

    public class EditUserTask extends AsyncTask<Void, Void, Boolean> {

        private User user;

        public EditUserTask(User user) {
            this.user = user;
        }

        @Override
        protected Boolean doInBackground(Void... params){
            Log.d(TAG, "DO IN BACKGROUND");
            Boolean ok;

            WalkaClient client = WalkaClient.getInstance();
            ok  = client.EditUser(user);

            return ok;
        }

        @Override
        protected void onPostExecute(Boolean succes) {
            Log.d(TAG, "onPostExecute");
            Log.d(TAG, "event collection ok");
            if(succes)
            {
                Intent intent = new Intent(UserDetailsActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
            else
                mUsername.setError("Se ha producido un error");

        }

        @Override
        protected void onCancelled() {
            mEditTask = null;
        }
    }


}
