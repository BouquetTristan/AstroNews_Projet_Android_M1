package com.astronews.core.apiServices;

import com.astronews.core.config.Config;
import com.astronews.core.model.mTranslation.Translation;

import java.util.Locale;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.concurrency.Schedulers;
import rx.subscriptions.Subscriptions;

public class MTranslationService {
    //Singleton
    private static final MTranslationService INSANTCE = new MTranslationService();
    //constructeur privé
    private MTranslationService() {
    }

    //calls manager
    private interface MTranslationServiceApiManager {
        @Headers({
                "x-rapidapi-key: " + Config.USEARCH_KEY,
                "x-rapidapi-host: just-translated.p.rapidapi.com",
                "useQueryString: true"
        })
        @GET("/")
        Translation translate(
                @Query("text") String query,
                @Query("lang") String lang
        );
    }

    private static final RestAdapter restAdapter = new RestAdapter.Builder()
            .setServer("https://just-translated.p.rapidapi.com")
            .build();

    private static final MTranslationService.MTranslationServiceApiManager apiManager = restAdapter.create(MTranslationService.MTranslationServiceApiManager.class);


    // Instance unique pré-initialisée
    public MTranslationService getInstance() {
        return INSANTCE;
    }


    public static Observable<Translation> getTranslation(final String query) {
        return Observable.create(new Observable.OnSubscribeFunc<Translation>() {
            @Override
            public Subscription onSubscribe(Observer<? super Translation> observer) {
                String lang = Locale.getDefault().getLanguage();
                try {
                    Translation trans = apiManager.translate(query, lang);
                    observer.onNext(trans);
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }
                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.threadPoolForIO());
    }
}
