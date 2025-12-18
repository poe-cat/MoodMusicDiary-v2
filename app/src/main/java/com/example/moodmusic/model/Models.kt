package com.example.moodmusic.model

import com.google.gson.annotations.SerializedName

data class TopTracksResponse(
    @SerializedName("tracks")
    val tracks: Tracks
)

data class Tracks(
    @SerializedName("track")
    val trackList: List<Track>
)

data class Track(
    val name: String,
    val artist: Artist,
    @SerializedName("image")
    val images: List<Image>
)

data class Artist(
    val name: String
)

data class Image(
    @SerializedName("#text")
    val url: String,
    val size: String
)
