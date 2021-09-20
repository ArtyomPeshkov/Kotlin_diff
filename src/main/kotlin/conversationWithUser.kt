import java.io.File

//Функция реализующая взаимодействие с пользователем
fun conversationWithUser(baseFile:File,resultFile: File) {
    while (true) {
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
                    changer(baseFile,resultFile);break
                }
                "RUN", "Run", "run" -> {
                    printer(baseFile,resultFile);break
                }
                "RUN T", "Run t", "run t" -> {
                    megaTester(); break
                }
                "q", "Q" -> return
                else -> println("${RED}Unknown command")
            }
        }
    }
}

//Алгоритм меняющий содержимое файла в зависимости от требований пользователя
fun changer(baseFile: File,resultFile: File
) {
    println(BLUE + "If you want to fill your file with random values, write '${RED}rnd$BLUE', if you want to fill it by yourself, write '${RED}my str$BLUE'")
    println("If you want to print your file, write '${RED}print$BLUE', if you want to quite, write '${RED}q$BLUE'" + RESET)
    while (true) when (readLine().toString()) {
        "rnd", "Rnd", "RND" -> {
            randomChanger(baseFile,resultFile)
            break
        }
        "my str", "My str", "MY STR" -> {
            userChanger(baseFile,resultFile)
            break
        }
        "print", "Print", "PRINT" -> {
            resultFile.readLines().forEach {
                println(it)
            }
            println()
            break
        }
        "q", "Q" -> break
        else -> println(RED + "Please, write 'rnd', 'my str', 'print', 'q'" + RESET)
    }
}

fun checkCorrectInput(minVal: Int, maxVal:Int) : Int
{
    var someNumber = readLine()
    while (someNumber?.toIntOrNull() == null || someNumber.toInt() < minVal || someNumber.toInt() > maxVal) {
        println(RED + "Write correct number")
        someNumber = readLine()
    }
    return someNumber.toInt()
}

fun randomChanger(baseFile: File,resultFile: File) {
    rewriteFile(resultFile.readLines(),baseFile)
    println(GREEN + "Write number of strings in your file (from 0 to 10000):")
    val strNumber = checkCorrectInput(0,10000)
    println(GREEN + "Write length of strings in your file (from 0 to 1000):")
    val strLength = checkCorrectInput(0,1000)
    println(GREEN + "Write how many different symbols you want to have in your string (from 1 to 26)")
    val difChars = checkCorrectInput(1,26)

    val newFile = mutableListOf(getRandomString(difChars, strLength))
    repeat(strNumber - 1)
    {
        newFile.add(getRandomString(difChars, strLength))
    }

    rewriteFile(newFile,resultFile)
    println(RED + "Random file generated")
    newFile.clear()
}

//Обрабатывает запросы пользователя по изменению файла
fun userChanger(baseFile: File,resultFile: File) {
    val saveFile = resultFile.readLines().toMutableList()
    val changingFile = resultFile.readLines().toMutableList()
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
                        changingFile.add(strNumber.toInt() - 1, newStr)
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
                rewriteFile(changingFile,resultFile)
                if (saveFile != changingFile)
                    rewriteFile(saveFile,baseFile)
                return
            }
            else -> println(RED + "Unknown command")
        }
    }
}