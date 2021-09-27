import java.io.File
import kotlin.test.*

internal class TestOfUserChanger {
    private val baseFile=File("src/test/resources/testBase.txt")
    private val resultFile=File("src/test/resources/testResult.txt")

    //функция аналогична той, что используется в коде, но пользовательский ввод симулируется входными параметрами
    private fun userChangerForTesting(baseFile: File, resultFile: File,commands:List<String>, nums:List<Int>,strings:List<String>) {
        val saveFile = resultFile.readLines().toMutableList()
        val changingFile = resultFile.readLines().toMutableList()
        commands.forEachIndexed  {index,it ->
            when (it) {
                "change" -> {
                        if (strings[index] == "q" || strings[index] == "Q")
                            return
                         changingFile[nums[index]] = strings[index]
                }
                "add" -> {
                    if (strings[index] == "q" || strings[index] == "Q")
                        return
                    changingFile.add(nums[index],  strings[index])
                    }
                "del" -> {
                    if (strings[index] == "q" || strings[index] == "Q")
                        return
                    changingFile.removeAt(nums[index])
                }
                "clear" -> changingFile.clear()
                "print" -> {/*не тестируется*/}
                "q" -> {
                    rewriteFile(changingFile, resultFile)
                    if (saveFile != changingFile)
                        rewriteFile(saveFile, baseFile)
                    return@forEachIndexed
                }
            }
        }
    }

    @Test
    fun userChangerTest1() {
        rewriteFile(listOf("abc","def","klm","nop"),resultFile)
        val res = listOf("aaa","klm","def","nop")
        val commands:List<String> = listOf("change","add","del","q")
        val nums:List<Int> = listOf(0,3,1,-1)
        val strings:List<String> = listOf("aaa","def","del","q")
        userChangerForTesting(baseFile,resultFile,commands,nums,strings)
        assert(res==resultFile.readLines())
    }

    @Test
    fun userChangerTest2() {
        rewriteFile(listOf("abc","def","klm","nop"),resultFile)
        val res:List<String> = listOf()
        val commands:List<String> = listOf("clear","q")
        val nums:List<Int> = listOf(-1,-1)
        val strings:List<String> = listOf("clear","q")
        userChangerForTesting(baseFile,resultFile,commands,nums,strings)
        assert(res==resultFile.readLines())
    }
    @Test
    fun userChangerTest3() {
        rewriteFile(listOf("111","111","111","111"),resultFile)
        val res = listOf("111","aaa","111","ddd","111","kkk","111","nnn")
        val commands:List<String> = listOf("add","change","add","change","add","change","add","change","q")
        val nums:List<Int> = listOf(1,1,3,3,5,5,7,7,-1)
        val strings:List<String> = listOf("abc","aaa","def","ddd","klm","kkk","nop","nnn")
        userChangerForTesting(baseFile,resultFile,commands,nums,strings)
        assert(res==resultFile.readLines())
    }
    @Test
    fun userChangerTest4() {
        rewriteFile(listOf("111","111","111","111"),resultFile)
        val res = listOf("000","aaa","222","ddd","444","kkk","666","nnn")
        val commands:List<String> = listOf("change","add","change","add","change","add","change","add","q")
        val nums:List<Int> = listOf(0,1,2,3,4,5,6,7,-1)
        val strings:List<String> = listOf("000","aaa","222","ddd","444","kkk","666","nnn")
        userChangerForTesting(baseFile,resultFile,commands,nums,strings)
        assert(res==resultFile.readLines())
    }
    @Test
    fun userChangerTest5() {
        rewriteFile(listOf("","111","","111","","111","","111"),resultFile)
        val res = listOf("000","111","222","333")
        val commands:List<String> = listOf("change","del","change","del","change","del","change","del","q")
        val nums:List<Int> = listOf(0,1,1,2,2,3,3,4,-1)
        val strings:List<String> = listOf("000","del","111","del","222","del","333","del")
        userChangerForTesting(baseFile,resultFile,commands,nums,strings)
        assert(res==resultFile.readLines())
    }
    @Test
    fun userChangerTest6() {
        rewriteFile(listOf("abc","def","klm","nop"),resultFile)
        val res = listOf("aaa","bbb","ccc")
        val commands:List<String> = listOf("clear","add","del","add","add","add","q")
        val nums:List<Int> = listOf(-1,0,0,0,1,2,-1)
        val strings:List<String> = listOf("clear","aaa","del","aaa","bbb","ccc","q")
        userChangerForTesting(baseFile,resultFile,commands,nums,strings)
        assert(res==resultFile.readLines())
    }
    @Test
    fun userChangerTest7() {
        rewriteFile(listOf("abc","def","klm","nop"),resultFile)
        val res = listOf("000","aaa","bbb","ccc")
        val commands:List<String> = listOf("clear","add","change","add","add","add","q")
        val nums:List<Int> = listOf(-1,0,0,1,2,3,-1)
        val strings:List<String> = listOf("clear","aaa","000","aaa","bbb","ccc","q")
        userChangerForTesting(baseFile,resultFile,commands,nums,strings)
        assert(res==resultFile.readLines())
    }
    @Test
    fun userChangerTest8() {
        rewriteFile(listOf("abc","def","klm","nop"),resultFile)
        val res = listOf("aaa","bbb","abc","def","klm","nop")
        val commands:List<String> = listOf("add","change","add","q","add","add")
        val nums:List<Int> = listOf(0,0,1,-1,2,3)
        val strings:List<String> = listOf("000","aaa","bbb","q","ccc","ddd")
        userChangerForTesting(baseFile,resultFile,commands,nums,strings)
        assert(res==resultFile.readLines())
    }
    @Test
    fun userChangerTest9() {
        rewriteFile(listOf("abc","def","klm","nop"),resultFile)
        val res = listOf("","","")
        val commands:List<String> = listOf("change","change","change","del","q")
        val nums:List<Int> = listOf(0,1,3,2,-1)
        val strings:List<String> = listOf("","","","del","q")
        userChangerForTesting(baseFile,resultFile,commands,nums,strings)
        assert(res==resultFile.readLines())
    }
}
