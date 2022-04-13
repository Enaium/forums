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

package cn.enaium.server.service.implement;

import cn.dev33.satoken.stp.StpUtil;
import cn.enaium.server.mapper.PostMapper;
import cn.enaium.server.model.dto.PostDTO;
import cn.enaium.server.model.entity.Post;
import cn.enaium.server.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Enaium
 */
@Service
public class PostServiceImplement implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Override
    public void post(PostDTO postDTO) {
        if (postDTO.getId() != null) {
            if (postMapper.selectByPrimaryKey(postDTO.getId()) != null) {
                postMapper.updateByPrimaryKeySelective(new Post(postDTO.getId(), postDTO.getTitle(), postDTO.getContent(), postDTO.getCategory(), null, null, new Date()));
            }
        } else {
            postMapper.insertSelective(new Post(null, postDTO.getTitle(), postDTO.getContent(), postDTO.getCategory(), new Date(), StpUtil.getLoginIdAsLong(), new Date()));
        }
    }
}
