/**
 * Created by endlick1989 on 2014/05/02.
 */

import excel.ExcelOperator
import generator.CodeGenerator
import java.io.PrintWriter
import collection.JavaConversions._

object Main {
  def main(args: Array[String]): Unit = {
    val excel = ExcelOperator.readExcel("sample.xls")
    val sheet = ExcelOperator.readSheet(excel, "ja")
    val list = ExcelOperator.getCodeList(sheet).groupByJavaClass

    val out = new PrintWriter("out.java")
    out.println(CodeGenerator.generate("Main", list))
    out.close
  }
}
