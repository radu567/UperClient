package md.uper.uperclient;

import md.uper.uperclient.app.SQLiteHandler;
import md.uper.uperclient.app.SessionManager;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ClientInfoActivity extends Activity implements View.OnClickListener{

    private static final String TAG = "ClientInfoActivity";
    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientinfo);


        TextView txtnume = (TextView) findViewById(R.id.nume);
        TextView txtEmail = (TextView) findViewById(R.id.email);
        Button btn_logout = (Button) findViewById(R.id.btn_logout);
        Button btn_harta = (Button) findViewById(R.id.btn_harta);

        btn_harta.setOnClickListener(this);

        Log.d(TAG, "pasul 1 este realizat");
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        Log.d(TAG, "pasul 2 este realizat");
        // session manager
        session = new SessionManager(getApplicationContext());
        Log.d(TAG, "pasul 3 este realizat");
        if (!session.isLoggedIn()) {
            logoutclient();
        }

        // Fetching client details from sqlite
        HashMap<String, String> client = db.getclientDetails();
        Log.d(TAG, "pasul 4 este realizat");
        try {
            String nume = client.get("nume");
            String email = client.get("email");
            txtnume.setText(nume);
            txtEmail.setText(email);
            Log.d(TAG, "pasul 5 este realizat");
        }
        catch (Exception e){
            Log.d(TAG, "pasul 5 nu este realizat", e);
        }

        // Afisare date in textview
        //txtnume.setText(nume);
        //txtEmail.setText(email);

        // Logout buton
        btn_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutclient();
            }
        });
    }

    /**
     * Logging out the client. Will set isLoggedIn flag to false in shared
     * preferences Clears the client data from sqlite clients table
     * */
    private void logoutclient() {
        session.setLogin(false);

        db.deleteclients();

        // Launching the login activity
        Intent intent = new Intent(ClientInfoActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ClientInfoActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}