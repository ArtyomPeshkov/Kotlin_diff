import java.io.File
import java.lang.Integer.max

const val RESET: String = "\u001B[0m"
const val RED: String = "\u001B[31m"
const val GREEN: String = "\u001B[32m"
const val BLUE: String = "\u001B[34m"
const val SEPARATOR: String = "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"

val baseFile = File("src\\main\\resources\\base.txt")
val resultFile = File("src\\main\\resources\\result.txt")

data class RelevantElements(val firstIndex: Int, val secondIndex: Int)

// Алгоритм lcs применённый для файла, чтобы найти наибольшую общую часть двух файлов (работает аналогично LCS)

fun longestCommonGroupOfStrings(): MutableList<RelevantElements> {
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
    // Алгоритм находит точку в которой буквы совпали и сохраняет её координты
        if (baseText[i] == resultText[j]) {
            res.add(RelevantElements(i, j))
            i++
            j++
        } else if (lengths[i + 1][j] >= lengths[i][j + 1]) i++
        else j++
    return res
}

// Алгоритм, работающий для вывода различий файлов или тестирования корректности работы утилиты
// Алгоритм считает расстояния между соседними одинаковыми строками, которые получает из lcs для файлов и выводит строки, которые надо убрать, оставить без изменений или добавить
fun printer(
    outputOrTestFlag: Boolean = true,
    testStrSequence: MutableList<String> = mutableListOf("")
) {
    val baseStrings = baseFile.readLines()
    val resultStrings = resultFile.readLines()
    val res: MutableList<RelevantElements> = longestCommonGroupOfStrings()

    var curIndexBase = 0 //Индексация в исходном файле
    var curIndexResult = 0 //Индексация в итоговом файле
    var stringNumberWriter = 1 //Индексация в выводе (консоли)
    testStrSequence.clear()
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
            // outputOrTestFlag указывает на то в каком режиме запущена функция (режим вывода или режим тестирования)
            if (outputOrTestFlag) {
                println("${stringNumberWriter}.$RED -${baseStrings[i]}$RESET")
                stringNumberWriter++
                if (i == res[it].firstIndex - 1 && curIndexResult <= res[it].secondIndex - 1 && outputOrTestFlag) //если строки только удаляются и не добавляются, SEPARATOR не вызывается
                    println(SEPARATOR)
                else if (i == res[it].firstIndex - 1 && outputOrTestFlag)
                    println()
            } else
                testStrSequence.add("-${baseStrings[i]}")
        }
        //После всех удалений возвращает индекс на положение, которое было до удаления строк
        stringNumberWriter -= res[it].firstIndex - curIndexBase
        //Проход по итоговому файлу от текущего индекса до ближайшей совпадающей строки
        for (i in curIndexResult until res[it].secondIndex) {
            if (outputOrTestFlag) {
                println("${stringNumberWriter}.$GREEN +${resultStrings[i]}$RESET")
                stringNumberWriter++
                if (i == res[it].secondIndex - 1 && outputOrTestFlag)
                    println()
            } else
                testStrSequence.add("+${resultStrings[i]}")
        }
        //Вывод строки, которую не надо менять (в случае, если это не наша искусственная строка)
        if (!(res[it].firstIndex == baseStrings.size && res[it].secondIndex == resultStrings.size)) {
            if (outputOrTestFlag) {
                println("${stringNumberWriter}.$BLUE *${baseStrings[res[it].firstIndex]}$RESET")
                stringNumberWriter++
                println()
            } else
                testStrSequence.add("*${baseStrings[res[it].firstIndex]}")
        }

        curIndexBase = res[it].firstIndex + 1
        curIndexResult = res[it].secondIndex + 1
    }

}
//Функция реализующая взаимодействие с пользователем
fun conversationWithUser(): Int {
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
                    printer();break // printer в режиме вывода
                }
                "RUN T", "Run t", "run t" -> {
                    // printer в режиме тестирования
                    if (test())
                        println("${GREEN}Test passed${RESET}")
                    else
                        println("${RED}Test failed${RESET}")
                    break
                }
                "q", "Q" -> return 0
                else -> println("${RED}Unknown command${RESET}")
            }
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

        while (true) {
            s = readLine().toString()
            when (s) {
                "rnd", "Rnd", "RND" -> {

                    println("${GREEN}Write number of strings in your file:${RESET}")
                    var strNumber = readLine()
                    while (strNumber?.toIntOrNull() == null) {
                        println("${RED}Write a number, without any symbols, excpet for numbers from 0 to 9${RESET}");
                        strNumber = readLine();
                    }

                    println("${GREEN}Write length of strings in your file:${RESET}")
                    var strLength = readLine()
                    while (strLength?.toIntOrNull() == null) {
                        println("${RED}Write a number, without any symbols, excpet for numbers from 0 to 9${RESET}");
                        strLength = readLine();
                    }

                    val newFile = mutableListOf(getRandomString(strLength.toInt())) // getRandomString описан далее
                    repeat(strNumber.toInt() - 1)
                    {
                        newFile.add(getRandomString(strLength.toInt()))
                    }

                    file.writeText("")
                    repeat(strNumber.toInt())
                    {
                        file.appendText(newFile[it] + "\n")
                    }
                    println("${RED}Random file generated${RESET}")
                    break
                }
                /* "my str", "My str", "MY STR" -> {
                     for (i in 0..file.readLines().toMutableList().size)
                         println("${i + 1}. ${file.readLines().toMutableList()[i]}")

                     println("You can change string, add new one, or delete one")
                     println("Write 'c n 'yourString'' (n is string number) to change string")
                     println("Write 'd n 'yourString'' (n is string number) to delete string")
                     println("Write 'a n 'yourString'' (n is string number) to add string")
                 }*/
                else -> println("${RED}Please, write 'ran' or 'my str'${RESET}")
            }
        }
    }
}

// Алгоритм генерирующий рандомную строку
fun getRandomString(length: Int): String {
    val allowedChars = ('a'..'z')
    return (1..length).map { allowedChars.random() }.joinToString("")
}

// Алгоритм тестирующий корректность работы алгоритма
fun test(): Boolean {
    val baseStrings = baseFile.readLines().toMutableList()
    val resultStrings = resultFile.readLines().toMutableList()
    val testStrSequence: MutableList<String> = mutableListOf("")
    var ind = 0
    printer(false, testStrSequence)
  /*  repeat(testStrSequence.size)
    {
        println(testStrSequence[it])
    }*/
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

fun main() {


    println("Welcome to diff utility testing system")
    conversationWithUser();

    //Lcs test zone
/*
   var first = "abcd"
   var second = "ckldr"

   println(lcsLength(first,second))
*/


//------------------------------------------------------

    //LittoralCombatShip test zone

/*
    val baseStrings: List<String> = baseFile.readLines()
    val resultStrings: List<String> = resultFile.readLines()
    val res: MutableList<RelevantElements> = littoralCombatShip(baseStrings, resultStrings)

      repeat(res.size)
      {
          println("${res[it].firstIndex + 1} ${res[it].secondIndex + 1}")
      }*/
    //printer(baseStrings, resultStrings, res/*littoralCombatShip(baseStrings, resultStrings) */)
    //System.out.flush()
    /* repeat(baseStrings.size)
     {
        println(baseStrings[it])
     }
     repeat(resultStrings.size)
     {
        println(resultStrings[it])
     }
     val lengths=Array(baseStrings.size+1) { IntArray(resultStrings.size+1) }
     println(littoralCombatShip(baseStrings,resultStrings))*/


//------------------------------------------------------

    // stringComparator test zone
/*
   var first = "abcdefghijk"
   var second = "abcdefghijk"
   stringComparator(first,second)
*/
    //------------------------------------------------------

    // Помощь в тестировании утилиты
    /*val example = "1234567890"
    baseFile.writeText("$example\n")
    repeat(9) {
       baseFile.appendText("${it+1} "+"$example\n")
    }
    val arr = baseFile.readLines()
    for (i in arr) {
       println(i); }
    val bufferedReader = BufferedReader(InputStreamReader(baseFile.inputStream()))
    bufferedReader.readLine()
    bufferedReader.readLine()
   println(bufferedReader.readLine())*/
}
