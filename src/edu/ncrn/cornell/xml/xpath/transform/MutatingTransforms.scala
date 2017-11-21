package edu.ncrn.cornell.xml.xpath.transform

import edu.ncrn.cornell.xml.extra.NodeListExtra._

import org.w3c.dom._
import javax.xml.xpath._

import scala.util.{Failure, Success, Try}

object MutatingTransforms {


  // TODO: note, not thread safe
  implicit val xPathFactory: XPathFactory = XPathFactory.newInstance()


  def removeNodes(doc: Document, nodeLocation: String): Unit = {
    for {
      childXpath <- makeXpath(nodeLocation)
      parentXpath <- makeXpath(nodeLocation + "/..")
      parentNodes <- Option(parentXpath.evaluate(doc, XPathConstants.NODESET)) match {
        case Some(pn: NodeList) => Success(pn)
        case None =>
          Failure(throw new XPathException(
          s"WARN: no parent node found for $nodeLocation"
        ))
      }
    } yield {
        Option(childXpath.evaluate(doc, XPathConstants.NODESET)) match {
          case Some(nodeList: NodeList) =>
            val nodeArray = nodeList.toArray()
            //FIXME: this iterates over all leafs to remove, even if they aren't
            //FIXME: directly children:
            nodeArray.foreach{node: Node =>
              parentNodes.toArray().foreach { pn =>
                if (pn.getChildNodes.toArray().contains(node)) {
                  pn.removeChild(node)
                }
              }
            }
          case _ =>
            println(s"WARN: no nodes found at $nodeLocation")
        }
      }
  }




  def makeXpath(xpathString: String)(implicit xpFact: XPathFactory): Try[XPathExpression] =
    Try(xpFact.newXPath().compile(xpathString))

}
