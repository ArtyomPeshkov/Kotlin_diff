import java.io.File
import kotlin.test.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.BeforeTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals


internal class TestOfPrinter {
    private val standardOut = System.out
    private val standardIn = System.`in`
    private val stream = ByteArrayOutputStream()

    @BeforeTest
    fun setUp() {
        System.setOut(PrintStream(stream))
    }

    @AfterTest
    fun tearDown() {
        System.setOut(standardOut)
        System.setIn(standardIn)
    }

    private val baseFile = File("src/test/resources/testBase.txt")
    private val resultFile = File("src/test/resources/testResult.txt")

    @Test
    fun printer1() {
        var input = mutableListOf("abc", "def", "klm", "abc", "sss")
        rewriteFile(input, baseFile)
        input = mutableListOf("aaa", "abc", "aaa", "dek", "klm", "ccc", "asf", "abc")
        rewriteFile(input, resultFile)
        printer(baseFile, resultFile)
        assertEquals(
            """
            $GREEN* means that string doesn't have to be changed
            - means that string should be deleted
            + means that string should be added$RESET
            1.$GREEN +aaa$RESET
            
            2.$BLUE *abc$RESET
            
            3.$RED -def$RESET
            $SEPARATOR
            3.$GREEN +aaa$RESET
            4.$GREEN +dek$RESET
            
            5.$BLUE *klm$RESET
            
            6.$GREEN +ccc$RESET
            7.$GREEN +asf$RESET
            
            8.$BLUE *abc$RESET
            
            9.$RED -sss$RESET
        """.trimIndent(), stream.toString().trimIndent().trim()
        )
    }

    @Test
    fun printer2() {
        var input = mutableListOf("", "", "", "", "")
        rewriteFile(input, baseFile)
        input = mutableListOf("", "", "")
        rewriteFile(input, resultFile)
        printer(baseFile, resultFile)
        assertEquals(
            """
            $GREEN* means that string doesn't have to be changed
            - means that string should be deleted
            + means that string should be added$RESET
            1.$BLUE *$RESET
            
            2.$BLUE *$RESET
            
            3.$BLUE *$RESET
           
            4.$RED -$RESET
            5.$RED -$RESET
        """.trimIndent(), stream.toString().trimIndent().trim()
        )
    }

    @Test
    fun printer3() {
        var input = mutableListOf("abc", "", "klm", "bbb", "", "ccc")
        rewriteFile(input, baseFile)
        input = mutableListOf("aaa", "", "abc", "", "bbb", "def", "ccc", "", "klm", "asf")
        rewriteFile(input, resultFile)
        printer(baseFile, resultFile)
        assertEquals(
            """
            $GREEN* means that string doesn't have to be changed
            - means that string should be deleted
            + means that string should be added$RESET
            1.$GREEN +aaa$RESET
            2.$GREEN +$RESET

            3.$BLUE *abc$RESET

            4.$BLUE *$RESET

            5.$RED -klm$RESET

            5.$BLUE *bbb$RESET

            6.$RED -$RESET
            <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            6.$GREEN +def$RESET

            7.$BLUE *ccc$RESET

            8.$GREEN +$RESET
            9.$GREEN +klm$RESET
            10.$GREEN +asf$RESET
        """.trimIndent(), stream.toString().trimIndent().trim()
        )
    }
}