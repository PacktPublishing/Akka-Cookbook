package com.packt.chapter9

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport
import spray.json.DefaultJsonProtocol
import scala.xml._

case class Item(id: Int, quantity: Int, unitPrice: Double, percentageDiscount: Option[Double])

case class Order(id: String, timestamp: Long, items: List[Item], deliveryPrice: Double, metadata: Map[String, String])

case class GrandTotal(id: String, amount: Double)

trait OrderJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val itemFormat = jsonFormat4(Item)
  implicit val orderFormat = jsonFormat5(Order)
  implicit val grandTotalFormat = jsonFormat2(GrandTotal)
}

trait OrderXmlSupport extends ScalaXmlSupport {
  implicit def grandTotalToXML(g: GrandTotal): NodeSeq =
    <grandTotal><id>{g.id}</id><amount>{g.amount}</amount></grandTotal>

  implicit def orderToXML(o: Order): NodeSeq =
    <order>
      <id>{o.id}</id>
      <timestamp>{o.timestamp}</timestamp>
      <deliveryPrice>{o.deliveryPrice}</deliveryPrice>
      <items>{o.items.map(itemToXML)}</items>
      <metadata>{o.metadata.map(keyValueToXML)}</metadata>
    </order>

  implicit def orderFromXML(xmlOrder: NodeSeq): Order = {
    val id = (xmlOrder \ "id").text
    val timestamp = (xmlOrder \ "timestamp").text.toLong
    val deliveryPrice = (xmlOrder \ "deliveryPrice").text.toDouble
    val items = (xmlOrder \ "item").map(itemFromXML).toList
    val metadata = keyValueFromXML(xmlOrder \ "metadata")
    Order(id, timestamp, items, deliveryPrice, metadata)
  }

  private def keyValueFromXML(xml: NodeSeq) = {
    xml.flatMap {
      case e: Elem => e.child
      case _ => NodeSeq.Empty
    }.map(x => x.label -> x.text).toMap
  }

  private def keyValueToXML(kv: (String, String)) =
    Elem(null, kv._1, Null, TopScope, false, Text(kv._2))

  private def itemFromXML(xmlItem: NodeSeq): Item = {
    val id = (xmlItem \ "id").text.toInt
    val quantity = (xmlItem \ "quantity").text.toInt
    val unitPrice = (xmlItem \ "unitPrice").text.toDouble
    val percentageDiscount =
      if ((xmlItem \ "percentageDiscount").isEmpty) None
      else Some((xmlItem \ "percentageDiscount").text.toDouble)

    Item(id, quantity, unitPrice, percentageDiscount)
  }

  private def itemToXML(i: Item) =
    <item>
      <id>{i.id}</id>
      <quantity>{i.quantity}</quantity>
      <unitPrice>{i.unitPrice}</unitPrice>
      {if (i.percentageDiscount.isDefined) <percentageDiscount>{i.percentageDiscount.get}</percentageDiscount>}
    </item>
}