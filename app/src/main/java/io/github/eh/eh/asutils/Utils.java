package io.github.eh.eh.asutils;

import android.app.AlertDialog;
import android.content.Context;

public class Utils {
    public static void showMessageBox(Context context, String title, String body,int... buttons){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        for(int button: buttons){
            switch (button){
                case AlertDialog.BUTTON_NEUTRAL:{
                    builder.setNeutralButton("확인",null);
                    break;
                }
                case AlertDialog.BUTTON_NEGATIVE:{
                    builder.setNegativeButton("아니오",null);
                    break;
                }
                case AlertDialog.BUTTON_POSITIVE:{
                    builder.setPositiveButton("네",null);
                    break;
                }
            }
        }
        builder.setTitle(title);
        builder.setMessage(body);
        builder.create().show();
    }
}
