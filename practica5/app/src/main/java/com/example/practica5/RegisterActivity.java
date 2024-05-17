package com.example.practica5;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.practica5.databinding.ActivityRegisterBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private PreferenceManager preferenceManager;
    private String encodedImage;

    private final ActivityResultLauncher<Intent> tomarImagen = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
       if (result.getResultCode() == RESULT_OK) {
           if (result.getData() != null) {
               Uri imageUri = result.getData().getData();
               try {
                   InputStream is = getContentResolver().openInputStream(imageUri);
                   Bitmap bitmap = BitmapFactory.decodeStream(is);
                     binding.FotoPerfil.setImageBitmap(bitmap);
                     binding.txtAgregarImagen.setVisibility(View.GONE);
                     encodedImage = condificarImagen(bitmap);
               } catch (FileNotFoundException e) {
                   throw new RuntimeException(e);
               }
           }
       }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
    }

    private void setListeners() {
        binding.txtIniciaSesion.setOnClickListener(v -> onBackPressed());
        binding.btnRegistrate.setOnClickListener(v -> {
            if (validarDetallesRegistro()) {
                registrarUsuario();
            }
        });
        binding.layoutImage.setOnClickListener(v -> {
            Intent mediaIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            mediaIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tomarImagen.launch(mediaIntent);
        });
    }

    private void registrarUsuario() {
        cargando(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME, binding.etNombre.getText().toString());
        user.put(Constants.KEY_EMAIL, binding.etCorreo.getText().toString());
        user.put(Constants.KEY_PASSWORD, binding.etContrasena.getText().toString());
        user.put(Constants.KEY_IMAGE, encodedImage);

        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    cargando(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_NAME, binding.etNombre.getText().toString());
                    preferenceManager.putString(Constants.KEY_EMAIL, binding.etCorreo.getText().toString());
                    //! Change
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    cargando(false);
                    mostrarMensaje("Error: " + e.getMessage());
                });
    }

    private void cargando(boolean isLoading) {
        if (isLoading) {
            binding.btnRegistrate.setVisibility(View.INVISIBLE);
            binding.BarraProgreso.setVisibility(View.VISIBLE);
        } else {
            binding.btnRegistrate.setVisibility(View.VISIBLE);
            binding.BarraProgreso.setVisibility(View.INVISIBLE);
        }
    }

    private boolean validarDetallesRegistro() {
        if (encodedImage == null) {
            mostrarMensaje("Selecciona una imagen");
            return false;
        }
        if (binding.etNombre.getText().toString().trim().isEmpty()) {
            mostrarMensaje("Ingresa tu nombre");
            return false;
        }
        if (binding.etCorreo.getText().toString().trim().isEmpty()) {
            mostrarMensaje("Ingresa tu correo electrónico");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.etCorreo.getText().toString()).matches()) {
            mostrarMensaje("Ingresa un correo electrónico válido");
            return false;
        }
        if (binding.etContrasena.getText().toString().trim().isEmpty()) {
            mostrarMensaje("Ingresa tu contraseña");
            return false;
        }
        if (binding.etContrasena.getText().toString().length() < 6) {
            mostrarMensaje("La contraseña debe tener al menos 6 caracteres");
            return false;
        }
        if (binding.etConfirmaContrasena.getText().toString().trim().isEmpty()) {
            mostrarMensaje("Las contraseñas no coinciden");
            return false;
        }
        return true;
    }

    private void mostrarMensaje(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private String condificarImagen(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap preview = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        preview.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);
    }
}