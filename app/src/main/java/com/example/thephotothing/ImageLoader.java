package com.example.thephotothing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

public class ImageLoader extends AppCompatActivity {

    private EditText folder,sec_code;
    private Button share,delete,logout,downloadImage;
    private String txt_folder,txt_sec_code,txt_email;
    private static final int PICK_IMAGE=1;
    private TextView email;
    ArrayList<Uri> Image = new ArrayList<Uri>();
    ArrayList<String> del_img= new ArrayList<String>();
    private Uri imageuri;
    private int upload_count = 0,share_value=0;
//    private FirebaseDatabase db;
//    FirebaseDatabase fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_loader);
        folder=findViewById(R.id.name_uploader);
        downloadImage=findViewById(R.id.image_loader_todownloader);
        downloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageLoader.this,Image_downloader.class);
                intent.putExtra("emaili",txt_email);
                startActivity(intent);

            }
        });
        sec_code=findViewById(R.id.security_code);
        logout=findViewById(R.id.logout_image_loader);
        share=findViewById(R.id.loader_image_loader);
        delete=findViewById(R.id.delete_image_loader);

        Intent intent = getIntent();
        txt_email = intent.getStringExtra("email");
        email= findViewById(R.id.email_profile);
        email.setText(txt_email);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_folder = folder.getText().toString();
                txt_sec_code = sec_code.getText().toString();
                if (txt_folder.isEmpty() || txt_sec_code.isEmpty()||txt_sec_code.length()<4){
                    Toast.makeText(ImageLoader.this, "empty views or lenght less than 4", Toast.LENGTH_SHORT).show();
                }else if(share_value == 0){
                    Toast.makeText(ImageLoader.this, "first start sharing to stop", Toast.LENGTH_SHORT).show();
                }else {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(txt_sec_code);
                    databaseReference.removeValue();
                    folder.setText("");
                    sec_code.setText("");




                    FirebaseStorage fb = FirebaseStorage.getInstance().getReference().getStorage();
                    for (int i = 0; i < del_img.size(); i++) {
                        StorageReference storageReference = fb.getReferenceFromUrl(del_img.get(i));
                        storageReference.delete().addOnCompleteListener(ImageLoader.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ImageLoader.this, "done delete", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                        share_value=0;
                    }
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_folder = folder.getText().toString();
                txt_sec_code = sec_code.getText().toString();
                if (txt_folder.isEmpty() || txt_sec_code.isEmpty()||txt_sec_code.length()<4){
                    Toast.makeText(ImageLoader.this, "empty views or lenght less than 4", Toast.LENGTH_SHORT).show();
                }else if (share_value==1){
                    Toast.makeText(ImageLoader.this, "First Delete Before Sharing Agian", Toast.LENGTH_SHORT).show();
                }
                else{

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(intent,PICK_IMAGE);}

            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ImageLoader.this,MainActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE){
            if (resultCode==RESULT_OK){

                if (data.getClipData()!=null){
                    txt_folder = folder.getText().toString();
                    txt_sec_code = sec_code.getText().toString();

                    StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child(txt_sec_code);


                    int countClipData = data.getClipData().getItemCount();
                    int currentImageSelect = 0;
                    while(currentImageSelect <countClipData){
                        imageuri =data.getClipData().getItemAt(currentImageSelect).getUri();
                        Image.add(imageuri);
                        currentImageSelect=currentImageSelect+1;




                    }
                    int ImageSize = Image.size();
                    for (upload_count=0;upload_count<Image.size();upload_count++){
                        Uri IndividualImage = Image.get(upload_count);
                        StorageReference ImageName = ImageFolder.child(txt_folder+upload_count);
                        ImageName.putFile(IndividualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url = uri.toString();
                                        del_img.add(url);
                                        StoreLink(url,txt_sec_code);
                                        share.setText("Sharing..");
                                        Toast.makeText(ImageLoader.this, "Sharing..", Toast.LENGTH_SHORT).show();
                                        share_value=1;


                                    }
                                });
                            }
                        });

                    }
                }else{
                    Toast.makeText(ImageLoader.this, "select multiple image", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private void StoreLink(String url,String sec_code) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(sec_code);
        databaseReference.push().setValue(url);




    }



}