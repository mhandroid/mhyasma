package com.android.mh.yasma.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.mh.yasma.UserRepository;
import com.android.mh.yasma.model.Album;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by @author Mubarak Hussain.
 */
public class AlbumsViewModel extends AndroidViewModel {

    UserRepository userRepository;
    private CompositeDisposable compositeDisposable;
    private MutableLiveData<List<Album>> albumListMutableLiveData = new MutableLiveData<>();

    public AlbumsViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance(application);
        compositeDisposable = new CompositeDisposable();
    }


    public LiveData<List<Album>> getListAlbums() {
        userRepository.getAlbums()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Album>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(List<Album> albums) {
                Log.d("MUB","albums list"+albums.size());
                albumListMutableLiveData.setValue(albums);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


        return albumListMutableLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }
}
