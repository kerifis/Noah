package com.talktv.aac.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.talktv.aac.data.local.WordUsageCount
import com.talktv.aac.data.model.PressEvent
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CaregiverAnalyticsView(
    totalPresses: Int,
    successfulRewards: Int,
    wordFrequencies: List<WordUsageCount>,
    recentEvents: List<PressEvent>,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // KPI Summary Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AnalyticsKpiCard(
                    title = "TOTAL TILE PRESSES",
                    value = totalPresses.toString(),
                    accentColor = Color(0xFF00E5FF),
                    modifier = Modifier.weight(1f)
                )

                AnalyticsKpiCard(
                    title = "REWARDED SHOWS",
                    value = successfulRewards.toString(),
                    accentColor = Color(0xFF00E676),
                    modifier = Modifier.weight(1f)
                )

                val ratio = if (totalPresses > 0) {
                    ((successfulRewards.toFloat() / totalPresses) * 100).toInt()
                } else 0

                AnalyticsKpiCard(
                    title = "INTENTIONAL REWARD RATIO",
                    value = "$ratio%",
                    accentColor = Color(0xFFFFD600),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Word Frequency Breakdown
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF141C2B))
                    .padding(20.dp)
            ) {
                Text(
                    text = "VOCABULARY USAGE FREQUENCY",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD600),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (wordFrequencies.isEmpty()) {
                    Text(
                        text = "No tile press data recorded yet.",
                        fontSize = 15.sp,
                        color = Color(0xFF8B9CB5)
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // Most used words
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Most Used Words",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF00E5FF),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            wordFrequencies.take(5).forEach { item ->
                                WordFrequencyRow(word = item.tileWord, count = item.count)
                            }
                        }

                        // Least used words (vocabulary growth opportunities)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Emerging / Needs Prompting",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFFFA726),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            wordFrequencies.takeLast(5).reversed().forEach { item ->
                                WordFrequencyRow(word = item.tileWord, count = item.count)
                            }
                        }
                    }
                }
            }
        }

        // Recent Activity Log
        item {
            Text(
                text = "RECENT PRESS LOG",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        if (recentEvents.isEmpty()) {
            item {
                Text(
                    text = "Usage history will appear here as tiles are pressed.",
                    fontSize = 15.sp,
                    color = Color(0xFF8B9CB5)
                )
            }
        } else {
            items(recentEvents.take(25)) { event ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF161F30))
                        .padding(horizontal = 18.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = event.tileWord.uppercase(),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (event.wasRewardTriggered) Color(0xFF00E676) else Color.White
                            )

                            if (event.wasRewardTriggered) {
                                Spacer(modifier = Modifier.width(10.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFF13361E))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "REWARD LAUNCHED",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF00E676)
                                    )
                                }
                            }
                        }

                        if (event.phraseAtPress.isNotEmpty()) {
                            Text(
                                text = "Phrase: \"${event.phraseAtPress.joinToString(" ")}\"",
                                fontSize = 13.sp,
                                color = Color(0xFF8B9CB5),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = dateFormat.format(Date(event.timestampMillis)),
                        fontSize = 13.sp,
                        color = Color(0xFF6B7E96)
                    )
                }
            }
        }
    }
}

@Composable
fun AnalyticsKpiCard(
    title: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF141C2B))
            .padding(18.dp)
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF8B9CB5)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            color = accentColor
        )
    }
}

@Composable
fun WordFrequencyRow(word: String, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = word.uppercase(),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = "$count presses",
            fontSize = 14.sp,
            color = Color(0xFF8B9CB5)
        )
    }
}
