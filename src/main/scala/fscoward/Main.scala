package fscoward

/**
 * Created by endlick1989 on 2014/05/02.
 */

import fscoward.excel.ExcelOperator
import fscoward.generator.CodeGenerator
import java.io.PrintWriter

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
