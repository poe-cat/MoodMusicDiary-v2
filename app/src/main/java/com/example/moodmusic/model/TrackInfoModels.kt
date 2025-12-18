package com.example.moodmusic.model

import com.google.gson.annotations.SerializedName

data class TrackInfoResponse(
    @SerializedName("track")
    val track: TrackInfo
)

data class TrackInfo(
    val name: String,
    val artist: ArtistInfo,
    val album: AlbumInfo?,
    val listeners: String?,
    val playcount: String?,
    @SerializedName("toptags")
    val topTags: TopTags?
)

data class ArtistInfo(
    val name: String
)

data class AlbumInfo(
    val title: String?,
    @SerializedName("image")
    val images: List<Image>?
)

data class TopTags(
    @SerializedName("tag")
    val tags: List<Tag>?
)

data class Tag(
    val name: String
)
