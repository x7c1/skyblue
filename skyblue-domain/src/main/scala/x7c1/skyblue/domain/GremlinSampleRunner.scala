package x7c1.skyblue.domain

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.{GraphTraversal, GraphTraversalSource}
import org.apache.tinkerpop.gremlin.structure.Vertex

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.concurrent.Await
import scala.concurrent.duration.DurationDouble
import scala.util.{Failure, Success}

object GremlinSampleRunner {

  import scala.concurrent.ExecutionContext.Implicits.global

  def main(args: Array[String]): Unit = {
    println("start")

    val onFinish = (xs: Seq[Int]) => {
      println(xs)
    }
    val future = SourceProvider.autoCloseable().using(run).map(onFinish)
    future.onComplete {
      case Success(_) =>
        println("[done]")
      case Failure(e) =>
        println("[unexpected]", e)
    }
    Await.ready(future, 10.seconds)
  }

  def run(g: GraphTraversalSource): Seq[Int] = {
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

    val traversal: GraphTraversal[Vertex, Int] =
      g.V()
        .has("name", "marko")
        .out("created")
        .values("sample-value")

    println("putting...", traversal)
    traversal.toList.asScala
  }
}
