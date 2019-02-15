package x7c1.skyblue.domain.sample

import java.util.concurrent.CompletionException

import org.apache.tinkerpop.gremlin.structure.T
import org.scalatest.{FlatSpec, Matchers}

trait tests_around_id extends FlatSpec with Matchers {
  self: GremlinSampleRunnerTest =>

  "T.id" can "be identifier when adding vertex" in {
    val await(_) = provider using { g =>
      g.addV("label1")
        .property(T.id, "123")
        .next()

      g.V()
        .has("label1", T.id.getAccessor, "123")
        .count()
        .next() shouldBe 1
    }
  }

  it should "be number" in {
    val await(_) = provider using { g =>
      intercept[CompletionException] {
        g.addV("label1")
          .property(T.id, "sample-string-id")
          .next()
      }
    }
  }

  it can "not be same as existing T.id" in {
    val await(_) = provider using { g =>
      g.addV("label1")
        .property(T.id, "123")
        .next()

      intercept[CompletionException] {
        g.addV("label1")
          .property(T.id, "123")
          .next()
      }
    }
  }

}
