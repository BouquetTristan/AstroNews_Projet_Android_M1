package com.astronews.core.apiServices;

import com.astronews.core.model.ssod.Planet;
import com.astronews.core.model.ssod.PlanetCollection;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.concurrency.Schedulers;
import rx.subscriptions.Subscriptions;

public class SsodService {

    //Singleton
    private static final SsodService INSANTCE = new SsodService();
    //constructeur privé
    private SsodService() {
    }

    //calls manager
    private interface SsodServiceApiManager {
        @GET("/bodies?data=id,name,englishName&filter[]=isPlanet,neq,true")
        PlanetCollection getAllPlanetIdsAndName();

        @GET("/bodies/{id}")
        Planet getFullPlanetInfos(@Path("id") String id);
    }

    private static final RestAdapter restAdapter = new RestAdapter.Builder()
            .setServer("http://api.le-systeme-solaire.net/rest/")
            .build();

    private static final SsodServiceApiManager apiManager = restAdapter.create(SsodServiceApiManager.class);


    // Instance unique pré-initialisée
    public SsodService getInstance() {
        return INSANTCE;
    }

    public static Observable<PlanetCollection> getAllPlanetIdsAndName() {
        return Observable.create(new Observable.OnSubscribeFunc<PlanetCollection>() {
            @Override
            public Subscription onSubscribe(Observer<? super PlanetCollection> observer) {
                try {
                    PlanetCollection planetList = apiManager.getAllPlanetIdsAndName();
                    observer.onNext(planetList);
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }
                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.threadPoolForIO());
    }

    public static Observable<Planet> getFullPlanetInfos(final String id) {
        return Observable.create(new Observable.OnSubscribeFunc<Planet>() {
            @Override
            public Subscription onSubscribe(Observer<? super Planet> observer) {
                try {
                    Planet planet = apiManager.getFullPlanetInfos(id);
                    observer.onNext(planet);
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }
                return Subscriptions.empty();
            }
        }).subscribeOn(Schedulers.threadPoolForIO());
    }
}
