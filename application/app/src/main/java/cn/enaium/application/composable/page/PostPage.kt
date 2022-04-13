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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.enaium.application.composable.component.push
import cn.enaium.application.composable.component.topBar
import cn.enaium.application.composable.page.home.avatarBar
import cn.enaium.application.model.Model
import cn.enaium.application.model.result.MemberInfo
import cn.enaium.application.model.result.Post
import cn.enaium.application.model.result.PostComment
import cn.enaium.application.util.Http
import cn.enaium.application.util.execute
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.launch

/**
 * @author Enaium
 */
@Composable
fun post(model: Model, id: Long) {
    val current = LocalContext.current
    var post: Post? by remember { mutableStateOf(null) }

    rememberCoroutineScope().launch {
        execute {
            post = Http.tReturn<Post>("post/getById/$id").data
        }
    }


    Box(Modifier.fillMaxSize().background(Color.White)) {
        if (post != null) {
            val currentPost = post!!

            var commentList: List<PostComment>? by remember { mutableStateOf(listOf()) }

            fun refresh() {
                execute {
                    commentList =
                        Http.tReturn<List<PostComment>>("post-comment/getByCommentTime/${currentPost.id}/0/10").data
                }
            }

            Column {
                topBar(model, currentPost.title) {
                    model.rememberNavController!!.popBackStack()
                }

                /*
                   post
                 */

                Box(Modifier.weight(1f)) {

                    rememberCoroutineScope().launch {
                        refresh()
                    }

                    Column(Modifier.fillMaxWidth()) {
                        Text("Post:${currentPost.postTime}", fontSize = 15.sp)
                        Text("Update:${currentPost.updateTime}", fontSize = 15.sp)
                        Divider(
                            color = Color.LightGray,
                            modifier = Modifier
                                .height(2.dp)
                                .fillMaxWidth()
                        )

                        LazyColumn(modifier = Modifier.padding(8.dp)) {
                            item {
                                MarkdownText(
                                    markdown = currentPost.content,
                                    disableLinkMovementMethod = true
                                )
                            }
                            if (!commentList.isNullOrEmpty()) {
                                item {
                                    Divider(
                                        color = Color.LightGray,
                                        modifier = Modifier
                                            .height(2.dp)
                                            .fillMaxWidth()
                                    )
                                }
                                itemsIndexed(commentList!!) { index, item ->
                                    commentItem(item, model)
                                    if (index < commentList!!.lastIndex) {
                                        Divider(
                                            startIndent = 68.dp,
                                            thickness = 0.8f.dp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                /*
                    post
                 */


                /*
                    comment send
                 */

                var comment: String by remember { mutableStateOf("") }
                Column(Modifier.fillMaxWidth()) {
                    Divider(
                        color = Color.LightGray,
                        modifier = Modifier
                            .height(2.dp)
                            .fillMaxWidth()
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextField(comment, {
                            comment = it
                        }, Modifier.weight(1f).padding(10.dp))
                        Button({
                            execute {
                                val tReturn =
                                    Http.tReturn<String>(
                                        "post-comment/comment",
                                        mapOf("postId" to currentPost.id, "comment" to comment)
                                    ).data
                                it.post {
                                    Toast.makeText(current, tReturn, Toast.LENGTH_SHORT).show()
                                    comment = ""

                                }
                            }
                        }) {
                            Text("Send")
                        }
                    }
                }

                /*
                    comment send
                */
            }

        }
    }
}

@Composable
fun commentItem(postComment: PostComment, model: Model) {

    var memberInfo: MemberInfo? by remember { mutableStateOf(null) }

    rememberCoroutineScope().launch {
        execute {
            memberInfo = Http.getMemberInfo(postComment.memberId)
        }
    }

    Column(Modifier.fillMaxWidth()) {
        if (memberInfo != null) {
            avatarBar(memberInfo!!.nickname, memberInfo!!.description) {
                model.rememberNavController!!.push("memberInfo/${postComment.memberId}/false")
            }
        }
        Text("${postComment.commentTime}" + if (postComment.edited) "(Edited)" else "")
        Text(postComment.comment)
    }
}