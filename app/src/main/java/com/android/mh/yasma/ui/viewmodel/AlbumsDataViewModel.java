package com.android.mh.yasma.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.mh.yasma.UserRepository;
import com.android.mh.yasma.exception.NoNetworkException;
import com.android.mh.yasma.model.AlbumPhoto;
import com.android.mh.yasma.model.Resource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * View model to handle album detail activity
 * Created by @author Mubarak Hussain.
 */
public class AlbumsDataViewModel extends AndroidViewModel {
    private final String TAG = AlbumsDataViewModel.class.getSimpleName();
    private UserRepository userRepository;
    private MutableLiveData<Resource<List<AlbumPhoto>>> albumMutableLiveData = new MutableLiveData<>();

    public AlbumsDataViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance(application);
    }


    /**
     * Method to fetch list of album detail
     * @param id
     * @return
     */
    public LiveData<Resource<List<AlbumPhoto>>> getAlbumsOfPhotos(String id) {
        userRepository.getAlbumsOfPhotos(id).enqueue(new Callback<List<AlbumPhoto>>() {
            @Override
            public void onResponse(@NonNull Call<List<AlbumPhoto>> call, @NonNull Response<List<AlbumPhoto>> response) {
                if (response.body() != null && response.isSuccessful())
                    albumMutableLiveData.setValue(Resource.success(response.body()));
                else {
                    albumMutableLiveData.setValue(Resource.<List<AlbumPhoto>>error("Something went wrong!!"));
                }
            }

            @Override
            public void onFailure(Call<List<AlbumPhoto>> call, Throwable t) {
                //network failure :( inform the user and possibly retry
                if (t instanceof NoNetworkException) {
                    Log.d(TAG, "No connectivity exception");
                    albumMutableLiveData.postValue(Resource.<List<AlbumPhoto>>noInternet(t.getMessage()));

                } else
                    albumMutableLiveData.postValue(Resource.<List<AlbumPhoto>>error(t.getMessage()));

            }
        });

        return albumMutableLiveData;
    }
}
