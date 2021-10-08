import java.io.File

/**
 @brief
 Функция поиска длины наибольшей общей подпоследовательности двух файлов
 @detailed
 Функция генерирует двумерный массив размера m*n, где m и n - это размеры
 файла до изменения и после, после чего для каждой позиции таблицы указывает наибольшую общую часть файлов, которую
 можно найти двигаясь с этих позиций до конца файла.
 @param
 Функция принимает файл до изменения и после.
 @return
 Функция возвращает массив пар индексов. Каждая пара - индексы строки принадлежащей наибольшей общей подпоследовательности
 в файле до изменения и после.
 */
fun longestCommonSubsequenceOfStrings(baseFile: File, resultFile: File): MutableList<RelevantElements> {
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
@brief
Функция восстановления наибольшей общей подпоследовательности двух файлов. Вызывается только из функции 'longestCommonSubsequenceOfStrings'.
@detailed
Функция двигается по двумерному массиву сгенерированному в 'longestCommonSubsequenceOfStrings'. Движение происходит следующим образом:
функция двигается к индексам, в которых строки совпали. При этом, если в одной из таких точек длина наибольшей общей части файлов от этого момента
до конца файлов равна n, пока алгоритм не найдёт хотя бы 1 точку в которой эта длина равна n-1, он не перейдёт к точке длина от которой равна n-2
@param
Функция принимает файл до изменения и после и двумерный массив размера m*n, где m и n - это размеры
файла до изменения и после (массив получен из 'longestCommonSubsequenceOfStrings').
@return
Функция возвращает массив пар индексов. Каждая пара - индексы строки принадлежащей наибольшей общей подпоследовательности
в файле до изменения и после.
 */
fun longestCommonPartRecovery(
    baseText: List<String>,
    resultText: List<String>,
    lengths: Array<IntArray>
): MutableList<RelevantElements> {
    val res = mutableListOf<RelevantElements>()
    var i = 0
    var j = 0
    while (i < baseText.size && j < resultText.size)
    // Алгоритм находит точку в которой строки совпали и сохраняет её 'координаты'
        if (baseText[i] == resultText[j]) {
            res.add(RelevantElements(i, j))
            i++
            j++
        } else if (lengths[i + 1][j] >= lengths[i][j + 1]) i++
        else j++
    return res
}

/**
@brief
Функция выводящая результат работы утилиты.
@detailed
Функция двигается по массиву строк принадлежащих наибольшей общей подпоследовательности и выводит все строки эти строки и все остальные строки файлов.
Сначала она выводит строки, лежащие между соседними строками из НОП
(или между началом и первой совпадающей строкой/ последней совпадающей строкой и концом файла), которые надо удалить из файла.
Потом она выводит строки, лежащие между соседними строками из НОП
(или между началом и первой совпадающей строкой/ последней совпадающей строкой и концом файла), которые надо добавить в файл.
Потом она выводит пару совпадающих строк.
В итоге на экране появится последовательность команд, после применения которой для исходного файла, получится итоговый.
@param
Функция принимает файл до изменения и после и флаг, который указывает на то, надо ли выводить строки, которые не изменились.
 */
fun printer(baseFile: File,resultFile: File, outputNotOnlyChanged:Boolean = true) {
    val baseStrings = baseFile.readLines()
    val resultStrings = resultFile.readLines()
    // Массив позиций строк принадлежащих наибольшей общей подпоследовательности
    val res: MutableList<RelevantElements> =
        longestCommonSubsequenceOfStrings(baseFile,
            resultFile
        )

    var curIndexBase = 0 //Индексация в исходном файле
    var curIndexResult = 0 //Индексация в итоговом файле
    var stringNumberWriter = 1 //Индексация в выводе (консоли)
    // Добавление несуществующего элемента, чтобы вывести изменения строк от последней совпадающей до конца файла
    res.add(
        RelevantElements(
            baseStrings.size,
            resultStrings.size
        )
    )
    println("$GREEN* means that string doesn't have to be changed")
    println("- means that string should be deleted")
    println("+ means that string should be added$RESET")
    println()

    //Функция для вывода удалённых или добавленных строк
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
        //Проход по исходному файлу от текущего индекса до ближайшей совпадающей строки, показывая, какие строки надо добавить
        printAddedOrDeleted(curIndexBase,elem.firstIndex,baseStrings,'-',RED,curIndexResult <= elem.secondIndex - 1)
        //После всех удалений возвращает индекс на положение, которое было до удаления строк
        stringNumberWriter -= elem.firstIndex - curIndexBase
        //Проход по итоговому файлу от текущего индекса до ближайшей совпадающей строки, показывая, какие строки надо удалить
        printAddedOrDeleted(curIndexResult,elem.secondIndex,resultStrings,'+',GREEN)
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