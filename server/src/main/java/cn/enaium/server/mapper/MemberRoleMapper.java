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

package cn.enaium.server.mapper;

import cn.enaium.server.model.entity.MemberRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Enaium
 */
@Mapper
public interface MemberRoleMapper {
    /**
    * deleteByPrimaryKey
    * @param id id
    * @return int int
    */
    int deleteByPrimaryKey(Byte id);

    /**
    * insert
    * @param row row
    * @return int int
    */
    int insert(MemberRole row);

    /**
    * insertSelective
    * @param row row
    * @return int int
    */
    int insertSelective(MemberRole row);

    /**
    * selectByPrimaryKey
    * @param id id
    * @return MemberRole MemberRole
    */
    MemberRole selectByPrimaryKey(Byte id);

    /**
    * updateByPrimaryKeySelective
    * @param row row
    * @return int int
    */
    int updateByPrimaryKeySelective(MemberRole row);

    /**
    * updateByPrimaryKey
    * @param row row
    * @return int int
    */
    int updateByPrimaryKey(MemberRole row);
}