package model;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface DogsApi {

    @GET("DevTides/DogsApi/master/dogs.json")
    Single<List<DogBreed>> getDogs();   //single(it`s an observable) means that it returns a single value and then closes(part of RxJava)

}
