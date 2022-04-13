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

package cn.enaium.application.composable.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.enaium.application.model.Model
import cn.enaium.application.R

/**
 * @author Enaium
 */
@Composable
fun bottomBar(select: Int, onSelectedChanged: (Int) -> Unit) {
    Row {
        tabItem(
            if (select == 0) R.drawable.ic_home_select else R.drawable.ic_home,
            "Post",
            if (select == 0) Color.Green else Color.Black,
            Modifier.weight(1f).clickable { onSelectedChanged(0) })
        tabItem(
            if (select == 1) R.drawable.ic_category_select else R.drawable.ic_category,
            "Category",
            if (select == 1) Color.Green else Color.Black,
            Modifier.weight(1f).clickable { onSelectedChanged(1) })
        tabItem(
            if (select == 2) R.drawable.ic_me_select else R.drawable.ic_me,
            "Me",
            if (select == 2) Color.Green else Color.Black,
            Modifier.weight(1f).clickable { onSelectedChanged(2) })
    }
}

@Composable
private fun tabItem(@DrawableRes icon: Int, title: String, color: Color, modifier: Modifier = Modifier) {
    Column(modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(painterResource(icon), title, Modifier.size(24.dp), tint = color)
        Text(title, fontSize = 11.sp, color = color)
    }
}


@Preview(showBackground = true)
@Composable
fun itemBar() {
    val model = Model()
    bottomBar(model.currentBottom) {
        model.currentBottom = it
    }
}