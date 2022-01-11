package com.example.ficoapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity implements GraphicView{

    private static final String CHANNEL_ID = "1";
    private static final CharSequence TESTO_TROLL = "Non mollare mai";
    private final String PLAYER = "tobia";
    private View background;
    private TextView pointsView;
    private TextView percentageView;
    private DataSource dataSource;
    private ProgressBar progressBar;
    private DecimalFormat formatter = new DecimalFormat("#.###");
    private boolean firstLoading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataSource = new DataSource(this);
        layout();
        updateData();


    }



    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    private void layout() {

        background = findViewById(R.id.wallpaper);
        background.getBackground().setDither(true);
        pointsView = findViewById(R.id.points);
        progressBar = findViewById(R.id.progressBar);
        percentageView = findViewById(R.id.percentage);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.mainrefresh);
        pullToRefresh.setOnRefreshListener(() -> {
            updateData(); // your code
            pullToRefresh.setRefreshing(false);
        });
        ImageView button = findViewById(R.id.ficoButton);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void updateData(){
        dataSource.update(PLAYER);
    }

    private String convertNumber(long number) {

        if (number > 999 && number < 1_000_000){
            return formatter.format( (double)number/1000) + "K";
        }
        if (number >= 1_000_000){
            return formatter.format((double)number/1_000_000) + "M";
        }
        return String.valueOf(number);
    }


    @Override
    public void updateView(String player, long points, long maxPoints) {
        pointsView.setText(convertNumber(points));
        double percentage = maxPoints !=0 ? ((double)(points*100) / maxPoints) : 0;
        System.out.println("Points->" + points + "  max ->" + maxPoints + " percentage ->" + percentage);
        percentageView.setText(formatter.format(percentage) + "%");
        progressBar.setProgress((int) percentage);
        if(points >= maxPoints){
            Intent intent = new Intent(MainActivity.this, FinalActivity.class);
            startActivity(intent);
            finish();
        }
        firstLoading = false;
    }

    @Override
    public void notifiction(String body) {
        if(!firstLoading) {
            showNotification(
                    "Ops...",
                    body);
        }
    }

    // Method to display the notifications
    public void showNotification(String title,
                                 String message) {
        // Pass the intent to switch to the MainActivity
        Intent intent
                = new Intent(this, MainActivity.class);
        // Assign channel ID
        String channel_id = "notification_channel";
        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Pass the intent to PendingIntent to start the
        // next Activity
        PendingIntent pendingIntent
                = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder(getApplicationContext(),
                channel_id)
                .setSmallIcon(R.drawable.fico)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000,
                        1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        // A customized design for the notification can be
        // set only for Android versions 4.1 and above. Thus
        // condition for the same is checked here.

        builder = builder.setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText((Long.parseLong(message) > 0 ? "+" : "-") + message + " " +TESTO_TROLL))
                .setSmallIcon(R.drawable.fico);

        // Create an object of NotificationManager class to
        // notify the
        // user of events that happen in the background.
        NotificationManager notificationManager
                = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel
                    = new NotificationChannel(
                    channel_id, "web_app",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(
                    notificationChannel);
        }

        notificationManager.notify(0, builder.build());
    }
}