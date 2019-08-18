package com.wunmansho.memeroller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.FileProvider;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;

//import com.google.android.gms.ads.MobileAds;

import com.wunmansho.memeroller.base.BaseActivity;
import com.wunmansho.memeroller.filters.FilterListener;
import com.wunmansho.memeroller.filters.FilterViewAdapter;
import com.wunmansho.memeroller.tools.EditingToolsAdapter;
import com.wunmansho.memeroller.tools.ToolType;
import com.wunmansho.util.LocaleHelper;
import com.wunmansho.util.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.SaveSettings;
import ja.burhanrashid52.photoeditor.ViewType;
import ja.burhanrashid52.photoeditor.PhotoFilter;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdView;

import static com.wunmansho.memeroller.tools.ToolType.MIC;
import static com.wunmansho.util.utils.splitFileExt;
import static java.security.AccessController.getContext;

public class EditImageActivity extends BaseActivity implements OnPhotoEditorListener,
        View.OnClickListener,
        PropertiesBSFragment.Properties,
        EmojiBSFragment.EmojiListener,
        StickerBSFragment.StickerListener, EditingToolsAdapter.OnItemSelected, FilterListener,
        RecognitionListener {
  //  private AdView mAdView;
    private static final String TAG = EditImageActivity.class.getSimpleName();
    public static final String EXTRA_IMAGE_PATHS = "extra_image_paths";
    private static final int CAMERA_REQUEST = 52;
    private static final int PICK_REQUEST = 53;
    private static final String CAMERA_REQ = "52";
    private static final String PICK_REQ = "53";
    private PhotoEditor mPhotoEditor;
    private PhotoEditorView mPhotoEditorView;
    private PropertiesBSFragment mPropertiesBSFragment;
    private EmojiBSFragment mEmojiBSFragment;
    private StickerBSFragment mStickerBSFragment;
  //  private TextView  mTxtCurrentTool;
    private Typeface mWonderFont;
    private RecyclerView mRxTools, mRxFilters;
    private EditingToolsAdapter mEditingToolsAdapter = new EditingToolsAdapter(this);
    private FilterViewAdapter mFilterViewAdapter = new FilterViewAdapter(this);
    private ConstraintLayout mRootView;
    private ConstraintSet mConstraintSet = new ConstraintSet();
    private boolean mIsFilterVisible;
    private String albumName;
    private File file;
    private static final int REQ_CODE_MEME_INPUT = 100;
    private int REQ_CODE;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private boolean micOn;
    private final String LOG_TAG = "VoiceRecActivity";
    private Context context;
    private String DA_RESULT;
    private View mView;
    private ImageView imgToolIcon;
    private RecyclerView mRecyclerView;
    private RotateAnimation rotateAnimation;
    private final String deviceLocale = Locale.getDefault().getCountry();
    private ProgressBar progressBar;
    private Uri myUri;
    private String DA_IMAGE;
    private String DA_REQUEST;
    private boolean exitNow;
   // private AdRequest adRequest;
    private final String locale = Locale.getDefault().toString();
    private Resources res;
    private String fileExtension;
    private static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }

        return message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  //      MobileAds.initialize(this, "ca-app-pub-9974842162530772~8204307123");
        makeFullScreen();

        setContentView(R.layout.activity_edit_image);

        initViews();

        mWonderFont = Typeface.createFromAsset(getAssets(), "beyond_wonderland.ttf");
     //   mAdView = findViewById(R.id.adView);
      //  adRequest = new AdRequest.Builder().build();
      //  mAdView.loadAd(adRequest);
        mPropertiesBSFragment = new PropertiesBSFragment();
        mEmojiBSFragment = new EmojiBSFragment();
        mStickerBSFragment = new StickerBSFragment();
        mStickerBSFragment.setStickerListener(this);
        mEmojiBSFragment.setEmojiListener(this);
        mPropertiesBSFragment.setPropertiesChangeListener(this);
        exitNow = false;
        LinearLayoutManager llmTools = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRxTools.setLayoutManager(llmTools);
        mRxTools.setAdapter(mEditingToolsAdapter);

        LinearLayoutManager llmFilters = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRxFilters.setLayoutManager(llmFilters);
        mRxFilters.setAdapter(mFilterViewAdapter);

      //  LocaleHelper.onAttach(this, locale);
        res = getResources();
        Intent intent = getIntent();
        DA_IMAGE = intent.getStringExtra("DA_IMAGE");
        DA_REQUEST = intent.getStringExtra("DA_REQUEST");
      //  myUri = Uri.parse(DA_IMAGE);
        fileExtension = splitFileExt(DA_IMAGE);
        file = new File(DA_IMAGE);
      //  myUri = FileProvider.getUriForFile(this, "com.wunmansho.memeroller.fileprovider", file);
        myUri = Uri.parse(DA_IMAGE);

        mPhotoEditorView.getSource().setImageURI(myUri);

        //Typeface mTextRobotoTf = ResourcesCompat.getFont(this, R.font.roboto_medium);
        //Typeface mEmojiTypeFace = Typeface.createFromAsset(getAssets(), "emojione-android.ttf");

        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(true) // set flag to make text scalable when pinch
                //.setDefaultTextTypeface(mTextRobotoTf)
                //.setDefaultEmojiTypeface(mEmojiTypeFace)
                .build(); // build photo editor sdk

        mPhotoEditor.setOnPhotoEditorListener(this);
         switch (DA_REQUEST) {
                     case CAMERA_REQ: {
                        doCamera();
                         break;
                     }

                     case PICK_REQ: {
                        doGallery();
                         break;
                     }
         			default: {

         			break;
         			}
         			}
        //Set Image Dynamically
        // mPhotoEditorView.getSource().setImageResource(R.drawable.color_palette);
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


private void doGallery() {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_REQUEST);
}
private void doCamera() {
    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
    startActivityForResult(cameraIntent, CAMERA_REQUEST);
}
    private void initViews() {
        ImageView imgUndo;
        ImageView imgRedo;
        ImageView imgCamera;
        ImageView imgGallery;
        ImageView imgSave;
//        ImageView imgClose;

        mPhotoEditorView = findViewById(R.id.photoEditorView);
      // //mTxtCurrentTool = findViewById(R.id.txtCurrentTool);
        mRxTools = findViewById(R.id.rvConstraintTools);
        mRxFilters = findViewById(R.id.rvFilterView);
        mRootView = findViewById(R.id.rootView);
        imgToolIcon = findViewById(R.id.imgToolIcon);
        progressBar = findViewById(R.id.progressBar1);

    }

    @Override
    public void onEditTextChangeListener(final View mRootView, String text, int colorCode) {
        TextEditorDialogFragment textEditorDialogFragment =
                TextEditorDialogFragment.show(this, text, colorCode);
        textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
            @Override
            public void onDone(String inputText, int colorCode) {
                mPhotoEditor.editText(mRootView, inputText, colorCode);
           //    //mTxtCurrentTool.setText(R.string.label_text);
            }
        });

    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.d(TAG, "onAddViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onRemoveViewListener(int numberOfAddedViews) {
        Log.d(TAG, "onRemoveViewListener() called with: numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.d(TAG, "onRemoveViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onClick(View view) {
      mView = view;
    }

     @SuppressLint("MissingPermission")
    private void saveImage() {
        if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            showLoading("Saving...");
            albumName = "MemeRoller/saved";
            file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), albumName);
            file.mkdirs();
            file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), albumName  + File.separator + "" + System.currentTimeMillis() + "." + fileExtension);

            try {
                file.createNewFile();

                SaveSettings saveSettings = new SaveSettings.Builder()
                        .setClearViewsEnabled(true)
                        .setTransparencyEnabled(true)
                        .build();

                mPhotoEditor.saveAsFile(file.getAbsolutePath(), saveSettings, new PhotoEditor.OnSaveListener() {
                    @Override
                    public void onSuccess(@NonNull String imagePath) {
                        hideLoading();
                        showSnackbar("Image Saved Successfully");
                        mPhotoEditorView.getSource().setImageURI(Uri.fromFile(new File(imagePath)));
                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        hideLoading();
                        showSnackbar("Failed to save Image");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                hideLoading();
                showSnackbar(e.getMessage());
            }
        }
    }
    @SuppressLint("MissingPermission")
    private void saveImageTmp() {
        if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
           // showLoading("Saving...");
            albumName = "MemeRoller/tmp";
            file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), albumName);
                    file.mkdirs();
            file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), albumName  + File.separator + "" + System.currentTimeMillis() + "." + fileExtension);

            try {
                file.createNewFile();

                SaveSettings saveSettings = new SaveSettings.Builder()
                        .setClearViewsEnabled(true)
                        .setTransparencyEnabled(true)
                        .build();

                mPhotoEditor.saveAsFile(file.getAbsolutePath(), saveSettings, new PhotoEditor.OnSaveListener() {
                    @Override
                    public void onSuccess(@NonNull String imagePath) {
                        hideLoading();
                      //  showSnackbar("Image Saved Successfully");
                        context = mRootView.getContext();
                        myUri = FileProvider.getUriForFile(context, "com.wunmansho.memeroller.fileprovider", file);

                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, myUri);
                      //  shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello");
                        shareIntent.setType("image/*");
                        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.label_send_to)));
                       // mPhotoEditorView.getSource().setImageURI(Uri.fromFile(new File(imagePath)));
                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        hideLoading();
                   //     showSnackbar("Failed to save Image");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                hideLoading();
                showSnackbar(e.getMessage());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    mPhotoEditor.clearAllViews();
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    mPhotoEditorView.getSource().setImageBitmap(photo);
                    break;
                case PICK_REQUEST:
                    try {
                        mPhotoEditor.clearAllViews();
                        Uri uri = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        mPhotoEditorView.getSource().setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } else {
        exitNow = true;
        onBackPressed();
        }
    }

    @Override
    public void onColorChanged(int colorCode) {
        mPhotoEditor.setBrushColor(colorCode);
       //mTxtCurrentTool.setText(R.string.label_brush);
    }

    @Override
    public void onOpacityChanged(int opacity) {
        mPhotoEditor.setOpacity(opacity);
       //mTxtCurrentTool.setText(R.string.label_brush);
    }

    @Override
    public void onBrushSizeChanged(int brushSize) {
        mPhotoEditor.setBrushSize(brushSize);
       //mTxtCurrentTool.setText(R.string.label_brush);
    }

    @Override
    public void onEmojiClick(String emojiUnicode) {
        mPhotoEditor.addEmoji(emojiUnicode);
       //mTxtCurrentTool.setText(R.string.label_emoji);

    }

    @Override
    public void onStickerClick(Bitmap bitmap) {
        int width=200;
        int height=200;
        Bitmap resizedbitmap=Bitmap.createScaledBitmap(bitmap, width, height, true);

        mPhotoEditor.addImage(resizedbitmap);
       //mTxtCurrentTool.setText(R.string.label_sticker);
    }

    @Override
    public void isPermissionGranted(boolean isGranted, String permission) {
        if (isGranted) {
            saveImage();
        }
    }

    private void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you want to exit without saving image ?");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveImage();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();

    }

    @Override
    public void onFilterSelected(PhotoFilter photoFilter) {
        mPhotoEditor.setFilterEffect(photoFilter);
    }

    @Override
    public void onToolSelected(ToolType toolType) {
        switch (toolType) {
            case MIC:
                if (!micOn) {
                    context = mRootView.getContext();
                    imgToolIcon = findViewById(R.id.imgToolIcon);

                    imgToolIcon.setImageResource(R.drawable.ic_mic_white_36dp);
                    micOn = true;
                    speech = SpeechRecognizer.createSpeechRecognizer(context);
                    speech.setRecognitionListener(EditImageActivity.this);
                    startMemeInput(mView);
                } else {
                    resetBeforeExit();
                }
               //mTxtCurrentTool.setText(R.string.label_mic);
                break;
            case SHARE:
//                Intent shareIntent = new Intent();
//                shareIntent.setAction(Intent.ACTION_SEND);
//                shareIntent.putExtra(Intent.EXTRA_STREAM, myUri);
//                shareIntent.setType("image/png");
//                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.label_send_to)));
                saveImageTmp();
//
//                Uri uriToImage = Uri.parse(file.getAbsolutePath());
//                Intent shareIntent = new Intent();
//                shareIntent.setAction(Intent.ACTION_SEND);
//                shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
//                shareIntent.setType("image/jpg");
//                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.label_send_to)));
                break;
            case UNDO:
                if(micOn) {resetBeforeExit();}
               mPhotoEditor.undo();
               //mTxtCurrentTool.setText(R.string.label_undo);

                break;
            case REDO:
                if(micOn) {resetBeforeExit();}
                mPhotoEditor.redo();
               //mTxtCurrentTool.setText(R.string.label_redo);
                   break;
            case SAVE:
                if(micOn) {resetBeforeExit();}
                saveImage();
               //mTxtCurrentTool.setText(R.string.label_save);
                break;
            case BRUSH:
                if(micOn) {resetBeforeExit();}
                scheduleDoBrush();

                break;
            case TEXT:
                if(micOn) {resetBeforeExit();}
              scheduleDoText();
                break;
            case ERASER:
                if(micOn) {resetBeforeExit();}
                mPhotoEditor.brushEraser();
               //mTxtCurrentTool.setText(R.string.label_eraser);
                break;
            case FILTER:
                if(micOn) {resetBeforeExit();}
               //mTxtCurrentTool.setText(R.string.label_filter);
                showFilter(true);
                break;
            case EMOJI:
                if(micOn) {resetBeforeExit();}
                scheduleDoEmoji();
                break;
            case STICKER:
                if(micOn) {resetBeforeExit();}
                scheduleDoSticker();
                break;
        }
    }
    private void scheduleDoText() {
        Handler handler = new Handler();
        handler.postDelayed(this::doText, 250);
    }
    private void doText() {
        TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(this);
        textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
            @Override
            public void onDone(String inputText, int colorCode) {
                mPhotoEditor.addText(inputText, colorCode);
               //mTxtCurrentTool.setText(R.string.label_text);
            }
        });
    }
    private void scheduleDoEmoji() {
        Handler handler = new Handler();
        handler.postDelayed(this::doEmoji, 250);
    }
    private void doEmoji() {
        mEmojiBSFragment.show(getSupportFragmentManager(), mEmojiBSFragment.getTag());
    }
    private void scheduleDoSticker() {
        Handler handler = new Handler();
        handler.postDelayed(this::doSticker, 250);
    }
    private void doSticker() {
        mStickerBSFragment.show(getSupportFragmentManager(), mStickerBSFragment.getTag());
    }
    private void scheduleDoBrush() {
        Handler handler = new Handler();
        handler.postDelayed(this::doBrush, 250);
    }
    private void doBrush() {
        mPhotoEditor.setBrushDrawingMode(true);
       //mTxtCurrentTool.setText(R.string.label_brush);
        mPropertiesBSFragment.show(getSupportFragmentManager(), mPropertiesBSFragment.getTag());
    }
    private void startMemeInput(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, false);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, deviceLocale);
        try {
            REQ_CODE = REQ_CODE_MEME_INPUT;
            speech.startListening(intent);
            // startActivityForResult(intent, REQ_CODE_INSURANCE_COMPANY);
        } catch (ActivityNotFoundException a) {

        }
    }

    private void retry() {
        switch (REQ_CODE) {
            case REQ_CODE_MEME_INPUT: {
                startMemeInput(mView);
                break;
            }

        }
    }

    @Override
    public void onResults(Bundle results) {

        ArrayList<String> result = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        DA_RESULT = result.get(0);

        switch(REQ_CODE) {


            case REQ_CODE_MEME_INPUT: {
                if (null != DA_RESULT) {

                    DA_RESULT = utils.capEachSentence(DA_RESULT);
                 //   mAddTextEditText.setText(DA_RESULT);
                    mPhotoEditor.addText(DA_RESULT, -1);
                    resetBeforeExit();
                    break;
                }

                startMemeInput(mView);
            }
            break;
        }
    }
    private void resetBeforeExit() {

     //   super.onStop().. dont do it;
        if (speech != null) {
            speech.stopListening();
            speech.destroy();

        }
       mView = mRxTools.getChildAt(0);
        imgToolIcon = findViewById(R.id.imgToolIcon);
        imgToolIcon.setImageResource(R.drawable.ic_mic_off_white_36dp);
        rotateAnimation = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setRepeatCount(1);
        rotateAnimation.setRepeatMode(Animation.RELATIVE_TO_SELF);
        rotateAnimation.setDuration(100);
        imgToolIcon.startAnimation(rotateAnimation);

        micOn = false;

    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (speech != null) {
            speech.destroy();
            Log.i(LOG_TAG, "destroy");
        }
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        progressBar.setVisibility(View.VISIBLE);

        progressBar.setIndeterminate(false);
        progressBar.setMax(10);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.INVISIBLE);
        //   toggleButton.setChecked(false);
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        //  tvDA_PROMPT.setText(errorMessage);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
        retry();
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
        progressBar.setProgress((int) rmsdB);
    }

    void showFilter(boolean isVisible) {
        mIsFilterVisible = isVisible;
        mConstraintSet.clone(mRootView);

        if (isVisible) {
            mConstraintSet.clear(mRxFilters.getId(), ConstraintSet.START);
            mConstraintSet.connect(mRxFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.START);
            mConstraintSet.connect(mRxFilters.getId(), ConstraintSet.END,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
        } else {
            mConstraintSet.connect(mRxFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
            mConstraintSet.clear(mRxFilters.getId(), ConstraintSet.END);
        }

        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(350);
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        TransitionManager.beginDelayedTransition(mRootView, changeBounds);

        mConstraintSet.applyTo(mRootView);
    }

    @Override
    public void onBackPressed() {
        if (mIsFilterVisible) {
            showFilter(false);
           //mTxtCurrentTool.setText(R.string.app_name);
        } else if (!mPhotoEditor.isCacheEmpty()) {
            showSaveDialog();
        } else {
            super.onBackPressed();
            Intent intent = new Intent(EditImageActivity.this, PhotoGalleryActivity.class);

            startActivity(intent);

        }
    }

}
