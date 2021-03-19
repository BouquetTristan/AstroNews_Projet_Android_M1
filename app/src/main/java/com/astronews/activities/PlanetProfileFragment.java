package com.astronews.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.astronews.adapter.ListNewsAdapter;
import com.astronews.core.apiServices.NewsService;
import com.astronews.core.apiServices.SsodService;
import com.astronews.core.model.News.ArticleCollection;
import com.astronews.core.model.ssod.Planet;
import com.astronews.core.model.ssod.PlanetRef;
import com.astronews.core.model.ssod.PlanetSizedNumber;

import java.util.ArrayList;

import rx.Observer;
import rx.Subscription;
import rx.android.concurrency.AndroidSchedulers;
import rx.concurrency.Schedulers;

/**
 * A fragment representing a list of Items.
 */
public class PlanetProfileFragment extends Fragment implements Observer<Planet> {
    private Subscription mSubscription;
    private Planet planet_profile;
    ListView profile_id_card;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PlanetProfileFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PlanetProfileFragment newInstance(String planet_id) {
        PlanetProfileFragment fragment = new PlanetProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get the view
        View view = inflater.inflate(R.layout.fragment_planet_profile, container, false);
        profile_id_card = (ListView) view.findViewById(R.id.profile_id_card);
        return view;
    }

    public void getProfile(String id) {
        if(mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mSubscription = SsodService.getFullPlanetInfos(
                id
        )
                .subscribeOn(Schedulers.threadPoolForIO())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    @Override
    public void onDestroy() {
        if(mSubscription != null)
            mSubscription.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable throwable) {
    }

    @Override
    public void onNext(Planet planet) {
        Log.d("DEBUG--TABS", "AAAAAAAAAAAAAAAAAAAAAAAAA  " + planet + "  AAAAAAAAAAAAAAAAAAAAAAAAAA");
        planet_profile = planet;
        ArrayList<CharSequence> profile_list = new ArrayList<CharSequence>();

        profile_list.add(Html.fromHtml("<b>" + getString(R.string.name) + ":</b> " + planet_profile.getName() + "\n"));
        profile_list.add(Html.fromHtml("<b>" + getString(R.string.mass) + ":</b> " + planet_profile.getMass().getValue() + ".10<sup>" + planet_profile.getMass().getMassExponent() + "</sup> kg.\n"));
        profile_list.add(Html.fromHtml("<b>" + getString(R.string.vol) + ":</b> " + planet_profile.getVol().getValue() + ".10<sup>" + planet_profile.getVol().getVolExponent() + "</sup> kg.\n"));
        profile_list.add(Html.fromHtml("<b>" + getString(R.string.gravity) + ":</b> " + planet_profile.getGravity() + " m.s<sup>-2</sup>.\n"));
        profile_list.add(Html.fromHtml("<b>" + getString(R.string.density) + ":</b> " + planet_profile.getDensity() + " g.cm<sup>3</sup>.\n"));
        profile_list.add(Html.fromHtml("<b>" + getString(R.string.mean_radius) + ":</b> " + planet_profile.getMeanRadius() + "km.\n"));
        profile_list.add(Html.fromHtml("<b>" + getString(R.string.sideral_orbit) + ":</b> " + planet_profile.getSideralOrbit() + " " + getString(R.string.day) + "\n"));
        profile_list.add(Html.fromHtml("<b>" + getString(R.string.sideral_rotation) + ":</b> " + planet_profile.getSideralRotation() + " " + getString(R.string.hour) + "\n"));
        profile_list.add(Html.fromHtml("<b>" + getString(R.string.discoverd_by) + ":</b> " + planet_profile.getDiscoveredBy() + "\n"));
        profile_list.add(Html.fromHtml("<b>" + getString(R.string.discovery_date) + ":</b> " + planet_profile.getDiscoveryDate() + "\n"));
        profile_list.add(Html.fromHtml("<b>" + getString(R.string.alternative_name) + ":</b> " + planet_profile.getAlternativeName() + "\n"));

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getActivity(), R.layout.profile_item_id_card, profile_list);
        profile_id_card.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.d("BB", planet.toString());
    }
}