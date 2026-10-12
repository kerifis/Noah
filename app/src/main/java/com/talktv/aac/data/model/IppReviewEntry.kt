package com.talktv.aac.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Historical 6-month IPP review logs for tracking Noah's long-term growth and adjustments.
 */
@Entity(tableName = "ipp_review_entries")
data class IppReviewEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val reviewDate: String,
    val reviewCycle: String, // e.g. "Term 1 Review (Oct 2025)", "Mid-Year Review (Jan 2026)", "Year-End Review (Jun 2026)"
    val reviewerName: String,
    val academicProgress: String,
    val communicationAACProgress: String,
    val behavioralProgress: String,
    val newAccommodationsAdded: String,
    val nextReviewDueDate: String
)
