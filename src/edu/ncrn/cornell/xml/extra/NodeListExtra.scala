package edu.ncrn.cornell.xml.extra

import org.w3c.dom.{Node, NodeList}

object NodeListExtra {

  implicit class NodeListExtra(val nodeList: NodeList) extends AnyVal {
    def toArray(): Array[Node] = {
      val nlArray: Array[Node] = Array.fill[Node](nodeList.getLength)(null)
      (0 until nodeList.getLength).foreach(ii => nlArray(ii) = nodeList.item(ii))
      nlArray
    }
  }

}
