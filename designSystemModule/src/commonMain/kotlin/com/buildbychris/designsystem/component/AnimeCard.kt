package com.buildbychris.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.buildbychris.domain.anime.Anime
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Star
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AnimeCard(
    modifier: Modifier = Modifier,
    anime: Anime
) {
    Column {
        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            shadowElevation = 8.dp,
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 3f),
                model = anime.imageUrl,
                contentDescription = anime.title,
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        AnimeInfo(anime)
    }
}

@Composable
@Preview
fun AnimeInfo(anime: Anime) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = anime.title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                modifier = Modifier
                    .size(16.dp),
                imageVector = Lucide.Star,
                contentDescription = anime.title,
                tint = Color(0xFFF1C40F)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append(anime.score.toString())
                    }
                    withStyle(style = SpanStyle(color = Color.Gray)) {
                        append(" (${anime.scoreBy})")
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
            )


            Spacer(modifier = Modifier.width(4.dp))
            if (anime.year != null) {
                Text(
                    text = "| ${anime.year.toString()}",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }

}
