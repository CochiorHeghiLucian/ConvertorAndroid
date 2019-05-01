package com.example.lucia.converter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.example.lucia.converter.logic.UnitateDeMasura;
import okhttp3.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Valuta extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private static final List<String> denumireMonede = Arrays.asList("RON", "USD", "EUR", "GBP",
            "CHF", "SEK", "MDL");

    private Set<UnitateDeMasura> monezi;
    private String dropDown;
    private EditText mEdit;
    private Button mButton;
    private Integer dropDownPosition;

    private Document loadXMLFromString(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

    private void initMonezi() {
        monezi = new HashSet<>();
        monezi.add(new UnitateDeMasura("EUR", new BigDecimal("1.0")));

        OkHttpClient client = new OkHttpClient();

        String url = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();

                    Valuta.this.runOnUiThread(() -> {
                        Document doc;
                        try {
                            doc = loadXMLFromString(myResponse);
                            NodeList nList = doc.getElementsByTagName("Cube");

                            for (int temp = 0; temp < nList.getLength(); temp++) {
                                Node nNode = nList.item(temp);
                                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element eElement = (Element) nNode;
                                    if (denumireMonede.contains(eElement.getAttribute("currency"))) {
                                        UnitateDeMasura unitateDeMasura = new UnitateDeMasura(eElement.getAttribute("currency"),
                                                new BigDecimal(eElement.getAttribute("rate")));
                                        monezi.add(unitateDeMasura);
                                    }
                                }


                            }

                        } catch (Exception e) {
                            Log.e("Eroare XLM", "Nu s-a putut face xml din string");
                            e.printStackTrace();
                        }

                        for (UnitateDeMasura mon : monezi) {
                            Log.d("rezultat", mon.getNume() + " " + mon.getRate().toString());
                        }


                    });
                }
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.valuta);
        spinner = findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Valuta.this,
                android.R.layout.simple_spinner_item, denumireMonede);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        initMonezi();

        mButton = findViewById(R.id.button2);
        mEdit = findViewById(R.id.editText2);

        mButton.setOnClickListener(
                view -> {
                    Log.v("EditText", mEdit.getText().toString());
                    Log.v("dropDown", dropDown);
                    UnitateDeMasura unitateDeMasura = UnitateDeMasura.findUnitateDeMasuraByName(monezi, dropDown);
                    BigDecimal amount = new BigDecimal(mEdit.getText().toString());
                    unitateDeMasura.setValue(amount);
                    for (UnitateDeMasura mon : monezi) {
                        if (!mon.getNume().equals(unitateDeMasura.getNume())) {
                            unitateDeMasura.convertTo(mon, amount);
                        }
                    }

                    for (UnitateDeMasura mon : monezi) {
                        Log.d("UnitateDeMasura Convert", mon.toString());
                    }

                    TableLayout convertTable = findViewById(R.id.displayLayout);
                    convertTable.setStretchAllColumns(true);
                    convertTable.bringToFront();
                    convertTable.removeAllViews();

                    TableRow tr = new TableRow(this);
                    TextView c1 = new TextView(this);
                    c1.setText("NUME");
                    TextView c2 = new TextView(this);
                    c2.setText("RATA(EUR)");
                    TextView c3 = new TextView(this);
                    c3.setText("SUMA");
                    tr.addView(c1);
                    tr.addView(c2);
                    tr.addView(c3);
                    convertTable.addView(tr);

                    for (UnitateDeMasura mon : monezi) {
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
        dropDownPosition = position;
        switch (position) {
            case 0:
                dropDown = "RON";
                break;
            case 1:
                dropDown = "USD";
                break;
            case 2:
                dropDown = "EUR";
                break;
            case 3:
                dropDown = "GBP";
                break;
            case 4:
                dropDown = "CHF";
                break;
            case 5:
                dropDown = "SEK";
                break;
            case 6:
                dropDown = "MDL";
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mEdit != null) {
            outState.putCharSequence("KEY_VALUE", mEdit.getText().toString());
        }

        if (dropDownPosition != null) {
            outState.putInt("KEY_DROPDOWN_POSITION", dropDownPosition);
        }

        if (dropDown != null) {
            outState.putCharSequence("KEY_DROPDOWN", dropDown);
        }

        Log.d("Test", "OnSaveInstance");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        CharSequence saveValue = savedInstanceState.getCharSequence("KEY_VALUE");
        Integer saveDropdownPosition = savedInstanceState.getInt("KEY_DROPDOWN_POSITION");
        CharSequence saveDropDown = savedInstanceState.getCharSequence("KEY_DROPDOWN");

        if (mEdit == null) {
            mEdit = findViewById(R.id.editText2);
        }
        if (saveValue != null) {
            mEdit.setText(saveValue);
        }

        if (saveDropdownPosition != null) {
            spinner.setSelection(saveDropdownPosition);

        }

        if (saveDropDown != null) {
            dropDown = saveDropDown.toString();
        }

        if (mButton != null) {
            mButton.callOnClick();
        }
        Log.d("Test", "OnRestoreInstance");

    }


}
