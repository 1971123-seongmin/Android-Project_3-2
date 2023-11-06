package com.example.secondhandmarket
data class ItemModel(
    val imgUri: String? = null,
    val title: String? = null,
    val status: String? = null, // Changed Boolean to String
    val price: Int? = null,
    val description: String? = null
)