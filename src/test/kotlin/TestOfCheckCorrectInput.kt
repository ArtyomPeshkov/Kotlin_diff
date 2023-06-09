import java.io.File
import kotlin.test.*

internal class TestOfCheckCorrectInput {
    //функция аналогична той, что используется в коде, но пользовательский ввод симулируется входными параметрами
    private fun checkCorrectInput(minVal: Int, maxVal: Int, commands: List<String?>): Int? {
        commands.forEach {
            if (it == null || it.toIntOrNull() == null || it.toInt() < minVal || it.toInt() > maxVal) {
               return@forEach
            }
            return it.toInt()
        }
        return null
    }

    @Test
    fun checkCorrectInputTestNull() {
        val commands:List<String?> = listOf(null,"afaf","","1","3",null)
        assertEquals(1, checkCorrectInput(0,10,commands))
    }

    @Test
    fun checkCorrectInputTestRange() {
        val commands:List<String?> = listOf(null,"afaf","","2","-1","3","0",null)
        assertEquals(0, checkCorrectInput(0,1,commands))
    }

    @Test
    fun checkCorrectInputTestNumsInWords() {
        val commands:List<String?> = listOf(null,"afaf","","1ewfe234","12fqwf","wqdf123","123","asff",null,"as2sf")
        assertEquals(123, checkCorrectInput(0,1000,commands))
    }

    @Test
    fun checkCorrectInputTestNoCorrectInput() {
        val commands:List<String?> = listOf(null,"afaf","","1ewfe234","12fqwf","wqdf123","asff",null,"as2sf")
        assertEquals(null, checkCorrectInput(0,1000,commands))
    }
}
