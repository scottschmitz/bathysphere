package com.sschmitz.google_photos_api.ext

import com.squareup.moshi.Moshi

internal inline fun <reified T : Any> Moshi.dump(obj: T): String {
  return this.adapter(T::class.java).toJson(obj)
}

internal inline fun <reified T : Any> Moshi.load(json: String): T {
  return this.adapter(T::class.java).fromJson(json)!!
}
