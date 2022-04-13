package cn.enaium.application

import cn.enaium.application.util.Http
import org.junit.Assert.*
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun getCategory() {
        println(Http.string("category/get"))
    }

    @Test
    fun getPostByCategory() {
        println(Http.string("post/getByCategory/1"))
    }

    @Test
    fun getMemberInfo() {
        println(Http.string("memberInfo/get/1"))
    }
}