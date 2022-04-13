/**
 * Copyright (c) 2022 Enaium
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package cn.enaium.server.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.enaium.server.mapper.PostCommentMapper;
import cn.enaium.server.mapper.PostMapper;
import cn.enaium.server.model.dto.PostCommentDTO;
import cn.enaium.server.model.entity.Post;
import cn.enaium.server.model.entity.PostComment;
import cn.enaium.server.model.result.Result;
import cn.enaium.server.model.result.StringResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author Enaium
 */
@RestController
@RequestMapping("/post-comment")
public class PostCommentController {

    @Autowired
    private PostCommentMapper postCommentMapper;
    @Autowired
    private PostMapper postMapper;

    @PostMapping("/comment")
    private Result<String> comment(@RequestBody PostCommentDTO postCommentDTO) {
        var post = postMapper.selectByPrimaryKey(postCommentDTO.getPostId());

        if (post == null) {
            return StringResult.fail("can not find this post");
        }

        var postComment = postCommentMapper.selectByPrimaryKey(postCommentDTO.getId());
        if (postComment != null) {
            if (postComment.getMemberId().equals(StpUtil.getLoginIdAsLong())) {
                postCommentMapper.updateByPrimaryKeySelective(new PostComment() {
                    {
                        setComment(postCommentDTO.getComment());
                        setEdited(true);
                        setCommentTime(new Date());
                    }
                });
            } else {
                return StringResult.fail("this comment is not your");
            }
        } else {
            postCommentMapper.insertSelective(new PostComment() {
                {
                    setPostId(postCommentDTO.getPostId());
                    setMemberId(StpUtil.getLoginIdAsLong());
                    setCommentTime(new Date());
                    setComment(postCommentDTO.getComment());
                }
            });
            postMapper.updateByPrimaryKeySelective(new Post() {
                {
                    setUpdateTime(new Date());
                }
            });
            return StringResult.fail("comment success");
        }
        return StringResult.fail("server error");
    }

    @PostMapping("/delete/{id}")
    private Result<String> delete(@PathVariable("id") Long id) {
        var postComment = postCommentMapper.selectByPrimaryKey(id);
        if (postComment == null) {
            return StringResult.fail("this comment is not exist");
        }

        if (!postComment.getMemberId().equals(StpUtil.getLoginIdAsLong())) {
            return StringResult.fail("this comment is not your");
        }

        postCommentMapper.updateByPrimaryKeySelective(new PostComment() {
            {
                setDeleted(true);
            }
        });

        return StringResult.fail("delete success");
    }

    @RequestMapping("/getByCommentTime/{id}/{start}/{end}")
    private Result<List<PostComment>> getByUpdate(@PathVariable("id") Long id, @PathVariable("start") Integer start, @PathVariable("end") Integer end) {
        return new Result<>(true, postCommentMapper.getByCommentTime(id, start, end));
    }
}
