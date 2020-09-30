package com.sschmitz.google_photos_api.account

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Bundle
import android.os.IBinder
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import timber.log.Timber

class AccountAuthenticator(
  applicationContext: Context
) : AbstractAccountAuthenticator(applicationContext) {

  companion object {
    private const val KEY_ACCESS_TOKEN = "google_access_token"
    private const val KEY_REFRESH_TOKEN = "google_refresh_token"
  }

  private val accountManager: AccountManager = AccountManager.get(applicationContext)
  private val packageInfo: PackageInfo = applicationContext.packageManager.getPackageInfo(
    applicationContext.packageName,
    0
  )
  private val accountType = packageInfo.packageName

  fun hasAccount(): Boolean {
    return getAccount() != null
  }

  fun createAccount(
    googleAccount: GoogleSignInAccount,
    refreshToken: String,
    accessToken: String
  ): Account {
    val account = Account(
      googleAccount.displayName,
      accountType
    )

    accountManager.addAccountExplicitly(
      account,
      "unused_password",
      null
    )

    accountManager.setUserData(account, KEY_REFRESH_TOKEN, refreshToken)
    accountManager.setUserData(account, KEY_ACCESS_TOKEN, accessToken)

    return account
  }

  fun getAcessToken(): String? {
    return getUserData(KEY_ACCESS_TOKEN)
  }

  fun updateAccessToken(authToken: String) {
    getAccount()?.let { account ->
      accountManager.setUserData(account, KEY_ACCESS_TOKEN, authToken)
    } ?: Timber.e(IllegalArgumentException("Account not found."))
  }

  fun getRefreshToken(): String? {
    return getUserData(KEY_REFRESH_TOKEN)
  }

  private fun getAccount(): Account? {
    return try {
      accountManager.getAccountsByType(accountType).firstOrNull()
    } catch (e: SecurityException) {
      Timber.e(e, "Unable to retrieve account list")
      null
    }
  }

  private fun getUserData(key: String): String? {
    return try {
      val account = getAccount()
      accountManager.getUserData(account, key)
    } catch (e: Exception) {
      Timber.e(e, "Failed to get AuthToken")
      null
    }
  }

  // <editor-fold desc="Unimplemented">
  override fun addAccount(
    response: AccountAuthenticatorResponse?,
    accountType: String,
    authTokenType: String?,
    requiredFeatures: Array<out String>?,
    options: Bundle?
  ): Bundle {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getAuthTokenLabel(authTokenType: String?): String {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun confirmCredentials(
    response: AccountAuthenticatorResponse?,
    account: Account?,
    options: Bundle?
  ): Bundle {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun updateCredentials(
    response: AccountAuthenticatorResponse?,
    account: Account?,
    authTokenType: String?,
    options: Bundle?
  ): Bundle {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getAuthToken(
    response: AccountAuthenticatorResponse?,
    account: Account?,
    authTokenType: String?,
    options: Bundle?
  ): Bundle {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun hasFeatures(
    response: AccountAuthenticatorResponse?,
    account: Account?,
    features: Array<out String>?
  ): Bundle {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun editProperties(response: AccountAuthenticatorResponse?, accountType: String?): Bundle {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
  // </editor-fold>

  class Service : android.app.Service() {
    override fun onBind(intent: Intent): IBinder? {
      return when (intent.action) {
        AccountManager.ACTION_AUTHENTICATOR_INTENT -> AccountAuthenticator(this).iBinder
        else -> null
      }
    }
  }
}
