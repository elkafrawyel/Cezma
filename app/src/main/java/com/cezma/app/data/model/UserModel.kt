package com.cezma.app.data.model


import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class UserModel(
    @field:Json(name = "id")
    var id: Int?,
    @field:Json(name = "username")
    var username: String?,
    @field:Json(name = "first_name")
    var firstName: String?,
    @field:Json(name = "last_name")
    var lastName: String?,
    @field:Json(name = "email")
    var email: String?,
    @field:Json(name = "avatar")
    var avatar: String?,
    @field:Json(name = "gender")
    var gender: Int?,
    @field:Json(name = "country_code")
    var countryCode: String?,
    @field:Json(name = "state")
    var state: Int?,
    @field:Json(name = "city")
    var city: Int?,
    @field:Json(name = "phonecode")
    var phonecode: String?,
    @field:Json(name = "phone")
    var phone: String?,
    @field:Json(name = "phone_hidden")
    var phoneHidden: Int?,
    @field:Json(name = "id_verified")
    var idVerified: Int?,
    @field:Json(name = "account_type")
    var accountType: Int?,
    @field:Json(name = "is_admin")
    var isAdmin: Int?,
    @field:Json(name = "status")
    var status: Int?,
    @field:Json(name = "has_store")
    var hasStore: Int?,
    @field:Json(name = "store_ends_at")
    var storeEndsAt: String?,
    @field:Json(name = "facebook_id")
    var facebookId: String?,
    @field:Json(name = "twitter_id")
    var twitterId: String?,
    @field:Json(name = "google_id")
    var googleId: String?,
    @field:Json(name = "instagram_id")
    var instagramId: String?,
    @field:Json(name = "pinterest_id")
    var pinterestId: String?,
    @field:Json(name = "linkedin_id")
    var linkedinId: String?,
    @field:Json(name = "vk_id")
    var vkId: String?,
    @field:Json(name = "identifyme_id")
    var identifymeId: String?,
    @field:Json(name = "last_login_ip")
    var lastLoginIp: String?,
    @field:Json(name = "last_login_at")
    var lastLoginAt: String?,
    @field:Json(name = "is_2fa")
    var is2fa: Int?,
    @field:Json(name = "created_at")
    var createdAt: String?,
    @field:Json(name = "updated_at")
    var updatedAt: String?,
    @field:Json(name = "country_id")
    var country_id: Int?,
    @field:Json(name = "country_name")
    var country_name: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(username)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(email)
        parcel.writeString(avatar)
        parcel.writeValue(gender)
        parcel.writeString(countryCode)
        parcel.writeValue(state)
        parcel.writeValue(city)
        parcel.writeString(phonecode)
        parcel.writeString(phone)
        parcel.writeValue(phoneHidden)
        parcel.writeValue(idVerified)
        parcel.writeValue(accountType)
        parcel.writeValue(isAdmin)
        parcel.writeValue(status)
        parcel.writeValue(hasStore)
        parcel.writeString(storeEndsAt)
        parcel.writeString(facebookId)
        parcel.writeString(twitterId)
        parcel.writeString(googleId)
        parcel.writeString(instagramId)
        parcel.writeString(pinterestId)
        parcel.writeString(linkedinId)
        parcel.writeString(vkId)
        parcel.writeString(identifymeId)
        parcel.writeString(lastLoginIp)
        parcel.writeString(lastLoginAt)
        parcel.writeValue(is2fa)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
        parcel.writeValue(country_id)
        parcel.writeString(country_name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserModel> {
        override fun createFromParcel(parcel: Parcel): UserModel {
            return UserModel(parcel)
        }

        override fun newArray(size: Int): Array<UserModel?> {
            return arrayOfNulls(size)
        }
    }
}