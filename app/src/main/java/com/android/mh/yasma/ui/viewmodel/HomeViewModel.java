package com.android.mh.yasma.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.mh.yasma.UserRepository;
import com.android.mh.yasma.model.Post;
import com.android.mh.yasma.model.UserDetail;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by @author Mubarak Hussain.
 */
public class HomeViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private CompositeDisposable compositeDisposable;
    private MutableLiveData<List<Post>> poListMutableLiveData = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance(application);
        compositeDisposable = new CompositeDisposable();
    }

    public LiveData<List<Post>> getListOfPost() {
        userRepository.getListOfPost()
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<Post>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<Post> list) {
                        mergeResponse(list);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });


        return poListMutableLiveData;
    }

    private List<Post> localCopyPost = new ArrayList<>();

    private void mergeResponse(List<Post> list) {
        createPostObservable(list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .concatMap(new Function<Post, Observable<Post>>() {
                    @Override
                    public Observable<Post> apply(Post post) throws Exception {
                        return getUserDetail(post);
                    }
                }).subscribe(new Observer<Post>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(Post post) {
                Log.d("MUB", post.getUserDetail() + "");
                localCopyPost.add(post);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.d("MUB", "onComplete====");
                poListMutableLiveData.setValue(localCopyPost);
            }
        });
    }


    private Observable<Post> createPostObservable(final List<Post> list) {
        return Observable.create(new ObservableOnSubscribe<Post>() {
            @Override
            public void subscribe(ObservableEmitter<Post> emitter) throws Exception {
                for (Post post : list) {
                    if (!emitter.isDisposed()) {
                        emitter.onNext(post);
                    }

                }
                if (!emitter.isDisposed()) {
                    emitter.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io());
    }


    private Observable<Post> getUserDetail(final Post post) {
        return Observable.create(new ObservableOnSubscribe<Post>() {

            @Override
            public void subscribe(final ObservableEmitter<Post> emitter) throws Exception {
                userRepository.getUserDetail(post.getUserId() + "").
                        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<UserDetail>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                compositeDisposable.add(d);
                            }

                            @Override
                            public void onSuccess(UserDetail userDetail) {
                                if (!emitter.isDisposed()) {
                                    post.setUserDetail(userDetail);
                                    emitter.onNext(post);
                                    emitter.onComplete();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
            }
        });

    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
