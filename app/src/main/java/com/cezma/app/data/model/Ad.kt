package com.cezma.app.data.model

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class Ad(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "ad_id")
    val adId: Long?,
    @field:Json(name = "affiliate_link")
    val affiliateLink: Any?,
    @field:Json(name = "slug")
    val slug: String?,
    @field:Json(name = "user_id")
    val userId: Int?,
    @field:Json(name = "category")
    val category: Int?,
    @field:Json(name = "attrValId")
    val attrValId: String?,
    @field:Json(name = "photos")
    val photos: List<String>,
    @field:Json(name = "thumbnails")
    val thumbnails: List<String>,
    @field:Json(name = "photos_number")
    val photosNumber: Int?,
    @field:Json(name = "images_host")
    val imagesHost: String?,
    @field:Json(name = "negotiable")
    val negotiable: Int?,
    @field:Json(name = "is_used")
    val isUsed: Int?,
    @field:Json(name = "title")
    val title: String?,
    @field:Json(name = "description")
    val description: String?,
    @field:Json(name = "country")
    val country: String?,
    @field:Json(name = "state")
    val state: Int?,
    @field:Json(name = "city")
    val city: Int?,
    @field:Json(name = "latitude")
    val latitude: String?,
    @field:Json(name = "longitude")
    val longitude: String?,
    @field:Json(name = "radius")
    val radius: Int?,
    @field:Json(name = "views")
    val views: Int?,
    @field:Json(name = "likes")
    val likes: Int?,
    @field:Json(name = "price")
    val price: Int?,
    @field:Json(name = "regular_price")
    val regularPrice: String?,
    @field:Json(name = "youtube")
    val youtube: String?,
    @field:Json(name = "currency")
    val currency: String?,
    @field:Json(name = "status")
    val status: Int?,
    @field:Json(name = "is_featured")
    val isFeatured: Int?,
    @field:Json(name = "is_archived")
    val isArchived: Int?,
    @field:Json(name = "is_oos")
    val isOos: Int?,
    @field:Json(name = "is_trashed")
    val isTrashed: Int?,
    @field:Json(name = "trashed_by_admin")
    val trashedByAdmin: Int?,
    @field:Json(name = "deleted_at")
    val deletedAt: String?,
    @field:Json(name = "ends_at")
    val endsAt: String?,
    @field:Json(name = "created_at")
    val createdAt: String?,
    @field:Json(name = "updated_at")
    val updatedAt: String?,
    @field:Json(name = "fav")
    var fav: Int?,
    @field:Json(name = "url")
    val url: String?,
    @field:Json(name = "username")
    val username: String?,
    @field:Json(name = "avatar")
    val avatar: String?,
    @field:Json(name = "subcategory_name")
    val subCategoryName: String?,
    @field:Json(name = "maincategory_name")
    val categoryName: String?,
    @field:Json(name = "hasstore")
    val hasStore: Int?,
    @field:Json(name = "atributes")
    val attributeModels: List<AttributeModel>
)

data class AttributeModel(
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "value")
    val value: String
)

data class AdImagesToSliderModel(
    val photos: List<String>
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createStringArrayList())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringList(photos)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AdImagesToSliderModel> {
        override fun createFromParcel(parcel: Parcel): AdImagesToSliderModel {
            return AdImagesToSliderModel(parcel)
        }

        override fun newArray(size: Int): Array<AdImagesToSliderModel?> {
            return arrayOfNulls(size)
        }
    }
}