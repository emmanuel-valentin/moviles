package com.example.productosapp;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {
    // Volley and constants
    private RequestQueue requestQueue;
    private String BASE_URL = ""; // pon tu IP

    // EditText
    private EditText edtSearchProduct;
    private EditText edtProductName;
    private EditText edtProductPrice;
    private EditText edtProductManufacturer;

    // Buttons
    private Button btnSearchProduct;
    private Button btnNewProduct;
    private Button btnEditProduct;
    private Button btnDeleteProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Read env variables
        try {
            Properties properties = new Properties();
            AssetManager assets = getAssets();
            System.out.println(Arrays.toString(assets.list("/")));
            InputStream inputStream = assets.open("app.properties");
            properties.load(inputStream);
            BASE_URL = properties.getProperty("BASE_URL");
        } catch (IOException e) {
            e.printStackTrace();
        }

        edtSearchProduct = findViewById(R.id.edtSearchProduct);
        edtProductName = findViewById(R.id.edtProductName);
        edtProductPrice = findViewById(R.id.edtProductPrice);
        edtProductManufacturer = findViewById(R.id.edtProductManufacturer);

        btnSearchProduct = findViewById(R.id.btnSearchProduct);
        btnNewProduct = findViewById(R.id.btnNewProduct);
        btnEditProduct = findViewById(R.id.btnEditProduct);
        btnDeleteProduct = findViewById(R.id.btnDeleteProduct);

        btnSearchProduct.setOnClickListener(v -> {
            //! Verifica el puerto en el que corre la "RESTFUL API"
            searchProduct(BASE_URL + "/buscar_producto.php?codigo=" + edtSearchProduct.getText().toString());
        });

        btnNewProduct.setOnClickListener(v -> {
            //! Verifica el puerto en el que corre la "RESTFUL API"
            executeService(BASE_URL + "/insertar_producto.php");
        });

        btnEditProduct.setOnClickListener(v -> {
            //! Verifica el puerto en el que corre la "RESTFUL API"
            executeService(BASE_URL + "/editar_producto.php");
        });

        btnDeleteProduct.setOnClickListener(v -> {
            //! Verifica el puerto en el que corre la "RESTFUL API"
            deleteProduct(BASE_URL + "/eliminar_producto.php");
        });
    }

    private void executeService(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            Toast.makeText(getApplicationContext(), "Operación exitosa", Toast.LENGTH_SHORT).show();
        }, error -> {
            Toast.makeText(getApplicationContext(), "Error en la operación", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("codigo", edtSearchProduct.getText().toString());
                params.put("producto", edtProductName.getText().toString());
                params.put("precio", edtProductPrice.getText().toString());
                params.put("fabricante", edtProductManufacturer.getText().toString());
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void searchProduct(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, response -> {
            JSONObject jsonObject;
            for (int i = 0; i < response.length(); i++) {
                try {
                    jsonObject = response.getJSONObject(i);
                    edtProductName.setText(jsonObject.getString("producto"));
                    edtProductPrice.setText(jsonObject.getString("precio"));
                    edtProductManufacturer.setText(jsonObject.getString("fabricante"));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, error -> {
            Toast.makeText(getApplicationContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
        });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void deleteProduct(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            Toast.makeText(getApplicationContext(), "El producto ha sido eliminado", Toast.LENGTH_SHORT).show();
            resetForm();
        }, error -> {
            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("codigo", edtSearchProduct.getText().toString());
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void resetForm() {
        edtSearchProduct.setText("");
        edtProductName.setText("");
        edtProductPrice.setText("");
        edtProductManufacturer.setText("");
    }
}