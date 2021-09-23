import java.io.File

//Константы для оформления текста
const val RESET: String = "\u001B[0m"
const val RED: String = "\u001B[31m"
const val GREEN: String = "\u001B[32m"
const val BLUE: String = "\u001B[34m"
const val SEPARATOR: String = "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"


//Класс хранящий индексы одинаковых строк файлов в алгоритме восстановления наибольшей общей подпоследовательности
data class RelevantElements(val firstIndex: Int, val secondIndex: Int)

//Функция очищает файл и записывает в него переданный список строк
fun rewriteFile(newFile: Collection<String>,destinationFile: File)
{
    destinationFile.writeText("")
    newFile.forEach {
        destinationFile.appendText(it + "\n")
    }
}

// Алгоритм генерирующий рандомную строку
fun getRandomString(difChars: Int, length: Int): String {
    val allowedChars = ('a' until 'a' + difChars)
    return (1..length).map { allowedChars.random() }.joinToString("")
}

fun main() {
    val baseFile = File("src/main/resources/base.txt")
    println(GREEN + "Welcome to diff utility testing system")
    conversationWithUser(baseFile)
}
