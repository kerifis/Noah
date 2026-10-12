package com.talktv.aac.data.local

import androidx.room.*
import com.talktv.aac.data.model.IppReviewEntry
import com.talktv.aac.data.model.StudentIppProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentIppDao {

    @Query("SELECT * FROM student_ipp_profile WHERE studentId = 'EPSB_16368473' LIMIT 1")
    fun getNoahProfile(): Flow<StudentIppProfile?>

    @Query("SELECT * FROM student_ipp_profile WHERE studentId = 'EPSB_16368473' LIMIT 1")
    suspend fun getNoahProfileSync(): StudentIppProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: StudentIppProfile)

    @Query("SELECT * FROM ipp_review_entries ORDER BY id DESC")
    fun getAllReviews(): Flow<List<IppReviewEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: IppReviewEntry): Long
}
