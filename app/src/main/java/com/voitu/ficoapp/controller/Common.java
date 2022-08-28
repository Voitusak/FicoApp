package com.voitu.ficoapp.controller;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.voitu.ficoapp.activities.AddRankActivity;
import com.voitu.ficoapp.activities.LoginActivity;
import com.voitu.ficoapp.activities.RegisterActivity;
import com.voitu.ficoapp.activities.UserDetails;

import java.text.DecimalFormat;

public class Common {

    public static final DecimalFormat formatter = new DecimalFormat("#.###");

    public static void showError(String msg, Context c){
        Toast toast = Toast.makeText(c, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static  String convertNumber(long number) {

        if (number > 999 && number < 1_000_000){
            return formatter.format( (double)number/1000) + "K";
        }
        if (number >= 1_000_000 && number < 1_000_000_000){
            return formatter.format((double)number/1_000_000) + "M";
        }
        if (number >= 1_000_000_000){
            return formatter.format((double)number/1_000_000_000) + "B";
        }
        return formatter.format(number);
    }
    public static String formatString(String s){
        return s.toUpperCase().charAt(0) + s.toLowerCase().substring(1);
    }

    public static void startMainActivity(Context c){
        Intent myIntent = new Intent(c, UserDetails.class);
        c.startActivity(myIntent);
    }
    public static void startRegisterActivity(Context c){
        Intent myIntent = new Intent(c, RegisterActivity.class);
        c.startActivity(myIntent);
    }
    public static void startLoginActivity(Context c){
        Intent myIntent = new Intent(c, LoginActivity.class);
        c.startActivity(myIntent);
    }

    public static void startAddTagsActivity(Context c){
        Intent myIntent = new Intent(c, AddRankActivity.class);
        c.startActivity(myIntent);
    }

}
