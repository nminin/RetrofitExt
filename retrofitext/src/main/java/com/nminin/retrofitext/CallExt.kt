package com.nminin.retrofitext

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> Call<T>.response(
    onSuccess: (Int, T) -> Unit,
    onError: (Int, String) -> Unit,
    onSuccessEmpty: ((Int) -> Unit)? = null,
    errorDeserializer: ((Int, ResponseBody?) -> String)? = null
) {

    this.enqueue(object : Callback<T> {

        override fun onResponse(call: Call<T>?, response: Response<T>?) {

            response?.let {
                if (response.code() / 100 == 2) {
                    response.body()?.let {
                        onSuccess.invoke(
                            response.code(),
                            it
                        )
                    } ?: run {
                        onSuccessEmpty?.let {
                            onSuccessEmpty.invoke(response.code())
                        } ?: kotlin.run {
                            onError.invoke(response.code(), "Empty body.")
                        }
                    }
                } else {
                    val message = errorDeserializer?.let {
                        it.invoke(response.code(), response.errorBody())
                    }?: response.errorBody()?.let {
                        try {
                            Gson().fromJson(
                                it.string(),
                                ErrorResponse::class.java
                            )
                                .message()
                        } catch (e: IllegalStateException) {
                            it.string()
                        } catch (e: JsonSyntaxException) {
                            it.string()
                        } catch (e: NullPointerException) {
                            it.string()
                        }
                    } ?: "Empty error message"
                    onError.invoke(response.code(), message)
                }
            } ?: run {
                onError.invoke(0, "Empty response")
            }
        }

        override fun onFailure(call: Call<T>?, t: Throwable?) {
            onError.invoke(0, t?.localizedMessage ?: "Request error")
        }
    })
}

class ErrorResponse {
    @SerializedName("message")
    @Expose
    var message: String = ""
    @SerializedName("error")
    @Expose
    var error: String = ""

    fun message(): String {
        return when {
            message.isNotBlank() -> message
            error.isNotBlank() -> error
            else -> "Unknown Error"
        }
    }
}