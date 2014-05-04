package fscoward.excel

/**
 * Created by FScoward on 2014/05/04.
 */
import org.apache.poi.ss.usermodel._
import fscoward.generator.Code
import collection.JavaConversions._
import scala.collection.mutable
import com.typesafe.config.ConfigFactory

object ExcelOperator {
  private var _codeList: List[Code] = Nil

  case class Address(exclude: Int, codeId: Int, codeName: Int, description: Int, codeValue: Int, name: Int, javaClass: Int, javaMethodName: Int)
  def getCodeList(sheet: Sheet): this.type = {
    val config = ConfigFactory.load()

    val address = Address(
      config.getInt("codeAddress.exclude"),
      config.getInt("codeAddress.codeId"),
      config.getInt("codeAddress.codeName"),
      config.getInt("codeAddress.description"),
      config.getInt("codeAddress.codeValue"),
      config.getInt("codeAddress.name"),
      config.getInt("codeAddress.javaClass"),
      config.getInt("codeAddress.javaMethodName")
    )

    val list = sheet.rowIterator().drop(address.exclude).map {row =>
      val codeId = row.getCell(address.codeId).toString
      val codeName = row.getCell(address.codeName).toString
      val description = row.getCell(address.description).toString
      val codeValue = row.getCell(address.codeValue).toString
      val name = row.getCell(address.name).toString
      val javaClass = {
        val tmp = row.getCell(address.javaClass)
        if(tmp != null) {
          tmp.toString
        }else{
          ""
        }
      }
      val javaMethodName = row.getCell(address.javaMethodName).toString
      Code(codeId, codeName, description, codeValue, name, javaClass, javaMethodName)
    }.toList

    _codeList = list

    this
  }

  def groupByJavaClass: Map[String, List[Code]] = {
    _codeList.groupBy(_.javaClass)
  }
}
