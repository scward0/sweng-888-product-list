package com.example.productlist
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


// Create Product Data Type, use Parcelable for passing objects between activities
@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val seller: String,
    val price: Double,
    val pictureUri: String,
    val category: String
) : Parcelable