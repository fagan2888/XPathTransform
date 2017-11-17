package xpathtransform.test

import edu.ncrn.cornell.xml.io.DomLoaders
import edu.ncrn.cornell.xml.xpath.transform.MutatingTransforms

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

    println("running simple tests on xml (shiporder.xm)")
    val shipOrderXmlStream = this.getClass.getResourceAsStream("/shiporder.xml")
    val shipOrderDocTry = DomLoaders.loadXMLFromStream(shipOrderXmlStream)
    shipOrderDocTry match {
      case Success(shipOrderDoc) => ()
      case Failure(ex) => testFailure(Some(s"Couldn't load ship order xml file: $ex"))
    }



  }
}
