package com.bansarichothani.kotlindemo.Utils

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

data class Coursemodle(
    @SerializedName("downloadid")
    @Expose
    var downloadid: Int? = null,

    @SerializedName("cd_downloads")
    @Expose
    var cdDownloads: Int? = null,

    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("status")
    @Expose
    var status: Int? = null,

    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null,

    @SerializedName("authorid")
    @Expose
    var authorid: Int? = null,

    @SerializedName("video_count")
    @Expose
    var videoCount: Int? = null,

    @SerializedName("style_tags")
    @Expose
    var styleTags: List<String>? = null,

    @SerializedName("skill_tags")
    @Expose
    var skillTags: List<String>? = null,

    @SerializedName("series_tags")
    @Expose
    var seriesTags: List<String>? = null,

    @SerializedName("curriculum_tags")
    @Expose
    var curriculumTags: List<String>? = null,

    @SerializedName("educator")
    @Expose
    var educator: String? = null,

    @SerializedName("owned")
    @Expose
    var owned: Int? = null,

    @SerializedName("sale")
    @Expose
    var sale: Int? = null,

    @SerializedName("watched")
    @Expose
    var watched: Int? = null
) : Serializable
