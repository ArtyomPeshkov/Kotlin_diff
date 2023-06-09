import java.io.File


/**
@brief
 Функция, запускающая 1000 рандомных тестов для нашей утилиты.
@detailed
 В теле функции генерируется рандомное количество строк от 1 до 100 для каждого файла, рандомное количество различных символов в строке от 1 до 3
 и для каждой строки рандомная длина от 1 до 10 после чего для этих файлов применяется test, которая проверяет корректность работы diff для этих файлов
 Если на каких-то файлах тест "падает", то информация о файлах выводится на экран
 */
fun megaTester() {
    repeat(1000)
    {
        var strNumber = (1..100).random() // количество строк
        val difChars = (1..3).random() // количество различных символов
        val baseFile = File("src/test/resources/testBase.txt")
        val resultFile = File("src/test/resources/testResult.txt")

        val newFileB = mutableListOf<String>()//генерация нового исходного файла
        repeat(strNumber)
        {
            newFileB.add(getRandomString(difChars,(0..10).random()))
        }
        rewriteFile(newFileB,baseFile)

        strNumber = (1..100).random()
        val newFileR = mutableListOf<String>()//генерация нового итогового файла
        repeat(strNumber)
        {
            newFileR.add(getRandomString(difChars,(0..10).random()))
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


/**
@brief
Алгоритм тестирующий корректность работы утилиты.
@detailed
Алгоритм симулирует предлагаемые программой изменения на массивах, совпадающих с файлами по содержанию и проверяет, стали ли они равны
 после применения предлагаемых действий. Если они стали равны, то утилита работает корректно.
@param
Функция принимает файл до изменения и после.
 @return
 Прошёл тест или упал
 */
fun test(baseFile: File, resultFile: File): Boolean {
    val baseStrings = baseFile.readLines().toMutableList()
    val resultStrings = resultFile.readLines().toMutableList()
    var ind = 0
    val testStrSequence = stringForTestingSystemGenerator(baseFile, resultFile)
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

/**
@brief
Генератор команд для тестирующего алгоритма.
@detailed
Генерирует команды для тестирующей системы, делает он это аналогично тому, как printer выводит результат работы,
но вместо вывода он сохраняет строки с соответствующими им командами
@param
Функция принимает файл до изменения и после.
@return
Массив индексов строк лежащих на соответственных позициях в наибольшей общей подпоследовательности
 */
fun stringForTestingSystemGenerator(baseFile: File,
                                    resultFile: File
): MutableList<String> {
    val baseStrings = baseFile.readLines()
    val resultStrings = resultFile.readLines()
    val res: MutableList<RelevantElements> = longestCommonSubsequenceOfStrings(baseFile,resultFile)
    val testStrSequence: MutableList<String> = mutableListOf()

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
    return testStrSequence
}
