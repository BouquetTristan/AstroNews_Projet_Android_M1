package com.astronews.core.apiServices;

import com.astronews.core.config.Config;
import com.astronews.core.model.News.ArticleCollection;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.concurrency.Schedulers;
import rx.subscriptions.Subscriptions;

public class NewsService {

    //Singleton
    private static final NewsService INSANTCE = new NewsService();
    //constructeur privé
    private NewsService() {
    }

    //calls manager
    private interface NewsApiServiceApiManager {
        @Headers({
                "x-rapidapi-key: " + Config.USEARCH_KEY,
                "x-rapidapi-host: contextualwebsearch-websearch-v1.p.rapidapi.com",
                "useQueryString: true"
        })
        @GET("/NewsSearchAPI")
        ArticleCollection getArticles(
                @Query("q") String query,
                @Query("pageNumber") int page_number,
                @Query("pageSize") int page_size,
                @Query("autoCorrect") boolean auto_correct,
                @Query("safeSearch") boolean safe_search,
                @Query("withThumbnails") boolean with_thumbnails
        );
    }

    private static final RestAdapter restAdapter = new RestAdapter.Builder()
            .setServer("https://rapidapi.p.rapidapi.com/api/search/")
            .build();

    private static final NewsService.NewsApiServiceApiManager apiManager = restAdapter.create(NewsService.NewsApiServiceApiManager.class);


    // Instance unique pré-initialisée
    public NewsService getInstance() {
        return INSANTCE;
    }

    public static Observable<ArticleCollection> getLatestArticlesAbout(
           final String query,
           final int page_number,
           final int page_size,
           final boolean auto_correct,
           final  boolean safe_search,
           final boolean with_thumbnails
    ) {
        return Observable.create(new Observable.OnSubscribeFunc<ArticleCollection>() {
            @Override
            public Subscription onSubscribe(Observer<? super ArticleCollection> observer) {
                try {
                     ArticleCollection articles = apiManager.getArticles(
                             query,
                             page_number,
                             page_size,
                             auto_correct,
                             safe_search,
                             with_thumbnails
                     );
                    observer.onNext(articles);
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }
                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.threadPoolForIO());
    }
}
