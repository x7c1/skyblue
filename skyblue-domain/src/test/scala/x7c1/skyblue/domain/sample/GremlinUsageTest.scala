package x7c1.skyblue.domain.sample

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal
import org.apache.tinkerpop.gremlin.structure.Vertex

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.concurrent.Future


class GremlinUsageTest
  extends WithSourceProvider
  with tests_around_addV
  with tests_around_id {

  "runner" can "connect to gremlin server" in {
    val await(xs): Future[Seq[Int]] = provider using { g =>
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

      traversal.toList.asScala
    }
    xs shouldBe Seq(345)
  }
}
