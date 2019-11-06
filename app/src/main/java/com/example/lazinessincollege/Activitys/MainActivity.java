package com.example.lazinessincollege.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.drm.DrmInfoRequest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.lazinessincollege.Activitys.Cadastro.CadastroDiciplinasActivity;
import com.example.lazinessincollege.Activitys.Cadastro.CadastroUsuarioActivity;
import com.example.lazinessincollege.Adapters.RecyclerViewAdapter;
import com.example.lazinessincollege.BuildConfig;
import com.example.lazinessincollege.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import id.zelory.compressor.Compressor;
import org.apache.commons.io.IOUtils;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MEU MAIN";

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    public String currentPhotoPath;

    private ArrayList<String> mFotoUrl = new ArrayList<>();

    private Compressor mCompressor;

    private File mPhotoFile;

    private String mNomeFoto;

    private String PathDiretoryPhotos = "/storage/sdcard/Android/data/com.example.lazinessincollege/files/Pictures/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button btLoginCadastrar = findViewById(R.id.bt_cadastre_LoginActivity);
        btLoginCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CadastroUsuarioActivity.class);
                startActivity(intent);
            }
        });
        mCompressor = new Compressor(this);


    }

    public void entrar(View view) {
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = findViewById(R.id.tb_main);
        setSupportActionBar(mToolbar);
        getlistFolderNames();
        getFotos();
    }

    private void getlistFolderNames() {
        File tolist = new File(Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_PICTURES );
        File[] files = tolist.listFiles();
        assert files != null;
        Log.d(TAG, "Size: "+ files.length);
        String[] namesFolders = new String[files.length];
        int j=0;
        for (int i = 0; i < files.length; i++)
        {
            if (files[i].isDirectory()){
                namesFolders[j++]=files[i].getName();
                Log.i(TAG,"DIRETORIO: "+ files[i].getName());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    private void setSupportActionBar(Toolbar mToolbar) {
        mToolbar.inflateMenu(R.menu.menu_main);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (R.id.menu_item_cadastrar_dic == item.getItemId()) {
                    Intent i = new Intent(getApplicationContext(), CadastroDiciplinasActivity.class);
                    startActivity(i);
                }
                return true;
            }
        });
    }

    private void getFotos() {

        Log.i(TAG, "Carregando fotos");
        File directory = new File(String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES)));
        File[] files = directory.listFiles();
        Log.i(TAG,"LOCALL :: "+ directory.getName());
        assert files != null;
        Log.d(TAG, "Size: "+ files.length);
        mFotoUrl.clear();
        for (int i = 0; i < files.length; i++)
        {
            Log.i(TAG, "FileName:" + files[i].getName());
            if (files[i].getName().endsWith(".jpg")) mFotoUrl.add(PathDiretoryPhotos+ files[i].getName());
        }
        Collections.reverse(mFotoUrl);
        startRecyclerView();
    }

    private void startRecyclerView() {
        Log.d(TAG, "Iniciando RecyclerView");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mFotoUrl, this);
        recyclerView.setAdapter(adapter);

    }

    public void bt_tirar_foto(View view) {
        verificarPermissoes();
    }

    private void verificarPermissoes() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        {

                            if (report.areAllPermissionsGranted()) {
                                Log.i(TAG, "PERMISSOES CONCEDIDAS");
                                tirarFoto();
                            }
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                Log.i(TAG, "PERMISSAO NEGADA");
                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    Toast.makeText(getApplicationContext(), "VOCE DEVE PERMITIR O ACESSO A CAMERA", Toast.LENGTH_LONG);
                                    Log.i(TAG, "USUARIO SENDO FAZENDO M*** :: PERMISSAO NEGADA FOREVER");
                                } else {
                                    Log.i(TAG, "PERMISSAO NEGADA, MAS NAO FOREVER..REQUISITANDO MAIS UMA VEZ NOVAMETE");
                                    verificarPermissoes();
                                }
                            }
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).onSameThread().check();}

    private void tirarFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.i(TAG, "DEEEU RUUIM :: CRIAR PHOTO FILE");
            }
            String nameAutority = BuildConfig.APPLICATION_ID + ".fileprovider";
            Log.i(TAG, "NOME DO AUTORITY :: " + nameAutority);
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, nameAutority, photoFile);
                Log.i(TAG, "URL :: " + photoURI.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 1);
            }
        }
    }

    private File createImageFile() throws IOException {

        String horafoto = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + horafoto + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        Log.i(TAG, "NOME DA FOTO GERADO:: " + imageFileName + "  SALVO NO DIR :: " + storageDir.getName());

        mNomeFoto = imageFileName;
        mPhotoFile=image;
        return image;
    }

 @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ///RECUPERANDO FOTO E ESCREVENDO ELA NA MEMORIA;
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            try {
                mPhotoFile.mkdir();
                mPhotoFile= mCompressor.compressToFile(mPhotoFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mPhotoFile.exists() && mPhotoFile.canWrite()){
                Log.i(TAG, "INICIANDO ESCRITA NA MEMORIA");
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(mPhotoFile);
                    fos.write((int) mPhotoFile.length());
                    Log.i(TAG, "FILE OUTPUT STREAM CRIADO");
                } catch (IOException e) {
                    Log.i(TAG, "ERRO CRIAR OUTPUT STREAM");
                    e.printStackTrace();
                }
                finally {
                    if(fos!=null){
                        try {
                            fos.flush();
                            Log.i(TAG, "FOTO ESCRITA NA MEMORIA");
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.i(TAG, "FOTO NAO NA MEMORIA");
                        }
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.i(TAG, "ARQUIVO NAO FECHADO");
                        }
                    }
                    else Log.i(TAG,"fos == null");
                }
            }
            else Log.i(TAG, "ERROR... ARQUIVO EXISTE? "+mPhotoFile.exists() + "  PODE SER ESCRITO? ::  "+mPhotoFile.canWrite());


            atualizarRecyclerView();
            testandoGravacao();
        }

}

    private void atualizarRecyclerView() {

        Log.d(TAG, "Atualizando RecyclerView");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        getFotos();
        Collections.reverse(mFotoUrl);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mFotoUrl, this);
        recyclerView.setAdapter(adapter);
    }

    private void testandoGravacao() {
        Log.i(TAG, "NOME RECUPERADO :: "+currentPhotoPath);
        File rFile = new File(currentPhotoPath);
        if(rFile.exists() && rFile.canRead()){
            Log.i(TAG,"DEEEU BOM KRL");
            FileInputStream fis = null;
            try {
                fis=new FileInputStream(rFile);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fis);
                Bitmap teste = BitmapFactory.decodeStream(bufferedInputStream);
                ImageView iv= findViewById(R.id.iv_teste_bitmap_MainActivity);
                iv.setImageBitmap(teste);
                startRecyclerView();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else Log.i(TAG,"DEEEU RUIM, ARQUIVO EXISTE? "+ rFile.exists() +" PODE SER LIDO? "+rFile.canRead());
      /*  File mSaveBit;
        String filePath = teste.getPath();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        ImageView iv= findViewById(R.id.iv_teste_bitmap_MainActivity);
        iv.setImageBitmap(bitmap);*/


    }
}
