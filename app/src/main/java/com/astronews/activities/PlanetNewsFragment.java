package com.astronews.activities;

import android.app.Fragment;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astronews.adapter.ListNewsAdapter;
import com.astronews.core.apiServices.NewsService;
import com.astronews.core.model.News.ArticleCollection;
import rx.Observer;
import rx.Subscription;
import rx.android.concurrency.AndroidSchedulers;
import rx.concurrency.Schedulers;

/**
 * A fragment representing a list of Items.
 */
public class PlanetNewsFragment extends Fragment implements Observer<ArticleCollection> {
    private Subscription mSubscription;
    private static String query;
    ListNewsAdapter adapter;
    RecyclerView newsList;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView.LayoutManager layoutManager;
    ArticleCollection articleCollection;
    private int nextPageToSearch = 1;
    private boolean tryToloadMore;

    private static final String PLANET_ID = "planet_id";
    // TODO: Customize parameters
    private int mPlanet_id = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PlanetNewsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PlanetNewsFragment newInstance(String planet_id) {
        PlanetNewsFragment fragment = new PlanetNewsFragment();
        Bundle args = new Bundle();
        args.putString(PLANET_ID, planet_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlanet_id = getArguments().getInt(PLANET_ID);
            query = mPlanet_id + " space";

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get the view
        View view = inflater.inflate(R.layout.fragment_planet_news_list, container, false);

        // Set the adapter
        if (view instanceof SwipeRefreshLayout) {
            query = "Space";

            newsList = view.findViewById(R.id.news_list);
            newsList.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            newsList.setLayoutManager(layoutManager);

            //View refresh
            swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefresh);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    nextPageToSearch = 1;
                    getArticles(query, 1);
                }
            });

            // load more items when scrolling from the bottom
            newsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (!recyclerView.canScrollVertically(1) && nextPageToSearch <= 10 && newState == 0) {
                        if(tryToloadMore) {
                            tryToloadMore = false;
                            getArticles(query, ++ nextPageToSearch);
                        } else {
                            tryToloadMore = true;
                        }
                    }
                }
            });
        }
        return view;
    }

    public void getArticles(String query, int page) {
        if(mSubscription != null) {
            mSubscription.unsubscribe();
        }
        if (!this.query.equals(query) && articleCollection != null) {
            articleCollection.getValue().removeAll(articleCollection.getValue());
            adapter.notifyDataSetChanged();
        }
        swipeRefreshLayout.setRefreshing(true);
        mSubscription = NewsService.getLatestArticlesAbout(
                query,
                page,
                10,
                true,
                true,
                true
        )
                .subscribeOn(Schedulers.threadPoolForIO())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
        /* used for when called from outside the class */
        nextPageToSearch = page;
        this.query = query;
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
        // swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onNext(ArticleCollection collection) {
        if (nextPageToSearch == 1) {
            articleCollection = collection;
            adapter = new ListNewsAdapter(articleCollection, getActivity());
            newsList.setAdapter(adapter);
        } else {
            articleCollection.getValue().addAll(collection.getValue());
            articleCollection.setTotalCount(collection.getTotalCount());
        }
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}