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

package cn.enaium.application.composable.page.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.enaium.application.R
import cn.enaium.application.composable.component.push
import cn.enaium.application.composable.component.topBar
import cn.enaium.application.model.Model
import cn.enaium.application.model.result.MemberInfo
import cn.enaium.application.util.Http
import cn.enaium.application.util.execute
import kotlinx.coroutines.launch

/**
 * @author Enaium
 */
@Composable
fun mePage(model: Model) {

    var memberInfo: MemberInfo? by remember { mutableStateOf(null) }

    rememberCoroutineScope().launch {
        execute {
            memberInfo = Http.getMemberInfo()
        }
    }

    Column {
        topBar(model, "Me")
        if (memberInfo != null) {
            avatarBar(memberInfo!!.nickname, memberInfo!!.description, memberInfo!!.role, memberInfo!!.level) {
                model.rememberNavController!!.push("memberInfo/${memberInfo!!.id}/true")
            }
        }

        item(R.drawable.ic_setting, "Setting") {

        }
        Box(Modifier.fillMaxHeight())
    }
}

@Composable
fun item(@DrawableRes icon: Int, title: String, clickable: () -> Unit) {
    Row(Modifier.fillMaxWidth().clickable {
        clickable()
    }, verticalAlignment = Alignment.CenterVertically) {
        Text(title, fontSize = 24.sp)
        Box(Modifier.weight(1f))
        Icon(painterResource(icon), title, Modifier.size(32.dp))
    }
}

@Composable
fun avatarBar(
    nickname: String = "Nickname",
    description: String = "Description",
    role: String = "User",
    level: Byte = 1,
    onClick: () -> Unit
) {
    Row(Modifier.fillMaxWidth().clickable { onClick() }) {
        Image(
            painterResource(R.drawable.akkarinn),
            "avatar",
            Modifier.size(48.dp).border(width = 1.dp, color = Color.Gray)
        )
        Column {
            Row {
                Text(nickname)
                Surface(Modifier.clip(CircleShape), color = Color.Red) {
                    Text(role, Modifier.padding(0.dp, 5.dp))
                }
                Surface(Modifier.clip(CircleShape), color = Color.Gray) {
                    Text(level.toString(), Modifier.padding(0.dp, 5.dp))
                }
            }
            Text(description)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun mePagePreview() {
    item(R.drawable.ic_setting, "Setting") {

    }
}

@Preview(showBackground = true)
@Composable
fun avatar() {
    avatarBar {

    }
}


