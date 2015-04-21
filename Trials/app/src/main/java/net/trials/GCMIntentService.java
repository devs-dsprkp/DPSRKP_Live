package net.trials;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by pranavsethi on 05/04/15.
 */
public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";

    private static final String NOTIFI_COUNT = "pending_counter";

    private Controller aController = null;


    public GCMIntentService() {
        // Call extended class Constructor GCMBaseIntentService
        super(Config.GOOGLE_SENDER_ID);

    }


    /**
     * Method called on device registered
     */
    @Override
    protected void onRegistered(Context context, String registrationId) {

        //Get Global Controller Class object (see application tag in AndroidManifest.xml)
        if (aController == null)
            aController = (Controller) getApplicationContext();

        Log.i(TAG, "Device registered: regId = " + registrationId);
        aController.displayMessageOnScreen(context, "Your device registred with GCM");
        Log.d("NAME", MainActivity.name);
        aController.register(context, MainActivity.name, MainActivity.email, MainActivity.Class, MainActivity.Sec, MainActivity.identity, registrationId, MainActivity.type);
    }

    /**
     * Method called on device unregistred
     */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        if (aController == null)
            aController = (Controller) getApplicationContext();
        Log.i(TAG, "Device unregistered");
        aController.displayMessageOnScreen(context, getString(R.string.gcm_unregistered));
        aController.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message from GCM server
     */
    @Override
    protected void onMessage(Context context, Intent intent) {

        if (aController == null)
            aController = (Controller) getApplicationContext();

        Log.i(TAG, "Received message");
        String message = intent.getExtras().getString("push");
        String numMSG = intent.getExtras().getString("numMSG");
        String typeMode = intent.getExtras().getString("type");

        aController.displayMessageOnScreen(context, message);
        // notifies user
        generateNotification(context, message, numMSG, typeMode);
    }

    /**
     * Method called on receiving a deleted message
     */
    @Override
    protected void onDeletedMessages(Context context, int total) {

        if (aController == null)
            aController = (Controller) getApplicationContext();

        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        aController.displayMessageOnScreen(context, message);
        // notifies user
        generateNotification(context, message, null, null);
    }

    /**
     * Method called on Error
     */
    @Override
    public void onError(Context context, String errorId) {

        if (aController == null)
            aController = (Controller) getApplicationContext();

        Log.i(TAG, "Received error: " + errorId);
        aController.displayMessageOnScreen(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {

        if (aController == null)
            aController = (Controller) getApplicationContext();

        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        aController.displayMessageOnScreen(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }


    /**
     * Create a notification to inform the user that server has sent a message.
     */

    private void generateNotification(Context context, String message, String NumberofMsgs, String typeMode) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int type = 0;
        if (typeMode != null && !typeMode.isEmpty())
            type = Integer.parseInt(typeMode);

        int Num = 1;
        if (NumberofMsgs != null && !NumberofMsgs.isEmpty()) {
            Num = Integer.parseInt(NumberofMsgs);
        }
        int idName = 0;

        SharedPreferences prefs = context.getSharedPreferences(NOTIFI_COUNT, MODE_PRIVATE);
        idName = prefs.getInt("idName", 0); //0 is the default value.


        String sumText = null;
        if (type == 0) {
            Num += idName;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("idName", Num);
            editor.commit();
            sumText = Num + " new notification";
            if (Num > 1)
                sumText = sumText + "s";

        } else if (type == 1) {
            sumText = "Priority Notification";
            Num = 1;
        }

        Intent notificationIntent = new Intent(context, MainActivity.class);
        //notificationIntent.putExtra("Tab", 1);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap large_icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("DPS RKP")
                .setLargeIcon(large_icon)
                .setSmallIcon(R.drawable.outline_ic)
                .setAutoCancel(true)
                .setContentIntent(intent)
                .setContentText(message)
                .setLights(Color.GREEN, 500, 5000)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(Notification.PRIORITY_HIGH)
                .setNumber(Num)
                .setVibrate(new long[]{0, 100, 200, 300});
//                builder.setColor(0xFF007508);
        Notification notification = new Notification.BigTextStyle(builder).bigText(message).setSummaryText(sumText).build();

        if (sumText.equals("Priority Notification")) {
            SQLiteAdapter adapter = new SQLiteAdapter(GCMIntentService.this);
            adapter.open();
            adapter.createHistory(message, new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            adapter.close();
        }
        notificationManager.notify(type, notification);

    }
}