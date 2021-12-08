package com.example.usodeapis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    Spinner spnr;
    TextView txtInfoLoad;
    RequestQueue rq;
    TextView txtvalor;
    private String URL = "https://gorest.co.in/public/v1/users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtInfoLoad = findViewById(R.id.txt_info);
        txtvalor = findViewById(R.id.txt_valor);
        txtInfoLoad.setMovementMethod(new ScrollingMovementMethod());
        addSpinerInfo();
        rq = Volley.newRequestQueue(this);
    }

    public void addSpinerInfo() {
        spnr = findViewById(R.id.spnr_apis);
        ArrayAdapter<CharSequence> loadSpiner =
                ArrayAdapter.createFromResource(this,
                        R.array.apis,
                        android.R.layout.simple_spinner_item);
        spnr.setAdapter(loadSpiner);
    }

    public void getRetrofit(String valor) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://gorest.co.in/").
                addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfazInfoInscriptionPais revistas = retrofit.create(InterfazInfoInscriptionPais.class);
        Call<List<data>> call = revistas.find(valor);
        call.enqueue(new Callback<List<data>>() {
            @Override
            public void onResponse(Call<List<data>> call, Response<List<data>> response) {
                try {
                    if(response.isSuccessful())
                        txtInfoLoad.setText("");
                    List<data> dataR = response.body();
                    String resultado = "";
                    for (data datos : dataR) {
                        resultado += " id: " + datos.getId() + "\n";
                        resultado += " name: " + datos.getName() + "\n";
                        resultado += " email: " + datos.getEmail() + "\n";
                        resultado += " gender: " + datos.getGender() + "\n";
                        resultado += " status: " + datos.getStatus() + "\n";

                        System.out.println("Entro a escribir retroft");

                        System.out.println(resultado);
                        System.out.println("Salio a escribir retroft");
                    }
                     txtInfoLoad.append(resultado);

                } catch (Exception ex) {
                              txtInfoLoad.setText(ex.toString());
                }



            }

            @Override
            public void onFailure(Call<List<data>> call, Throwable t) {

                String msj = "ERROR..!" + t.getMessage();
                txtInfoLoad.setText(msj);
            }
        });

    }


    private void stringRequestVolley(String valor) {
        ArrayList<String> ListDatos = new ArrayList<String>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                URL + "?id=" + valor, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectdata = jsonArray.getJSONObject(i);
                                ListDatos.add("id :"+ jsonObjectdata.getString("id")+"\n"+
                                        "name : " + jsonObjectdata.getString("name") + "\n" +
                                        "email : " + jsonObjectdata.getString("email") + "\n" +
                                        "gender : " + jsonObjectdata.getString("gender") + " \n" +
                                        "status : " + jsonObjectdata.getString("status") + "\n\n");
                            }
                            txtInfoLoad.append(ListDatos.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txtInfoLoad.append(error.toString());
            }
        });
        rq.add(jsonObjectRequest);
    }

    public void getApisBotton(View view) {
        txtInfoLoad.setText("");
        if (spnr.getSelectedItem().toString().toUpperCase().equals("Retrofit".toUpperCase())) {
            Toast.makeText(this, "Cargando...", Toast.LENGTH_SHORT).show();
            getRetrofit(txtvalor.getText().toString());

        } else if (spnr.getSelectedItem().toString().toUpperCase().equals("Volley".toUpperCase())) {
            Toast.makeText(this, "Cargando...", Toast.LENGTH_SHORT).show();
            stringRequestVolley(txtvalor.getText().toString());


        } else {
            txtInfoLoad.setText("");
            Toast.makeText(spnr.getContext(), "Seleccionar una librería", Toast.LENGTH_SHORT).show();
        }

    }

}