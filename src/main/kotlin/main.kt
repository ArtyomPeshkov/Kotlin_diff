
import java.io.File
import java.lang.Integer.max

/*fun getFileLines(filename: String): List<String> {
   return File(filename).readLines()
}*/

/*fun lcsLength(first:String,second:String,lengths:Array<IntArray>) :Int
{
   for (i in first.length downTo 0)
      for (j in second.length downTo 0)
      {
         if (i==first.length || j==second.length) lengths[i][j]=0
         else if (first[i]==second[i]) lengths[i][j]=1+lengths[i+1][j+1]
         else lengths[i][j] = max(lengths[i+1][j],lengths[i][j+1])
      }
   return lengths[0][0]
}*/

fun /*longestCommonPartOfFile*/littoralCombatShip(baseText:List<String>, resultText:List<String>, lengths:Array<IntArray>) : Int{
   for (i in baseText.size downTo 0)
      for (j in resultText.size downTo 0)
      {
         if (i==baseText.size || j==resultText.size) lengths[i][j]=0
         else if (baseText[i]==resultText[i]) lengths[i][j]=1+lengths[i+1][j+1]
         else lengths[i][j] = max(lengths[i+1][j],lengths[i][j+1])
      }
   return lengths[0][0]
}


fun main(){

   //Lcs test zone

  /* var first = "gdc"
   var second = "gdfjc"
   var lengths=Array(first.length+1) { IntArray(second.length+1) }

   println(lcsLength("gdf","gdf", lengths))*/

//------------------------------------------------------

   //LittoralCombatShip test zone

   val baseFile = File("src\\main\\resources\\base.txt")
   val resultFile = File("src\\main\\resources\\result.txt")

   val baseStrings:List<String> = baseFile.readLines()
   val resultStrings:List<String> = resultFile.readLines()
   val lengths=Array(baseStrings.size+1) { IntArray(resultStrings.size+1) }
   println(littoralCombatShip(baseStrings,resultStrings, lengths))



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
