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
import cn.enaium.server.mapper.CategoryMapper;
import cn.enaium.server.mapper.PostMapper;
import cn.enaium.server.model.dto.PostDTO;
import cn.enaium.server.model.entity.Post;
import cn.enaium.server.model.result.*;
import cn.enaium.server.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Enaium
 */
@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private PostMapper postMapper;

    @RequestMapping("/new-post")
    private Result<String> post(@RequestBody PostDTO postDTO) {
        if (!(StringUtils.hasText(postDTO.getTitle()) || StringUtils.hasText(postDTO.getContent()))) {
            return StringResult.fail("empty title or content");
        }

        if (categoryMapper.selectByPrimaryKey(postDTO.getCategory()) == null) {
            return StringResult.fail("category not exists");
        }

        if (postDTO.getId() != null) {
            if (!postMapper.selectByPrimaryKey(postDTO.getId()).getFrom().equals(StpUtil.getLoginIdAsLong())) {
                return StringResult.fail("this is not your post");
            }
        }

        postService.post(postDTO);

        return StringResult.success("post success");
    }

    @RequestMapping("/getById/{id}")
    private Result<Object> getById(@PathVariable("id") Long id) {
        var post = postMapper.selectByPrimaryKey(id);
        if (post == null) {
            return ObjectResult.fail("Not Found");
        }
        return ObjectResult.success(post);
    }

    @RequestMapping("/getByCategory/{category}")
    private Result<List<Post>> getByCategory(@PathVariable("category") Integer category) {
        return new Result<>(true, postMapper.selectByCategory(category));
    }

    @RequestMapping("/getByUpdate/{start}/{end}")
    private Result<List<Post>> getByUpdate(@PathVariable("start") Integer start, @PathVariable("end") Integer end) {
        return new Result<>(true, postMapper.selectByUpdateTime(start, end));
    }

    @RequestMapping("/getCount")
    private Result<Long> getCount() {
        return LongResult.success(postMapper.getCount());
    }
}
