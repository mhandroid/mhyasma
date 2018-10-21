package com.android.mh.yasma.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.mh.yasma.UserRepository;
import com.android.mh.yasma.model.Post;
import com.android.mh.yasma.model.Resource;
import com.android.mh.yasma.model.UserDetail;

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
    private final String TAG = HomeViewModel.class.getSimpleName();
    private UserRepository userRepository;
    private CompositeDisposable compositeDisposable;
    private MutableLiveData<Resource<Post>> poListMutableLiveData = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance(application);
        compositeDisposable = new CompositeDisposable();
    }

    public LiveData<Resource<Post>> getListOfPost() {
        userRepository.getListOfPost()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<List<Post>, Observable<Post>>() {
                    @Override
                    public Observable<Post> apply(List<Post> list) throws Exception {
                        return Observable.fromIterable(list);
                    }
                })
                .concatMapEager(new Function<Post, Observable<Post>>() {
                    @Override
                    public Observable<Post> apply(Post post) throws Exception {
                        Log.d("MUB", post.getUserId() + "");
                        return getUserDetail(post);
                    }
                })
                .subscribe(new Observer<Post>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Post post) {
                        Log.d("MUB", post.getId() + "POST");
                        poListMutableLiveData.setValue(Resource.success(post));
                    }

                    @Override
                    public void onError(Throwable e) {
                        poListMutableLiveData.setValue(Resource.<Post>error(e.getMessage()));
                    }

                    @Override
                    public void onComplete() {

                    }
                });


        return poListMutableLiveData;

    }

    /**
     * Mthode to fetch user details of created post
     * @param post
     * @return
     */
    private Observable<Post> getUserDetail(final Post post) {
        return Observable.create(new ObservableOnSubscribe<Post>() {

            @Override
            public void subscribe(final ObservableEmitter<Post> emitter) throws Exception {
                userRepository.getUserDetail(post.getUserId() + "").
                        subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
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
                                Log.d(TAG, "onError====" + e.getMessage());
                                post.setUserDetail(null);
                                emitter.onNext(post);
                                emitter.onComplete();
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
