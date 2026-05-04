package com.ronny.naturenest.models


data class UserProfile(
    val name: String = "",
    val dueDate: String = "",
    val currentWeek: Int = 0,
    val isPostpartum: Boolean = false,
    val babyBirthDate: String = "",
    val profileImageUrl: String = ""
)

data class PregnancyWeekInfo(
    val week: Int,
    val babySize: String,
    val babyWeight: String,
    val babyLength: String,
    val babyDevelopment: String,
    val momChanges: String,
    val tips: List<String>,
    val emoji: String
)

data class HealthTip(
    val id: Int,
    val title: String,
    val description: String,
    val category: TipCategory,
    val readTime: String,
    val isBookmarked: Boolean = false
)

enum class TipCategory(val label: String, val emoji: String) {
    NUTRITION("Nutrition", "🥗"),
    EXERCISE("Exercise", "🧘"),
    MENTAL_HEALTH("Mental Health", "💆"),
    BABY_CARE("Baby Care", "👶"),
    POSTPARTUM("Postpartum", "💕"),
    PRENATAL("Prenatal", "🌸")
}

data class Reminder(
    val id: Int,
    val title: String,
    val description: String,
    val time: String,
    val date: String,
    val type: ReminderType,
    val isCompleted: Boolean = false
)

enum class ReminderType(val label: String, val emoji: String) {
    CLINIC_VISIT("Clinic Visit", "🏥"),
    MEDICATION("Medication", "💊"),
    NUTRITION("Nutrition", "🥤"),
    EXERCISE("Exercise", "🧘"),
    SUPPLEMENT("Supplement", "🌿")
}

data class CommunityPost(
    val id: Int,
    val authorName: String,
    val authorInitials: String,
    val authorWeek: String,
    val content: String,
    val likes: Int,
    val comments: Int,
    val timeAgo: String,
    val category: String,
    val isLiked: Boolean = false
)

data class NutritionItem(
    val name: String,
    val amount: String,
    val unit: String,
    val current: Float,
    val target: Float,
    val color: Long
)

data class OnboardingPage(
    val title: String,
    val subtitle: String,
    val description: String,
    val emoji: String,
    val backgroundColor: Long
)
