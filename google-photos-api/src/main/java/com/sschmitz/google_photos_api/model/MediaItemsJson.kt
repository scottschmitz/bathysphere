package com.sschmitz.google_photos_api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

class MediaItemsJson {

  @JsonClass(generateAdapter = true)
  data class MediaItems(
    @Json(name = "mediaItems")
    val mediaItems: List<MediaItem>,

    @Json(name = "nextPageToken")
    val nextPageToken: String?
  )

  @JsonClass(generateAdapter = true)
  data class MediaItem(
    @Json(name = "id")
    val id: String,

    @Json(name = "productUrl")
    val productUrl: String,

    @Json(name = "baseUrl")
    val baseUrl: String,

    @Json(name = "mimeType")
    val mimeType: String,

    @Json(name = "mediaMetadata")
    val mediaMetadata: MediaMetadata,

    @Json(name = "filename")
    val filename: String
  )

  @JsonClass(generateAdapter = true)
  data class MediaMetadata(
    @Json(name = "creationTime")
    val creationTime: String,

    @Json(name = "width")
    val width: Int,

    @Json(name = "height")
    val height: Int,

    @Json(name = "photo")
    val photo: Photo?
  )

  @JsonClass(generateAdapter = true)
  data class Photo(
    @Json(name = "cameraMake")
    val cameraMake: String?,

    @Json(name = "cameraModel")
    val cameraModel: String?,

    @Json(name = "focalLength")
    val focalLength: Double?,

    @Json(name = "apertureFNumber")
    val apertureFNumber: Double?,

    @Json(name = "isoEquivalent")
    val isoEquivalent: Int?
  )
}




