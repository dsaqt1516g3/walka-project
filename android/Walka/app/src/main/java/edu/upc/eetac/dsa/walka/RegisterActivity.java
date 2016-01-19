package edu.upc.eetac.dsa.walka;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.upc.eetac.dsa.walka.client.WalkaClient;
import edu.upc.eetac.dsa.walka.client.entity.User;

public class RegisterActivity extends AppCompatActivity {

    private final static String TAG = WalkaClient.class.toString();
    private UserRegisterTask mAuthTask = null;
    private TextView mUsernameView;
    private TextView mNameView;
    private EditText mPasswordView;
    private TextView mEmailView;
    private TextView  mRepeatPasswordView;
    private View mProgressView;
    private View mRegisterFormView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsernameView = (EditText) findViewById(R.id.Username);
        mNameView = (EditText) findViewById(R.id.Name);
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.etPassword);
        mRepeatPasswordView = (EditText) findViewById(R.id.etPassword2);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Button mRegisterButton = (Button) findViewById(R.id.bDone);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        Button mCancelButton = (Button) findViewById(R.id.bCancel);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);
    }

    private void attemptRegister(){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mNameView.setError(null);
        mPasswordView.setError(null);
        mRepeatPasswordView.setError(null);
        mEmailView.setError(null);

        String username = mUsernameView.getText().toString();
        Log.d(TAG,"username: "+username);
        String name = mNameView.getText().toString();
        Log.d(TAG,"name: "+name);
        String email = mEmailView.getText().toString();
        Log.d(TAG,"email: "+email);
        String password = mPasswordView.getText().toString();
        Log.d(TAG,"pass: "+password);
        String repeatPassword = mRepeatPasswordView.getText().toString();
        Log.d(TAG,"pass1: "+repeatPassword);

        boolean cancel = false;
        View focusView = null;

        //comprobar que las contraseñas sean iguales
        if(!password.equals(repeatPassword)){
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            focusView = mPasswordView;
            cancel = true;
        }

        //comprobar que el email sea válido
        if(isEmailValid(email) == false){
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        //comprobar que no haya campos vacios
        if(TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
        if(TextUtils.isEmpty(repeatPassword)){
            mRepeatPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        if(TextUtils.isEmpty(name)){
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }
        if(TextUtils.isEmpty(username)){
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt register and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserRegisterTask(username, name, email, password);
            mAuthTask.execute((Void) null);
        }

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

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class UserRegisterTask extends AsyncTask<Void, Void, User> {

        private final String mUsername;
        private final String mPassword;
        private final String mEmail;
        private final String mName;

        UserRegisterTask(String username, String name, String email, String password) {
            mUsername = username;
            mEmail = email;
            mPassword = password;
            mName = name;
        }

        @Override
        protected User doInBackground(Void... params) {
            User user;
            WalkaClient client = WalkaClient.getInstance();
            user = client.register(mUsername, mName, mEmail, mPassword);
            return user;
        }

        @Override
        protected void onPostExecute(final User user) {
            View focusView = null;
            mAuthTask = null;
            showProgress(false);
            Log.d(TAG, "ha entrado en postexecute");
            if(user.getLoginSuccesful() == true){
                startActivity(new Intent(RegisterActivity.this, CalendarActivity.class));
            }
            else{
                if(user.getLoginid() == null){
                    mNameView.setError("Username ya utilizado");
                    mNameView.requestFocus();
                    focusView = mNameView;
                }
                else {
                    mUsernameView.setError("No ha sido posible registrarse");
                    mPasswordView.requestFocus();
                    focusView = mPasswordView;

                    //TODO: cuando te registras = inicias sesión en el calendar

                    //TODO: no poder volver atrás

                }
            }

        }

        @Override
        protected void onCancelled() {
            Log.d(TAG, "cancelled y peto");
            mAuthTask = null;
            showProgress(false);

        }
    }
}
