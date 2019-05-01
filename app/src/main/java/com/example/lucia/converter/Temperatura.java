package com.example.lucia.converter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.example.lucia.converter.logic.UnitateDeMasura;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Temperatura extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private static final List<String> denumireMonede = Arrays.asList("C", "F", "k", "R");

    private Set<UnitateDeMasura> unitateDeMasuraSet;

    private String dropDown;
    private EditText mEdit;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.greutate);

        spinner = findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Temperatura.this,
                android.R.layout.simple_spinner_item, denumireMonede);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        initSetUnitateDeMasura();

        mButton = findViewById(R.id.button2);
        mEdit = findViewById(R.id.editText2);

        mButton.setOnClickListener(
                view -> {
                    Log.v("EditText", mEdit.getText().toString());
                    Log.v("dropDown", dropDown);
                    UnitateDeMasura unitateDeMasura = UnitateDeMasura.findUnitateDeMasuraByName(unitateDeMasuraSet, dropDown);
                    BigDecimal amount = new BigDecimal(mEdit.getText().toString());
                    unitateDeMasura.setValue(amount);
                    for (UnitateDeMasura mon : unitateDeMasuraSet) {
                        if (!mon.getNume().equals(unitateDeMasura.getNume())) {
                            unitateDeMasura.specialConvertTemperatureTo(mon, amount);
                        }
                    }

                    for (UnitateDeMasura mon : unitateDeMasuraSet) {
                        Log.d("UnitateDeMasura Convert", mon.toString());
                    }

                    TableLayout convertTable = findViewById(R.id.displayLayout);
                    convertTable.setStretchAllColumns(true);
                    convertTable.bringToFront();
                    convertTable.removeAllViews();

                    TableRow tr = new TableRow(this);
                    TextView c1 = new TextView(this);
                    c1.setText("UNITATE");
                    TextView c2 = new TextView(this);
                    c2.setText("RATA(C)");
                    TextView c3 = new TextView(this);
                    c3.setText("TEMPERATURA:");
                    tr.addView(c1);
                    tr.addView(c2);
                    tr.addView(c3);
                    convertTable.addView(tr);

                    for (UnitateDeMasura mon : unitateDeMasuraSet) {
                        tr = new TableRow(this);
                        c1 = new TextView(this);
                        c1.setText(mon.getNume());
                        c2 = new TextView(this);
                        c2.setText(String.valueOf(mon.getRate()));
                        c3 = new TextView(this);
                        c3.setText(String.valueOf(mon.getValue()));
                        tr.addView(c1);
                        tr.addView(c2);
                        tr.addView(c3);

                        convertTable.addView(tr);
                    }

                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                dropDown = "C";
                break;
            case 1:
                dropDown = "F";
                break;
            case 2:
                dropDown = "K";
                break;
            case 3:
                dropDown = "R";
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void initSetUnitateDeMasura() {
        unitateDeMasuraSet = new HashSet<>();
        unitateDeMasuraSet.add(new UnitateDeMasura("C", BigDecimal.valueOf(1)));
        unitateDeMasuraSet.add(new UnitateDeMasura("F", BigDecimal.valueOf(-17.22223)));
        unitateDeMasuraSet.add(new UnitateDeMasura("K", BigDecimal.valueOf(-272.15)));
        unitateDeMasuraSet.add(new UnitateDeMasura("R", BigDecimal.valueOf(-272.594445)));

    }
}
