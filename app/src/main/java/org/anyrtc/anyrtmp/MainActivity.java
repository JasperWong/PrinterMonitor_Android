/**
*  Copyright (c) 2016 The AnyRTC project authors. All Rights Reserved.
*
*  Please visit https://www.anyrtc.io for detail.
*
* The GNU General Public License is a free, copyleft license for
* software and other kinds of works.
*
* The licenses for most software and other practical works are designed
* to take away your freedom to share and change the works.  By contrast,
* the GNU General Public License is intended to guarantee your freedom to
* share and change all versions of a program--to make sure it remains free
* software for all its users.  We, the Free Software Foundation, use the
* GNU General Public License for most of our software; it applies also to
* any other work released this way by its authors.  You can apply it to
* your programs, too.
* See the GNU LICENSE file for more info.
*/
package org.anyrtc.anyrtmp;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import org.anyrtc.core.AnyRTMP;
import org.anyrtc.core.RTMPGuestHelper;
import org.anyrtc.core.RTMPGuestKit;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoRenderer;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,RTMPGuestHelper {

    private EditText mEditRtmpUrl;
    private SurfaceViewRenderer mSurfaceView = null;
    private VideoRenderer mRenderer = null;
    private String rtmpUrl="rtmp://123.207.18.69:1935/myapp/testav";
    private RTMPGuestKit mGuest = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        mSurfaceView = (SurfaceViewRenderer) findViewById(R.id.suface_view);
        mSurfaceView.init(AnyRTMP.Inst().Egl().getEglBaseContext(), null);
        mRenderer = new VideoRenderer(mSurfaceView);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
//        localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
//        Window window = getWindow();
//
        AnyRTMP.Inst();



        mGuest = new RTMPGuestKit(this, this);
        mGuest.StartRtmpPlay(rtmpUrl, mRenderer.GetRenderPointer());
    }

    public void OnBtnClicked(View view) {
//        String rtmpUrl = mEditRtmpUrl.getEditableText().toString();
//        if (rtmpUrl.length() == 0) {
//            return;
//        }
//        if (view.getId() == R.id.btn_start_live) {
//            Intent it = new Intent(this, HosterActivity.class);
//            Bundle bd = new Bundle();
//            bd.putString("rtmp_url", rtmpUrl);
//            it.putExtras(bd);
//            startActivity(it);
//        } else {
//            Intent it = new Intent(this, GuestActivity.class);
//            Bundle bd = new Bundle();
//            bd.putString("rtmp_url", rtmpUrl);
//            it.putExtras(bd);
//            startActivity(it);
//        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//        if (id == R.id.nav_guide) {
//            Intent guideIntent= new Intent(this,RouteActivity.class);
//            startActivity(guideIntent);
//        } else if (id == R.id.nav_switch) {
//            Intent switchIntent=new Intent(this,SwitchActivity.class);
//            startActivity(switchIntent);
//        } else if (id == R.id.nav_setting) {
//            Intent settingIntent=new Intent(this,SettingActivity.class);
//            startActivity(settingIntent);
//        }else if(id==R.id.nav_share){
//            Intent intent=new Intent(this,UserActivity.class);
//            startActivity(intent);
//        }
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

    }

    @Override
    public void OnRtmplayerStatus(int cacheTime, int curBitrate) {

    }

    @Override
    public void OnRtmplayerCache(int time) {

    }

    @Override
    public void OnRtmplayerClosed(int errcode) {

    }
}
