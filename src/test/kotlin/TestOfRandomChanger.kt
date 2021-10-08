import java.io.File
import kotlin.test.*

internal class TestOfRandomChanger {
    private val baseFile=File("src/test/resources/testBase.txt")
    private val resultFile=File("src/test/resources/testResult.txt")

    //функция аналогична той, что используется в коде, но пользовательский ввод симулируется входными параметрами
    private fun randomChangerForTesting(baseFile: File, resultFile: File, strNumber:Int, strLength:Int, difChars:Int) {
        rewriteFile(resultFile.readLines(), baseFile)
        val newFile:MutableList<String> = mutableListOf()
        repeat(strNumber )
        {
            newFile.add(getRandomString(difChars, strLength))
        }
        rewriteFile(newFile, resultFile)
        newFile.clear()
    }

    @Test
    fun randomChangerTestBigRange() {
        val strNumber=10000
        var strNumberF=false
        val strLength=1000
        var strLengthF=true
        val difChars=26
        var difCharsF=true
        randomChangerForTesting(baseFile,resultFile,strNumber,strLength,difChars)
        strNumberF = resultFile.readLines().size==strNumber
        resultFile.forEachLine { str ->
            if (str.length != strLength)
                strLengthF=false
            val check: HashSet<Char> = hashSetOf()
            str.forEach { chr->
                check.add(chr)
            }
            if (check.size > difChars)
                difCharsF=false
        }
        assert(strNumberF && strLengthF && difCharsF)
    }

    @Test
    fun randomChangerTestSmallRange() {
        val strNumber=10
        var strNumberF=false
        val strLength=10
        var strLengthF=true
        val difChars=3
        var difCharsF=true
        randomChangerForTesting(baseFile,resultFile,strNumber,strLength,difChars)
        strNumberF = resultFile.readLines().size==strNumber
        resultFile.forEachLine { str ->
            if (str.length != strLength)
                strLengthF=false
            val check: HashSet<Char> = hashSetOf()
            str.forEach { chr->
                check.add(chr)
            }
            if (check.size > difChars)
                difCharsF=false
        }
        assert(strNumberF && strLengthF && difCharsF)
    }
    @Test
    fun randomChangerTestZeroStrings() {
        val strNumber=0
        var strNumberF=false
        val strLength=1000
        var strLengthF=true
        val difChars=3000
        var difCharsF=true
        randomChangerForTesting(baseFile,resultFile,strNumber,strLength,difChars)
        println(resultFile.readLines().size)
        strNumberF = resultFile.readLines().size==strNumber
        resultFile.forEachLine { str ->
            if (str.length != strLength)
                strLengthF=false
            val check: HashSet<Char> = hashSetOf()
            str.forEach { chr->
                check.add(chr)
            }
            if (check.size > difChars)
                difCharsF=false
        }
        assert(strNumberF && strLengthF && difCharsF)
    }
    @Test
    fun randomChangerTestZeroLengthStrings() {
        val strNumber=100
        var strNumberF=false
        val strLength=0
        var strLengthF=true
        val difChars=1000000
        var difCharsF=true
        randomChangerForTesting(baseFile,resultFile,strNumber,strLength,difChars)
        strNumberF = resultFile.readLines().size==strNumber
        resultFile.forEachLine { str ->
            if (str.length != strLength)
                strLengthF=false
            val check: HashSet<Char> = hashSetOf()
            str.forEach { chr->
                check.add(chr)
            }
            if (check.size > difChars)
                difCharsF=false
        }
        assert(strNumberF && strLengthF && difCharsF)
    }
}
