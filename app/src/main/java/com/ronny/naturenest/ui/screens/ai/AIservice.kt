package com.ronny.naturenest.ui.screens.ai

import okhttp3.MediaType.Companion.toMediaType

object AiService {

    private const val API_KEY = "YOUR_OPENAI_API_KEY"

    fun askAi(prompt: String, callback: (String) -> Unit) {

        val client = okhttp3.OkHttpClient()

        val body = okhttp3.RequestBody.create(
            "application/json".toMediaType(),
            """
            {
              "model": "gpt-4o-mini",
              "messages": [
                {"role": "system", "content": "You are a helpful maternal health assistant."},
                {"role": "user", "content": "$prompt"}
              ]
            }
            """
        )

        val request = okhttp3.Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer $API_KEY")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                callback("Error: ${e.message}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val result = response.body?.string()
                callback(result ?: "No response")
            }
        })
    }
}