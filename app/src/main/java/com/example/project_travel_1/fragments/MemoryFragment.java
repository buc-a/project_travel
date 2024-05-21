package com.example.project_travel_1.fragments;

import android.app.Activity;
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

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project_travel_1.R;
import com.example.project_travel_1.objects.Memory;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Random;


public class MemoryFragment extends Fragment {

    public static final int IMAGE_CODE = 1;
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;


    Button btn_save, btn_see;
    EditText et_place, et_description;
    ImageView new_photo;
    Uri image_uri;
    String place, description;



    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_memory, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference(mAuth.getUid()).child("memory");
        mStorageRef = FirebaseStorage.getInstance().getReference();

        et_description = (EditText) view.findViewById(R.id.et_description);
        et_place = (EditText) view.findViewById(R.id.et_place);
        btn_save = (Button) view.findViewById(R.id.btn_add);
        btn_see = (Button) view.findViewById(R.id.btn_see);
        new_photo = (ImageView) view.findViewById(R.id.new_photo);


        ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri ->{
            //вызывается после того, как пользователь закроет окно выбора фото
            if ( uri != null )
            {
                Log.d("MyTag","Photo URI: " + uri);
                new_photo.setImageURI(uri);

            }

            else {
                Log.d("MyTag", "Photo is not found");
            }
        });



        //добавление данных в бд
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                place = et_place.getText().toString();
                description = et_description.getText().toString();
                if ( !TextUtils.isEmpty(place) && !TextUtils.isEmpty(description))
                {

                    imageToString();
                }
                else
                {
                    Toast.makeText(getActivity(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //переход на фрагмент с просмотром бд
        btn_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(view).navigate(R.id.action_memoryFragment_to_seeMemoryFragment);
            }
        });


        //добавление выбранного фото на экран
        new_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());


            }
        });
        return view;
    }

    public void imageToString()
    {
        Bitmap bitmap = ((BitmapDrawable)new_photo.getDrawable()).getBitmap();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteStream);
        byte[] bytes = byteStream.toByteArray();


        et_place.setText("");
        et_description.setText("");
        new_photo.setImageResource(R.drawable.no_photo);


        Date d = new Date();
        Random r = new Random(d.getTime());

        final StorageReference myRef = mStorageRef.child(String.valueOf(r.nextDouble()));
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

                image_uri = task.getResult();


                Memory memory = new Memory(place, description, image_uri.toString());

                Log.d("MyTag", "image_uri = "+image_uri.toString());
                mDataBase.push().setValue(memory);

                Toast.makeText(getActivity(), "Воспоминание добавлено", Toast.LENGTH_SHORT).show();

                

            }
        });

    }




}