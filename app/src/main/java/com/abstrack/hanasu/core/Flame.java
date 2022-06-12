package com.abstrack.hanasu.core;

import android.os.AsyncTask;
import android.util.Log;

import com.abstrack.hanasu.core.user.UserManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Flame {

    // :(
    public static final String FCM_API_URL = "https://fcm.googleapis.com/fcm/send";
    public static final String SERVER_KEY = "AAAARBH4WWo:APA91bEZ5nEryWPkD102rd8Y6-yAFmD9C-8NBWOchw4nA5r3Y6HX3443XrHu69dF4aa14F3DJ7gQB1JqIteyZrayRAHo0e0oM2IE70run0DN__X2wH3p18ca5ZxHSSi2UFS1CEgRqDfw";
    public static final MediaType CONTENT_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static boolean isPersistenceEnabled;

    public static void sendMessageNotification(String notificationTitle, String notificationMessage, String tokenToSend) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json=new JSONObject();
                    JSONObject notificationDataJson =new JSONObject();

                    notificationDataJson.put("title", notificationTitle);
                    notificationDataJson.put("body", notificationMessage);

                    json.put("notification", notificationDataJson);
                    json.put("to", tokenToSend);

                    RequestBody body = RequestBody.create(CONTENT_TYPE, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization","key="+ SERVER_KEY)
                            .url(FCM_API_URL)
                            .post(body)
                            .build();

                    client.newCall(request).execute();
                } catch (Exception e){
                    Log.e("Flame", "An error ocurred sending notification ", e);
                }
                return null;
            }
        }.execute();
    }

    public static void sendFriendRequestNotification(String notificationTitle, String notificationMessage, String chatRoomUUID, String tokenToSend) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json =new JSONObject();

                    JSONObject notificationDataJson =new JSONObject();
                    JSONObject messageDataJson =new JSONObject();

                    notificationDataJson.put("title", notificationTitle);
                    notificationDataJson.put("body", notificationMessage);

                    messageDataJson.put("chatRoomUUID", chatRoomUUID);
                    messageDataJson.put("contactIdentifier", UserManager.currentPublicUser.getIdentifier());

                    json.put("to", tokenToSend);
                    json.put("notification", notificationDataJson);
                    json.put("data", messageDataJson);

                    RequestBody body = RequestBody.create(CONTENT_TYPE, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization","key="+ SERVER_KEY)
                            .url(FCM_API_URL)
                            .post(body)
                            .build();

                    Response response = client.newCall(request).execute();
                    Log.d("Test", response.toString());
                    Log.d("Test", notificationDataJson.toString());
                    Log.d("Test", messageDataJson.toString());
                } catch (Exception e){
                    Log.e("Flame", "An error ocurred sending notification ", e);
                }
                return null;
            }
        }.execute();
    }

    public static void setDataPersistence(){
        if(!isPersistenceEnabled){
            getFireDatabase().setPersistenceEnabled(true);
            isPersistenceEnabled = true;
        }
    }

    public static FirebaseDatabase getFireDatabase() {
        return FirebaseDatabase.getInstance();
    }

    public static DatabaseReference getDataBaseReferenceWithPath(String path) {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference(path);
        dbReference.keepSynced(true);
        return dbReference;
    }

    public static DatabaseReference getDataBaseReferenceWithPath(String path, boolean synced) {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference(path);
        dbReference.keepSynced(synced);
        return dbReference;
    }

    public static StorageReference getStorageReference() {
        return FirebaseStorage.getInstance().getReference();
    }

    public static FirebaseAuth getFireAuth() {
        return FirebaseAuth.getInstance();
    }

    public static boolean isFireUserLogged() {
        if (getFireAuth().getCurrentUser() != null) {
            return true;
        }

        return false;
    }
}
