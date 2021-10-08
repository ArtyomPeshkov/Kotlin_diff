import java.io.File
import kotlin.test.*

internal class TestOfLCSPart {
    private val baseFile=File("src/test/resources/testBase.txt")
    private val resultFile=File("src/test/resources/testResult.txt")

    @Test
    fun lcsAndRecoveryAlgoBasic() {
        var input= mutableListOf("abc","def","klm","abc")
        rewriteFile(input,baseFile)
        input= mutableListOf("aaa","abc","aaa","dek","klm","ccc","asf","abc","sss")
        rewriteFile(input,resultFile)
        assertEquals(mutableListOf(RelevantElements(0,1),RelevantElements(2,4),RelevantElements(3,7)),longestCommonSubsequenceOfStrings(baseFile,resultFile))
    }
    @Test
    fun lcsAndRecoveryAlgoBiggerBasic() {
        var input= mutableListOf("abc","def","klm","aaa","bbb","ccc","ddd")
        rewriteFile(input,baseFile)
        input= mutableListOf("aaa","abc","bbb","dek","klm","ccc","asf","abc","sss","ddd")
        rewriteFile(input,resultFile)
        assertEquals(mutableListOf(RelevantElements(3,0),RelevantElements(4,2),RelevantElements(5,5),RelevantElements(6,9)),longestCommonSubsequenceOfStrings(baseFile,resultFile))
    }
    @Test
    fun lcsAndRecoveryAlgoNotOneCorrectAns() {
        var input= mutableListOf("abc","def","klm","aaa","bbb","ccc")
        rewriteFile(input,baseFile)
        input= mutableListOf("aaa","abc","bbb","abc","def","ccc","klm","asf")
        rewriteFile(input,resultFile)
        assertEquals(mutableListOf(RelevantElements(3,0),RelevantElements(4,2),RelevantElements(5,5)),longestCommonSubsequenceOfStrings(baseFile,resultFile))
    }
    @Test
    fun lcsAndRecoveryAlgoNotOnlyEmpty() {
        var input= mutableListOf("abc","","klm","bbb","","ccc")
        rewriteFile(input,baseFile)
        input= mutableListOf("aaa","","abc","","bbb","def","ccc","","klm","asf")
        rewriteFile(input,resultFile)
        assertEquals(mutableListOf(RelevantElements(0, 2), RelevantElements(1, 3), RelevantElements(3, 4), RelevantElements(5, 6)),longestCommonSubsequenceOfStrings(baseFile,resultFile))
    }
    @Test
    fun lcsAndRecoveryAlgoEmptyFile() {
        var input= mutableListOf<String>()
        rewriteFile(input,baseFile)
        input= mutableListOf("")
        rewriteFile(input,resultFile)
        assertEquals(mutableListOf(),longestCommonSubsequenceOfStrings(baseFile,resultFile))
    }
    @Test
    fun lcsAndRecoveryAlgoOnlyEmptyStrings() {
        var input= mutableListOf("")
        rewriteFile(input,baseFile)
        input= mutableListOf("","","")
        rewriteFile(input,resultFile)
        assertEquals(mutableListOf(RelevantElements(0,0)),longestCommonSubsequenceOfStrings(baseFile,resultFile))
    }
}
