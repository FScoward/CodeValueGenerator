package generator

/**
 * Created by endlick1989 on 2014/05/04.
 */

import env.Environment

case class Code(codeId: String, codeName: String, description: String, codeValue: String, name: String,
                javaClass: String, javaMehotdName: String)

object CodeGenerator extends Environment {

  def generate(mainClassName: String, classCodeList: Map[String, List[Code]]): String = {
    headerGenerator +
    mainClassGenerator(mainClassName, classCodeList)
  }

  private def headerGenerator: String = {
    s"/* \n" +
    s"* Copyright(C) 2014 TIS Inc. \n" +
    s"*/ \n" +
    s"package org.sample;\n\n" +
    s"/** \n" +
    s" * XXXXXXXX \n" +
    s" * @author ${author} \n" +
    s" * @since ${since} \n" +
    s" */ \n"
  }

  private def codePatternGenerator(number: String): String = {
    s"    /** コードパターン${number} */\n" +
    s"""    public static final String CODE_PATTERN_${number} = "PATTERN0${number}";\n"""
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
