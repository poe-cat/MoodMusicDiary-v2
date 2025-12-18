package com.example.moodmusic.model

enum class Mood(val tags: List<String>) {

    SAD(
        listOf(
            "sad",
            "melancholic",
            "depressive",
            "lonely",
            "breakup"
        )
    ),

    HAPPY(
        listOf(
            "happy",
            "feel good",
            "uplifting",
            "dance",
            "summer"
        )
    ),

    CALM(
        listOf(
            "chillout",
            "ambient",
            "acoustic",
            "relaxing",
            "downtempo"
        )
    ),

    ENERGETIC(
        listOf(
            "energetic",
            "party",
            "electronic",
            "rock",
            "edm"
        )
    ),

    FOCUS(
        listOf(
            "instrumental",
            "post-rock",
            "minimal",
            "soundtrack",
            "ambient"
        )
    ),

    ANGRY(
        listOf(
            "angry",
            "metal",
            "hardcore",
            "industrial",
            "aggressive"
        )
    );
}
