package com.example.mialdebu.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mialdebu.Adapters.CheckableSpinnerAdapter;
import com.example.mialdebu.Models.Comunicado;
import com.example.mialdebu.Models.SpinnerCheckbox;
import com.example.mialdebu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ServerTimestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComunicadosActivity extends AppCompatActivity {

    EditText Comun_edit;
    Button Volver,Agregar;
    ProgressBar pb;
    private CheckBox alldiv,allyears;
    private String[] divisionlist,añosList;
    Spinner div_Spinner,yearSpinner;
    private List<SpinnerCheckbox> spinList;
    FirebaseAuth mAuth;


    private final  List<CheckableSpinnerAdapter.SpinnerItem<SpinnerCheckbox>> spinner_items_Div = new ArrayList<>();
    private final  Set<SpinnerCheckbox> selected_items_Div = new HashSet<>();

    private final  List<CheckableSpinnerAdapter.SpinnerItem<SpinnerCheckbox>> spinner_items_Years = new ArrayList<>();
    private final  Set<SpinnerCheckbox> selected_items_Years = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunicados);
        Volver = findViewById(R.id.comun_Btn_Volver);
        Agregar = findViewById(R.id.comun_Btn_Agregar);
        Comun_edit = findViewById(R.id.comun_edit);
        pb = findViewById(R.id.comun_ProgressBar);
        divisionlist =  getResources().getStringArray(R.array.div);
        añosList = getResources().getStringArray(R.array.Años);
        alldiv = findViewById(R.id.all_div);
        allyears = findViewById(R.id.all_Years);
        div_Spinner = findViewById(R.id.comun_div_spinner);
        yearSpinner = findViewById(R.id.comun_year_spinner);

        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setTitle("Subir un Comunicado");

        final List<SpinnerCheckbox> all_objects = new ArrayList<>();  // from wherever

            for(int i = 0;i<divisionlist.length;i++) {
                SpinnerCheckbox spinnerCheckbox = new SpinnerCheckbox();
                spinnerCheckbox.setInfo(divisionlist[i]);
                spinnerCheckbox.setSelected(false);
                all_objects.add(spinnerCheckbox);
            }

        for(SpinnerCheckbox o : all_objects) {
            spinner_items_Div.add(new CheckableSpinnerAdapter.SpinnerItem<>(o,o.getInfo()));
        }

        // to start with any pre-selected, add them to the `selected_items_Div` set

        String headerText = "Divisiones";


        CheckableSpinnerAdapter adapter = new CheckableSpinnerAdapter<>(this, headerText, spinner_items_Div, selected_items_Div);
        div_Spinner.setAdapter(adapter);


        
        
        alldiv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    selected_items_Div.addAll(all_objects);
                else
                    for(SpinnerCheckbox d:all_objects) {
                        selected_items_Div.remove(d);
                    }

            }
        });



        final List<SpinnerCheckbox> all_years_obj = new ArrayList<>();  // from wherever

        for(int i = 0;i<añosList.length;i++) {
            SpinnerCheckbox spinnerCheckbox = new SpinnerCheckbox();
            spinnerCheckbox.setInfo(añosList[i]);
            spinnerCheckbox.setSelected(false);
            all_years_obj.add(spinnerCheckbox);
        }

        for(SpinnerCheckbox o : all_years_obj) {
            spinner_items_Years.add(new CheckableSpinnerAdapter.SpinnerItem<>(o,o.getInfo()));
        }

        // to start with any pre-selected, add them to the `selected_items_Years` set

        String yearText = "Años";


        CheckableSpinnerAdapter yearsAdapter = new CheckableSpinnerAdapter<>(this, yearText, spinner_items_Years, selected_items_Years);
        yearSpinner.setAdapter(yearsAdapter);



        allyears.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    selected_items_Years.addAll(all_years_obj);
                else
                    for(SpinnerCheckbox d:all_years_obj) {
                        selected_items_Years.remove(d);
                    }

            }
        });
        
        
        Volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Comun_edit.length()<300) {
                    Cargando();
                    SubirComunicado(selected_items_Div, selected_items_Years, Comun_edit.getText().toString());
                }
                else
                    Toast.makeText(ComunicadosActivity.this, "El Comunicado es Demasiado Largo.\n El limite de caracteres es 256", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void Cargando() {
        pb.setVisibility(View.VISIBLE);
        Agregar.setVisibility(View.INVISIBLE);
        Volver.setVisibility(View.INVISIBLE);
    }


    private void SubirComunicado(Set<SpinnerCheckbox> selected_items_Div, Set<SpinnerCheckbox> selected_items_Years, String toString) {

        FirebaseFirestore mDataRef = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Comunicado comunicado

                = new Comunicado();

        comunicado.setCurso(PasarDeSpinnerALista(selected_items_Years,añosList));
        Timestamp t =Timestamp.now ();
        comunicado.setTime(sdf.format( t.toDate ()));

        comunicado.setDivisiones(PasarDeSpinnerALista(selected_items_Div,divisionlist));

        comunicado.setComunicado(toString);

        comunicado.setSubidoPor(mAuth.getCurrentUser().getDisplayName());


        mDataRef.collection("Comunicados").add(comunicado).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(!task.isSuccessful())
                    Toast.makeText(ComunicadosActivity.this, "No se Pudo Subir el Comunicado", Toast.LENGTH_SHORT).show();
                else
                    finish();
            }
        });


    }

    private String PasarDeSpinnerALista(Set<SpinnerCheckbox> selected_items,String[] lista) {


        String yList = new String();


        for(SpinnerCheckbox d:selected_items) {
                   yList+=d.getInfo()+", ";



        }
        
        return yList;
    }
}
