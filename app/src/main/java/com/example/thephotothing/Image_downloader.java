package com.example.thephotothing;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Image_downloader extends AppCompatActivity {
    private EditText name,sec_code;
    private Button search ,logout,download_all;
    private String txt_name,txt_sec_code,txt_email;

    private TextView email_profile;
    private ArrayList<String> uris_down=new ArrayList<String>();
    private int size ,i;

    RecyclerView rc;
    ArrayList<modelClass> modelClassesuri = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_downloader);
        rc = findViewById(R.id.recy_download);






        name=findViewById(R.id.name_download);
        sec_code=findViewById(R.id.security_code_download);
        logout=findViewById(R.id.logout_download);
        search=findViewById(R.id.search_download);
        email_profile =findViewById(R.id.email_profile);
        Intent intent = getIntent();
        txt_email = intent.getStringExtra("emaili");
        email_profile.setText(txt_email);


        download_all=findViewById(R.id.search_download_all);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txt_sec_code=sec_code.getText().toString();
                txt_name=name.getText().toString();
                if (txt_name.isEmpty()||txt_sec_code.isEmpty()){
                    Toast.makeText(Image_downloader.this, "Empty views", Toast.LENGTH_SHORT).show();
                }else {
                    hideKeyboard(Image_downloader.this);

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child(txt_sec_code);
                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            size = (int) snapshot.getChildrenCount();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Image_downloader.this, "fail in getting size", Toast.LENGTH_SHORT).show();


                        }
                    });

                    StorageReference sdn = FirebaseStorage.getInstance().getReference().child(txt_sec_code);

                    for (i =0 ; i<size;i++) {

                        String end = String.valueOf(i);
                        sdn.child(txt_name+end).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                modelClassesuri.add(new modelClass(uri.toString()));
                                uris_down.add(uri.toString());
                                search.setVisibility(View.GONE);
                                download_all.setVisibility(View.VISIBLE);




                            }
                        });
                    }
                    rc.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                    rc.setNestedScrollingEnabled(false);
                    rc.setHasFixedSize(true);


                    uriAdapter adapter = new uriAdapter(Image_downloader.this,modelClassesuri);
                    rc.setAdapter(adapter);

                    adapter.notifyDataSetChanged();








                }
            }




        });



        download_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int c =0 ;c <modelClassesuri.size();c++){
                Uri uri = Uri.parse(modelClassesuri.get(c).getImg());
                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI| DownloadManager.Request.NETWORK_MOBILE);

                request.setTitle("file is downloading from url");
                request.setDescription("Android Data download using Downloadmanager.");

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"/images/"+"/"+"Salmankhan"+".png");
                request.setMimeType("*/*");
                downloadManager.enqueue(request);


            }}
        });

    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}