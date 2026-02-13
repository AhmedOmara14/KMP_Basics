package com.omaradev.kmp_example.data

import platform.Foundation.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class URLSessionClient {

    suspend fun fetch(
        baseUrl: String,
        headers: Map<String, String> = emptyMap()
    ): String = suspendCoroutine { continuation ->

        val url = NSURL(string = baseUrl)

        val request = NSMutableURLRequest.requestWithURL(url).apply {
            HTTPMethod = "GET"
            setValue("application/json", forHTTPHeaderField = "accept")
            headers.forEach { (k, v) -> setValue(v, forHTTPHeaderField = k) }
        }

        val task = NSURLSession.sharedSession.dataTaskWithRequest(request) { data, response, error ->
            when {
                error != null -> {
                    continuation.resumeWithException(Exception(error.localizedDescription))
                }

                response !is NSHTTPURLResponse -> {
                    continuation.resumeWithException(Exception("Invalid response"))
                }

                response.statusCode.toInt() !in 200..299 -> {
                    continuation.resumeWithException(
                        Exception("HTTP ${response.statusCode}")
                    )
                }

                data == null -> {
                    continuation.resumeWithException(Exception("No data received"))
                }

                else -> {
                    val text = NSString.create(data = data, encoding = NSUTF8StringEncoding)?.toString()
                        ?: return@dataTaskWithRequest continuation.resumeWithException(
                            Exception("Failed to decode data as UTF-8")
                        )

                    continuation.resume(text)
                }
            }
        }

        task.resume()
    }
}
