package edu.ncrn.cornell.xml.extra

import java.io.StringWriter
import javax.xml.transform.{OutputKeys, TransformerException, TransformerFactory}
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

import org.w3c.dom.Document
import edu.ncrn.cornell.xml.xpath.transform.util.Transforms

import scala.util.{Failure, Success, Try}

object DocumentExtra {
  implicit class DocumentExtra(val doc: Document) extends AnyVal {
    def docClone(): Document = Transforms.docClone(doc)

    def toXmlString(pretty: Boolean = true):Try[String] = {
      getStringFromDocument(doc, pretty)
    }
  }

  def getStringFromDocument(doc: Document, pretty: Boolean): Try[String] = {
    try {
      val domSource= new DOMSource(doc)
      val writer = new StringWriter()
      val result = new StreamResult(writer)
      val tf = TransformerFactory.newInstance()
      val transformer = tf.newTransformer()
      if (pretty) {
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
      }
      transformer.transform(domSource, result)
      Success(writer.toString)
    }
    catch {
      case ex: TransformerException => Failure(ex)
    }
  }

}
