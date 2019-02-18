package x7c1.skyblue.domain.sample

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import x7c1.skyblue.domain.SourceProvider
import x7c1.skyblue.domain.sample.SourceProviderInitializer.clean

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.concurrent.ExecutionContext.Implicits
import scala.concurrent.{ExecutionContext, Future}

class GremlinSampleRunnerTest
  extends FlatSpec
  with Matchers
  with BeforeAndAfter
  with tests_around_addV
  with tests_around_id {

  implicit val context: ExecutionContext = Implicits.global

  protected lazy val provider: SourceProvider = SourceProvider.autoCloseable()

  before {
    clean(provider)
  }

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
