package x7c1.skyblue.domain

import org.scalatest.{FlatSpec, Matchers}
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

class GremlinSampleRunnerTest extends FlatSpec with Matchers {
  import scala.concurrent.ExecutionContext.Implicits.global

  "runner" can "connect to gremlin server" in {
    val onFinish = (xs: Seq[Int]) => {
      println(xs)
    }
    val future = SourceProvider.autoCloseable().using(GremlinSampleRunner.run).map(onFinish)
    future.onComplete {
      case Success(_) =>
        println("[done]")
      case Failure(e) =>
        println("[unexpected]", e)
    }
    Await.ready(future, 10.seconds)
    true shouldBe true
  }
}
