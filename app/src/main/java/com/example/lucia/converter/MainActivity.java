package com.example.lucia.converter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("OnOptionItemSelected", "OnOptionItemSelected was called");
        Intent intent;
        switch (item.getItemId()) {
            case R.id.valuta:
                intent = new Intent(this, Valuta.class);
                startActivity(intent);
                return true;

            case R.id.lungime:
                intent = new Intent(this, Lungime.class);
                startActivity(intent);
                return true;

            case R.id.greutate:
                intent = new Intent(this, Greutate.class);
                startActivity(intent);
                return true;

            case R.id.temperatura:
                intent = new Intent(this, Temperatura.class);
                startActivity(intent);
                return true;

            case R.id.presiune:
                intent = new Intent(this, Presiune.class);
                startActivity(intent);
                return true;

            default:
                Log.d("default", "Intra");
                return super.onOptionsItemSelected(item);
        }

    }
}
