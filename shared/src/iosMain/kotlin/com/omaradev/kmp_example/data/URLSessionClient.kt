package com.omaradev.kmp_example.data

import platform.Foundation.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class URLSessionClient {
    suspend fun fetch(baseUrl: String): String {
        val url = NSURL(string = baseUrl)
        return suspendCoroutine { continuation ->
            val task = NSURLSession.sharedSession.dataTaskWithURL(url = url) { data: NSData?, _, error ->
                when {
                    error != null -> {
                        NSLog("🔴 Error: ${error.localizedDescription}")
                        continuation.resumeWithException(Exception(error.localizedDescription))
                    }
                    data == null -> {
                        NSLog("🔴 No data received")
                        continuation.resumeWithException(Exception("No data received"))
                    }
                    else -> {
                        val nsString = NSString.create(
                            data = data,
                            encoding = NSUTF8StringEncoding
                        )

                        if (nsString == null) {
                            NSLog("🔴 Failed to convert data to string")
                            continuation.resumeWithException(Exception("Failed to decode data"))
                        } else {
                            val json = nsString.toString()
                            NSLog("✅ Success: ${json.length} characters")
                            continuation.resume(json)
                        }
                    }
                }
            }

            task.resume()
        }
    }
}