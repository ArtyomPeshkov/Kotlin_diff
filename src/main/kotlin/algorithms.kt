import java.io.File

// Алгоритм lcs применённый для файла, чтобы найти наибольшую общую часть двух файлов (работает аналогично LCS)
/**
 @brief Функция поиска длины наибольшей общей подпоследовательности двух файлов
 @detailed Функция генерирует двумерный массив размера m*n, где m и n - это размеры
 файла до изменения и после, после чего для каждой позиции таблицы указывает наибольшую общую часть файлов, которую
 можно найти двигаясь с этих позиций до конца файла.
 @param Функция принимает файл до изменения и после.
 @return Функция возвращает массив пар индексов. Каждая пара - индексы строки принадлежащей наибольшей общей подпоследовательности
 в файле до изменения и после.
 */
fun longestCommonGroupOfStrings(baseFile: File,resultFile: File): MutableList<RelevantElements> {
    val baseText = baseFile.readLines()
    val resultText = resultFile.readLines()
    // Массив, с помощью которого на основе меньших подстрок подсчитываются большие
    val lengths =
        Array(baseText.size + 1) { IntArray(resultText.size + 1) }
    for (i in baseText.size downTo 0)
        for (j in resultText.size downTo 0) {
            if (i == baseText.size || j == resultText.size) lengths[i][j] = 0
            else if (baseText[i] == resultText[j]) lengths[i][j] = 1 + lengths[i + 1][j + 1]
            else lengths[i][j] = Integer.max(lengths[i + 1][j], lengths[i][j + 1])
        }
    return longestCommonPartRecovery(baseText, resultText, lengths)

}

/**
@brief Функция восстановления наибольшей общей подпоследовательности двух файлов. Вызывается только из функции 'longestCommonGroupOfStrings'.
@detailed Функция двигается по двумерному массиву сгенерированному в 'longestCommonGroupOfStrings'. Движение происходит следующим образом:
функция двигается к индексам, в которых строки совпали. При этом, если в одной из таких точек длина наибольшей общей части файлов от этого момента
до конца файлов равна n, пока алгоритм не найдёт хотя бы 1 точку в которой эта длина равна n-1, он не перейдёт к точке длина от которой равна n-2
@param Функция принимает файл до изменения и после и двумерный массив размера m*n, где m и n - это размеры
файла до изменения и после (массив получен из 'longestCommonGroupOfStrings').
@return Функция возвращает массив пар индексов. Каждая пара - индексы строки принадлежащей наибольшей общей подпоследовательности
в файле до изменения и после.
 */
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

/**
@brief Функция выводящая результат работы утилиты.
@detailed
@param Функция принимает файл до изменения и после и двумерный массив размера m*n, где m и n - это размеры
файла до изменения и после (массив получен из 'longestCommonGroupOfStrings').
@return Функция возвращает массив пар индексов. Каждая пара - индексы строки принадлежащей наибольшей общей подпоследовательности
в файле до изменения и после.
 */
// Алгоритм, работающий для вывода различий файлов или тестирования корректности работы утилиты
// Алгоритм считает расстояния между соседними одинаковыми строками, которые получает из lcs для файлов, и выводит строки, которые надо убрать, оставить без изменений или добавить
fun printer(baseFile: File,resultFile: File, outputNotOnlyChanged:Boolean = true) {
    val baseStrings = baseFile.readLines()
    val resultStrings = resultFile.readLines()
    // Массив позиций строк принадлежащих наибольшей общей подпоследовательности
    val res: MutableList<RelevantElements> =
        longestCommonGroupOfStrings(baseFile,
            resultFile
        )

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
    println()
    fun printAddedOrDeleted(valueFrom:Int, valueTo:Int, fileStrings: List<String>, plusMinus:Char, color:String, flagForSeparator:Boolean=false){
        for (i in valueFrom until valueTo) {
            println("${stringNumberWriter++}.$color $plusMinus${fileStrings[i]}$RESET")
            if (flagForSeparator && i == valueTo - 1) //если строки только удаляются и не добавляются, SEPARATOR не вызывается
                println(SEPARATOR)
            else if (i == valueTo - 1)
                println()
        }
    }
    res.forEach()
    { elem ->
        //Проход по исходному файлу от текущего индекса до ближайшей совпадающей строки
        printAddedOrDeleted(curIndexBase,elem.firstIndex,baseStrings,'-',RED,curIndexResult <= elem.secondIndex - 1)
     /*   for (i in curIndexBase until elem.firstIndex) {
            println("${stringNumberWriter++}.$RED -${baseStrings[i]}$RESET")
            if (curIndexResult <= elem.secondIndex - 1 && i == elem.firstIndex - 1) //если строки только удаляются и не добавляются, SEPARATOR не вызывается
                println(SEPARATOR)
            else if (i == elem.firstIndex - 1)
                println()
        }*/
        //После всех удалений возвращает индекс на положение, которое было до удаления строк
        stringNumberWriter -= elem.firstIndex - curIndexBase
        //Проход по итоговому файлу от текущего индекса до ближайшей совпадающей строки
        printAddedOrDeleted(curIndexResult,elem.secondIndex,resultStrings,'+',GREEN)
       /* for (i in curIndexResult until elem.secondIndex) {
            println("${stringNumberWriter++}.$GREEN +${resultStrings[i]}$RESET")
            if (i == elem.secondIndex - 1)
                println()
        }*/
        //Вывод строки, которую не надо менять (в случае, если это не наша искусственная строка)
        if (elem.firstIndex != baseStrings.size && outputNotOnlyChanged) {
            println("${stringNumberWriter++}.$BLUE *${baseStrings[elem.firstIndex]}$RESET")
            println()
        } else stringNumberWriter++

        curIndexBase = elem.firstIndex + 1
        curIndexResult = elem.secondIndex + 1
    }
    res.clear()
}