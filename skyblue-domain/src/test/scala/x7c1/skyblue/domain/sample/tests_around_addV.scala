package x7c1.skyblue.domain.sample

import org.scalatest.{FlatSpec, Matchers}

trait tests_around_addV extends FlatSpec with Matchers {
  self: GremlinUsageTest =>

  "addV" can "add vertex with properties" in {
    val await(_) = provider using { g =>
      // no vertex added
      g.V().has("x1", "key1", "value1").hasNext shouldBe false
    }
    val await(_) = provider using { g =>
      g.addV("x1")
        .property("key1", "abcde")
        .next()

      // unknown value
      g.V().has("x1", "key1", "value1").hasNext shouldBe false
    }
    val await(_) = provider using { g =>
      g.addV("x1")
        .property("key1", "value1")
        .next()

      // vertex (with given property) found
      g.V().has("x1", "key1", "value1").hasNext shouldBe true
      g.V().has("x1", "key1", "value1").count().next() shouldBe 1
    }
    val await(_) = provider using { g =>
      g.addV("x1")
        .property("key1", "value1")
        .next()

      // can add same label with same property
      g.V().has("x1", "key1", "value1").count().next() shouldBe 2

      // abcde, value1, value1
      g.V().hasLabel("x1").count().next() shouldBe 3
    }
    val await(_) = provider using { g =>
      // unknown vertex
      g.V().hasLabel("y1").count().next()
    }
  }

}
