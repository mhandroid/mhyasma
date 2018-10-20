package com.android.mh.yasma.api;

import com.android.mh.yasma.model.Album;
import com.android.mh.yasma.model.AlbumDetails;
import com.android.mh.yasma.model.AlbumPhoto;
import com.android.mh.yasma.model.Post;
import com.android.mh.yasma.model.PostComments;
import com.android.mh.yasma.model.PostDetails;
import com.android.mh.yasma.model.UserDetail;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface of api resource
 */
public interface ApiInterface {

    @GET("posts")
    Single<List<Post>> getListOfPost();

    @GET("/posts/{post_id}")
    Call<PostDetails> getPostDetails(@Path("post_id") String postId);

    @GET("/posts/{post_id}/comments")
    Call<List<PostComments>> getPostComment(@Path("post_id") String postId);

    @GET("/albums")
    Observable<List<Album>> getListOfPostAlbums();

    @GET("/albums/{album_id}")
    Observable<AlbumDetails> getAlbumsDetails(@Path("album_id") String albumId);

    @GET("/albums/{album_id}/photos")
    Call<List<AlbumPhoto>> getAlbumsOfPhotos(@Path("album_id") String albumId);

    @GET("/users/{user_id}")
    Single<UserDetail> getUserDetails(@Path("user_id") String userId);
    @GET("/users")
    Observable<List<UserDetail>> getUsers();
}
