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

package cn.enaium.application.composable.page

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.enaium.application.composable.component.topBar
import cn.enaium.application.composable.page.home.avatarBar
import cn.enaium.application.model.Model
import cn.enaium.application.model.result.MemberInfo
import cn.enaium.application.util.Http
import cn.enaium.application.util.execute
import com.vanpra.composematerialdialogs.*
import input
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Enaium
 */
@Composable
fun memberInfo(model: Model, id: Long, edit: Boolean) {
    val current = LocalContext.current
    var memberI: MemberInfo? by remember { mutableStateOf(null) }

    rememberCoroutineScope().launch {
        execute {
            memberI = Http.getMemberInfo(id)
        }
    }

    Box(Modifier.fillMaxSize()) {
        if (memberI != null) {
            val memberInfo = memberI!!

            Column(Modifier.fillMaxWidth()) {
                topBar(model, memberInfo.nickname) {
                    model.rememberNavController!!.popBackStack()
                }
                avatarBar(memberInfo.nickname, memberInfo.description, memberInfo.role, memberInfo.level) {

                }
                val nickname = dialog("Nickname", memberInfo.nickname) { input ->
                    if (input.isNotBlank()) {
                        execute {
                            val tReturn = Http.tReturn<String>("memberInfo/update", mapOf("nickname" to input)).data
                            it.post {
                                Toast.makeText(current, tReturn, Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(current, "cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                }

                val description = dialog("Description", memberInfo.description) { input ->
                    if (input.isNotBlank()) {
                        execute {
                            Http.tReturn<String>("memberInfo/update", mapOf("description" to input))
                        }
                    } else {
                        Toast.makeText(current, "cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                }

                val birthday =
                    dialog(
                        "Birthday",
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(memberInfo.birthday)
                    ) { input ->
                        if (input.isNotBlank()) {
                            if (input.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
                                execute {
                                    Http.tReturn<String>(
                                        "memberInfo/update",
                                        mapOf(
                                            "birthday" to input
                                        )
                                    )
                                }
                            } else {
                                Toast.makeText(current, "please input the right format", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(current, "cannot be empty", Toast.LENGTH_SHORT).show()
                        }
                    }

                val gender = dialog("Gender", if (memberInfo.gender == 'f') "Female" else "Male") { input ->
                    if (input.isNotBlank()) {

                        execute {
                            Http.tReturn<String>(
                                "memberInfo/update",
                                mapOf("gender" to if (input.contains("female", true)) "f" else "m")
                            )
                        }
                    } else {
                        Toast.makeText(current, "cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                }

                infoItem("Nickname", memberInfo.nickname, edit) {
                    nickname.show()
                }
                infoItem("Description", memberInfo.description, edit) {
                    description.show()
                }
                infoItem(
                    "Birthday",
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(memberInfo.birthday),
                    edit
                ) {
                    birthday.show()
                }
                infoItem("Gender", if (memberInfo.gender == 'f') "Female" else "Male", edit) {
                    gender.show()
                }
            }
        }
    }
}

@Composable
fun infoItem(title: String, value: String, edit: Boolean, onClick: () -> Unit) {
    Box(Modifier.fillMaxWidth().clickable {
        if (edit) {
            onClick()
        }
    }) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(48.dp).padding(5.dp)) {
            Text(title)
            Box(Modifier.weight(1f))
            Text(value)
        }
    }
}

@Composable
fun dialog(label: String, placeholder: String, ok: (String) -> Unit): MaterialDialogState {
    val rememberMaterialDialogState = rememberMaterialDialogState()
    MaterialDialog(dialogState = rememberMaterialDialogState, buttons = {
        positiveButton("Ok")
        negativeButton("Cancel")
    }) {
        input(label, placeholder) { inputString ->
            ok(inputString)
        }
    }
    return rememberMaterialDialogState
}

@Preview(showBackground = true)
@Composable
fun infoItemPreview() {
    infoItem("Nickname", "Enaium", true) {

    }
}