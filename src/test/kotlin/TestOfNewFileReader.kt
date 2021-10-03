import java.io.File
import kotlin.test.*

internal class TestOfNewFileReader {
    //функция аналогична той, что используется в коде, но пользовательский ввод симулируется входными параметрами
    private fun newFileReader(commands: List<String?>): String? {
        commands.forEach {
            when {
                (it == null || it.isBlank() || !File(it).exists() || !File(it).isFile) -> return@forEach
                (File(it).absoluteFile == File("src/main/resources/base.txt").absoluteFile || File(it).absoluteFile == File(
                    "src/test/resources/testResult.txt"
                ).absoluteFile || File(it).absoluteFile == File("src/test/resources/testBase.txt").absoluteFile) -> return@forEach
                else -> return it
            }
        }
        return null
    }

    @Test
    fun checkCorrectInputTest1() {
        val commands:List<String?> = listOf(null,"afaf","","1","3",null,"y")
        assertEquals(null, newFileReader(commands))
    }

    @Test
    fun checkCorrectInputTest2() {
        val commands:List<String?> = listOf(null,"src/main/resources/base.txt","","2","src/test/resources/testResult.txt","-1","3","0",null,"n","src/test/resources/testBase.txt")
        assertEquals(null, newFileReader(commands))
    }

    @Test
    fun checkCorrectInputTest3() {
        val commands:List<String?> = listOf(null,"src/main/resources/base.txt","","2","src/test/resources/testResult.txt","-1","3","0",null,"n","src/test/resources/testBase.txt","src/main/resources/file.txt")
        assertEquals("src/main/resources/file.txt", newFileReader(commands))
    }

    @Test
    fun checkCorrectInputTest4() {
        val commands:List<String?> = listOf(null,"afaf","","1ewfe234","12fqwf","No","wqdf123","123","asff",null,"as2sf","Y")
        assertEquals(null, newFileReader(commands))
    }

}
