package org.anyrtc.anyrtmp;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.anyrtc.core.AnyRTMP;
import org.anyrtc.core.RTMPGuestHelper;
import org.anyrtc.core.RTMPGuestKit;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoRenderer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class SecondActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RTMPGuestHelper {

    private SurfaceViewRenderer mSurfaceView = null;
    private VideoRenderer mRenderer = null;
    private String rtmpUrl = "rtmp://123.207.18.69:1935/myapp/testav2";
    private RTMPGuestKit mGuest = null;
    private ImageView mSwitch1 = null;
    private ImageView mSwitch2 = null;
    private ImageView mCamera = null;
    private int IsSwitch2On ;
    private int IsCameraOn;
    private int IsSwitch1On;

    private int switch1Remote;
    private int switch2Remote;
    private int cameraRemote;

    private boolean isChange = false;
    public static final int RESPONSE = 0;

    final private int SelectID = 2;
    Timer UpdateTimer = new Timer();

    private String remoteJson = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_second);
        setSupportActionBar(toolbar);

        WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
        localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mSurfaceView = (SurfaceViewRenderer) findViewById(R.id.surface_view);
        mSurfaceView.init(AnyRTMP.Inst().Egl().getEglBaseContext(), null);
        mRenderer = new VideoRenderer(mSurfaceView);
        mSwitch1 = (ImageView) findViewById(R.id.switch1_iv);
        mSwitch2 = (ImageView) findViewById(R.id.switch2_iv);
        mCamera = (ImageView) findViewById(R.id.camera_iv);

        mSwitch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsSwitch1On == 0) {
                    IsSwitch1On = 1;
                } else if (IsSwitch1On == 1) {
                    IsSwitch1On = 0;
                    mSwitch1.setImageResource(R.drawable.switch_off);
                }
                isChange = true;
            }
        });
        mSwitch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsSwitch2On == 0) {
                    IsSwitch2On = 1;
                } else if (IsSwitch2On == 1) {
                    IsSwitch2On = 0;
                }
                isChange = true;
            }
        });
        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsCameraOn == 0) {
                    IsCameraOn = 1;
                } else if (IsCameraOn == 1) {
                    IsCameraOn = 0;
                }
                isChange = true;
            }
        });
        AnyRTMP.Inst();
        mGuest = new RTMPGuestKit(this, this);
        UpdateTimer.schedule(UpdateTask,0, 50);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void OnBtnClicked(View view) {

    }

    private void makeSwitch1On(){

    }

    private void makeSwitch2ON(){

    }

    private void makeSwitch2OFF(){

    }

    TimerTask UpdateTask = new TimerTask() {
        @Override
        public void run() {
            if (isChange) {
                String url = new String(
                        "http://123.207.18.69:8082/3dPrinterMonitor/user/insert?"
                                + "id=" + SelectID
                                + "&username=first"
                                + "&switch1=" + IsSwitch1On
                                + "&switch2=" + IsSwitch2On
                                + "&camera=" + IsCameraOn
                );
                sendRequestWithHttpURLConnection(url);
            } else if (!isChange) {
                String url = new String("http://123.207.18.69:8082/3dPrinterMonitor/user/index?" + "SelectId=" + SelectID);
                sendRequestWithHttpURLConnection(url);
            }

//            if(switch2Remote==1){
//                makeCameraOn();
//            }
//            else if(switch2Remote==0){
//
//            }

        }
    };


    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RESPONSE: {
                    String response = (String) msg.obj;

                    String success = new String("update success");
                    if (response.equals(success)) {
                        isChange = false;
                    } else if(!isChange){
                        remoteJson = response;
                        {
                            try {
                                JSONObject jsonObject = new JSONObject(remoteJson);
                                switch2Remote=Integer.parseInt(jsonObject.getString("switch2"));
                                switch1Remote=Integer.parseInt(jsonObject.getString("switch1"));
                                cameraRemote=Integer.parseInt(jsonObject.getString("camera"));
                                IsSwitch2On=switch2Remote;
                                IsSwitch1On=switch1Remote;
                                IsCameraOn=cameraRemote;
                                if(1==IsSwitch2On){
                                    mSwitch2.setImageResource(R.drawable.switch_on);
                                }else if(0==IsSwitch2On){
                                    mSwitch2.setImageResource(R.drawable.switch_off);
                                }
                                if(1==IsSwitch1On){
                                    mSwitch1.setImageResource(R.drawable.switch_on);
                                }else if(0==IsSwitch1On){
                                    mSwitch1.setImageResource(R.drawable.switch_off);
                                }
                                if(1==IsCameraOn){
                                    mGuest.StartRtmpPlay(rtmpUrl, mRenderer.GetRenderPointer());
                                    mCamera.setImageResource(R.drawable.switch_on);
                                }else if(0==IsCameraOn){
                                    mGuest.StopRtmpPlay();
                                    mCamera.setImageResource(R.drawable.switch_off);
                                }
//                                Log.d("switch2", switch2Remote + "");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
//                        Log.d("test", remoteJson);

                    }
                    break;
                }
            }
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (UpdateTimer != null) {
            UpdateTimer.cancel();
            UpdateTimer = null;
        }
        if (mGuest != null) {
            mGuest.StopRtmpPlay();
            mGuest.Clear();
            mGuest = null;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.first) {
            Intent switchInten=new Intent(this,MainActivity.class);
            startActivity(switchInten);
        } else if (id == R.id.second) {
//            Intent switchIntent=new Intent(this,SecondActivity.class);
//            startActivity(switchIntent);
        } else if (id == R.id.third) {
//            Intent settingIntent=new Intent(this,SettingActivity.class);
//            startActivity(settingIntent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void OnRtmplayerOK() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SecondActivity.this, "打开监控", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void OnRtmplayerStatus(int cacheTime, int curBitrate) {

    }

    @Override
    public void OnRtmplayerCache(int time) {

    }

    @Override
    public void OnRtmplayerClosed(int errcode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SecondActivity.this, "关闭监控", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendRequestWithHttpURLConnection(final String urlConfig) {
        // 开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(new String(urlConfig));
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                    // 下面对获取到的输入流进行读取
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Message message = new Message();
                    message.what = RESPONSE;
                    // 将服务器返回的结果存放到Message中
                    message.obj = response.toString();
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
