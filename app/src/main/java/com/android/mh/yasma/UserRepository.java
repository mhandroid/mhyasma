package com.android.mh.yasma;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.android.mh.yasma.api.ApiClient;
import com.android.mh.yasma.api.ApiInterface;
import com.android.mh.yasma.model.Album;
import com.android.mh.yasma.model.AlbumPhoto;
import com.android.mh.yasma.model.Post;
import com.android.mh.yasma.model.PostComments;
import com.android.mh.yasma.model.PostDetails;
import com.android.mh.yasma.model.UserDetail;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Repository class to fetch data
 */
public class UserRepository {
    private static final String TAG = UserRepository.class.getSimpleName();

    private static UserRepository sUserReposatory;
    private static ApiInterface apiInterface;

    private UserRepository() {
    }

    public static UserRepository getInstance(Context context) {
        if (sUserReposatory == null) {
            sUserReposatory = new UserRepository();
            Retrofit retrofit = ApiClient.getClient(context);
            apiInterface = retrofit.create(ApiInterface.class);
        }
        return sUserReposatory;
    }

    public Single<List<Post>> getListOfPost(){
        return apiInterface.getListOfPost();
    }

    public Single<UserDetail> getUserDetail(String id){
        return apiInterface.getUserDetails(id);
    }
    public Observable<List<UserDetail>> getUsers(){
        return apiInterface.getUsers();
    }
    public Observable<List<Album>> getAlbums(){
        return apiInterface.getListOfPostAlbums();
    }

    public Call<PostDetails> getPostDetails(String postId){
        return apiInterface.getPostDetails(postId);
    }

    public Call<List<PostComments>> getPostComments(String postId){
        return apiInterface.getPostComment(postId);
    }

    public Call<List<AlbumPhoto>> getAlbumsOfPhotos(String albumId){
        return apiInterface.getAlbumsOfPhotos(albumId);
    }

}
