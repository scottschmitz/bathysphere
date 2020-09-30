package com.sschmitz.google_photos_api

import android.content.Context
import com.squareup.moshi.Moshi
import com.sschmitz.google_photos_api.interceptors.AuthenticationInterceptor
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

class Api(
  private val applicationContext: Context,
  googleClientId: String,
  googleClientSecret: String
) {

  private val moshi = Moshi.Builder().build()

  private val loggingInterceptor: Interceptor by lazy {
    val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
      override fun log(message: String) {
        Timber.tag("HTTP").d(message)
      }
    })
    interceptor.level = HttpLoggingInterceptor.Level.BASIC
    interceptor
  }

  private val authenticationInterceptor: Interceptor by lazy {
    AuthenticationInterceptor(
      applicationContext = applicationContext,
      googleClientId = googleClientId,
      googleClientSecret = googleClientSecret
    )
  }

  private val authenticatedOkHttpClient: OkHttpClient by lazy {
    OkHttpClient.Builder()
      .followSslRedirects(false)
      .followRedirects(false)
      .addInterceptor(loggingInterceptor)
      .addInterceptor(authenticationInterceptor)
      .build()
  }

  private val unauthenticatedOkHttpClient: OkHttpClient by lazy {
    OkHttpClient.Builder()
      .followSslRedirects(false)
      .followRedirects(false)
      .addInterceptor(loggingInterceptor)
      .build()
  }

  fun buildRetrofit(baseUrl: String, authenticated: Boolean): Retrofit {
    return Retrofit.Builder()
      .baseUrl(baseUrl)
      .client(if (authenticated) authenticatedOkHttpClient else unauthenticatedOkHttpClient)
      .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .build()
  }
}
