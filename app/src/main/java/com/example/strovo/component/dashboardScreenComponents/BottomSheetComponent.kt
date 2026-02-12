package com.example.strovo.component.dashboardScreenComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.data.getStravaActivitiesModelItem


@Composable
fun BottomSheetComponent(
    selectedActivities: List<getStravaActivitiesModelItem>?,
    onclick: (Int) -> Unit
) {
    val activityCount = selectedActivities?.size ?: 0
    val pagerState = rememberPagerState (
        pageCount = {activityCount},
        initialPage = 0
    )
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 6.dp),

    ) {
        Text(
            text = "$activityCount activités",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        HorizontalPager(
            state = pagerState,
        ) { page ->
            BottomSheetContent(
                selectedActivities = selectedActivities?.let { listOf(it[page]) },
                onclick = { onclick(page) }
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "${pagerState.currentPage + 1} / ${selectedActivities?.size ?: 0}",
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

