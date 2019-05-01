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

public class Greutate extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner spinner;
    private static final List<String> denumireMonede = Arrays.asList("G", "KG", "LB", "MG",
            "SLUG", "T", "OZ(AV)", "OZ(TROY)");

    private Set<UnitateDeMasura> unitateDeMasuraSet;

    private String dropDown;
    private EditText mEdit;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.greutate);

        spinner = findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Greutate.this,
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
                    c2.setText("RATA(KG)");
                    TextView c3 = new TextView(this);
                    c3.setText("GREUTATE:");
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
                dropDown = "G";
                break;
            case 1:
                dropDown = "KG";
                break;
            case 2:
                dropDown = "LB";
                break;
            case 3:
                dropDown = "MG";
                break;
            case 4:
                dropDown = "SLUG";
                break;
            case 5:
                dropDown = "T";
                break;
            case 6:
                dropDown = "OZ(AV)";
                break;
            case 7:
                dropDown = "OZ(TROY)";
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void initSetUnitateDeMasura() {
        unitateDeMasuraSet = new HashSet<>();
        unitateDeMasuraSet.add(new UnitateDeMasura("G", BigDecimal.valueOf(0.001)));
        unitateDeMasuraSet.add(new UnitateDeMasura("KG", BigDecimal.valueOf(1)));
        unitateDeMasuraSet.add(new UnitateDeMasura("LB", BigDecimal.valueOf(0.453592)));
        unitateDeMasuraSet.add(new UnitateDeMasura("MG", BigDecimal.valueOf(0.000001)));
        unitateDeMasuraSet.add(new UnitateDeMasura("SLUG", BigDecimal.valueOf(14.593903)));
        unitateDeMasuraSet.add(new UnitateDeMasura("T", BigDecimal.valueOf(1000)));
        unitateDeMasuraSet.add(new UnitateDeMasura("OZ(AV)", BigDecimal.valueOf(0.0283495)));
        unitateDeMasuraSet.add(new UnitateDeMasura("OZ(TROY)", BigDecimal.valueOf(0.0311035)));
    }
}
