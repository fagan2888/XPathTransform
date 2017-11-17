package edu.ncrn.cornell.xml.xpath.transform

import org.w3c.dom._
import javax.xml.xpath._

import scala.util.{Failure, Success, Try}

object MutatingTransforms {


  // TODO: note, not thread safe
  implicit val xPathFactory: XPathFactory = XPathFactory.newInstance()


  def removeNodes(doc: Document, nodeLocation: String): Unit = {
    val newDoc: Try[Unit] = for {
      childXpath <- makeXpath(nodeLocation)
      parentXpath <- makeXpath(nodeLocation + "/..")
      parentNode <- parentXpath.evaluate(doc, XPathConstants.NODE) match {
        case Some(pn: Node) => Success(pn)
        case None => Failure(throw new XPathException(
          s"WARN: no parent node found for $nodeLocation"
        ))
      }
    } yield {
        childXpath.evaluate(doc, XPathConstants.NODESET) match {
          case Some(nodeList: NodeList) =>
            println("DEBUG: found nodes to remove")
            val nodeArray = nodeList.toArray()
            nodeArray.foreach{node: Node =>
              parentNode.removeChild(node)
            }
          case _ =>
            println(s"WARN: no nodes found at $nodeLocation")
        }
      }
  }



  def makeXpath(xpathString: String)(implicit xpFact: XPathFactory): Try[XPathExpression] =
    Try(xpFact.newXPath().compile(xpathString))

  implicit class NodeListExtra(val nodeList: NodeList) extends AnyVal {
    def toArray(): Array[Node] = {
      val nlArray: Array[Node] = Array.fill[Node](nodeList.getLength)(null)
      (0 until nodeList.getLength).foreach(ii => nlArray(ii) = nodeList.item(ii))
      nlArray
    }
  }
}
