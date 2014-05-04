package excel

/**
 * Created by endlick1989 on 2014/05/04.
 */
import org.apache.poi.ss.usermodel._
import generator.Code
import collection.JavaConversions._
import java.io.FileInputStream
import scala.collection.mutable

object ExcelOperator {
  private var _codeList: List[Code] = Nil

  def readExcel(filename: String): Workbook = {
    WorkbookFactory.create(new FileInputStream(filename))
  }

  def readSheet(workBook: Workbook, sheetName: String): Sheet = {
    workBook.getSheet(sheetName)
  }

  def getCodeList(sheet: Sheet): this.type = {
    val rit: Iterator[Row] = sheet.rowIterator()
    val list = rit.drop(11).map {row =>
      val codeId = row.getCell(0).toString
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
      Code(codeId, codeName, description, codeValue, name, javaClass, javaMethodName)
    }.toList

    _codeList = list

    this
  }

  def groupByJavaClass: Map[String, List[Code]] = {
    _codeList.groupBy(_.javaClass)
  }
}
