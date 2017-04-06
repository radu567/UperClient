package md.uper.uperclient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


import md.uper.uperclient.app.*;


public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";
    private EditText input_email;
    private EditText input_parola;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        input_email = (EditText) findViewById(R.id.email);
        input_parola = (EditText) findViewById(R.id.parola);
        Button btn_login = (Button) findViewById(R.id.btn_login);
        Button btn_link_to_sighup = (Button) findViewById(R.id.btn_link_to_signup);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if client is already logged in or not
        if (session.isLoggedIn()) {
            // client is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btn_login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = input_email.getText().toString().trim();
                String parola = input_parola.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !parola.isEmpty()) {
                    // login client
                    checkLogin(email, parola);
                } else {
                    // Prompt client to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Indicati datele de conectare!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btn_link_to_sighup.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        SignupActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * functia de verificare a datelor de logare in mysql db
     * */
    private void checkLogin(final String email, final String parola) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logare ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                Config.url_login, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Raspuns logare: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        Log.d(TAG, "pasul 1 este realizat");
                        // client successfully logged in
                        // Create login session
                        session.setLogin(true);
                        Log.d(TAG, "pasul 2 este realizat");
                        // Now store the client in SQLite
                        JSONObject client = jObj.getJSONObject("client");

                        Log.d(TAG, "pasul 3 este realizat");

                        String id_client = client.getString("id_client");
                        String nume = client.getString("nume");
                        String prenume = client.getString("prenume");
                        String telefon = client.getString("telefon");
                        String oras = client.getString("oras");
                        String email = client.getString("email");
                        String data_creare = client.getString("data_creare").substring(0, 10);

                        Log.d(TAG, "pasul 4 este realizat");

                        // Inserting row in clients table
                        try{
                            db.addclient(id_client, nume, prenume, telefon, oras, email, data_creare);
                        }
                        catch(Exception e){
                            Log.d(TAG, "pasul 5 nu este realizat", e);
                        }


                        Log.d(TAG, "pasul 5 este realizat");

                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();

                        Log.d(TAG, "pasul 6 este realizat");
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("parola", parola);

                return params;
            }

        };

        // Adding request to request queue
        Controller.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}