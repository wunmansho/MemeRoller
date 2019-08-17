package com.wunmansho.util;


import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;

import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import static com.wunmansho.util.utils.splitFileExt;
import java.util.Locale;
import android.content.res.Resources;
//import com.auto.accident.report.BuildConfig;
//import com.auto.accident.report.R;
//import com.auto.accident.report.models.PersistenceObjDao;
//import com.auto.accident.report.objects.PersistenceObj;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
//import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

/**
 * Created by myron on 5/20/2018.
 */

public class utils extends AppCompatActivity {
    private static  String configLocale = Locale.getDefault().getCountry().toLowerCase();
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    //   private static final Context context = ApplicationContextProvider.getContext();
//    private static PersistenceObjDao mPersistenceObjDao;
//    private static PersistenceObj persistenceObj;

    //




    public static String extractCharacter(String DA_RESULT) {
        if (DA_RESULT != null) {
            DA_RESULT = DA_RESULT.replaceAll("'", "");

        }
        if (DA_RESULT == null) {
            DA_RESULT = "";

        }


        return DA_RESULT;
    }

    public static String getFirstWord(String DA_RESULT2) {
        String[] splitString;
        int splitLength;
        String DA_RESULT = "";
        if (!DA_RESULT2.equals(null)) {
            String[] words = DA_RESULT2.split(" ");
            splitLength = words.length;
            int index = 0;
            DA_RESULT = words[0];
        }
        return DA_RESULT;
    }

    public static String getSecondWord(String DA_RESULT2) {
        int splitLength;
        int index = 1;
        StringBuilder DA_RESULT = new StringBuilder();
        String[] words = DA_RESULT2.split(" ");
        splitLength = words.length;
        while (index < splitLength) {
            DA_RESULT.append(words[index]).append(" ");
            index++;
        }
        return DA_RESULT.toString();
    }

    public static String getThirdWord(String DA_RESULT2) {
        int splitLength;
        int index = 2;
        StringBuilder DA_RESULT = new StringBuilder();
        String[] words = DA_RESULT2.split(" ");
        splitLength = words.length;
        while (index < splitLength) {
            DA_RESULT.append(words[index]).append(" ");
            index++;
        }
        return DA_RESULT.toString();
    }

    public static String getFourthWord(String DA_RESULT2) {
        int splitLength;
        int index = 3;
        StringBuilder DA_RESULT = new StringBuilder();
        String[] words = DA_RESULT2.split(" ");
        splitLength = words.length;
        while (index < splitLength) {
            DA_RESULT.append(words[index]).append(" ");
            index++;
        }
        return DA_RESULT.toString();
    }

    public static String getFifthWord(String DA_RESULT2) {
        int splitLength;
        int index = 4;
        StringBuilder DA_RESULT = new StringBuilder();
        String[] words = DA_RESULT2.split(" ");
        splitLength = words.length;
        while (index < splitLength) {
            DA_RESULT.append(words[index]).append(" ");
            index++;
        }
        return DA_RESULT.toString();
    }

    public static String removeSpaces(String DA_RESULT2) {
        int splitLength;
        int index = 0;
        StringBuilder DA_RESULT = new StringBuilder();
        String[] words = DA_RESULT2.split(" ");
        splitLength = words.length;
        while (index < splitLength) {
            DA_RESULT.append(words[index]);
            index++;
        }
        return DA_RESULT.toString();
    }

    public static String removeDashes(String DA_RESULT2) {
        int splitLength;
        int index = 0;
        StringBuilder DA_RESULT = new StringBuilder();
        String[] words = DA_RESULT2.split("-");
        splitLength = words.length;
        while (index < splitLength) {
            DA_RESULT.append(words[index]);
            index++;
        }
        DA_RESULT = new StringBuilder(removePluses(DA_RESULT.toString()));
        return DA_RESULT.toString();
    }

    public static String getPictureName(String DA_RESULT) {
        int splitLength;
        String[] words = DA_RESULT.split("/");
        splitLength = words.length;
        splitLength = splitLength - 1;
        DA_RESULT = words[splitLength];
        return DA_RESULT;
    }

    public static String removePluses(String DA_RESULT) {
        DA_RESULT = DA_RESULT.replaceAll("[^a-zA-Z0-9]", "");

        return DA_RESULT;
    }

    public static String capEachWord(String DA_RESULT) {
        int splitLength;
        int index = 0;
        String[] words = DA_RESULT.split(" ");
        splitLength = words.length;
        StringBuilder DA_RESULTBuilder = new StringBuilder();
        while (index < splitLength) {
            int DA_SIZE = words[index].length();
            words[index] = words[index].substring(0, 1).toUpperCase() + words[index].substring(1, DA_SIZE);
            DA_RESULTBuilder.append(words[index]);
            if (index != splitLength) {
                DA_RESULTBuilder.append(" ");
            }
            index++;
        }
        DA_RESULT = DA_RESULTBuilder.toString();

        return DA_RESULT;
    }

    public static String capEachSentence(String DA_RESULT) {
        int splitLength;
        int index = 0;
        String[] words = DA_RESULT.split("\\.");
        splitLength = words.length;
        StringBuilder DA_RESULTBuilder = new StringBuilder();
        while (index < splitLength) {
            int DA_SIZE = words[index].length();
            words[index] = words[index].substring(0, 1).toUpperCase() + words[index].substring(1, DA_SIZE);
            DA_RESULTBuilder.append(words[index]);
            if (index != splitLength) {
                DA_RESULTBuilder.append(". ");
            }
            index++;
        }
        DA_RESULT = DA_RESULTBuilder.toString();

        return DA_RESULT;
    }

    public static String capSentence(String DA_RESULT) {
        int DA_SIZE = DA_RESULT.length();
        DA_RESULT = DA_RESULT.substring(0, 1).toUpperCase() + DA_RESULT.substring(1, DA_SIZE);


        return DA_RESULT;
    }

    public static boolean isNumber(String string) {
        return string.matches("^\\d+$");
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
    public static String removeBadChars(String DA_RESULT) {
       // int splitLength;
      //  int index = 0;
     //   StringBuilder DA_RESULT = new StringBuilder();
      //  String[] words = DA_RESULT2.split("=\n");
     //   splitLength = words.length;
     //   while (index < splitLength) {
       //     DA_RESULT.append(words[index]);
       //     index++;
       // }
        String[] words = DA_RESULT.split("=\n");
               DA_RESULT = words[0];
               DA_RESULT = removePluses(DA_RESULT);
      //  return DA_RESULT.toString();
        return DA_RESULT;
    }
    public static String splitDash(String DA_RESULT2) {
        String[] splitString;
        int splitLength;
        String DA_RESULT = "";
        if (!DA_RESULT2.equals(null)) {
            String[] words = DA_RESULT2.split("-");
            splitLength = words.length;
            int index = 0;
            DA_RESULT = words[0];
        }
        return DA_RESULT;
    }
    public static String splitDash2(String DA_RESULT2) {
        String[] splitString;
        int splitLength;
        String DA_RESULT = "";
        if (!DA_RESULT2.equals(null)) {
            String[] words = DA_RESULT2.split("-");
            splitLength = words.length;
            int index = 0;
            if (splitLength == 2) {
                DA_RESULT = words[1];
            }
            if (splitLength == 1) {
                DA_RESULT = words[0];
            }
        }
        return DA_RESULT;
    }
    public static String splitFileExt(String DA_RESULT2) {
        String[] splitString;
        int splitLength;
        String DA_RESULT = "";
        if (!DA_RESULT2.equals(null)) {
            DA_RESULT2 = getPictureName(DA_RESULT2);
            String[] words = DA_RESULT2.split("\\.");
            splitLength = words.length;
            int index = 0;
            if (splitLength == 2) {
                DA_RESULT = words[1];
            }
            if (splitLength == 1) {
                DA_RESULT = "none";
            }
        }
        return DA_RESULT;
    }


}
