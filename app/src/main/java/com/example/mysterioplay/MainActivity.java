package com.example.mysterioplay;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.myListView);

        runtimePermission();

    }

    public void runtimePermission(){
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        display();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                    }
                }).check();

    }

    public ArrayList<File> findSong(File file){
        ArrayList<File> al = new ArrayList<>();
        File[] files = file.listFiles();

//        File folderPath = new File(getFilesDir() + "/" + getResources().getString(R.string.app_name));
//
//        if (folderPath.exists()) {

            for (File singleFile : files) {

                    if (singleFile.isDirectory() && !singleFile.isHidden()) {
                        al.addAll(findSong(singleFile));
                    } else {
                        if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")
                                || singleFile.getName().endsWith(".aac")) {
                            al.add(singleFile);
                        }
                    }
                }
//            }

        return al;
    }


    public void display(){
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
        items= new String[mySongs.size()];
        for (int i=0;i<mySongs.size();i++){
            items[i]=mySongs.get(i).getName().toString().replace(".mp3", "").replace(".wav","")
            .replace(".aac", "");

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String songName =  listView.getItemAtPosition(position).toString();

                startActivity(new Intent(MainActivity.this, PlayMusic.class)
                .putExtra("songs", mySongs).putExtra("songName", songName)
                .putExtra("pos", position));

            }
        });

    }
}