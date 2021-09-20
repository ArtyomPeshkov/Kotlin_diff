import java.io.File

//Константы для оформления текста
const val RESET: String = "\u001B[0m"
const val RED: String = "\u001B[31m"
const val GREEN: String = "\u001B[32m"
const val BLUE: String = "\u001B[34m"
const val SEPARATOR: String = "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"


//Класс хранящий индексы одинаковых строк файлов в алгоритме восстановления наибольшей общей подпоследовательности
data class RelevantElements(val firstIndex: Int, val secondIndex: Int)

fun rewriteFile(newFile: Collection<String>,destinationFile: File)
{
    destinationFile.writeText("")
    newFile.forEach {
        destinationFile.appendText(it + "\n")
    }
}

// Алгоритм генерирующий рандомную строку
fun getRandomString(difChars: Int, length: Int): String {
    if (length == 0)
        return ""
    val allowedChars = ('a' until 'a' + difChars)
    return (1..length).map { allowedChars.random() }.joinToString("")
}

fun main() {
    val baseFile = File("src/main/resources/base.txt")
    baseFile.writeText("")
    println(GREEN + "Welcome to diff utility testing system")
    println(BLUE + "Write path to your file (now you are in project folder)")
    var resultFile: String? = readLine()
    while (true) {
        when{
            (resultFile == null || !File(resultFile).exists()) -> println(RED + "Please write existing file")
            (File(resultFile).absoluteFile==File("src/main/resources/base.txt").absoluteFile || File(resultFile).absoluteFile==File("src/main/resources/testResult.txt").absoluteFile || File(resultFile).absoluteFile==File("src/main/resources/testBase.txt").absoluteFile) -> println(RED + "You do not have access to that file")
            else -> break
        }
        resultFile = readLine()
    }
    conversationWithUser(baseFile, File(resultFile!!))
}
