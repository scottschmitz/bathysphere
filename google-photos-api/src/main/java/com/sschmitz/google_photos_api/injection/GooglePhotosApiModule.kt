package com.sschmitz.google_photos_api.injection

import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.sschmitz.google_photos_api.GoogleAuth
import com.sschmitz.google_photos_api.GooglePhotos
import com.sschmitz.google_photos_api.GooglePhotosScope
import dagger.Module
import dagger.Provides
import timber.log.Timber
import javax.inject.Named

@Module
class GooglePhotosApiModule {
  @Provides
  fun provideGoogleSignInClient(
    application: Application,
    @Named("GoogleClientId") clientId: String
  ): GoogleSignInClient {
    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestScopes(GooglePhotosScope.ReadOnly.scope)
      .requestEmail()
      .requestServerAuthCode(clientId)
      .build()

    // Build a GoogleSignInClient with the options specified by gso.
    return GoogleSignIn.getClient(application, gso)
  }

  @Provides
  fun provideGoogleAuthService(
    application: Application,
    @Named("GoogleClientId") clientId: String,
    @Named("GoogleClientSecret") clientSecret: String
  ): GoogleAuth.Service {
    return GoogleAuth.Service(application, clientId, clientSecret)
  }

  @Provides
  fun provideGooglePhotosService(
    application: Application,
    @Named("GoogleClientId") clientId: String,
    @Named("GoogleClientSecret") clientSecret: String
  ): GooglePhotos.Service {
    Timber.e(clientId)
    Timber.e(clientSecret)
    return GooglePhotos.Service(application, clientId, clientSecret)
  }
}
