import java.io.File
import kotlin.test.*

internal class TestOfSimpleAnswerFun {
    //функция аналогична той, что используется в коде, но пользовательский ввод симулируется входными параметрами
    private fun simpleAnswerForQuestion(commands: List<String?>): Boolean? {
        commands.forEach {
            when (it) {
                "Y", "y", "Yes", "yes", "YES" -> return true
                "N", "n", "No", "no", "NO" -> return false
                else -> return@forEach
            }
        }
        return null
    }


    @Test
    fun checkCorrectInputTest1() {
        val commands:List<String?> = listOf(null,"afaf","","1","3",null,"y")
        assertEquals(true, simpleAnswerForQuestion(commands))
    }

    @Test
    fun checkCorrectInputTest2() {
        val commands:List<String?> = listOf(null,"afaf","","2","-1","3","0",null,"n")
        assertEquals(false, simpleAnswerForQuestion(commands))
    }

    @Test
    fun checkCorrectInputTest3() {
        val commands:List<String?> = listOf(null,"afaf","","1ewfe234","12fqwf","wqdf123","123","asff",null,"as2sf","Y")
        assertEquals(true, simpleAnswerForQuestion(commands))
    }

    @Test
    fun checkCorrectInputTest4() {
        val commands:List<String?> = listOf(null,"afaf","","1ewfe234","12fqwf","No","wqdf123","123","asff",null,"as2sf","Y")
        assertEquals(false, simpleAnswerForQuestion(commands))
    }

    @Test
    fun checkCorrectInputTest5() {
        val commands:List<String?> = listOf(null,"afaf","","1ewfe234","12fqwf","wqdf123","YES","123","asff",null,"as2sf","Y")
        assertEquals(true, simpleAnswerForQuestion(commands))
    }
    @Test
    fun checkCorrectInputTest6() {
        val commands:List<String?> = listOf(null,"afaf","","1ewfe234","12fqwf","wqdf123","NO","123","asff",null,"as2sf","Y")
        assertEquals(false, simpleAnswerForQuestion(commands))
    }
    @Test
    fun checkCorrectInputTest7() {
        val commands:List<String?> = listOf(null,"afaf","","1ewfe234","12fqwf","wqdf123","123","asff",null,"as2sf")
        assertEquals(null, simpleAnswerForQuestion(commands))
    }
}
