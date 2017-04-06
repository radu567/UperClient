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

import md.uper.uperclient.app.*;


public class SignupActivity extends Activity {
    private static final String TAG = SignupActivity.class.getSimpleName();
    private EditText input_nume;
    private EditText input_prenume;
    private EditText input_telefon;
    private EditText input_oras;
    private EditText input_email;
    private EditText input_parola;

    private ProgressDialog pDialog;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button btn_signup = (Button) findViewById(R.id.btn_signup);
        Button btn_link_to_login = (Button) findViewById(R.id.btn_link_to_login);
        input_nume = (EditText) findViewById(R.id.nume);
        input_prenume = (EditText) findViewById(R.id.prenume);
        input_telefon = (EditText) findViewById(R.id.telefon);
        input_oras = (EditText) findViewById(R.id.oras);
        input_email = (EditText) findViewById(R.id.email);
        input_parola = (EditText) findViewById(R.id.parola);


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        SessionManager session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if client is already logged in or not
        if (session.isLoggedIn()) {
            // client is already logged in. Take him to main activity
            Intent intent = new Intent(SignupActivity.this,
                    ClientInfoActivity.class);
            startActivity(intent);
            finish();
        }

        // Signup Button Click event
        btn_signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String nume = input_nume.getText().toString().trim();
                String prenume = input_prenume.getText().toString().trim();
                String telefon = input_telefon.getText().toString().trim();
                String oras = input_oras.getText().toString().trim();
                String email = input_email.getText().toString().trim();
                String parola = input_parola.getText().toString().trim();

                if (!nume.isEmpty() && !prenume.isEmpty() &&!telefon.isEmpty() && !oras.isEmpty() && !email.isEmpty() && !parola.isEmpty()) {
                    Signupclient(nume, prenume, telefon, oras, email, parola);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Indicati toate datele!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btn_link_to_login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * stocarea clientilor in db (tag, nume,
     * email, password) to Signup url
     * */
    private void Signupclient(final String nume, final String prenume, final String telefon, final String oras, final String email,
                              final String parola) {
        // Tag used to cancel the request
        String tag_string_req = "req_Signup";

        pDialog.setMessage("Inregistrare ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                Config.url_signup, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Signup Raspuns: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // client stocat cu succes in  MySQL
                        // Stocare client in SQLite

                        JSONObject client = jObj.getJSONObject("client");
                        String id_client = client.getString("id_client");
                        String nume = client.getString("nume");
                        String prenume = client.getString("prenume");
                        String telefon = client.getString("telefon");
                        String oras = client.getString("oras");
                        String email = client.getString("email");
                        String data_creare = client.getString("data_creare");

                        // Inserting row in clients table
                        // db.addclient(id_client, nume, prenume, telefon, oras, email, data_creare);
                        db.addclient(id_client, nume, prenume, telefon, oras, email, data_creare);

                        Toast.makeText(getApplicationContext(), "Client inregistrat. Logheaza-te!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                SignupActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Eroare ed inregistrare: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to Signup url
                Map<String, String> params = new HashMap<>();
                params.put("nume", nume);
                params.put("prenume", prenume);
                params.put("telefon", telefon);
                params.put("oras", oras);
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