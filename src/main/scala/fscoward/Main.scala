package fscoward

/**
 * Created by FScoward on 2014/05/02.
 */

import fscoward.excel.{ExcelReader, ExcelOperator}
import fscoward.generator.CodeGenerator
import java.io.PrintWriter
import com.typesafe.config.ConfigFactory

object Main {
  def main(args: Array[String]): Unit = {
    val (filename, sheetname) = init

    val sheet = ExcelReader.readExcel(filename).readSheet(sheetname)
    val list = ExcelOperator.getCodeList(sheet).groupByJavaClass

    val out = new PrintWriter("out.java")
    out.println(CodeGenerator.generate("Main", list))
    out.close
  }

  def init = {
    val config = ConfigFactory.load()
    val filename = config.getString("excel.filename")
    val sheetname = config.getString("excel.sheetname")
    (filename, sheetname)
  }
}
