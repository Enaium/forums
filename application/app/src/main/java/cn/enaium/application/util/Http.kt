/**
 * Copyright (c) 2022 Enaium
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package cn.enaium.application.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import cn.enaium.application.LoginActivity
import cn.enaium.application.loginActivityContext
import cn.enaium.application.mainActivityContext
import cn.enaium.application.model.result.MemberInfo
import cn.enaium.application.model.result.Return
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


/**
 * @author Enaium
 */
@SuppressLint("StaticFieldLeak")
object Http {

    private const val prefix = "http://192.168.116.239:8080/"
    private const val GET = "GET"
    private const val POST = "POST"


    private val client = OkHttpClient.Builder().build()

    private fun build(url: String, requestBody: RequestBody? = null): Request.Builder {
        return Request.Builder()
            .url(prefix + url).method(if (requestBody == null) GET else POST, requestBody)
    }

    fun string(url: String, requestBody: RequestBody? = null): String {
        val build = build(url, requestBody)
        if (mainActivityContext != null) {
            val sharedPreferences = mainActivityContext!!.getSharedPreferences("config", ComponentActivity.MODE_PRIVATE)
            build.addHeader("token", sharedPreferences.getString("token", "")!!)
        }
        client.newCall(build.build()).execute().use {
            return it.body!!.string()
        }
    }

    inline fun <reified T> tReturn(url: String, map: Map<Any, Any> = mapOf()): Return<T> {
        var requestBody: RequestBody? = null
        if (map.isNotEmpty()) {
            requestBody = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(map)
                .toRequestBody("application/json; charset=utf-8".toMediaType())
        }

        val type = object : TypeToken<Return<T>>() {}.type
        val string = string(
            url, requestBody
        )

        val fromJson = Gson().fromJson(string, JsonObject::class.java)

        if (fromJson.has("login")) {
            if (fromJson.get("login").asBoolean && mainActivityContext != null && loginActivityContext == null) {
                mainActivityContext!!.startActivity(Intent(mainActivityContext, LoginActivity::class.java))
            }
        }

        return GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
            .fromJson(
                string, type
            )
    }


    fun getMemberId(): Long = tReturn<Long>("member/getId").data

    fun getMemberInfo(id: Long = getMemberId()): MemberInfo =
        tReturn<MemberInfo>("memberInfo/getFull/$id").data
}