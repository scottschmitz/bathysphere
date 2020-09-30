package com.sschmitz.google_photos_api

import android.content.Context
import com.sschmitz.google_photos_api.model.GoogleAuthJson
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

/*
 * https://developers.google.com/identity/protocols/OAuth2WebServer
 */
object GoogleAuth {

  private const val BASE_URL = "https://accounts.google.com/o/oauth2/"

  internal interface Endpoint {
    @POST("token")
    fun getToken(
      @Query("scope") scope: String,
      @Query("access_type") accessType: String = "offline",
      @Query("prompt") approvalPrompt: String = "consent",
      @Body request: GoogleAuthJson.Request
    ): Single<GoogleAuthJson.Response>

    @POST("token")
    fun refreshAuthToken(
      @Query("scope") scope: String,
      @Query("access_type") accessType: String = "offline",
      @Query("grant_type") grantType: String = "refresh_token",
      @Query("prompt") approvalPrompt: String = "consent",
      @Body request: GoogleAuthJson.Request
    ): Single<GoogleAuthJson.Response>
  }

  class Service(
    applicationContext: Context,
    private val googleClientId: String,
    private val googleClientSecret: String
  ) {
    private val retrofit = Api(applicationContext, googleClientId, googleClientSecret).buildRetrofit(BASE_URL, false)
    private val endpoint = retrofit.create(Endpoint::class.java)

    // https://github.com/googleapis/google-api-python-client/issues/213
    // To unauthenticate https://myaccount.google.com/u/1/permissions?pageId=none
    fun getToken(code: String): Single<Result<GoogleAuthJson.Response>> {
      val body = GoogleAuthJson.Request(
        clientId = googleClientId,
        clientSecret = googleClientSecret,
        code = code
      )
      return endpoint.getToken(
        scope = "https://www.googleapis.com/auth/photoslibrary.readonly",
        request = body
      )
        .map { Result.success(it) }
        .onErrorReturn { Result.failure(it) }
    }

    fun refreshToken(refreshToken: String): Single<Result<GoogleAuthJson.Response>> {
      val body = GoogleAuthJson.Request(
        clientId = googleClientId,
        clientSecret = googleClientSecret,
        refreshToken = refreshToken
      )
      return endpoint.refreshAuthToken(
        scope = "https://www.googleapis.com/auth/photoslibrary.readonly",
        request = body
      )
        .map { Result.success(it) }
        .onErrorReturn { Result.failure(it) }
    }
  }
}
