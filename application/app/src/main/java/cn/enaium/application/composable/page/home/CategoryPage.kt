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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.enaium.application.R
import cn.enaium.application.composable.component.topBar
import cn.enaium.application.model.Model
import cn.enaium.application.model.result.Category
import cn.enaium.application.util.Http
import cn.enaium.application.util.execute

/**
 * @author Enaium
 */
@Composable
fun categoryPage(model: Model) {

    var list by remember { mutableStateOf(listOf<Category>()) }

    execute { list = Http.tReturn<List<Category>>("category/get").data }

    Column {
        topBar(model, "Category")
        LazyColumn(Modifier.fillMaxSize()) {
            itemsIndexed(list) { index, item ->
                categoryItem(item) {

                }
            }
        }
    }
}

@Composable
fun categoryItem(category: Category, onSelectedChanged: (Int) -> Unit) {
    Row(
        Modifier.fillMaxWidth().clickable { onSelectedChanged(category.id) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painterResource(R.drawable.ic_notice), category.title, Modifier.size(64.dp))
        Box(Modifier.weight(1f))
        Column {
            Text(category.title, fontSize = 24.sp)
            Text(category.description, fontSize = 20.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun categoryItemPreview() {
    categoryItem(Category(1, "Notice", "Official Notice")) {

    }
}
