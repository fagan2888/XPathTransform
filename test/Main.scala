package xpathtransform.test

import edu.ncrn.cornell.xml.io.DomLoaders
import edu.ncrn.cornell.xml.xpath.transform.MutatingTransforms
import edu.ncrn.cornell.xml.extra.DocumentExtra._


import scala.util.{Failure, Success}

object Main{
  def testFailure(msgOpt: Option[String] = None): Unit = {
    msgOpt match {
      case Some(msg) => assert(false, msg)
      case None =>  assert(false)
    }
  }

  def main( args: Array[String] ): Unit = {
    println( Console.GREEN ++ "Executing xpathtransform.test" ++ Console.RESET )

    println("running simple tests on xml (shiporder.xml)")
    val shipOrderXmlStream = this.getClass.getResourceAsStream("/shiporder.xml")
    val shipOrderDocTry = DomLoaders.loadXMLFromStream(shipOrderXmlStream)
    shipOrderDocTry match {
      case Success(shipOrderDoc) =>
        val shipOrderDocMod = shipOrderDoc.docClone()
        assert(
          !(shipOrderDocMod eq shipOrderDoc),
          Some("cloned docs are referentially equal!")
        )
        assert( // Should be the same as the above test
          !shipOrderDocMod.isSameNode(shipOrderDoc),
          Some("cloned docs are referentially equal!")
        )
        assert(
          shipOrderDocMod.isEqualNode(shipOrderDoc),
          Some("cloned docs are not structurally equal!")
        )

        val quantityXpath = "/shiporder/item/quantity"
        MutatingTransforms.removeNodes(shipOrderDocMod, quantityXpath)

        assert(
          !shipOrderDocMod.isEqualNode(shipOrderDoc),
          Some("modified docs are structurally equal!")
        )
        val shipOrderDocModString = shipOrderDocMod.toXmlString().get
        assert(!shipOrderDocModString.contains("quantity"))
        println(shipOrderDocMod.toXmlString())
      case Failure(ex) => testFailure(Some(s"Couldn't load ship order xml file: $ex"))
    }



  }
}
