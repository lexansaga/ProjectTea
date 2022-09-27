package com.capstone.projecttea;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

    public Utils() {

    }
    public static void ShowDialog(Context context,String title,String message){
        AlertDialog.Builder builder = new  AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Whatever...
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.font_color));
    }
    public static String CheckTextIfNull(Object object,String fallback) {
            return object != null ? (object.toString().equals("") ? fallback : object.toString()) : fallback;
    }
    public static String CapitalizeString(Context context,String str) {
      //  String retStr = str;
      //  try { // We can face index out of bound exception if the string is null
      //      retStr = str.substring(0, 1).toUpperCase() + str.substring(1);
      //  }catch (Exception e){}
      //  return retStr;
        String[] arr = str.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
    public static String TimeAndDateNow(String format) {
        String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August","September", "October", "November", "December"};

        format = format.toLowerCase(Locale.ROOT);
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        int month = today.month + 1;
        int day = today.monthDay;
        int year = today.year;
        String time = today.format("%k%M%S");

         if(format.equals("date")){
            return month+""+day+""+year;
        }
        else if(format.equals("time")){
            return time;
        }
         else if(format.equals("datename")){
             return months[month] + " " + day + ", " + year;
         }


        return month+""+day+""+year+""+time;
    }


    public void showNotification(Context context, String title, String message, Intent intent, int reqCode) {


        PendingIntent pendingIntent = PendingIntent.getActivity(context, reqCode, intent, PendingIntent.FLAG_ONE_SHOT);
        String CHANNEL_ID = "channel_name";// The id of the channel.
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(reqCode, notificationBuilder.build()); // 0 is the request code, it should be unique id

        Log.d("showNotification", "showNotification: " + reqCode);
    }
}
