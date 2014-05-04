package fscoward.excel

import org.apache.poi.ss.usermodel.{Sheet, WorkbookFactory, Workbook}
import java.io.FileInputStream

/**
 * Created by FScoward on 2014/05/04.
 */
object ExcelReader {
  var _workbook: Workbook = null
  def readExcel(filename: String): this.type = {
    _workbook = WorkbookFactory.create(new FileInputStream(filename))
    this
  }

  def readSheet(sheetName: String): Sheet = {
    _workbook.getSheet(sheetName)
  }
}
