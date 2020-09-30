package com.sschmitz.bathysphere

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Stack
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.WithConstraints
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.sschmitz.bathysphere.ui.BathysphereTheme
import dev.chrisbanes.accompanist.coil.CoilImageWithCrossfade

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)
    setContent {
      BathysphereTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
          Grid()
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  BathysphereTheme {
    Grid()
  }
}

@Composable
fun Grid() {

  val numPerRow = 4
  val imageUrls = listOf(
    "https://source.unsplash.com/pGM4sjt_BdQ",
    "https://source.unsplash.com/Yc5sL-ejk6U",
    "https://source.unsplash.com/-LojFX9NfPY",
    "https://source.unsplash.com/3U2V5WqK1PQ",
    "https://source.unsplash.com/Y4YR9OjdIMk",
    "https://source.unsplash.com/bELvIg_KZGU",
    "https://source.unsplash.com/AqorcpZIKnU",
    "https://source.unsplash.com/0u_vbeOkMpk",
    "https://source.unsplash.com/yb16pT5F_jE",
    "https://source.unsplash.com/AHF_ZktTL6Q",
    "https://source.unsplash.com/rqFm0IgMVYY",
    "https://source.unsplash.com/qRE_OpbVPR8",
    "https://source.unsplash.com/33fWPnyN6tU",
    "https://source.unsplash.com/aX_ljOOyWJY",
    "https://source.unsplash.com/7meCnGCJ5Ms",
    "https://source.unsplash.com/m741tj4Cz7M",
    "https://source.unsplash.com/iuwMdNq0-s4",
    "https://source.unsplash.com/qgWWQU1SzqM",
    "https://source.unsplash.com/9MzCd76xLGk",
    "https://source.unsplash.com/1d9xXWMtQzQ",
    "https://source.unsplash.com/wZxpOw84QTU",
    "https://source.unsplash.com/okzeRxm_GPo",
    "https://source.unsplash.com/pGM4sjt_BdQ",
    "https://source.unsplash.com/Yc5sL-ejk6U",
    "https://source.unsplash.com/-LojFX9NfPY",
    "https://source.unsplash.com/3U2V5WqK1PQ",
    "https://source.unsplash.com/Y4YR9OjdIMk",
    "https://source.unsplash.com/bELvIg_KZGU",
    "https://source.unsplash.com/AqorcpZIKnU",
    "https://source.unsplash.com/0u_vbeOkMpk",
    "https://source.unsplash.com/yb16pT5F_jE",
    "https://source.unsplash.com/AHF_ZktTL6Q",
  )
  val chunked = imageUrls.chunked(numPerRow)

  LazyColumnFor(items = chunked) {
    WithConstraints {
      val squareWidth = maxWidth / numPerRow

      Row {
        it.forEach { imageUrl ->
          ThumbnailSquare(
            modifier = Modifier.size(squareWidth),
            imageUrl = imageUrl,
          )
        }
      }
    }
  }
}

@Composable
fun ThumbnailSquare(modifier: Modifier, imageUrl: String) {
  Box(
    modifier = modifier,
    backgroundColor = Color.Black,
  ) {
    CoilImageWithCrossfade(
      data = imageUrl,
      contentScale = ContentScale.Crop,
      modifier = Modifier.fillMaxSize(),
      loading = {
        Stack(modifier = Modifier.fillMaxSize()) {
          CircularProgressIndicator(
            modifier = Modifier
              .preferredSize(48.dp)
              .align(Alignment.Center)
          )
        }
      }
    )
  }
}
