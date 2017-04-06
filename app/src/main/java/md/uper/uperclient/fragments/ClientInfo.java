package md.uper.uperclient.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import md.uper.uperclient.R;
import md.uper.uperclient.app.SQLiteHandler;
import md.uper.uperclient.app.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClientInfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClientInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientInfo extends Fragment {

    private static final String TAG = "ClientInfoActivity";
    private SQLiteHandler db;
    private SessionManager session;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ClientInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClientInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientInfo newInstance(String param1, String param2) {
        ClientInfo fragment = new ClientInfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_client_info, container, false);
        TextView txtnume = (TextView) view.findViewById(R.id.nume);
        TextView txtEmail = (TextView) view.findViewById(R.id.email);
        Button btn_logout = (Button) view.findViewById(R.id.btn_logout);



        Log.d(TAG, "pasul 1 este realizat");
        // SqLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());
        Log.d(TAG, "pasul 2 este realizat");
        // session manager
        session = new SessionManager(getActivity().getApplicationContext());
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

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_info, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

/*    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void logoutclient() {
/*        session.setLogin(false);

        db.deleteclients();

        // Launching the login activity
        Intent intent = new Intent(ClientInfoActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();*/
    }
}
