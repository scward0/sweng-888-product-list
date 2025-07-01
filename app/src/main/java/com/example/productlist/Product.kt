package com.example.productlist
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val seller: String,
    val price: Double,
    val pictureUri: String, // Will store drawable resource name
    val category: String
) : Parcelable