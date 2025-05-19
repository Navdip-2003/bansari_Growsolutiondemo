package com.bansarichothani.kotlindemo.Utils

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Modelfilter(
    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("label")
    @Expose
    var label: String? = null,

    @SerializedName("smart")
    @Expose
    var smart: String? = null,

    @SerializedName("courses")
    @Expose
    var courses: List<Int>? = null,

    @SerializedName("is_default")
    @Expose
    var isDefault: Int? = null,

    @SerializedName("is_archive")
    @Expose
    var isArchive: Int? = null,

    @SerializedName("description")
    @Expose
    var description: String? = null
)
