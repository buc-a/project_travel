package com.example.project_travel_1.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_travel_1.LoginActivity;
import com.example.project_travel_1.MainActivity;
import com.example.project_travel_1.R;
import com.example.project_travel_1.objects.Memory;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Random;


public class ProfileFragment extends Fragment {


    private FirebaseAuth mAuth;
    Button btn_exit, btn_toMemory, btn_my_nodes;
    ShapeableImageView avatar;
    StorageReference mStorageRef;
    DatabaseReference mDataBase;
    TextView tv_login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference(mAuth.getUid());
        mStorageRef = FirebaseStorage.getInstance().getReference();
        tv_login = (TextView) view.findViewById(R.id.tv_login);

        btn_exit = (Button) view.findViewById(R.id.btn_exit);
        btn_my_nodes = (Button) view.findViewById(R.id.my_nodes);
        btn_toMemory = (Button) view.findViewById(R.id.my_memory);

        avatar = (ShapeableImageView) view.findViewById(R.id.image_avatar);

        //устанавливаем аватар (если есть) и логин
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("login").exists())
                {
                    tv_login.setText(snapshot.child("login").getValue().toString());
                }
            if (snapshot.child("avatar").exists())
            {
                Picasso.get().load((snapshot.child("avatar").getValue().toString())).into(avatar);
                Log.d("MyTag", "Фото установлено" + mDataBase.child("avatar").toString());
            }
            else
            {
                Log.d("MyTag", "Фото профиля не найденo");
                avatar.setImageResource(R.drawable.prifile);
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("MyTag", "Ошибка при чтении из бд");
            }
        };
        mDataBase.addValueEventListener(listener);


        //выход из аккаунта
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Activity activity = getActivity();
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_my_nodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_myNodesFragment);
            }
        });

        //переход к воспоминаниям
        btn_toMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_seeMemoryFragment);
            }
        });


        ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri ->{
            //вызывается после того, как пользователь закроет окно выбора фото
            if ( uri != null )
            {
                Log.d("MyTag","Photo URI: " + uri);
                avatar.setImageURI(uri);



                Bitmap bitmap = ((BitmapDrawable)avatar.getDrawable()).getBitmap();
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteStream);
                byte[] bytes = byteStream.toByteArray();


                StorageReference myRef = mStorageRef.child(mAuth.getUid());
                UploadTask up = myRef.putBytes(bytes);

                Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        //звгрузили картинку
                        return myRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    //сохранили ссылку на картинку, которая нам вернулась
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        Uri image_uri = task.getResult();

                        Log.d("MyTag", "image_uri = "+image_uri.toString());
                        mDataBase.child("avatar").setValue(image_uri.toString());



                    }
                });

            }

        });

        //добавление аватара
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());



            }
        });
        return view;
    }
}