package com.tailwind.kos.model

import com.google.gson.annotations.SerializedName

open class Response {
    @SerializedName("response_message")
    var responseMessage: String? = null
}