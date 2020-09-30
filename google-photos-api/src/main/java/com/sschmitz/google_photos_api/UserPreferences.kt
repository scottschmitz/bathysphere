package com.sschmitz.google_photos_api

import android.content.Context
import androidx.preference.PreferenceManager

class UserPreferences(context: Context) {

  companion object {
    private const val KEY_AUTH_TOKEN = "KEY_AUTH_TOKEN"
    private const val KEY_AUTH_CODE = "KEY_AUTH_CODE"
  }

  private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

  var authToken: String?
    get() = sharedPreferences.getString(KEY_AUTH_TOKEN, null)
    set(value) {
      if (value == null) {
        sharedPreferences.edit().remove(KEY_AUTH_TOKEN).apply()
      } else {
        sharedPreferences
          .edit()
          .putString(KEY_AUTH_TOKEN, value)
          .apply()
      }
    }

  var serverAuthCode: String?
    get() = sharedPreferences.getString(KEY_AUTH_CODE, null)
    set(value) {
      if (value == null) {
        sharedPreferences.edit().remove(KEY_AUTH_CODE).apply()
      } else {
        sharedPreferences
          .edit()
          .putString(KEY_AUTH_CODE, value)
          .apply()
      }
    }
}
