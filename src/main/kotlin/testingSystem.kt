import java.io.File


/**
@brief
 Функция, запускающая 1000 рандомных тестов для нашей утилиты.
@detailed
 В теле функции генерируется рандомное количество строк от 1 до 100, рандомное количество различных символов в строке от 1 до 5
 и
 */
fun megaTester() {
    repeat(1000)
    {
        var strNumber = (50..100).random() // количество строк
        val difChars = (1..3).random() // количество различных символов
        val baseFile = File("src/test/resources/testBase.txt")
        val resultFile = File("src/test/resources/testResult.txt")

        val newFileB = mutableListOf<String>()//генерация нового исходного файла
        repeat(strNumber)
        {
            newFileB.add(getRandomString(difChars,3 /*(0..10).random()*/))
        }
        rewriteFile(newFileB,baseFile)

        strNumber = (50..100).random()
        val newFileR = mutableListOf<String>()//генерация нового итогового файла
        repeat(strNumber)
        {
            newFileR.add(getRandomString(difChars,3 /*(0..10).random()*/))
        }
        rewriteFile(newFileR,resultFile)

        //Вывод теста, который упал (если таковой есть)
        if (!test(baseFile,resultFile))
        {
            println(RED + "Test failed" + RESET)
            println(RED + "Please send this test to ${GREEN}artemyipeshkov@gmail.com" + RESET)
            println()
            newFileB.forEach {
                println(it)
            }
            println()
            newFileR.forEach {
                println(it)
            }
            println()
            printer(baseFile,resultFile)
            return
        }
        newFileB.clear()
        newFileR.clear()
    }
    println(GREEN + "All tests passed" + RESET)
}

// Алгоритм тестирующий корректность работы алгоритма
// Симулирует предлагаемые программой изменения на массивах, совпадающих с файлами по содержанию и проверяет стали ли они равны
fun test(baseFile: File, resultFile: File): Boolean {
    val baseStrings = baseFile.readLines().toMutableList()
    val resultStrings = resultFile.readLines().toMutableList()
    val testStrSequence: MutableList<String> = mutableListOf("")
    var ind = 0
    stringForTestingSystemGenerator(baseFile, resultFile, testStrSequence)
    testStrSequence.forEach()
    {
        when {
            (it.first() == '-') -> baseStrings.removeAt(ind)
            (it.first() == '*') -> ind++
            (it.first() == '+') -> {
                baseStrings.add(ind, it.substring(1 until it.length));ind++
            }
        }
    }
    return baseStrings == resultStrings
}

// Генерирует команды для тестирующей системы (работает аналогично printer, но ничего не выводит)
fun stringForTestingSystemGenerator(baseFile: File,
                                    resultFile: File,
                                    testStrSequence: MutableList<String>
) {
    val baseStrings = baseFile.readLines()
    val resultStrings = resultFile.readLines()
    val res: MutableList<RelevantElements> = longestCommonSubsequenceOfStrings(baseFile,resultFile)

    var curIndexBase = 0
    var curIndexResult = 0
    testStrSequence.clear()
    res.add(
        RelevantElements(baseStrings.size, resultStrings.size)
    )
    repeat(res.size)
    {

        for (i in curIndexBase until res[it].firstIndex)
            testStrSequence.add("-${baseStrings[i]}")

        for (i in curIndexResult until res[it].secondIndex)
            testStrSequence.add("+${resultStrings[i]}")

        if (!(res[it].firstIndex == baseStrings.size && res[it].secondIndex == resultStrings.size))
            testStrSequence.add("*${baseStrings[res[it].firstIndex]}")

        curIndexBase = res[it].firstIndex + 1
        curIndexResult = res[it].secondIndex + 1
    }
    res.clear()
}
