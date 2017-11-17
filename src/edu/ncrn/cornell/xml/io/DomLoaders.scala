package edu.ncrn.cornell.xml.io

import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.DocumentBuilder

import org.w3c.dom.Document
import java.io.{ByteArrayInputStream, InputStream}

import scala.util.Try

object DomLoaders {

  //TODO: optimize usage of newInstance,newDocumentBuilder
  //TODO: while taking into account thread safety
  def makeBuilder(): DocumentBuilder = {
    val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
    factory.setNamespaceAware(true)
    factory.newDocumentBuilder()
  }

  def loadXMLFromString(xml: String): Try[Document] = Try{
    val builder = makeBuilder()
    builder.parse(new ByteArrayInputStream(xml.getBytes))
  }

  def loadXMLFromStream(xml: InputStream): Try[Document] = Try{
    val builder = makeBuilder()
    builder.parse(xml)
  }

}
