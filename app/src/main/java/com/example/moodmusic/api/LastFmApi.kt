package com.example.moodmusic.api

import com.example.moodmusic.model.TopTracksResponse
import com.example.moodmusic.model.TrackInfoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFmApi {

    @GET("2.0/")
    suspend fun getTopTracksByTag(
        @Query("method") method: String = "tag.gettoptracks",
        @Query("tag") tag: String,
        @Query("limit") limit: Int = 20,
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json"
    ): TopTracksResponse


    @GET("2.0/")
    suspend fun getTrackInfo(
        @Query("method") method: String = "track.getInfo",
        @Query("artist") artist: String,
        @Query("track") track: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json"
    ): TrackInfoResponse


}
