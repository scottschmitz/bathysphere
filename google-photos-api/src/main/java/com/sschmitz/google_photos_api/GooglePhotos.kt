package com.sschmitz.google_photos_api

import android.content.Context
import com.sschmitz.google_photos_api.model.MediaItemsJson.MediaItems
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

object GooglePhotos {

  private const val BASE_URL = "https://photoslibrary.googleapis.com"

  internal interface Endpoint {
    @GET("v1/mediaItems")
    fun list(
      @Query("pageSize") pageSize: Int,
      @Query("pageToken") pageToken: String?
    ): Single<MediaItems>
  }

  class Service(
    applicationContext: Context,
    googleClientId: String,
    googleClientSecret: String
  ) {
    private val retrofit = Api(applicationContext, googleClientId, googleClientSecret).buildRetrofit(BASE_URL, true)
    private val endpoint = retrofit.create(Endpoint::class.java)

    fun list(
      pageToken: String? = null,
      pageSize: Int = 100
    ): Single<Result<MediaItems>> {
      return endpoint.list(pageSize, pageToken)
        .map { Result.success(it) }
        .onErrorReturn { Result.failure(it) }
    }
  }
}
