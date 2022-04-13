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

import cn.enaium.server.mapper.MemberInfoMapper;
import cn.enaium.server.mapper.MemberMapper;
import cn.enaium.server.mapper.MemberPermissionMapper;
import cn.enaium.server.model.dto.LoginDTO;
import cn.enaium.server.model.dto.RegisterDTO;
import cn.enaium.server.model.entity.Member;
import cn.enaium.server.model.entity.MemberInfo;
import cn.enaium.server.model.entity.MemberPermission;
import cn.enaium.server.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Random;

/**
 * @author Enaium
 */
@Service
public class MemberServiceImplement implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MemberInfoMapper memberInfoMapper;

    @Autowired
    private MemberPermissionMapper memberPermissionMapper;

    @Override
    public boolean hasUsername(String username) {
        return memberMapper.selectByUsername(username) != null;
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        var member = new Member(null, registerDTO.getUsername(), DigestUtils.md5DigestAsHex(registerDTO.getPassword().getBytes(StandardCharsets.UTF_8)), new Date());
        memberMapper.insertSelective(member);

        memberInfoMapper.insertSelective(new MemberInfo(member.getId(), null, null, null, null, "user_" + new Random().nextInt()));

        memberPermissionMapper.insertSelective(new MemberPermission(member.getId(), null, null));
    }

    @Override
    public long login(LoginDTO loginDTO) {
        loginDTO.setPassword(DigestUtils.md5DigestAsHex(loginDTO.getPassword().getBytes(StandardCharsets.UTF_8)));
        var member = memberMapper.selectByLogin(loginDTO);
        return member != null ? member.getId() : 0;
    }
}
