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
import cn.enaium.server.mapper.FullMemberInfoMapper;
import cn.enaium.server.mapper.MemberInfoMapper;
import cn.enaium.server.model.entity.MemberInfo;
import cn.enaium.server.model.result.MemberInfoResult;
import cn.enaium.server.model.result.Result;
import cn.enaium.server.model.result.StringResult;
import cn.enaium.server.model.vo.FullMemberInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Enaium
 */
@RestController
@RequestMapping("/memberInfo")
public class MemberInfoController {

    @Autowired
    private MemberInfoMapper memberInfoMapper;

    @Autowired
    private FullMemberInfoMapper fullMemberInfoMapper;

    @RequestMapping("/get/{id}")
    private Result<MemberInfo> get(@PathVariable("id") Long id) {
        var memberInfo = memberInfoMapper.selectByPrimaryKey(id);
        if (memberInfo != null) {
            return MemberInfoResult.success(memberInfo);
        }
        return MemberInfoResult.fail(null);
    }

    @RequestMapping("/getFull/{id}")
    private Result<FullMemberInfo> getFullInfo(@PathVariable("id") Long id) {
        return new Result<>(true, fullMemberInfoMapper.selectByPrimaryKey(id));
    }

    @PostMapping("/update")
    private Result<String> update(@RequestBody MemberInfo memberInfo) {
        memberInfo.setId(StpUtil.getLoginIdAsLong());
        memberInfoMapper.updateByPrimaryKeySelective(memberInfo);
        return StringResult.success("Update success");
    }
}

