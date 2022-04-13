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
import cn.enaium.server.model.dto.LoginDTO;
import cn.enaium.server.model.dto.RegisterDTO;
import cn.enaium.server.model.result.*;
import cn.enaium.server.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author Enaium
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @RequestMapping("/register")
    private Result<String> register(@RequestBody RegisterDTO registerDTO) {

        var username = registerDTO.getUsername();

        if (username.length() < 3 || username.length() > 10) {
            return StringResult.fail("Please input the right length!");
        }

        if (memberService.hasUsername(username)) {
            return StringResult.fail("Username is already exists!");
        }

        if (!Objects.equals(registerDTO.getPassword(), registerDTO.getCheckPassword())) {
            return StringResult.fail("Two inputs don't match!");
        }

        if (!(StringUtils.hasText(registerDTO.getUsername()) || StringUtils.hasText(registerDTO.getPassword()))) {
            return StringResult.fail("empty username or password");
        }

        memberService.register(registerDTO);


        return StringResult.success("Register Success!");
    }

    @RequestMapping("/login")
    private Result<String> login(@RequestBody LoginDTO loginDTO) {
        var id = memberService.login(loginDTO);
        if (id == 0) {
            return StringResult.fail("wrong username or password!");
        }
        StpUtil.login(id);
        return StringResult.success(StpUtil.getTokenValue());
    }

    @RequestMapping("/isLogin")
    private Result<Boolean> isLogin() {
        return BooleanResult.success(StpUtil.isLogin());
    }

    @RequestMapping("/getId")
    private Result<Long> getInfo() {
        return LongResult.success(StpUtil.getLoginIdAsLong());
    }
}
