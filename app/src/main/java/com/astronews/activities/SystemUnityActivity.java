package com.astronews.activities;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.astronews.core.communicationBridge.CommunicationBridge;
import com.astronews.core.config.Config;
import com.daimajia.swipe.SwipeLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.unity3d.player.UnityPlayerActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

import rx.Observer;
import rx.Subscription;
import rx.android.concurrency.AndroidSchedulers;
import rx.concurrency.Schedulers;

public class SystemUnityActivity extends UnityPlayerActivity implements Observer<String> {
    Subscription mSubscription;
    LayoutParams lp;
    LayoutParams lp_profile;
    FrameLayout layout;
    FragmentManager manager;
    TabLayout tab_layout;
    SwipeLayout swipe_layout;
    PlanetNewsFragment news_frag;
    PlanetProfileFragment profile_frag;

    String current_planet_name;
    String current_search_query = "Space";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solar_system);
        setTitle(R.string.solar_map_title);

        // Listening to unity requests
        listenToUnity();

        // Set up view and add the Unity view
        layout = (FrameLayout) findViewById(R.id.frame_layout_solar_system);
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp_profile = new LayoutParams(LayoutParams.MATCH_PARENT, 500);
        layout.addView(mUnityPlayer.getView(), 0, lp);

        tab_layout = findViewById(R.id.ass_tab_layout);
        swipe_layout = (SwipeLayout) findViewById(R.id.ass_swipe_layout);

        //Link fragments to the tabs
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab_layout.getSelectedTabPosition() == 0) {
                    swipe_layout.close();
                } else {
                    swipe_layout.open();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Set swipe Layout callbacks
        swipe_layout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //tab_layout.setSelectedTabIndicator((int) R.id.tab_item_1);
                tab_layout.selectTab(tab_layout.getTabAt(0));
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {}

            @Override
            public void onStartOpen(SwipeLayout layout) {}

            @Override
            public void onOpen(SwipeLayout layout) {
                //tab_layout.setSelectedTabIndicator((int) R.id.tab_item_2);
                tab_layout.selectTab(tab_layout.getTabAt(1));
            }

            @Override
            public void onStartClose(SwipeLayout layout) {}

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {}
        });

        //init the news fragment
        if (savedInstanceState == null) {
            news_frag = new PlanetNewsFragment();
            profile_frag = new PlanetProfileFragment();
            manager = getFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.news_fragment, news_frag)
                    .commit();
            manager.beginTransaction()
                    .replace(R.id.profile_fragment, profile_frag)
                    .commit();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Snackbar sb = Snackbar.make(findViewById(R.id.main_layout), R.string.how_to_use, Snackbar.LENGTH_INDEFINITE);
        sb.setAction(R.string.sb_action, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sb.dismiss();
            }
        })
        .show();
    }

    private void listenToUnity() {
        mSubscription = CommunicationBridge.bs
                .subscribeOn(Schedulers.threadPoolForIO())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    @Override
    protected void onDestroy() {
        if(mSubscription != null)
            mSubscription.unsubscribe();
        super.onDestroy();
        mUnityPlayer.quit();
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable throwable) {
        Log.d("OBSERVABLE_ERROR", throwable.getMessage());
        Toast.makeText(this, "Error =>" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNext(String data) {
        Log.d("JSON_DATA", data);
        JSONObject obj;
        String action;
        JSONArray args;

        try {
            obj = new JSONObject(data);
            action = obj.getString("action");
            args = obj.getJSONArray("args");
        } catch (Exception e) {
            //TO DO handle errors from unity
            Log.d("UNITYDATA_ER", e.toString());
            return;
        }

        if (action.equals("RETURN")) {
            current_planet_name = "";
            layout.setLayoutParams(lp);
            /*try {
                if (args.get(0).equals("out")) {
                    mUnityPlayer.quit();
                } else {
                        current_planet_name = "";
                }
            } catch (Exception e) {
                Log.d("DEBUG_RETURN", e.toString());
            }*/
        }

        if (action.equals("SHOW_PLANET_PROFILE")) {
            try {
                String itemName = args.getString(0);
                if (planetNameHasProfile(itemName) && !itemName.equals(current_planet_name)) {
                    layout.setLayoutParams(lp_profile);
                    current_planet_name = itemName;
                    current_search_query = current_planet_name + " space";
                    // load profile if possible
                    news_frag.getArticles(current_search_query, 1);
                    profile_frag.getProfile(current_planet_name);

                    manager.beginTransaction()
                            .replace(R.id.profile_fragment, profile_frag)
                            .commit();

                    // load contextual profile name
                    tab_layout.getTabAt(1).setText(getResources()
                            .getIdentifier(
                                    current_planet_name.toLowerCase(),
                                    "string",
                                    this.getPackageName())
                    );
                    tab_layout.selectTab(tab_layout.getTabAt(0));
                } else {
                    //show some placeholder ?
                }
            } catch (Exception e) {
                Log.d("DEBUG_SHOW_PROFILE", e.toString());
                return; //TO DO handle errors properly
            }
        }
    }

    private boolean planetNameHasProfile(String name) {
        return (Arrays.asList(Config.SUPPORTED_PLANETS)).contains(name);
    }
}