package x7c1.skyblue.domain.sample

import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import x7c1.skyblue.domain.SourceProvider
import x7c1.skyblue.domain.sample.SourceProviderInitializer.clean

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits

trait WithSourceProvider extends FlatSpec with Matchers with BeforeAndAfter {

  implicit val context: ExecutionContext = Implicits.global

  protected lazy val provider: SourceProvider = SourceProvider.autoCloseable()

  before {
    clean(provider)
  }

}
