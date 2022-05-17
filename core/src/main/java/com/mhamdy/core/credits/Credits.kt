package com.mhamdy.core.credits

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Credits(
    @SerializedName("id") val id: Int,
    @SerializedName("cast") val cast: List<Person>,
    @SerializedName("crew") val crew: List<Person>
) : Serializable

data class Person(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("known_for_department") val department: String,
    @SerializedName("popularity") val popularity: Float,
    @SerializedName("profile_path") val profileImgPath: String?
) : Serializable{
    fun profileImgUrl() = "https://image.tmdb.org/t/p/w185${profileImgPath}"
}

data class CreditView(val actors: List<Person>, val directors: List<Person>) : Serializable