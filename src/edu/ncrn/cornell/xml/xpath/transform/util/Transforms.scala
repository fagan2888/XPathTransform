package edu.ncrn.cornell.xml.xpath.transform.util

import javax.xml.transform.{Transformer, TransformerFactory}
import javax.xml.transform.dom.{DOMResult, DOMSource}

import org.w3c.dom.Document

object Transforms {

  def docClone(doc: Document): Document  = {
    val tfactory: TransformerFactory = TransformerFactory.newInstance()
    val tx: Transformer = tfactory.newTransformer()
    val source: DOMSource = new DOMSource(doc)
    val result: DOMResult = new DOMResult()
    tx.transform(source, result)
    result.getNode.asInstanceOf[Document]
  }

}
