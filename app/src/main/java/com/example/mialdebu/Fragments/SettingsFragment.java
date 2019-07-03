package com.example.mialdebu.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mialdebu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDataRef;
    private EditText Nombre,DNI,Mail;
    private  String NOmb,DIN,Meil;
    private Button btn;

    private ImageView userPhoto;
    FirebaseUser user;


    private OnFragmentInteractionListener mListener;



    public SettingsFragment( ) {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        Nombre = v.findViewById(R.id.prof_Name);
        Mail = v.findViewById(R.id.prof_Email);
        DNI = v.findViewById(R.id.prof_DNI);
        mAuth = FirebaseAuth.getInstance();

        userPhoto = v.findViewById(R.id.prof_UserPhoto);
        mDatabase = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();
        mDataRef = mDatabase.getReference().child("usuarios").child(user.getUid()).child("DNI");
        mDataRef.keepSynced(true);
        NOmb = user.getDisplayName();
        Meil = user.getEmail();


        btn = v.findViewById(R.id.btn_ActualizarInformacion);

        mDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        try {
                            DIN = snapshot.getValue().toString();
                            DNI.setText(snapshot.getValue().toString());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Nombre.setText(NOmb);
        Mail.setText(Meil);
        DNI.setText(DIN);


        Glide.with(this).load(user.getPhotoUrl()).into(userPhoto);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser currentUser = mAuth.getCurrentUser();
                if(!Mail.getText().toString().equals(user.getEmail()))
                user.updateEmail(Mail.getText().toString());
                if(!Nombre.getText().toString().equals(user.getDisplayName()))
                {
                    UserProfileChangeRequest pprofileupdate = new UserProfileChangeRequest.Builder()
                            .setDisplayName(Nombre.getText().toString())
                            .build();
                    user.updateProfile(pprofileupdate)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //se actualizo la informacion del usuario
                                        Toast.makeText(getContext(), "Se  Actualizo correctamente :D", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                if (!DNI.getText().toString().equals(DIN)) {
                    mDataRef.setValue(DNI.getText().toString());
                }


            }
        });




        // Inflate the layout for this fragment
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

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
}
