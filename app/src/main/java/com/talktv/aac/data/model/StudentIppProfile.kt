package com.talktv.aac.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Alberta Individual Program Plan (IPP) and Individualized Behaviour Support Plan (IBSP)
 * Student Profile Entity for Noah Tuyishime (Edmonton Public Schools - EPSB).
 *
 * Configured to be reviewed and updated on a 6-month cyclical schedule.
 */
@Entity(tableName = "student_ipp_profile")
data class StudentIppProfile(
    @PrimaryKey
    val studentId: String = "EPSB_16368473",
    val studentName: String = "Noah Tuyishime",
    val birthDate: String = "Feb 03, 2015",
    val grade: String = "Grade 4",
    val schoolName: String = "Lymburn School (2025-2026)",
    val epsbNumber: String = "16368473",
    val asnNumber: String = "308366541",
    val program: String = "Interactions (Severe Physical or Medical Disability, ASD)",
    val homeroomTeacher: String = "Zayd Rutkowski",
    val teamMembers: String = "Zayd Rutkowski, Joyce Nosko, Sharon Weisgerber",
    val behaviorConsultant: String = "Rhiannon Roth (Education and Behavior Consultant)",
    
    // Clinical Diagnoses
    val asdDiagnosis: String = "Autism Spectrum Disorder (Dr. Cara Dosman, Glenrose Pediatric Clinic, June 2018)",
    val slpDiagnosis: String = "Severe Expressive Language Delay, Severe Receptive Language Disorder (Keri-Lynn Heise, R. SLP, May 2018)",
    val otDiagnosis: String = "Sensory processing difficulties, delayed self care, severe fine motor delay (Johanna LaVallee, OT, June 2019)",

    // Strengths & Interests
    val strengthsFamily: String = "Follows verbal directions; very affectionate when regulated; learns through repetition.",
    val strengthsStaff: String = "Transitions well between locations; independent in routines; communicates needs non-verbally.",
    val interestsFamily: String = "Playing with water; food videos; heavy blankets.",
    val interestsStaff: String = "Animal toys; playing chase with staff/students; being read certain books.",

    // Learning Style & Accommodations
    val learningStyle: String = "Visual, Kinesthetic",
    val accommodations: String = "Communication device (AAC) accessible; alternative seating; open access to fidgets & ear defenders; visual timers; First-Then language; movement breaks.",

    // IBSP Behaviour Triggers & Interoception
    val ibspKeyUnderstanding: String = "When dysregulated, Noah does not act out of anger towards any person, but rather appears immensely frustrated that he is not able to communicate what he needs.",
    val antecedentInteroception: String = "Hunger, thirst, feeling sick, feeling tired, feeling pain, feeling hot/cold, needing bathroom, bright lights, loud noises, realizing food is finished.",
    val warningSignLevel1: String = "Furrowed brow and groaning or quietly wailing (can transition to Level 2 rapidly).",
    val warningSignLevel2: String = "Biting own hand, hitting self on head, jumping up and down, screaming, hitting, scratching, biting, headbutting.",
    val actionsToDiffuseLevel1: String = "Immediately diagnose interoceptive need: offer bathroom, water, food; minimize light and noise.",
    val actionsToDiffuseLevel2: String = "Redirect to calm sensory space; provide visual supports of needs (water, diaper, food); use calm voice.",

    // 6-Month Review Tracking
    val lastReviewDate: String = "Jan 30, 2026",
    val nextReviewDate: String = "Jun 23, 2026",
    val reviewIntervalMonths: Int = 6,
    val baselineNotes: String = "Noah is doing much better since starting new medication in October. Making communication attempts for hunger and bathroom. Still needs proactive interoceptive support."
)
