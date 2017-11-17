package edu.ncrn.cornell.xml.extra

import org.w3c.dom.Document
import edu.ncrn.cornell.xml.xpath.transform.util.Transforms

object DocumentExtra {
  implicit class DocumentExtra(val doc: Document) extends AnyVal {
    def docClone(): Document = Transforms.docClone(doc)
  }

}
