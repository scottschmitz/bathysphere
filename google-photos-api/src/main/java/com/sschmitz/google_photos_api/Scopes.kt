package com.sschmitz.google_photos_api

import com.google.android.gms.common.api.Scope

sealed class GooglePhotosScope(val scope: Scope) {
  object ReadOnly : GooglePhotosScope(Scope("https://www.googleapis.com/auth/photoslibrary.readonly"))
}
