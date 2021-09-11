import java.io.File
import java.lang.Integer.max

/*fun getFileLines(filename: String): List<String> {
   return File(filename).readLines()
}*/
const val RESET: String = "\u001B[0m"
const val RED: String = "\u001B[31m"
const val GREEN: String = "\u001B[32m"
const val BLUE: String = "\u001B[34m"
const val SEPARATOR: String = "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"

data class RelevantElements(val firstIndex: Int, val secondIndex: Int)

//Функция сравнивающая строк на основе lcs с возвращением значения, показывающего на какой суммарный процент совпали строки
fun stringComparator(first: String, second: String, requiredMatch: Double = 2.0/*max()*/): Boolean {
    val lcsLength: Double = lcsLength(first, second)
    return (lcsLength / first.length + lcsLength / second.length) >= requiredMatch
}
// Длина наибольшей общей подпоследовательности
fun lcsLength(first: String, second: String): Double {
    val lengths = Array(first.length + 1) { IntArray(second.length + 1) }
    // Пустые строки равны
    if (first.isEmpty() && second.isEmpty())
        return 2.0
    // Нахождение LCS с запоминанием в массиве lengths (чтобы программа использовала полученные ранее значения для более длинных подстрок)
    for (i in first.length downTo 0)
        for (j in second.length downTo 0) {
            if (i == first.length || j == second.length) lengths[i][j] = 0
            else if (first[i] == second[j]) lengths[i][j] = 1 + lengths[i + 1][j + 1]
            else lengths[i][j] = max(lengths[i + 1][j], lengths[i][j + 1])
        }
    //Возвращает значение от 0.0 до 2.0 (смысл описан в stringComparator)
    return lengths[0][0].toDouble()
}
// Алгоритм lcs применённый для файла, чтобы найти наибольшую общую часть двух файлов (работает аналогично LCS)
fun /*longestCommonGroupOfStrings*/littoralCombatShip(
    baseText: List<String>,
    resultText: List<String>,
    requiredMatch: Double = 2.0
): MutableList<RelevantElements> {
    val lengths = Array(baseText.size + 1) { IntArray(resultText.size + 1) }
    for (i in baseText.size downTo 0)
        for (j in resultText.size downTo 0) {
            if (i == baseText.size || j == resultText.size) lengths[i][j] = 0
            else if (stringComparator(baseText[i], resultText[j])) lengths[i][j] = 1 + lengths[i + 1][j + 1] //В данном случае алгоритм пытается находить строки, которые были образованы друг от друга, а не только полностью совпадающие
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
        if (baseText[i] == resultText[j]) {
            res.add(RelevantElements(i, j))
            i++
            j++
        } else if (lengths[i + 1][j] >= lengths[i][j + 1]) i++
        else j++
    return res
}
// Алгоритм, показывающий последовательность действий, которую необходимо произвести, чтобы превратить один файл в другой
fun printer(baseStrings: List<String>, resultStrings: List<String>,res: MutableList<RelevantElements>) {
    var curIndexBase = 0
    var curIndexResult = 0
    var stringNumberWriter = 1
    res.add(RelevantElements(baseStrings.size,resultStrings.size))
    repeat(res.size)
    {
        for (i in curIndexBase until res[it].firstIndex) {
            println("${stringNumberWriter}.$RED -${baseStrings[i]}$RESET")
            stringNumberWriter++
            if (i == res[it].firstIndex - 1 && curIndexResult <= res[it].secondIndex - 1) //если строки только удаляются и не добавляются, SEPARATOR не вызывается
                println(SEPARATOR)
            else if (i==res[it].firstIndex-1)
                println()
        }
        stringNumberWriter-=res[it].firstIndex-curIndexBase
        for (i in curIndexResult until res[it].secondIndex) {
            println("${stringNumberWriter}.$GREEN +${resultStrings[i]}$RESET")
            stringNumberWriter++
            if (i==res[it].secondIndex-1)
                println()
        }
        if (!(res[it].firstIndex==baseStrings.size && res[it].secondIndex==resultStrings.size)) {
        println("${stringNumberWriter}.$BLUE *${baseStrings[res[it].firstIndex]}$RESET")
            stringNumberWriter++
            println()
        }

        curIndexBase = res[it].firstIndex + 1
        curIndexResult = res[it].secondIndex + 1
    }

}

fun main() {

    //Lcs test zone
/*
   var first = "abcd"
   var second = "ckldr"

   println(lcsLength(first,second))
*/


//------------------------------------------------------

    //LittoralCombatShip test zone

    val baseFile = File("src\\main\\resources\\base.txt")
    val resultFile = File("src\\main\\resources\\result.txt")

    val baseStrings: List<String> = baseFile.readLines()
    val resultStrings: List<String> = resultFile.readLines()
    val res: MutableList<RelevantElements> = littoralCombatShip(baseStrings, resultStrings)
    repeat(res.size)
    {
        println("${res[it].firstIndex + 1} ${res[it].secondIndex + 1}")
    }
    printer(baseStrings, resultStrings, res/*littoralCombatShip(baseStrings, resultStrings) */)

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

//fun test
