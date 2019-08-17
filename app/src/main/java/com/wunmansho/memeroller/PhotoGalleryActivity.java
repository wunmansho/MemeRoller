package com.wunmansho.memeroller;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.wunmansho.memeroller.about.AboutActivity;
import com.wunmansho.util.LocaleHelper;
import com.wunmansho.util.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.SaveSettings;

import static com.wunmansho.util.utils.splitFileExt;
//import java.io.FileOutputStream;
//import com.giphy.sdk;

public class PhotoGalleryActivity extends AppCompatActivity {

    private PhotoEditor mPhotoEditor;
    private Context context;
    private AssetManager assetManager;
   // private AdView mAdView;
    // private File file;


    private File saved;
    private File tmp;
    private File file;
    private List<PicturesInfo> picturesinfo;
    private RecyclerView photoGalleryAdapter;
    private RotateAnimation rotateAnimation;
    private Intent intent;
    private Intent fullScreenIntent;
    private String albumName;
    private ImageView btnCamera;
    private ImageView btnGallery;
    private File mStorageDirectory;
    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private String[] FileNameStrings1;
    private String[] FileNameStringsFinal;
    private File[] listFile;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private StorageReference mStorageRef;
    private int _selectedTab;     // 0=all 1=celebrity   2=pets  3=political  4=toons  5=urban  6=saved
    private int albumCategory;
    private int allAlbums;
    private String[] names;
    private String[] political;
    private String[] toons;
    private String[] urban;
    private String[] celebrity;
    private String[] pets;
    private int politicalCnt;
    private int toonsCnt;
    private int urbanCnt;
    private int celebrityCnt;
    private int petsCnt;
    private final String deviceLocale = Locale.getDefault().getCountry();
    private Uri myUri;
    private String imageIn;
    private String imageOut;
    private int folderPosition;
    private String luckyFolder;
    private Drawer mDrawer;
    private View view;
   // private AdRequest adRequest;
    private String locale = Locale.getDefault().toString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  MobileAds.initialize(this, "ca-app-pub-3940256099942544/6300978111");
      // GPHApi client = new GPHApiClient("Gh1y5S5ufnqP70hlXKU2HzYCTk0PV1vm");
     //   LocaleHelper.onAttach(this, locale);
        setContentView(R.layout.photo_gallery_adapter);

        photoGalleryAdapter = findViewById(R.id.photoGalleryAdapter);
        int numberOfColumns = 3;
        photoGalleryAdapter.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        appBarLayout = findViewById(R.id.appbar);
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabs);
        btnGallery = findViewById(R.id.btnGallery);
        btnCamera = findViewById(R.id.btnCamera);
    //    mAdView = findViewById(R.id.adView);
    //    adRequest = new AdRequest.Builder().build();
     //   mAdView.loadAd(adRequest);
        appBarLayout.setExpanded(false);
        appBarLayout.setActivated(false);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
        } else {
            // Locate the image folder in your SD Card
            albumName = "MemeRoller";
            file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), albumName);
            file.mkdirs();
            albumName = "MemeRoller/tmp";
            file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), albumName);
            file.mkdirs();

            if (file.isDirectory()) {
                listFile = file.listFiles();

                int j;
                for (int i = 0; i < listFile.length; i++) {


                    try {
                        boolean bool = listFile[i].delete();

                        // print
                        System.out.println("File deleted: " + bool);

                    } catch (Exception e) {
                        // if any error occurs
                        e.printStackTrace();
                    }

                }
            }
        }




    context = this;
        new DrawerBuilder().withActivity(this).build();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 0=all 1=celebrity   2=pets  3=political  4=toons  5=urban  6=saved
                _selectedTab = tab.getPosition();
                // initializeDataNew();
                initializeData();
                initializeAdapter();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
// 0=all 1=celebrity   2=pets  3=political  4=toons  5=urban  6=saved
        _selectedTab = 0;
        //  initializeDataNew();
        initializeData();
        initializeAdapter();
        btnCamera.setOnClickListener(view -> {

            rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setRepeatCount(1);
            rotateAnimation.setRepeatMode(Animation.RELATIVE_TO_SELF);
            rotateAnimation.setDuration(100);
            btnCamera.startAnimation(rotateAnimation);
            scheduleDoCamera();

        });
        toolbar.setNavigationOnClickListener(view -> {
            mDrawer.openDrawer();

        });

        btnGallery.setOnClickListener(view -> {

            rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setRepeatCount(1);
            rotateAnimation.setRepeatMode(Animation.RELATIVE_TO_SELF);
            rotateAnimation.setDuration(100);
            btnGallery.startAnimation(rotateAnimation);
            scheduleDoGallery();

        });
        mDrawer = new DrawerBuilder()
                .withActivity(this)

                .withTranslucentStatusBar(true)
                .withTranslucentNavigationBar(true)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)

                .addDrawerItems(
                        new DividerDrawerItem(),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.app_name),
                        new DividerDrawerItem(),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.exit_app).withIcon(FontAwesome.Icon.faw_home).withIconColor(0xFFFFFFFF).withSelectedIconColor(0xFF0288D1),
                        new PrimaryDrawerItem().withName(R.string.action_credits).withIcon(FontAwesome.Icon.faw_star).withIconColor(0xFFFFFFFF).withSelectedIconColor(0xFF0288D1)
                        //      new PrimaryDrawerItem().withName(R.string.label_help).withIcon(FontAwesome.Icon.faw_question_circle).withIconColor(0xFFFFFFFF).withSelectedIconColor(0xFF0288D1)
                ) // add the items we want to use with our Drawer
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {

                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {

                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {

                        //   mDrawer.openDrawer();

                    }
                })
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        context = view.getContext();
                        //  Toast.makeText(AccidentMenu.this, ((Nameable) drawerItem).getName().getText(AccidentMenu.this), Toast.LENGTH_SHORT).show();
                        switch (position) {


                            case 5: {

                                doClose();
                                finishAffinity();
                                System.exit(0);

                                break;
                            }
                            case 6: {

                                doClose();
                                intent = new Intent(context, AboutActivity.class);
                                context.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));

                                break;
                            }

                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(false)
                .withCloseOnClick(false)
                .build();

//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Code to be executed when an ad request fails.
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when an ad opens an overlay that
//                // covers the screen.
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Code to be executed when the user is about to return
//                // to the app after tapping on an ad.
//            }
//        });
    }

    private void initializeData() {
        picturesinfo = new ArrayList<>();
        assetManager = context.getAssets();
        // 0=all 1=celebrity   2=pets  3=political  4=toons  5=urban  6=saved
        celebrityCnt = 0;
        petsCnt = 0;
        politicalCnt = 0;
        toonsCnt = 0;
        urbanCnt = 0;
        if (_selectedTab == 6 || _selectedTab == 1) {
            try {
                celebrity = assetManager.list("celebrity");
                celebrityCnt = celebrity.length;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 0=all 1=celebrity   2=pets  3=political  4=toons  5=urban  6=saved
        if (_selectedTab == 6 || _selectedTab == 2) {
            try {
                pets = assetManager.list("pets");
                petsCnt = pets.length;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 0=all 1=celebrity   2=pets  3=political  4=toons  5=urban  6=saved
        if (_selectedTab == 6 || _selectedTab == 3) {
            try {
                political = assetManager.list("political");
                politicalCnt = political.length;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 0=all 1=celebrity   2=pets  3=political  4=toons  5=urban  6=saved
        if (_selectedTab == 6 || _selectedTab == 4) {
            try {
                toons = assetManager.list("toons");
                toonsCnt = toons.length;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 0=all 1=celebrity   2=pets  3=political  4=toons  5=urban  6=saved
        if (_selectedTab == 6 || _selectedTab == 0) {
            try {
                urban = assetManager.list("urban");
                urbanCnt = urban.length;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
        } else {
                      // 0=all 1=celebrity   2=pets  3=political  4=toons  5=urban  6=saved
            // _selectedTab
            if (_selectedTab == 6 || _selectedTab == 5) {
                albumName = "MemeRoller/saved";

                saved = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), albumName);
                saved.mkdirs();
            }

        }

          if (_selectedTab == 6 || _selectedTab == 1) {
            //  listFile = celebrity.listFiles();
            // Create a String array for FilePathStrings
            FilePathStrings = new String[celebrity.length];
            FileNameStrings = new String[celebrity.length];
            allAlbums = 0;
            for (albumCategory = 0; albumCategory < celebrity.length; albumCategory++) {
                // Get the path of the image file

                FilePathStrings[albumCategory] = "file:///android_asset/celebrity/" + celebrity[albumCategory];
                if (splitFileExt(FilePathStrings[albumCategory]).equals("jpg") || splitFileExt(FilePathStrings[albumCategory]).equals("png") || splitFileExt(FilePathStrings[albumCategory]).equals("gif")) {

                    picturesinfo.add(new PicturesInfo(FilePathStrings[albumCategory]));
                }
                // Get the name image file
                FileNameStrings[albumCategory] = celebrity[albumCategory];
                //   FileNameStrings[i] = "The Wiz";
                allAlbums++;

            }
        }

//            // 0=all 1=celebrity   2=pets  3=political  4=toons  5=urban  6=saved
//            // _selectedTab
        if (_selectedTab == 6 || _selectedTab == 2) {
            //  listFile = pets.listFiles();

            // Create a String array for FilePathStrings
            FilePathStrings = new String[pets.length];
            FileNameStrings = new String[pets.length];
            allAlbums = 0;
            for (albumCategory = 0; albumCategory < pets.length; albumCategory++) {
                // Get the path of the image file

                FilePathStrings[albumCategory] = "file:///android_asset/pets/" + pets[albumCategory];
                if (splitFileExt(FilePathStrings[albumCategory]).equals("jpg") || splitFileExt(FilePathStrings[albumCategory]).equals("png") || splitFileExt(FilePathStrings[albumCategory]).equals("gif")) {

                    picturesinfo.add(new PicturesInfo(FilePathStrings[albumCategory]));
                }
                // Get the name image file
                FileNameStrings[albumCategory] = pets[albumCategory];
                //   FileNameStrings[i] = "The Wiz";
                allAlbums++;

            }
        }

//            // 0=all 1=celebrity   2=pets  3=political  4=toons  5=urban  6=saved
//            // _selectedTab
        if (_selectedTab == 6 || _selectedTab == 3) {
            //  listFile = political.listFiles();
            // Create a String array for FilePathStrings
            FilePathStrings = new String[political.length];
            FileNameStrings = new String[political.length];
            allAlbums = 0;
            for (albumCategory = 0; albumCategory < political.length; albumCategory++) {
                // Get the path of the image file

                FilePathStrings[albumCategory] = "file:///android_asset/political/" + political[albumCategory];
                if (splitFileExt(FilePathStrings[albumCategory]).equals("jpg") || splitFileExt(FilePathStrings[albumCategory]).equals("png") || splitFileExt(FilePathStrings[albumCategory]).equals("gif")) {

                    picturesinfo.add(new PicturesInfo(FilePathStrings[albumCategory]));
                }
                // Get the name image file
                FileNameStrings[albumCategory] = political[albumCategory];
                //   FileNameStrings[i] = "The Wiz";
                allAlbums++;

            }
        }

//            // 0=all 1=celebrity   2=pets  3=political  4=toons  5=urban  6=saved
//            // _selectedTab
        if (_selectedTab == 6 || _selectedTab == 4) {
            //listFile = toons.listFiles();
            // Create a String array for FilePathStrings
            FilePathStrings = new String[toons.length];
            FileNameStrings = new String[toons.length];
            allAlbums = 0;
            for (albumCategory = 0; albumCategory < toons.length; albumCategory++) {
                // Get the path of the image file

                FilePathStrings[albumCategory] = "file:///android_asset/toons/" + toons[albumCategory];
                if (splitFileExt(FilePathStrings[albumCategory]).equals("jpg") || splitFileExt(FilePathStrings[albumCategory]).equals("png") || splitFileExt(FilePathStrings[albumCategory]).equals("gif")) {

                    picturesinfo.add(new PicturesInfo(FilePathStrings[albumCategory]));
                }
                // Get the name image file
                FileNameStrings[albumCategory] = toons[albumCategory];
                //   FileNameStrings[i] = "The Wiz";
                allAlbums++;

            }
        }
//            // 0=all 1=celebrity   2=pets  3=political  4=toons  5=urban  6=saved
//            // _selectedTab
        if (_selectedTab == 6 || _selectedTab == 0) {
            // listFile = urban.listFiles();
            // Create a String array for FilePathStrings
            FilePathStrings = new String[urban.length];
            FileNameStrings = new String[urban.length];
            allAlbums = 0;
            for (albumCategory = 0; albumCategory < urban.length; albumCategory++) {
                // Get the path of the image file

                FilePathStrings[albumCategory] = "file:///android_asset/urban/" + urban[albumCategory];
                if (splitFileExt(FilePathStrings[albumCategory]).equals("jpg") || splitFileExt(FilePathStrings[albumCategory]).equals("png") || splitFileExt(FilePathStrings[albumCategory]).equals("gif")) {

                    picturesinfo.add(new PicturesInfo(FilePathStrings[albumCategory]));
                }
                // Get the name image file
                FileNameStrings[albumCategory] = urban[albumCategory];
                //   FileNameStrings[i] = "The Wiz";
                allAlbums++;

            }
        }
        // 0=all 1=celebrity   2=pets  3=political  4=toons  5=urban  6=saved
        // _selectedTab
        if (_selectedTab == 6 || _selectedTab == 5) {
            listFile = saved.listFiles();
            // Create a String array for FilePathStrings
            FilePathStrings = new String[listFile.length];
            FileNameStrings = new String[listFile.length];
            allAlbums = 0;
            for (albumCategory = 0; albumCategory < listFile.length; albumCategory++) {
                // Get the path of the image file

                FilePathStrings[albumCategory] = listFile[albumCategory].getAbsolutePath();
                if (splitFileExt(FilePathStrings[albumCategory]).equals("jpg") || splitFileExt(FilePathStrings[albumCategory]).equals("png") || splitFileExt(FilePathStrings[albumCategory]).equals("gif")) {

                    picturesinfo.add(new PicturesInfo(FilePathStrings[albumCategory]));
                }
                // Get the name image file
                FileNameStrings[albumCategory] = listFile[albumCategory].getName();
                //   FileNameStrings[i] = "The Wiz";
                allAlbums++;

            }
        }


    }


    private void initializeAdapter() {
        PhotoGalleryAdapter adapter = new PhotoGalleryAdapter(getApplicationContext(), picturesinfo, (v, position) -> {
            //   Toast.makeText(PhotoGalleryActivity.this, "Clicked Item: "+position,Toast.LENGTH_SHORT).show();
            folderPosition = 0;
            if (_selectedTab == 6) {
                // 0=all 1=celebrity   2=pets  3=political  4=toons  5=urban  6=saved

                folderPosition = celebrityCnt + petsCnt + politicalCnt + toonsCnt + urbanCnt;
                if ((folderPosition + 1) >= position) {
                    luckyFolder = "urban/";
                }
                folderPosition = celebrityCnt + petsCnt + politicalCnt + toonsCnt;
                if ((folderPosition + 1) >= position) {
                    luckyFolder = "toons/";
                }
                folderPosition = celebrityCnt + petsCnt + politicalCnt;
                if ((folderPosition + 1) >= position) {
                    luckyFolder = "political/";
                }
                folderPosition = celebrityCnt + petsCnt;
                if ((folderPosition + 1) >= position) {
                    luckyFolder = "pets/";
                }
                folderPosition = celebrityCnt;
                if ((folderPosition + 1) >= position) {
                    luckyFolder = "celebrity/";
                }


            }

            if (_selectedTab == 5) {
                imageOut = picturesinfo.get(position).photoId;
            } else {
                imageIn = picturesinfo.get(position).photoId;
                albumName = "MemeRoller/tmp";

                tmp = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), albumName);
                tmp.mkdirs();


                imageOut = utils.getPictureName(imageIn);
                // 0=all 1=celebrity   2=pets  3=political  4=toons  5=urban  6=saved
                if (_selectedTab == 6) {
                    imageIn = luckyFolder + imageOut;
                }
                if (_selectedTab == 1) {
                    imageIn = "celebrity/" + imageOut;
                }
                // 0=all 1=celebrity   2=pets  3=political  4=toons  5=urban  6=saved
                if (_selectedTab == 2) {
                    imageIn = "pets/" + imageOut;
                }
                // 0=all 1=celebrity   2=pets  3=political  4=toons  5=urban  6=saved
                if (_selectedTab == 3) {
                    imageIn = "political/" + imageOut;
                }
                // 0=all 1=celebrity   2=pets  3=political  4=toons  5=urban  6=saved
                if (_selectedTab == 4) {
                    imageIn = "toons/" + imageOut;
                }
                // 0=all 1=celebrity   2=pets  3=political  4=toons  5=urban  6=saved
                if (_selectedTab == 0) {
                    imageIn = "urban/" + imageOut;
                }
                imageOut = tmp + "/" + imageOut;

                copyFileAssets();
            }
            Log.v("Tag", "The file name is: " + imageIn);
            Log.v("Tag", "The file name is: " + imageOut);
            fullScreenIntent = new Intent(PhotoGalleryActivity.this, EditImageActivity.class);
            fullScreenIntent.putExtra("DA_IMAGE", imageOut);
            fullScreenIntent.putExtra("DA_REQUEST", "22");
            startActivity(fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));


        });
        photoGalleryAdapter.setAdapter(adapter);
    }

    private void copyFileAssets() {
        AssetManager assetManager = context.getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(imageIn);
            out = new FileOutputStream(imageOut);
            byte[] buffer = new byte[1024];

            int read;

            while ((read = in.read(buffer)) >= 0) {
                out.write(buffer, 0, read);
            }
            //copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (IOException e) {
            Log.e("tag", "Failed to copy asset file: " + imageIn, e);
            Log.e("tag", "Failed to copy asset file: " + imageOut, e);
        }

    }

    @SuppressLint("MissingPermission")
    private void saveImageTmp() {

        //   if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        // showLoading("Saving...");
        albumName = "MemeRoller/tmp";
        file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        file.mkdirs();
        file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName + File.separator + "" + System.currentTimeMillis() + ".png");

        try {
            file.createNewFile();

            SaveSettings saveSettings = new SaveSettings.Builder()
                    .setClearViewsEnabled(true)
                    .setTransparencyEnabled(true)
                    .build();

            mPhotoEditor.saveAsFile(file.getAbsolutePath(), saveSettings, new PhotoEditor.OnSaveListener() {
                @Override
                public void onSuccess(@NonNull String imagePath) {

                    //  showSnackbar("Image Saved Successfully");

                    myUri = FileProvider.getUriForFile(context, "com.wunmansho.memeroller.fileprovider", file);

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, myUri);
                    shareIntent.setType("image/png");
                    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.label_send_to)));
                    // mPhotoEditorView.getSource().setImageURI(Uri.fromFile(new File(imagePath)));
                }

                @Override
                public void onFailure(@NonNull Exception exception) {

                    //     showSnackbar("Failed to save Image");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();

        }
        // }
    }

    public void doClose() {

    }

    private void scheduleDoCamera() {
        Handler handler = new Handler();
        handler.postDelayed(this::doCamera, 200);
    }

    private void doCamera() {
        String image = "/storage/emulated/0/Pictures/MemeRoller/pets/meme(302).jpg";
        String fileExt = splitFileExt(image);
        fullScreenIntent = new Intent(PhotoGalleryActivity.this, EditImageActivity.class);
        fullScreenIntent.putExtra("DA_IMAGE", image);
        fullScreenIntent.putExtra("DA_REQUEST", "52");
        startActivity(fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
    }

    private void scheduleDoGallery() {
        Handler handler = new Handler();
        handler.postDelayed(this::doGallery, 200);
    }

    private void doGallery() {
        String image = "/storage/emulated/0/Pictures/MemeRoller/pets/meme(302).jpg";
        String fileExt = splitFileExt(image);
        fullScreenIntent = new Intent(PhotoGalleryActivity.this, EditImageActivity.class);
        fullScreenIntent.putExtra("DA_IMAGE", image);
        fullScreenIntent.putExtra("DA_REQUEST", "53");
        startActivity(fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
    }


    @Override
    public void onBackPressed() {
        doClose();
        finishAffinity();
        System.exit(0);
//        intent = new Intent(this, MultiMediaMenu.class);
//        startActivity(intent);
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }


//    private void testFile() {
//
//        Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
//        StorageReference riversRef = mStorageRef.child("images/rivers.jpg");
//
//        riversRef.putFile(file)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        // Get a URL to the uploaded content
//                       Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Handle unsuccessful uploads
//                        // ...
//                    }
//                });
//    }

}
