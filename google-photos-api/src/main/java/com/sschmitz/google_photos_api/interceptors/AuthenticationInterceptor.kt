package com.sschmitz.google_photos_api.interceptors

import android.content.Context
import com.sschmitz.google_photos_api.GoogleAuth
import com.sschmitz.google_photos_api.account.AccountAuthenticator
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber

class AuthenticationInterceptor(
  applicationContext: Context,
  googleClientId: String,
  googleClientSecret: String
) : Interceptor {

  private val accountHelper = AccountAuthenticator(applicationContext)
  private val authService = GoogleAuth.Service(applicationContext, googleClientId, googleClientSecret)

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request().newBuilder()
      .setAuthenticationHeader()
      .build()
    val response = chain.proceed(request)

    val refreshToken = accountHelper.getRefreshToken()
    if (response.code in listOf(401, 403) && refreshToken != null) {
      val result = authService.refreshToken(refreshToken).blockingGet()

      if (result.isSuccess) {
        result.getOrNull()?.let { body ->
          accountHelper.updateAccessToken(body.accessToken)
        }

        response.close()
        val newRequest = chain.request().newBuilder()
          .setAuthenticationHeader()
          .build()
        return chain.proceed(newRequest)
      } else {
        Timber.e("${result.exceptionOrNull()}")
      }
    }

    return response
  }

  private fun Request.Builder.setAuthenticationHeader(): Request.Builder {
    accountHelper.getAcessToken()?.let { token ->
      this.apply {
        addHeader("Authorization", "Bearer $token")
      }
    }
    return this
  }
}
