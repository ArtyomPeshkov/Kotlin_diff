import java.io.File
import java.lang.Integer.max

//Константы для оформления текста
const val RESET: String = "\u001B[0m"
const val RED: String = "\u001B[31m"
const val GREEN: String = "\u001B[32m"
const val BLUE: String = "\u001B[34m"
const val SEPARATOR: String = "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"

//Исходный и итоговый файлы
val baseFile = File("src\\main\\resources\\base.txt")
val resultFile = File("src\\main\\resources\\result.txt")

//Класс хранящий индексы одинаковых строк файлов в алгоритме восстановления наибольшей общей подпоследовательности
data class RelevantElements(val firstIndex: Int, val secondIndex: Int)

// Алгоритм lcs применённый для файла, чтобы найти наибольшую общую часть двух файлов (работает аналогично LCS)

fun longestCommonGroupOfStrings(baseFile: File,resultFile: File): MutableList<RelevantElements> {
    val baseText = baseFile.readLines()
    val resultText = resultFile.readLines()
    val lengths =
        Array(baseText.size + 1) { IntArray(resultText.size + 1) } // Массив, с помощью которого на основе меньших подстрок подсчитываются большие
    for (i in baseText.size downTo 0)
        for (j in resultText.size downTo 0) {
            if (i == baseText.size || j == resultText.size) lengths[i][j] = 0
            else if (baseText[i] == resultText[j]) lengths[i][j] = 1 + lengths[i + 1][j + 1]
            else lengths[i][j] = max(lengths[i + 1][j], lengths[i][j + 1])
        }
    return longestCommonPartRecovery(baseText, resultText, lengths)

}

//Алгоритм восстановления индексов на которых находятся соответственные строки, принадлежащие общей подпоследовательности
fun longestCommonPartRecovery(
    baseText: List<String>,
    resultText: List<String>,
    lengths: Array<IntArray>
): MutableList<RelevantElements> {
    val res = mutableListOf<RelevantElements>()
    var i = 0
    var j = 0
    while (i < baseText.size && j < resultText.size)
    // Алгоритм находит точку в которой буквы совпали и сохраняет её координаты
        if (baseText[i] == resultText[j]) {
            res.add(RelevantElements(i, j))
            i++
            j++
        } else if (lengths[i + 1][j] >= lengths[i][j + 1]) i++
        else j++
    return res
}

// Алгоритм, работающий для вывода различий файлов или тестирования корректности работы утилиты
// Алгоритм считает расстояния между соседними одинаковыми строками, которые получает из lcs для файлов, и выводит строки, которые надо убрать, оставить без изменений или добавить
fun printer(baseFile:File,resultFile: File) {
    val baseStrings = baseFile.readLines()
    val resultStrings = resultFile.readLines()
    val res: MutableList<RelevantElements> =
        longestCommonGroupOfStrings(baseFile,resultFile) // Массив позиций строк принадлежащих наибольшей общей подпоследовательности

    var curIndexBase = 0 //Индексация в исходном файле
    var curIndexResult = 0 //Индексация в итоговом файле
    var stringNumberWriter = 1 //Индексация в выводе (консоли)
    res.add(
        RelevantElements(
            baseStrings.size,
            resultStrings.size
        )
    ) // Добавляет несуществующий элемент, чтобы вывести изменения строк от последней совпадающей до конца файла
    repeat(res.size)
    {
        //Проход по исходному файлу от текущего индекса до ближайшей совпадающей строки
        for (i in curIndexBase until res[it].firstIndex) {
            println("${stringNumberWriter++}.$RED -${baseStrings[i]}$RESET")
            if (curIndexResult <= res[it].secondIndex - 1 && i == res[it].firstIndex - 1) //если строки только удаляются и не добавляются, SEPARATOR не вызывается
                println(SEPARATOR)
            else
                println()
        }
        //После всех удалений возвращает индекс на положение, которое было до удаления строк
        stringNumberWriter -= res[it].firstIndex - curIndexBase
        //Проход по итоговому файлу от текущего индекса до ближайшей совпадающей строки
        for (i in curIndexResult until res[it].secondIndex) {
            println("${stringNumberWriter++}.$GREEN +${resultStrings[i]}$RESET")
            if (i == res[it].secondIndex - 1)
                println()
        }
        //Вывод строки, которую не надо менять (в случае, если это не наша искусственная строка)
        if (res[it].firstIndex != baseStrings.size) {
            println("${stringNumberWriter++}.$BLUE *${baseStrings[res[it].firstIndex]}$RESET")
            println()
        }

        curIndexBase = res[it].firstIndex + 1
        curIndexResult = res[it].secondIndex + 1
    }
    res.clear()
}


fun testHelper(
    baseFile: File,
    resultFile: File,
    testStrSequence: MutableList<String>
) {
    val baseStrings = baseFile.readLines()
    val resultStrings = resultFile.readLines()
    val res: MutableList<RelevantElements> = longestCommonGroupOfStrings(baseFile,resultFile)

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

// Алгоритм тестирующий корректность работы алгоритма
fun test(baseFile:File,resultFile:File): Boolean {
    val baseStrings = baseFile.readLines().toMutableList()
    val resultStrings = resultFile.readLines().toMutableList()
    val testStrSequence: MutableList<String> = mutableListOf("")
    var ind = 0
    testHelper(baseFile,resultFile,testStrSequence)
    repeat(testStrSequence.size)
    {
        when {
            (testStrSequence[it][0] == '-') -> baseStrings.removeAt(ind)
            (testStrSequence[it][0] == '*') -> ind++
            (testStrSequence[it][0] == '+') -> {
                baseStrings.add(ind, testStrSequence[it].substring(1 until testStrSequence[it].length));ind++
            }
        }
    }
    return baseStrings == resultStrings
}

fun megaTester(){
   repeat(1000)
   {
       val strNumber = (1..100).random()
       val strLength = (1..10).random()
       val difChars = (1..5).random()

       val newFileB = mutableListOf<String>()
       repeat(strNumber)
       {
           newFileB.add(getRandomString(difChars, strLength))
       }
       File("testBase.txt").writeText("")
       repeat(strNumber)
       {
           File("testBase.txt").appendText(newFileB[it] + "\n")
       }
       val newFileR = mutableListOf<String>()
       repeat(strNumber)
       {
           newFileR.add(getRandomString(difChars, strLength))
       }
       File("testResult.txt").writeText("")
       repeat(strNumber)
       {
           File("testResult.txt").appendText(newFileR[it] + "\n")
       }
       if (!test(File("testBase.txt"),File("testResult.txt")))
       {
           println(RED + "Test failed" + RESET)
           println()
           newFileB.forEach(){
               println(it)
           }
           println()
           newFileR.forEach(){
               println(it)
           }
           println()
           printer(File("testBase.txt"),File("testResult.txt"))
           return
       }
       newFileB.clear()
       newFileR.clear()
   }
    println(GREEN + "All tests passed" + RESET)
}

//Функция реализующая взаимодействие с пользователем
fun conversationWithUser() {
    while (true) {
        println("${GREEN}Command list:${RESET}")
        println("${BLUE}Write 'ch' if you want to change input or output files${RESET}")
        println("${BLUE}Write 'run' if you want to run utility${RESET}")
        println("${BLUE}Write 'run t' if you want to run testing system${RESET}")
        println("${BLUE}Write 'q' if you want to quit${RESET}")
        var s: String
        while (true) {
            s = readLine().toString()
            when (s) {
                "ch", "Ch", "CH" -> {
                    changer();break // changer описан далее
                }
                "RUN", "Run", "run" -> {
                    printer(baseFile,resultFile);break
                }
                "RUN T", "Run t", "run t" -> {
                    megaTester()
                }
                "q", "Q" -> return
                else -> println("${RED}Unknown command${RESET}")
            }
        }
    }
}

fun randomChanger(file: File) {
    println("${GREEN}Write number of strings in your file:${RESET}")
    var strNumber = readLine()
    while (strNumber?.toIntOrNull() == null) {
        println("${RED}Write a number, without any symbols, except for numbers from 0 to 9${RESET}")
        strNumber = readLine()
    }

    println("${GREEN}Write length of strings in your file:${RESET}")
    var strLength = readLine()
    while (strLength?.toIntOrNull() == null) {
        println("${RED}Write a number, without any symbols, except for numbers from 0 to 9${RESET}")
        strLength = readLine()
    }

    println("${GREEN}Write how many different symbols you want to have in your string (from 1 to 26)${RESET}")
    var difChars = readLine()
    while (difChars?.toIntOrNull() == null || difChars.toInt() > 26 || difChars.toInt() < 1) {
        println("${RED}Write a number between 1 and 26${RESET}")
        difChars = readLine()
    }

    val newFile = mutableListOf(
        getRandomString(
            difChars.toInt(),
            strLength.toInt()
        )
    ) // getRandomString описан далее
    repeat(strNumber.toInt() - 1)
    {
        newFile.add(getRandomString(difChars.toInt(), strLength.toInt()))
    }

    file.writeText("")
    repeat(strNumber.toInt())
    {
        file.appendText(newFile[it] + "\n")
    }
    println("${RED}Random file generated${RESET}")
    newFile.clear()
}

// Алгоритм генерирующий рандомную строку
fun getRandomString(difChars: Int, length: Int): String {
    val allowedChars = ('a' until 'a' + difChars)
    return (1..length).map { allowedChars.random() }.joinToString("")
}

fun userChanger(file: File) {
    val changingFile = file.readLines().toMutableList()
    changingFile.forEachIndexed() { index, str ->
        println("${(index + 1).toString().padEnd(changingFile.size.toString().length)} $str")
    }
    println()
    println("You can change strings, add new, delete some, clear the file, print it or quite")
    while (true) {
        println("Write 'change', 'add', 'del', 'clear', 'print' or 'q'")
        var choice = readLine().toString()
        when (choice) {
            "Change", "change", "CHANGE" -> {
                while (true) {
                    println("${GREEN}Write number of strings you want to change or q to quite${RESET}")
                    println("${GREEN}There are ${RED}${changingFile.size}${GREEN} strings in your file${RESET}")
                    var strNumber = readLine()
                    while (!(strNumber == "q" || strNumber == "Q" || strNumber!!.toIntOrNull() != null && strNumber.toInt() >= 1 && strNumber.toInt() <= changingFile.size)) {
                        println("${RED}Write correct number or 'q'${RESET}")
                        strNumber = readLine()
                    }
                    if (strNumber == "q" || strNumber == "Q")
                        break
                    println("${GREEN}Write new string:${RESET}")
                    val newStr = readLine()
                    if (newStr != null)
                        changingFile[strNumber.toInt() - 1] = newStr
                    else
                        changingFile[strNumber.toInt() - 1] = ""
                }
            }
            "Add", "add", "ADD" -> {
                while (true) {
                    println("${GREEN}Write number of strings you want to add or q to quite${RESET}")
                    println("${GREEN}There are ${RED}${changingFile.size}${GREEN} strings in your file${RESET}")
                    var strNumber = readLine()
                    while (!(strNumber == "q" || strNumber == "Q" || strNumber!!.toIntOrNull() != null && strNumber.toInt() >= 1 && strNumber.toInt() <= changingFile.size+1)) {
                        println("${RED}Write correct number or 'q'${RESET}")
                        strNumber = readLine()
                    }
                    if (strNumber == "q" || strNumber == "Q")
                        break
                    println("${GREEN}Write new string:${RESET}")
                    val newStr = readLine()
                    if (newStr != null)
                        changingFile.add(strNumber.toInt() - 1, newStr)
                    else
                        changingFile.add(strNumber.toInt() - 1, "")
                }
            }
            "Del", "del", "DEL" -> {
                while (true) {
                    println(GREEN+ "Write number of string you want to delete or 'q' to quite" + RESET)
                    println(GREEN + "There are$RED ${changingFile.size}$GREEN strings in your file" + RESET)
                    var strNumber = readLine()
                    while (!(strNumber == "q" || strNumber == "Q" || strNumber!!.toIntOrNull() != null && strNumber.toInt() >= 1 && strNumber.toInt() <= changingFile.size)) {
                        println("${RED}Write correct number or 'q'${RESET}")
                        strNumber = readLine()
                    }
                    if (strNumber == "q" || strNumber == "Q")
                        break
                    changingFile.removeAt(strNumber.toInt() - 1)
                }
            }
            "Clear", "clear", "CLEAR" -> {
                changingFile.clear()
                println("$GREEN File was cleared $RESET")
            }
            "Print", "print", "PRINT" -> {
                if (changingFile.isEmpty()) println("Your file is empty")
                changingFile.forEachIndexed() { index, str ->
                    println("${(index + 1).toString().padEnd(changingFile.size.toString().length)} $str")
                }
            }
            "q", "Q" -> return
            else -> println("${RED}Unknown command${RESET}")
        }
    }
}

fun randomAndUserInput(
    file: File
) {
    while (true) {
        var choice = readLine().toString()
        when (choice) {
            "rnd", "Rnd", "RND" -> {
                randomChanger(file)
                break
            }
            "my str", "My str", "MY STR" -> {
                userChanger(file)
                break
            }
            else -> println("${RED}Please, write 'rnd' or 'my str'${RESET}")
        }

    }
}

//Алгоритм меняющий содержимое файлов в зависимости от требований пользователя
fun changer(): Int {
    while (true) {
        println("${BLUE}Choose file to change (write 'original' or 'result') or print 'q' to quit${RESET}")
        var s: String
        var file: File
        while (true) {
            s = readLine().toString()
            when (s) {
                "original", "Original", "ORIGINAL" -> {
                    file = baseFile;break
                }
                "result", "Result", "RESULT" -> {
                    file = resultFile;break
                }
                "q", "Q" -> return 0
                else -> println("Please, write 'original' or 'result' ")
            }
        }

        println("${BLUE}If you want to fill your file with random values, write 'rnd', if you want to fill by yourself, write 'my str'${RESET}")
        randomAndUserInput(file)
    }
}

fun main() {
    println("Welcome to diff utility testing system")
    conversationWithUser()
}
