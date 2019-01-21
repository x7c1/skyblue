package x7c1.skyblue.domain

import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.driver.{Client, Cluster}
import org.apache.tinkerpop.gremlin.process.remote.RemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.{
  GraphTraversal,
  GraphTraversalSource
}
import org.apache.tinkerpop.gremlin.structure.Vertex

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.util.control.NonFatal

object GremlinSampleRunner {

  def main(args: Array[String]): Unit = {
    println("start")

    val cluster = Cluster.build
      .addContactPoint("localhost")
      .port(8182)
      .create()

    try {
      val connection: RemoteConnection = {
        val client: Client = cluster.connect()
        DriverRemoteConnection.using(client, "g")
      }
      val g: GraphTraversalSource =
        AnonymousTraversalSource
          .traversal()
          .withRemote(connection)

      run(g)
    } catch {
      case NonFatal(e) =>
        println("unexpected error", e)
    }
    println("closing...", cluster)
    cluster.close()
  }

  def run(g: GraphTraversalSource): Unit = {
    val marko: Vertex =
      g.addV("person")
        .property("name", "marko")
        .property("age", 29)
        .next

    val lop: Vertex =
      g.addV("software")
        .property("name", "lop")
        .property("lang", "java")
        .property("sample-value", 345)
        .next

    g.addE("created")
      .from(marko)
      .to(lop)
      .property("weight", 0.6d)
      .iterate

    val traversal: GraphTraversal[Vertex, Int] = g
      .V()
      .has("name", "marko")
      .out("created")
      .values("sample-value")

    println("putting...", traversal)
    val collection: Seq[Int] = traversal.toList.asScala
    collection.zipWithIndex.foreach {
      case (v, k) =>
        println(s"$k : $v")
    }
  }
}
