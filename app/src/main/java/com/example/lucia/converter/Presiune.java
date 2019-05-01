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

public class Presiune extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Spinner spinner;
    private static final List<String> denumireMonede = Arrays.asList("AT", "ATM", "BAR", "LBF",
            "MBAR", "MCA", "N", "PASCAL");

    private Set<UnitateDeMasura> unitateDeMasuraSet;

    private String dropDown;
    private EditText mEdit;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.greutate);

        spinner = findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Presiune.this,
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
                            unitateDeMasura.specialConvertTo(mon, amount);
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
                    c2.setText("RATA(ATM)");
                    TextView c3 = new TextView(this);
                    c3.setText("PRESIUNE:");
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
                dropDown = "AT";
                break;
            case 1:
                dropDown = "ATM";
                break;
            case 2:
                dropDown = "BAR";
                break;
            case 3:
                dropDown = "LBF";
                break;
            case 4:
                dropDown = "MBAR";
                break;
            case 5:
                dropDown = "MCA";
                break;
            case 6:
                dropDown = "N";
                break;
            case 7:
                dropDown = "PASCAL";
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void initSetUnitateDeMasura() {
        //"AT", "ATM", "BAR", "LBF","MBAR", "MCA", "N", "PASCAL"
        unitateDeMasuraSet = new HashSet<>();
        unitateDeMasuraSet.add(new UnitateDeMasura("AT", BigDecimal.valueOf(0.9678384900648549)));
        unitateDeMasuraSet.add(new UnitateDeMasura("ATM", BigDecimal.valueOf(1)));
        unitateDeMasuraSet.add(new UnitateDeMasura("BAR", BigDecimal.valueOf(0.9671821878659459)));
        unitateDeMasuraSet.add(new UnitateDeMasura("LBF", BigDecimal.valueOf(0.000472537583214505)));
        unitateDeMasuraSet.add(new UnitateDeMasura("MBAR", BigDecimal.valueOf(0.0009671821878659459)));
        unitateDeMasuraSet.add(new UnitateDeMasura("MCA", BigDecimal.valueOf(0.09671821878659459)));
        unitateDeMasuraSet.add(new UnitateDeMasura("N", BigDecimal.valueOf(9.8692059986321)));
        unitateDeMasuraSet.add(new UnitateDeMasura("PASCAL", BigDecimal.valueOf(0.000009869205998632101)));
    }
}
