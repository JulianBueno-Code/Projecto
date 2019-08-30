package com.example.mialdebu.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuItemView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mialdebu.Fragments.HomeFragment;
import com.example.mialdebu.Fragments.ProfileFragment;
import com.example.mialdebu.Fragments.SettingsFragment;
import com.example.mialdebu.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ProfileFragment pf;
    private SettingsFragment SF;
    private int idPage = 1;
    FloatingActionButton fab;
    private HomeFragment hf;


    public void Accion(int idPag)
    {
        switch (idPag)
        {
            case 1:
                Intent homeintent = new Intent(getApplicationContext(),ComunicadosActivity.class);
                startActivity(homeintent);


                break;
            case 2:
                break;
            case 3:
                Intent intent = new Intent(getApplicationContext(),Alumno_Activity.class);
                break;
            case 4:
                break;
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        hf = new HomeFragment();
        pf = new ProfileFragment();
        SF= new SettingsFragment();

        getSupportActionBar().setTitle("Comunicados");

        fab = findViewById(R.id.fab);
        fab.setImageResource(R.drawable.baseline_keyboard_capslock_white_18dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Accion(idPage);

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        updateNavHeader();

        //se configura para que el home_fragment sea la pagina predeterminada
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Asistencia) {
            Toast.makeText(this, "Esta es una Funcion aun en desarrollo", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        //Aca se le da las acciones a los Botones del costado
        if (id == R.id.nav_home) {
            idPage = 1;
            fab.show();
            fab.setImageResource(R.drawable.baseline_keyboard_capslock_white_18dp);

            getSupportActionBar().setTitle("Comunicados");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, hf).commit();


        } else if (id == R.id.nav_profile) {
            idPage = 2;
            fab.hide();

            getSupportActionBar().setTitle("Alumnos");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, pf).commit();


        } /* else
          if(id == R.id.nav_Asistencia)
        {
            getSupportActionBar().setTitle("Asistencia");
            Intent i = new Intent(getApplicationContext(),AsistenciaActivity.class);
            startActivity(i);


        }*/else if (id == R.id.nav_settings) {
            idPage = 3;
            fab.hide();
            getSupportActionBar().setTitle("Configuracion");

            getSupportFragmentManager().beginTransaction().replace(R.id.container,SF).commit();


        } else if (id == R.id.nav_signout) {
            idPage = 4;

            FirebaseAuth.getInstance().signOut();

            Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginActivity);
            finish();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateNavHeader()
    {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.nav_user_name);
        TextView navUserMail = headerView.findViewById(R.id.nav_user_email);
        ImageView navuserPhoto = headerView.findViewById(R.id.nav_user_Photo);

        navUserMail.setText(currentUser.getEmail());
        navUserName.setText(currentUser.getDisplayName());


        Glide.with(this).load(currentUser.getPhotoUrl()).into(navuserPhoto);


    }
}
