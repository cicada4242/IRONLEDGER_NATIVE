package com.ironledger.app.data.local

import java.util.*

/**
 * IronLedger - Motivation Data (Quotes and Videos)
 */
data class VideoEntry(
    val label: String,
    val videoId: String,
    val youtubeUrl: String
)

data class VideoCategory(
    val title: String,
    val accentStart: String, // Hex color or simple description
    val accentEnd: String,
    val videos: List<VideoEntry>
)

object MotivationDB {
    val quotes = listOf(
        "Discipline is remembering what you want most.",
        "Small steps every day build uncommon strength.",
        "The body grows where consistency goes.",
        "Momentum is built one honest rep at a time.",
        "You do not need perfect days, only repeated ones.",
        "Strength is confidence you can feel.",
        "Progress hides inside ordinary days.",
        "Your future physique is built by today’s choices.",
        "Train like your health matters, because it does.",
        "Muscle is rented and the rent is due daily.",
        "Intensity matters, but consistency wins seasons.",
        "A calm mind lifts heavier.",
        "The goal is not motivation every day. The goal is standards.",
        "When you keep promises to yourself, self-respect grows.",
        "Recovery is not weakness. It is part of the program.",
        "Eat for the person you are becoming.",
        "Every logged meal is a vote for your goal.",
        "Form first, ego last.",
        "The best program is the one you can repeat.",
        "Show up tired if you must. Just show up.",
        "Training teaches patience in visible form.",
        "Heavy is earned through technique.",
        "Your habits are writing your results in real time.",
        "Good sleep is legal performance enhancement.",
        "Today’s discipline becomes tomorrow’s confidence.",
        "The mirror rewards consistency more than excitement.",
        "A strong body supports a strong life.",
        "You are one month of focused effort away from surprise.",
        "It gets easier after it becomes part of who you are.",
        "Nutrition is training that happens in the kitchen.",
        "Be proud of the reps nobody sees.",
        "You do not need to rush growth to prove it is happening.",
        "Master the basics until they look expensive.",
        "Your energy follows your routines.",
        "The strongest flex is keeping your word to yourself.",
        "One workout will not change you. One hundred will.",
        "Streaks are built by protecting the next decision.",
        "Great physiques are quiet about how they were built.",
        "Lift with intent. Eat with intent. Recover with intent.",
        "Consistency turns average plans into elite results.",
        "Confidence is a side effect of repeated effort.",
        "You can feel behind and still be on track.",
        "The basics done well are never basic.",
        "Hard things become normal when you repeat them.",
        "A patient bulk beats an impulsive one.",
        "Your standard sets your ceiling.",
        "Effort compounds faster than doubt.",
        "The daily win is enough for today."
    )

    val videoCategories = listOf(
        VideoCategory(
            title = "Mindset",
            accentStart = "#14b8a6", // Teal 500
            accentEnd = "#22d3ee",   // Cyan 400
            videos = listOf(
                VideoEntry("Build self-discipline", "4-079YIasck", "https://www.youtube.com/watch?v=4-079YIasck"),
                VideoEntry("Stay consistent when motivation dips", "mgmVOuLgFB0", "https://www.youtube.com/watch?v=mgmVOuLgFB0"),
                VideoEntry("Patience during the process", "TQMbvJNRpLE", "https://www.youtube.com/watch?v=TQMbvJNRpLE")
            )
        ),
        VideoCategory(
            title = "Training",
            accentStart = "#f97316", // Orange 500
            accentEnd = "#fbbf24",   // Amber 400
            videos = listOf(
                VideoEntry("Progressive overload basics", "U9ENCvFf9yQ", "https://www.youtube.com/watch?v=U9ENCvFf9yQ"),
                VideoEntry("Train hard without ego lifting", "lu_BObG6dj8", "https://www.youtube.com/watch?v=lu_BObG6dj8"),
                VideoEntry("How to stay locked into the gym habit", "mX8xO7l6zco", "https://www.youtube.com/watch?v=mX8xO7l6zco")
            )
        ),
        VideoCategory(
            title = "Nutrition",
            accentStart = "#10b981", // Emerald 500
            accentEnd = "#2dd4bf",   // Teal 400
            videos = listOf(
                VideoEntry("Healthy bulking meal guide", "K8mZFu0c-FM", "https://www.youtube.com/watch?v=K8mZFu0c-FM"),
                VideoEntry("High protein meal prep ideas", "2B0kK0V0dWs", "https://www.youtube.com/watch?v=2B0kK0V0dWs"),
                VideoEntry("Simple eating plan for muscle gain", "s8E95N0XQdM", "https://www.youtube.com/watch?v=s8E95N0XQdM")
            )
        )
    )

    fun getQuoteOfDay(): String {
        val calendar = Calendar.getInstance()
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        return quotes[dayOfYear % quotes.size]
    }
}
