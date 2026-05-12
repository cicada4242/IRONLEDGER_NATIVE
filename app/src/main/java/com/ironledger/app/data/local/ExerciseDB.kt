package com.ironledger.app.data.local

/**
 * IronLedger - Exercise Database and Starter Program
 */
data class Exercise(
    val name: String,
    val muscleGroup: String,
    val type: String, // push, pull, legs
    val videoIds: List<String>,
    val youtubeUrl: String
)

data class ProgramExercise(
    val name: String,
    val sets: Int,
    val reps: Int,
    val notes: String
)

data class WorkoutDay(
    val id: String,
    val name: String,
    val label: String,
    val description: String,
    val exercises: List<ProgramExercise>
)

data class StarterProgram(
    val name: String,
    val description: String,
    val daysPerWeek: Int,
    val workouts: List<WorkoutDay>
)

object ExerciseDB {
    val exerciseList = listOf(
        // Push exercises
        Exercise(
            name = "Bench Press",
            muscleGroup = "Chest",
            type = "push",
            videoIds = listOf("SCVCLChPQFY", "rT7DgCr-3pg", "VmB1G1K7v94"),
            youtubeUrl = "https://www.youtube.com/results?search_query=bench+press+proper+form"
        ),
        Exercise(
            name = "Overhead Press",
            muscleGroup = "Shoulders",
            type = "push",
            videoIds = listOf("2yjwXTZQDDI", "wol7Hko8RhY", "QAQ64hK4m40"),
            youtubeUrl = "https://www.youtube.com/results?search_query=overhead+press+proper+form"
        ),
        Exercise(
            name = "Incline Dumbbell Press",
            muscleGroup = "Chest",
            type = "push",
            videoIds = listOf("8iPEnn-ltC8", "0f6-uCUKqgA", "M2rwvNhTOu0"),
            youtubeUrl = "https://www.youtube.com/results?search_query=incline+dumbbell+press+proper+form"
        ),
        Exercise(
            name = "Tricep Pushdowns",
            muscleGroup = "Triceps",
            type = "push",
            videoIds = listOf("2-LAMcpzODU", "vB5OHsJ3EME", "_gsUck-7M74"),
            youtubeUrl = "https://www.youtube.com/results?search_query=tricep+pushdown+proper+form"
        ),

        // Pull exercises
        Exercise(
            name = "Deadlifts",
            muscleGroup = "Back",
            type = "pull",
            videoIds = listOf("wYREQkVtvEc", "ytGaGIn3SjE", "MBbyAqvTNkU"),
            youtubeUrl = "https://www.youtube.com/results?search_query=deadlift+proper+form"
        ),
        Exercise(
            name = "Barbell Rows",
            muscleGroup = "Back",
            type = "pull",
            videoIds = listOf("FWJR5Ve8bnQ", "kBWAon7ItDw", "6FZHJGzMFEc"),
            youtubeUrl = "https://www.youtube.com/results?search_query=barbell+row+proper+form"
        ),
        Exercise(
            name = "Pull-ups",
            muscleGroup = "Back",
            type = "pull",
            videoIds = listOf("eGo4IYlbE5g", "sIvJTfGxdFo", "HRV5YKKaeVw"),
            youtubeUrl = "https://www.youtube.com/results?search_query=pull+up+proper+form"
        ),
        Exercise(
            name = "Barbell Curls",
            muscleGroup = "Biceps",
            type = "pull",
            videoIds = listOf("kwG2ipFRgfo", "in7PaeYlhrM", "QZEqB6wUPxQ"),
            youtubeUrl = "https://www.youtube.com/results?search_query=barbell+curl+proper+form"
        ),

        // Leg exercises
        Exercise(
            name = "Squats",
            muscleGroup = "Legs",
            type = "legs",
            videoIds = listOf("bEv6CCg2BC8", "UFs6E3Ti1jg", "ultWZbUMPL8"),
            youtubeUrl = "https://www.youtube.com/results?search_query=squat+proper+form"
        ),
        Exercise(
            name = "Leg Press",
            muscleGroup = "Legs",
            type = "legs",
            videoIds = listOf("IZxyjW7MPJQ", "sEM_zo9w2ss", "YyvSfVjQeL0"),
            youtubeUrl = "https://www.youtube.com/results?search_query=leg+press+proper+form"
        ),
        Exercise(
            name = "Romanian Deadlift",
            muscleGroup = "Legs",
            type = "legs",
            videoIds = listOf("JCXUYuzwNrM", "2SHsk9AzdjA", "ymL6b50Al6U"),
            youtubeUrl = "https://www.youtube.com/results?search_query=romanian+deadlift+proper+form"
        )
    )

    val starterProgram = StarterProgram(
        name = "Beginner Push/Pull/Legs",
        description = "A simple 3-day program designed for beginners. Focus on learning proper form with lighter weights before increasing.",
        daysPerWeek = 3,
        workouts = listOf(
            WorkoutDay(
                id = "push",
                name = "Push Day",
                label = "A",
                description = "Chest, Shoulders & Triceps",
                exercises = listOf(
                    ProgramExercise("Bench Press", 3, 10, "Start with just the bar (20kg) and add weight as you get comfortable"),
                    ProgramExercise("Overhead Press", 3, 10, "Keep core tight, press straight up"),
                    ProgramExercise("Incline Dumbbell Press", 3, 12, "Use dumbbells you can control, 30° incline"),
                    ProgramExercise("Tricep Pushdowns", 3, 12, "Keep elbows tucked at your sides")
                )
            ),
            WorkoutDay(
                id = "pull",
                name = "Pull Day",
                label = "B",
                description = "Back & Biceps",
                exercises = listOf(
                    ProgramExercise("Deadlifts", 3, 8, "Start light, focus on keeping your back straight"),
                    ProgramExercise("Barbell Rows", 3, 10, "Pull the bar to your lower chest, squeeze shoulder blades"),
                    ProgramExercise("Pull-ups", 3, 8, "Use assisted pull-up machine if needed"),
                    ProgramExercise("Barbell Curls", 3, 12, "Control the weight, no swinging")
                )
            ),
            WorkoutDay(
                id = "legs",
                name = "Leg Day",
                label = "C",
                description = "Quadriceps, Hamstrings & Glutes",
                exercises = listOf(
                    ProgramExercise("Squats", 3, 10, "Start with bodyweight or just the bar, go as deep as comfortable"),
                    ProgramExercise("Leg Press", 3, 12, "Feet shoulder-width apart on the platform"),
                    ProgramExercise("Romanian Deadlift", 3, 10, "Slight knee bend, feel the stretch in hamstrings")
                )
            )
        )
    )

    fun getYoutubeUrl(exerciseName: String): String {
        val exercise = exerciseList.find { it.name == exerciseName }
        return exercise?.youtubeUrl ?: "https://www.youtube.com/results?search_query=how+to+${exerciseName}+proper+form+beginner"
    }

    fun getYoutubeVideoId(exerciseName: String): String? {
        val exercise = exerciseList.find { it.name == exerciseName }
        return exercise?.videoIds?.randomOrNull()
    }

    fun getNextWorkoutDay(lastWorkoutType: String?): WorkoutDay {
        val order = listOf("push", "pull", "legs")
        val lastIndex = order.indexOf(lastWorkoutType)
        val nextIndex = (lastIndex + 1) % order.size
        val nextType = order[nextIndex]
        return starterProgram.workouts.find { it.id == nextType } ?: starterProgram.workouts[0]
    }
}
