package com.example.productosapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        edtSearchProduct = (EditText) findViewById(R.id.edtSearchProduct);
        edtProductName = (EditText) findViewById(R.id.edtProductName);
        edtProductPrice = (EditText) findViewById(R.id.edtProductPrice);
        edtProductManufacturer = (EditText) findViewById(R.id.edtProductManufacturer);

        btnSearchProduct = (Button) findViewById(R.id.btnSearchProduct);
        btnNewProduct = (Button) findViewById(R.id.btnNewProduct);
        btnEditProduct = (Button) findViewById(R.id.btnEditProduct);
    }
}