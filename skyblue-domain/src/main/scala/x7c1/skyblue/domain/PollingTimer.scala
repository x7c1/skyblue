package x7c1.skyblue.domain

import scala.concurrent.{ExecutionContext, Future, Promise}

object PollingTimer {

  def until(predicate: () => Boolean)(
      implicit c: ExecutionContext): Future[Unit] = {

    if (predicate()) {
      Future successful {}
    } else {
      FutureTimer
        .await(millis = 1000)
        .flatMap(_ => until(predicate))
    }
  }
}
