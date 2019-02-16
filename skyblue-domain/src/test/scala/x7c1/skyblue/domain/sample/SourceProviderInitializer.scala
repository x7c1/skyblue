package x7c1.skyblue.domain.sample

import x7c1.skyblue.domain.SourceProvider

import scala.concurrent.ExecutionContext

object SourceProviderInitializer {

  def clean(provider: SourceProvider)(implicit c: ExecutionContext): Unit = {
    val await(_) = provider.using { g =>
      g.V().drop().iterate()
    }
  }

}
