package com.Nhom10.BTL;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class NhaBepNhanDL extends AppCompatActivity {

    DatabaseReference mData = FirebaseDatabase.getInstance("https://smartorder-13eb1.firebaseio.com/").getReference();
    ListView lvBanAn;
    QLBanAnAdapter adapter;
    String tentb,dl="";
    String key;
    int size1,size2;
    int flag=0;
    ArrayList<QLBanAn> list;
    static boolean active = false;

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nha_bep_nhan_dl);
        lvBanAn = (ListView) findViewById(R.id.lvBanAn);
        list = new ArrayList<>();
        addBan();
        HienThi();

        lvBanAn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent MoBan = new Intent(NhaBepNhanDL.this, NhaBepHienMon.class);
                MoBan.putExtra("tenmon",list.get(position).getTenban());
                startActivity(MoBan);

                //Toast.makeText(NhaBepNhanDL.this,list.get(position).getTenban(),Toast.LENGTH_SHORT).show();
            }
        });

    }
    public  void HienThi()
    {
        adapter = new QLBanAnAdapter(this,R.layout.nhabep,list);
        lvBanAn.setAdapter(adapter);
    }
    private String CHANNEL_ID="App gọi món";

    private void createNotificationChannel() {
        CharSequence channelName = CHANNEL_ID;
        String channelDesc = "Thanh Nguyen - NB";
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_NONE;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDesc);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            NotificationChannel currChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if (currChannel == null)
                notificationManager.createNotificationChannel(channel);
        }
    }




    public void createNotification(String message) {

        CHANNEL_ID = "App gọi món - Nhà bếp";
        if (message != null ) {
            createNotificationChannel();

            Intent intent = new Intent(this, NhaBepNhanDL.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_dashboard_black_24dp)
                    .setContentTitle("Yêu cầu chế biến")
                    .setContentText(message)
                    .setPriority(NotificationCompat.FLAG_LOCAL_ONLY)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            Uri uri = RingtoneManager.getDefaultUri(R.raw.cb);
            mBuilder.setSound(uri);
            MediaPlayer mp = MediaPlayer.create(this, R.raw.cb);
            mp.start();
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(0, mBuilder.build());
        }
    }

    private void addBan() {
        list.clear();
        mData.child("BanAn").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                key = dataSnapshot.getKey().trim();
                if (dataSnapshot.child("TinhTrang").getValue().toString().trim().equals("true") || dataSnapshot.child("TinhTrang").getValue().toString().trim().equals("cho")){
                    list.add(new QLBanAn(key));
                    adapter.notifyDataSetChanged();
                    tentb=dataSnapshot.getKey().trim();
                }
                size1=list.size();


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(active==true)
                {
                    startActivity(getIntent());
                    createNotification(tentb);
                }


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                adapter.notifyDataSetChanged();
            }
        });
    }
    private void addBan1() {
        mData.child("BanAn").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                key = dataSnapshot.getKey().trim();
                list.clear();
                if (dataSnapshot.child("TinhTrang").getValue().toString().trim().equals("true") || dataSnapshot.child("TinhTrang").getValue().toString().trim().equals("cho")){
                    list.add(new QLBanAn(key));
                    adapter.notifyDataSetChanged();
                    tentb=dataSnapshot.getKey().trim();
                }
                size2=list.size();
                Toast.makeText(NhaBepNhanDL.this, size1+ "-"+ size2, Toast.LENGTH_SHORT).show();

                new Timer().schedule(
                        new TimerTask(){

                            @Override
                            public void run(){
                               if(size2>size1)
                                {
                                    createNotification(tentb);
                                }


                            }

                        }, 500);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //adapter.notifyDataSetChanged();
            }
        });
    }

}
