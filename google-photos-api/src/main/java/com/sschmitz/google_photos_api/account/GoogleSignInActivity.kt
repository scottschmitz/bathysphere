package com.sschmitz.google_photos_api.account

import android.accounts.Account
import android.content.Intent
import androidx.core.app.ActivityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.sschmitz.google_photos_api.GoogleAuth
import com.sschmitz.google_photos_api.GooglePhotosScope
import dagger.android.support.DaggerAppCompatActivity
import timber.log.Timber

abstract class GoogleSignInActivity: DaggerAppCompatActivity() {

  companion object {
    private const val REQUEST_GOOGLE_SIGN_IN = 0
  }

  abstract var clientId: String
  abstract val clientSecret: String
  abstract fun loginResult(result: Result<Account>)

  private val authService: GoogleAuth.Service by lazy {
    GoogleAuth.Service(this, clientId, clientSecret)
  }

  private val googleSignInClient: GoogleSignInClient by lazy {
    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestScopes(GooglePhotosScope.ReadOnly.scope)
      .requestEmail()
      .requestServerAuthCode(clientId)
      .build()

    // Build a GoogleSignInClient with the options specified by gso.
    GoogleSignIn.getClient(this, gso)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    try {
      // The Task returned from this call is always completed, no need to attach a listener.
      val task = GoogleSignIn.getSignedInAccountFromIntent(data)
      val googleAccount: GoogleSignInAccount = task.getResult(ApiException::class.java)!!
      val serverAuthCode = googleAccount.serverAuthCode!!

      val result = authService.getToken(serverAuthCode).blockingGet()

      if (result.isSuccess) {
        val auth = result.getOrThrow()
        val refreshToken = auth.refreshToken
        if (refreshToken == null) {
          Timber.d("Refresh Token was null on initial login.")
        }

        val account = AccountAuthenticator(this).createAccount(
          googleAccount,
          refreshToken ?: "",
          auth.accessToken
        )

        Timber.i("Successfully logged in as ${googleAccount.displayName}")
        loginResult(Result.success(account))
      } else {
        val exception = result.exceptionOrNull()
          ?: IllegalArgumentException("Failed to authenticate with Google OAuth. No error provided.")
        loginResult(Result.failure(exception))
      }
    } catch (e: ApiException) {
      // The ApiException status code indicates the detailed failure reason.
      // Please refer to the GoogleSignInStatusCodes class reference for more information.
      Timber.e(e, "ApiException - signInResult:failed code=${e.statusCode}, message=${e.message}")
      loginResult(Result.failure(e))
    } catch (e: Exception) {
      Timber.e(e, "Error occurred while signing in.")
      loginResult(Result.failure(e))
    }
  }

  fun signIn() {
    val signInIntent: Intent = googleSignInClient.signInIntent
    ActivityCompat.startActivityForResult(this, signInIntent, REQUEST_GOOGLE_SIGN_IN, null)
  }
}
