package com.android.mh.yasma.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.android.mh.yasma.UserRepository;
import com.android.mh.yasma.model.PostComments;
import com.android.mh.yasma.model.PostDetails;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by @author Mubarak Hussain.
 */
public class PostDetailsViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private CompositeDisposable compositeDisposable;
    private MutableLiveData<List<PostComments>> postCommentsMutable = new MutableLiveData<>();
    private MutableLiveData<PostDetails> postDetalsMutable = new MutableLiveData<>();

    public PostDetailsViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance(application);
        compositeDisposable = new CompositeDisposable();
    }

    public LiveData<PostDetails> getPostDetails(final String postId) {
        Call<PostDetails> call = userRepository.getPostDetails(postId);
        call.enqueue(new Callback<PostDetails>() {
            @Override
            public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                if (response.body() != null) {
                    postDetalsMutable.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<PostDetails> call, Throwable t) {

            }
        });

       return postDetalsMutable;
    }

    public LiveData<List<PostComments>> getPostComments(final String postId) {
        Call<List<PostComments>> call = userRepository.getPostComments(postId);
        call.enqueue(new Callback<List<PostComments>>() {
            @Override
            public void onResponse(Call<List<PostComments>> call, Response<List<PostComments>> response) {
                postCommentsMutable.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<PostComments>> call, Throwable t) {

            }
        });

        return postCommentsMutable;
    }

}
