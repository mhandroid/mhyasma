package com.android.mh.yasma.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.mh.yasma.UserRepository;
import com.android.mh.yasma.exception.NoNetworkException;
import com.android.mh.yasma.model.PostComments;
import com.android.mh.yasma.model.PostDetails;
import com.android.mh.yasma.model.Resource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by @author Mubarak Hussain.
 */
public class PostDetailsViewModel extends AndroidViewModel {
    private final String TAG = PostDetailsViewModel.class.getSimpleName();
    private UserRepository userRepository;
    private MutableLiveData<Resource<List<PostComments>>> postCommentsMutable = new MutableLiveData<>();
    private MutableLiveData<Resource<PostDetails>> postDetalsMutable = new MutableLiveData<>();

    public PostDetailsViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance(application);
    }

    /**
     * Method to get post details
     * @param postId
     * @return
     */
    public LiveData<Resource<PostDetails>> getPostDetails(final String postId) {
        Call<PostDetails> call = userRepository.getPostDetails(postId);
        call.enqueue(new Callback<PostDetails>() {
            @Override
            public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                if (response.body() != null && response.isSuccessful()) {
                    postDetalsMutable.setValue(Resource.success(response.body()));
                } else {
                    postDetalsMutable.setValue(Resource.<PostDetails>error("Something went wrong!!"));
                }
            }

            @Override
            public void onFailure(Call<PostDetails> call, Throwable t) {
                if (t instanceof NoNetworkException) {
                    Log.d(TAG, "No connectivity exception");
                    postDetalsMutable.postValue(Resource.<PostDetails>noInternet(t.getMessage()));

                } else
                    postDetalsMutable.postValue(Resource.<PostDetails>error(t.getMessage()));
            }
        });

        return postDetalsMutable;
    }

    /**
     * Method to get post comments
     * @param postId
     * @return
     */
    public LiveData<Resource<List<PostComments>>> getPostComments(final String postId) {
        Call<List<PostComments>> call = userRepository.getPostComments(postId);
        call.enqueue(new Callback<List<PostComments>>() {
            @Override
            public void onResponse(Call<List<PostComments>> call, Response<List<PostComments>> response) {
                Log.d(TAG, "onResponse" + response);
                if (response.body() != null && response.isSuccessful()) {
                    postCommentsMutable.setValue(Resource.success(response.body()));
                } else {
                    postCommentsMutable.setValue(Resource.<List<PostComments>>error("Something went wrong!!"));
                }
            }

            @Override
            public void onFailure(Call<List<PostComments>> call, Throwable t) {
                if (t instanceof NoNetworkException) {
                    Log.d(TAG, "No connectivity exception");
                    postCommentsMutable.postValue(Resource.<List<PostComments>>noInternet(t.getMessage()));

                } else
                    postCommentsMutable.postValue(Resource.<List<PostComments>>error(t.getMessage()));

            }
        });

        return postCommentsMutable;
    }

}
