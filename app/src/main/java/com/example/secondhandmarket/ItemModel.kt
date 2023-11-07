package com.example.secondhandmarket
data class ItemModel(
    val imgUri: String? = null,
    val seller: String? = null,
    val title: String? = null,
    val status: String? = null,
    val price: String? = null, // String으로 수정
    val description: String? = null
)