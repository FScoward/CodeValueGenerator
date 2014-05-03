/**
 * Created by endlick1989 on 2014/05/02.
 */
import org.apache.poi.ss.usermodel._
import java.io.FileInputStream
import collection.JavaConversions._
import java.io.PrintWriter

case class Code(codeId: String, codeName: String, description: String, codeValue: String, name: String,
                javaClass: String, javaMehotdName: String)

object Main {

  def main(args: Array[String]): Unit = {
    val excel = readExcel("sample.xls")
    val sheet = readSheet(excel, "ja")

    val rit: Iterator[Row] = sheet.rowIterator()
    val x: List[Code] = rit.drop(11).map {row =>
      val codeIdCells = row.getCell(0).toString
      val codeName = row.getCell(2).toString
      val description = row.getCell(6).toString
      val codeValue = row.getCell(12).toString
      val name = row.getCell(14).toString
      val javaClass = {
        if(row.getCell(78) != null) {
          row.getCell(78).toString
        }else{
          ""
        }
      }
      val javaMethodName = row.getCell(79).toString
      Code(codeIdCells, codeName, description, codeValue, name, javaClass, javaMethodName)
    }.toList

    val list: Map[String, List[Code]] = x.groupBy(_.javaClass)


    val out = new PrintWriter("out.java")
//    makeJavaCode(x).foreach(out.println)
//    println(makeJavaCode(x))

    out.println(headerGenerator)
    out.println(mainClassGenerator("sample", list))
    out.close
//    println(headerGenerator)

//    println(mainClassGenerator("main", list))

  }

  def readExcel(filename: String): Workbook = {
    WorkbookFactory.create(new FileInputStream(filename))
  }

  def readSheet(workBook: Workbook, sheetName: String): Sheet = {
    workBook.getSheet(sheetName)
  }

  def headerGenerator = {
    s"/* \n" +
    s"* Copyright(C) 2014 TIS Inc. \n" +
    s"*/ \n" +
    s"package org.sample;\n\n" +
    s"/** \n" +
    s" * XXXXXXXX \n" +
    s" * @author CodeValueGenerator \n" +
    s" * @since 0.1 \n" +
    s" */"
  }

  def codePatternGenerator(number: String) = {
    s"    /** コードパターン${number} */\n" +
    s"""    public static final String CODE_PATTERN_${number} = "PATTERN0${number}";\n"""
  }

  def mainClassGenerator(mainClassName: String, classCodeList: Map[String, List[Code]]) = {
    s"public class ${mainClassName} {\n\n" +
    ((1 to 20).map { number =>
      codePatternGenerator(number.toString)
    }).mkString +
    classCodeList.map(code => {
      innerClassGenerator(code._1, code._2)
    }).mkString +
    s"\n}"
  }

  def innerClassGenerator(className: String, list: List[Code]) = {
    def codegen(inlist: List[Code]): String = {
      inlist.drop(0).map(code => {
        s"        /** ${code.name} */ \n" +
        s"""        public static final String ${code.javaMehotdName} = "${code.codeValue}"; \n\n"""
      }).mkString
    }

    s"\n    /** \n" +
    s"     * ${list.head.codeName} \n" +
    s"     * 説明: ${list.head.description} \n     * \n" +
    s"     * @author CodeValueGenerator \n" +
    s"     * @since 0.1 \n" +
    s"     */\n" +
    s"    public static class $className { \n\n" +
    s"        /** コードID */ \n" +
    s"""        public static final String CODE_ID = "${list.head.codeId}"; \n\n""" +
    s"        /** ${list.head.name} */ \n" +
    s"""        public static final String ${list.head.javaMehotdName} = "${list.head.codeValue}"; \n\n""" +
    codegen(list.drop(1)) +
    s"    }\n"
  }


}
