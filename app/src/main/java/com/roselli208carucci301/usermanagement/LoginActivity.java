package com.roselli208carucci301.usermanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button init = findViewById(R.id.init);

        init.setOnClickListener(v -> {
            EditText user = findViewById(R.id.email);
            EditText pass = findViewById(R.id.password);
            if(user.getText().toString().equals("admin@admin.com") && pass.getText().toString().equals("1234")){
                Toast.makeText(LoginActivity.this,"Logged in",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(LoginActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });

    }
}