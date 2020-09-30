package com.sschmitz.google_photos_api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

object GoogleAuthJson {
  @JsonClass(generateAdapter = true)
  data class Request(
    @Json(name = "grant_type")
    val grantType: String = "authorization_code",

    @Json(name = "client_id")
    val clientId: String,

    @Json(name = "client_secret")
    val clientSecret: String,

    @Json(name = "redirect_uri")
    val redirectUri: String = "",

    @Json(name = "code")
    val code: String? = null,

    @Json(name = "refresh_token")
    val refreshToken: String? = null
  )


  @JsonClass(generateAdapter = true)
  data class Response(
    @Json(name = "access_token")
    val accessToken: String,

    @Json(name = "expires_in")
    val expiresIn: Int,

    @Json(name = "scope")
    val scope: String,

    @Json(name = "token_type")
    val tokenType: String,

    @Json(name = "id_token")
    val idToken: String? = null,

    @Json(name = "refresh_token")
    val refreshToken: String? = null
  )
}
