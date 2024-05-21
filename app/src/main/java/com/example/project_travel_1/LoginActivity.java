package com.example.project_travel_1;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_travel_1.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


//FirebaseAuth.getInstance().signOut() - выйти из аккаунта
public class LoginActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;
    private String login = NULL, password = NULL;
    private ActivityLoginBinding loginBinding;

    Button btn_continue, logIn, register;
    TextView text1;
    EditText et_login, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());


        btn_continue = loginBinding.butContinue;
        logIn = loginBinding.logIn;
        register = loginBinding.register;
        et_login = loginBinding.login;
        et_password = loginBinding.password;
        text1 = loginBinding.text1;



        mAuth = FirebaseAuth.getInstance();



        //авторизироваться
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = et_login.getText().toString();
                password = et_password.getText().toString();

                if ( !login.isEmpty() && !password.isEmpty()){
                    mAuth.signInWithEmailAndPassword(login, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Что-то пошло не так" , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(LoginActivity.this, "Не все данные введены", Toast.LENGTH_SHORT).show();

                }
            }
        });

        //зарегестрироваться
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = et_login.getText().toString();
                password = et_password.getText().toString();

                if ( !login.isEmpty() && !password.isEmpty()){

                    mAuth.createUserWithEmailAndPassword(login, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                mDataBase = FirebaseDatabase.getInstance().getReference(mAuth.getUid()).child("login");
                                mDataBase.setValue(login);
                                Toast.makeText(getApplicationContext(), "Вы зарегестрировались" , Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Что-то пошло не так" , Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
                else{
                    Toast.makeText(LoginActivity.this, "Не все данные введены", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        //проверка, зашел/авторизировался ли пользователь
        FirebaseUser fbUser = mAuth.getCurrentUser();
        if(fbUser != null){
            et_password.setVisibility(View.GONE);
            et_login.setVisibility(View.GONE);
            logIn.setVisibility(View.GONE);
            register.setVisibility(View.GONE);

            text1.setVisibility(View.VISIBLE);
            btn_continue.setVisibility(View.VISIBLE);
        }
        else{
            et_password.setVisibility(View.VISIBLE);
            et_login.setVisibility(View.VISIBLE);
            logIn.setVisibility(View.VISIBLE);
            register.setVisibility(View.VISIBLE);

            text1.setVisibility(View.GONE);
            btn_continue.setVisibility(View.GONE);
        }
    }
}
