import java.io.File
import java.lang.Integer.max

//Константы для оформления текста
const val RESET: String = "\u001B[0m"
const val RED: String = "\u001B[31m"
const val GREEN: String = "\u001B[32m"
const val BLUE: String = "\u001B[34m"
const val SEPARATOR: String = "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"


//Класс хранящий индексы одинаковых строк файлов в алгоритме восстановления наибольшей общей подпоследовательности
data class RelevantElements(val firstIndex: Int, val secondIndex: Int)

// Алгоритм lcs применённый для файла, чтобы найти наибольшую общую часть двух файлов (работает аналогично LCS)

fun longestCommonGroupOfStrings(baseFile: File, resultFile: File): MutableList<RelevantElements> {
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
fun printer(baseFile: File, resultFile: File) {
    val baseStrings = baseFile.readLines()
    val resultStrings = resultFile.readLines()
    val res: MutableList<RelevantElements> =
        longestCommonGroupOfStrings(
            baseFile,
            resultFile
        ) // Массив позиций строк принадлежащих наибольшей общей подпоследовательности

    var curIndexBase = 0 //Индексация в исходном файле
    var curIndexResult = 0 //Индексация в итоговом файле
    var stringNumberWriter = 1 //Индексация в выводе (консоли)
    res.add(
        RelevantElements(
            baseStrings.size,
            resultStrings.size
        )
    ) // Добавляет несуществующий элемент, чтобы вывести изменения строк от последней совпадающей до конца файла
    println("$GREEN* means that string doesn't have to be changed")
    println("- means that string should be deleted")
    println("+ means that string should be added$RESET")
    res.forEach()
    { elem ->
        //Проход по исходному файлу от текущего индекса до ближайшей совпадающей строки
        for (i in curIndexBase until elem.firstIndex) {
            println("${stringNumberWriter++}.$RED -${baseStrings[i]}$RESET")
            if (curIndexResult <= elem.secondIndex - 1 && i == elem.firstIndex - 1) //если строки только удаляются и не добавляются, SEPARATOR не вызывается
                println(SEPARATOR)
            else if (i == elem.firstIndex - 1)
                println()
        }
        //После всех удалений возвращает индекс на положение, которое было до удаления строк
        stringNumberWriter -= elem.firstIndex - curIndexBase
        //Проход по итоговому файлу от текущего индекса до ближайшей совпадающей строки
        for (i in curIndexResult until elem.secondIndex) {
            println("${stringNumberWriter++}.$GREEN +${resultStrings[i]}$RESET")
            if (i == elem.secondIndex - 1)
                println()
        }
        //Вывод строки, которую не надо менять (в случае, если это не наша искусственная строка)
        if (elem.firstIndex != baseStrings.size) {
            println("${stringNumberWriter++}.$BLUE *${baseStrings[elem.firstIndex]}$RESET")
            println()
        }

        curIndexBase = elem.firstIndex + 1
        curIndexResult = elem.secondIndex + 1
    }
    res.clear()
}

fun megaTester() {
    repeat(100)
    {
        var strNumber = (1..1000).random() // количество строк
        val difChars = (1..5).random() // количество различных символов
        val baseFile = File("src/main/resources/testBase.txt")
        val resultFile = File("src/main/resources/testResult.txt")

        val newFileB = mutableListOf<String>()//генерация нового исходного файла
        repeat(strNumber)
        {
            newFileB.add(getRandomString(difChars, (0..10).random()))
        }
        baseFile.writeText("")
        repeat(strNumber)
        {
            baseFile.appendText(newFileB[it] + "\n")
        }

        strNumber = (1..1000).random()
        val newFileR = mutableListOf<String>()//генерация нового итогового файла
        repeat(strNumber)
        {
            newFileR.add(getRandomString(difChars, (0..10).random()))
        }
        resultFile.writeText("")
        repeat(strNumber)
        {
            resultFile.appendText(newFileR[it] + "\n")
        }

        //Вывод теста, который упал (если таковой есть)
        if (!test(baseFile, resultFile)) {
            println(RED + "Test failed" + RESET)
            println()
            newFileB.forEach {
                println(it)
            }
            println()
            newFileR.forEach {
                println(it)
            }
            println()
            printer(baseFile, resultFile)
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
            (it[0] == '-') -> baseStrings.removeAt(ind)
            (it[0] == '*') -> ind++
            (it[0] == '+') -> {
                baseStrings.add(ind, it.substring(1 until it.length));ind++
            }
        }
    }
    return baseStrings == resultStrings
}

// Генерирует команды для тестирующей системы (работает аналогично printer, но ничего не выводит)
fun stringForTestingSystemGenerator(
    baseFile: File,
    resultFile: File,
    testStrSequence: MutableList<String>
) {
    val baseStrings = baseFile.readLines()
    val resultStrings = resultFile.readLines()
    val res: MutableList<RelevantElements> = longestCommonGroupOfStrings(baseFile, resultFile)

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

//Функция реализующая взаимодействие с пользователем
fun conversationWithUser() {
    while (true) {
        val baseFile = File("src/main/resources/base.txt")
        val resultFile = File("src/main/resources/result.txt")
        println(GREEN + "Command list:")
        println(BLUE + "Write '${RED}ch$BLUE' if you want to change input or output files")
        println("Write '${RED}run$BLUE' if you want to run utility")
        println("Write '${RED}run t$BLUE' if you want to run testing system")
        println("Write '${RED}q$BLUE' if you want to quit")
        var s: String
        while (true) {
            s = readLine().toString()
            when (s) {
                "ch", "Ch", "CH" -> {
                    changer(baseFile, resultFile);break // changer будет описан далее
                }
                "RUN", "Run", "run" -> {
                    printer(baseFile, resultFile);break // printer описан выше
                }
                "RUN T", "Run t", "run t" -> {
                    megaTester(); break // megaTester описан выше
                }
                "q", "Q" -> return
                else -> println("${RED}Unknown command")
            }
        }
    }
}

//Алгоритм меняющий содержимое файлов в зависимости от требований пользователя
fun changer(baseFile: File, resultFile: File): Int {
    while (true) {
        println(BLUE + "Choose file to change (write '${RED}original$BLUE' or '${RED}result$BLUE') or print '${RED}q$BLUE' to quit")
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
                else -> println(RED + "Please, write 'original' or 'result'")
            }
        }

        println(BLUE + "If you want to fill your file with random values, write '${RED}rnd$BLUE', if you want to fill it by yourself, write '${RED}my str$BLUE'")
        println("If you want to print your file, write '${RED}print$BLUE', if you want to quite, write '${RED}q$BLUE'" + RESET)
        randomAndUserInput(file) // описан далее
    }
}

fun randomAndUserInput(
    file: File
) {
    while (true) {
        when (readLine().toString()) {
            "rnd", "Rnd", "RND" -> {
                randomChanger(file) // Меняет файл рандомно
                break
            }
            "my str", "My str", "MY STR" -> {
                userChanger(file) //Меняет файл по запросам пользователя
                break
            }
            "print", "Print", "PRINT" -> {
                file.readLines().forEach() {
                    println(it)
                }
                println()
                break
            }
            "q", "Q" -> break
            else -> println(RED + "Please, write 'rnd', 'my str', 'print', 'q'" + RESET)
        }

    }
}

fun randomChanger(file: File) {
    println(GREEN + "Write number of strings in your file (from 0 to 10000):")
    var strNumber = readLine()
    while (strNumber?.toIntOrNull() == null || strNumber.toInt() < 0 || strNumber.toInt() > 10000) {
        println(RED + "Write correct number")
        strNumber = readLine()
    }

    println(GREEN + "Write length of strings in your file (from 0 to 1000):")
    var strLength = readLine()
    while (strLength?.toIntOrNull() == null || strLength.toInt() < 0 || strLength.toInt() > 1000) {
        println(RED + "Write correct number")
        strLength = readLine()
    }

    println(GREEN + "Write how many different symbols you want to have in your string (from 1 to 26)")
    var difChars = readLine()
    while (difChars?.toIntOrNull() == null || difChars.toInt() > 26 || difChars.toInt() < 1) {
        println(RED + "Write a number between 1 and 26")
        difChars = readLine()
    }

    val newFile = mutableListOf(getRandomString(difChars.toInt(), strLength.toInt())) // getRandomString описан далее
    repeat(strNumber.toInt() - 1)
    {
        newFile.add(getRandomString(difChars.toInt(), strLength.toInt()))
    }

    file.writeText("")
    newFile.forEach {
        file.appendText(it + "\n")
    }
    println(RED + "Random file generated")
    newFile.clear()
}

// Алгоритм генерирующий рандомную строку
fun getRandomString(difChars: Int, length: Int): String {
    if (length == 0)
        return ""
    val allowedChars = ('a' until 'a' + difChars)
    return (1..length).map { allowedChars.random() }.joinToString("")
}

//Обрабатывает запросы пользователя по изменению файла
fun userChanger(file: File) {
    val changingFile = file.readLines().toMutableList()
    changingFile.forEachIndexed { index, str ->
        println("${(index + 1).toString().padEnd(changingFile.size.toString().length)} $str")
    }
    println()
    println(GREEN + "You can change strings, add new, delete some, clear the file, print it or quite")
    while (true) {
        println(BLUE + "Write 'change', 'add', 'del', 'clear', 'print' or 'q'" + RESET)
        when (readLine().toString()) {
            "Change", "change", "CHANGE" -> {
                while (true) {
                    println(GREEN + "Write number of strings you want to change or q to quite")
                    println("There are$RED ${changingFile.size}$GREEN strings in your file")
                    var strNumber = readLine()
                    while (!(strNumber == "q" || strNumber == "Q" || strNumber!!.toIntOrNull() != null && strNumber.toInt() >= 1 && strNumber.toInt() <= changingFile.size)) {
                        println(RED + "Write correct number or 'q'")
                        strNumber = readLine()
                    }
                    if (strNumber == "q" || strNumber == "Q")
                        break
                    println(GREEN + "Write new string:")
                    val newStr = readLine()
                    if (newStr != null)
                        changingFile[strNumber.toInt() - 1] = newStr
                    else
                        changingFile[strNumber.toInt() - 1] = ""
                }
            }
            "Add", "add", "ADD" -> {
                while (true) {
                    println(GREEN + "Write number of strings you want to add or q to quite")
                    println("There are$RED ${changingFile.size}$GREEN strings in your file")
                    var strNumber = readLine()
                    while (!(strNumber == "q" || strNumber == "Q" || strNumber!!.toIntOrNull() != null && strNumber.toInt() >= 1 && strNumber.toInt() <= changingFile.size + 1)) {
                        println(RED + "Write correct number or 'q'" + RESET)
                        strNumber = readLine()
                    }
                    if (strNumber == "q" || strNumber == "Q")
                        break
                    println(GREEN + "Write new string:")
                    val newStr = readLine()
                    if (newStr != null)
                        changingFile.add(strNumber.toInt() - 1, newStr )
                    else
                        changingFile.add(strNumber.toInt() - 1, "")
                }
            }
            "Del", "del", "DEL" -> {
                while (true) {
                    println(GREEN + "Write number of string you want to delete or 'q' to quite")
                    println("There are$RED ${changingFile.size}$GREEN strings in your file")
                    var strNumber = readLine()
                    while (!(strNumber == "q" || strNumber == "Q" || strNumber!!.toIntOrNull() != null && strNumber.toInt() >= 1 && strNumber.toInt() <= changingFile.size)) {
                        println(RED + "Write correct number or 'q'")
                        strNumber = readLine()
                    }
                    if (strNumber == "q" || strNumber == "Q")
                        break
                    changingFile.removeAt(strNumber.toInt() - 1)
                }
            }
            "Clear", "clear", "CLEAR" -> {
                changingFile.clear()
                println(GREEN + "File was cleared" + RESET)
            }
            "Print", "print", "PRINT" -> {
                if (changingFile.isEmpty()) println(RED + "Your file is empty")
                changingFile.forEachIndexed { index, str ->
                    println("${(index + 1).toString().padEnd(changingFile.size.toString().length)} $str")
                }
            }
            "q", "Q" -> {
                file.writeText("")
                changingFile.forEach {
                    file.appendText(it + "\n")
                }
                return
            }
            else -> println(RED + "Unknown command")
        }
    }
}

fun main() {
    println(GREEN + "Welcome to diff utility testing system")
    conversationWithUser()
}
