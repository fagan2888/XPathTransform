package edu.ncrn.cornell.xml.xpath.transform

import java.io.{File, StringReader}
import javax.xml.transform.stream.{StreamResult, StreamSource}
import javax.xml.transform._

import org.w3c.dom.Document

import scala.util.Try

object Transforms {

  //TODO: basis is from https://stackoverflow.com/questions/11307520/filter-xml-with-xsd
  //
  //TODO: but see more generic identity transform here: https://stackoverflow.com/questions/9484912/how-to-transform-xml-for-one-xsd-into-another-xml-format-that-is-very-similar-bu
  //
  // And related to that, we need to somehow change the root node dynamically,
  // e.g. detect or let the user specify it
  private def makeXSL(targetXSD: String) =
    s"""
      |   <xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
      |        xmlns:xs="http://www.w3.org/2001/XMLSchema">
      |        <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
      |
      |        <xsl:variable name="xsd" select="document('$targetXSD')"/>
      |        <xsl:template match="/">
      |            <xsl:apply-templates select="nodes"/>
      |        </xsl:template>
      |
      |        <xsl:template match="nodes">
      |            <allowed>
      |                <xsl:variable name="allowedNodes" select="$$xsd//xs:element[@name='nodes']"/>
      |                <xsl:for-each select="*">
      |                    <xsl:variable name="name" select="name()"/>
      |                    <xsl:if test="$$allowedNodes//xs:element[@name=$$name and @minOccurs='1']">
      |                        <xsl:copy-of select="."/>
      |                    </xsl:if>
      |                </xsl:for-each>
      |            </allowed>
      |         </xsl:template>
      |
      |    </xsl:stylesheet>
    """.stripMargin

  def mapXml(targetSchema: String): Try[String] = {
    //val xmlInput: Source = new StreamSource(new File("c:/path/to/input.xml"))
    val xmlInput: Source = new StreamSource(new StringReader(makeXSL(targetSchema)))
    val xsl: Source = new StreamSource(new File("c:/path/to/file.xsl"))
    val xmlOutput: Result = new StreamResult(new File("c:/path/to/output.xml"))
    Try {
      val transformer: Transformer =
        TransformerFactory.newInstance().newTransformer(xsl)
      transformer.transform(xmlInput, xmlOutput)
      xmlOutput.toString
    }
  }

}

// An interesting note for further improvement ideas: https://stackoverflow.com/questions/668805/can-i-validate-an-xpath-expression-against-an-xml-schema