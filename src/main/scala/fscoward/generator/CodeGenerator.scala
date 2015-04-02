package fscoward.generator

/**
 * Created by FScoward on 2014/05/04.
 */

import fscoward.env.Environment
import com.typesafe.config.ConfigFactory

case class Code(codeId: String, codeName: String, description: String, codeValue: String, name: String,
                javaClass: String, javaMehotdName: String)

object CodeGenerator extends Environment {

  def generate(mainClassName: String, classCodeList: Map[String, List[Code]]): String = {
    headerGenerator +
      mainClassGenerator(mainClassName, classCodeList)
  }

  private def headerGenerator: String = {
    val config = ConfigFactory.load()

    s"/*\n" +
    s" * Copyright(C) 2014 ${company} Inc.\n" +
    s" */\n" +
    s"package ${config.getString("java.package")};\n\n" +
    s"/**\n" +
    s" * ${config.getString("java.description")}\n" +
    s" *\n" +
    s" * @author ${author}\n" +
    s" * @since ${since}\n" +
    s" */\n"
  }

  private def codePatternGenerator(number: String): String = {
    s"    /** コードパターン${number} */\n" +
    f"""    public static final String CODE_PATTERN_${number} = "PATTERN${number.toInt}%02d";\n"""
  }

  private def mainClassGenerator(mainClassName: String, classCodeList: Map[String, List[Code]]): String = {
    s"public class ${mainClassName} {\n\n" +
    ((1 to 20).map { number =>
      codePatternGenerator(number.toString)
    }).mkString +
    classCodeList.map(code => {
      innerClassGenerator(code._1, code._2)
    }).mkString +
    s"\n}"
  }

  private def innerClassGenerator(className: String, list: List[Code]) = {
    def codegen(inlist: List[Code]): String = {
      inlist.drop(0).map(code => {
        s"        /** ${code.name} */ \n" +
        s"""        public static final String ${code.javaMehotdName} = "${code.codeValue}"; \n\n"""
      }).mkString
    }

    s"\n    /** \n" +
    s"     * ${list.head.codeName} \n" +
    s"     * 説明: ${list.head.description} \n     * \n" +
    s"     * @author ${author} \n" +
    s"     * @since ${since} \n" +
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
