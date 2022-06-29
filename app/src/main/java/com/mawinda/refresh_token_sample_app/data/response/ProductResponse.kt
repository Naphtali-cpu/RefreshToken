package com.mawinda.refresh_token_sample_app.data.response

import com.google.gson.annotations.SerializedName

data class ProductResponseItem(

	@field:SerializedName("sortno")
	val sortno: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("created_date")
	val createdDate: String? = null,

	@field:SerializedName("slug")
	val slug: String? = null
)
