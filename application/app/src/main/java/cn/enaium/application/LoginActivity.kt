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

package cn.enaium.application

import android.annotation.SuppressLint
import android.app.DownloadManager.Request
import android.app.Instrumentation
import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import cn.enaium.application.ui.theme.applicationTheme
import cn.enaium.application.util.Http
import cn.enaium.application.util.execute
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@SuppressLint("StaticFieldLeak")
var loginActivityContext: Context? = null

/**
 * @author Enaium
 */
class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginActivityContext = this

        Toast.makeText(this, "Please login", Toast.LENGTH_SHORT).show()
        setContent {
            applicationTheme {
                preview()
            }
        }
    }

    override fun onDestroy() {
        loginActivityContext = null
        super.onDestroy()
    }
}


@Preview(showBackground = true)
@Composable
private fun preview() {
    val current = LocalContext.current

    var username: String by remember { mutableStateOf("") }
    var password: String by remember { mutableStateOf("") }

    Box(Modifier.fillMaxSize()) {

        Column(Modifier.align(Alignment.Center)) {
            Row {
                Box(Modifier.align(Alignment.CenterVertically)) { Text("Username:") }
                TextField(username, {
                    username = it
                }, Modifier.weight(1f))
            }
            Row {
                Box(Modifier.align(Alignment.CenterVertically)) { Text("Password:") }
                TextField(
                    password, { password = it },
                    visualTransformation = PasswordVisualTransformation(), modifier = Modifier.weight(1f)
                )
            }

            Button({
                execute {
                    val tReturn = Http.tReturn<String>(
                        "member/login",
                        mapOf("username" to username, "password" to password)
                    )
                    it.post {
                        if (tReturn.success) {
                            val edit = current.getSharedPreferences("config", ComponentActivity.MODE_PRIVATE).edit()
                            edit.putString("token", tReturn.data)
                            edit.apply()
                            Toast.makeText(current, "Login success", Toast.LENGTH_SHORT).show()
                            Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_BACK)
                        } else {
                            Toast.makeText(current, tReturn.data, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }, Modifier.fillMaxWidth()) {
                Text("Login")
            }
        }
    }
}

