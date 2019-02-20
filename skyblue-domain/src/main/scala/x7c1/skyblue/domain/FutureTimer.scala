package x7c1.skyblue.domain

import java.util.{Timer, TimerTask}
import scala.concurrent.{Future, Promise}

object FutureTimer {
  def await(millis: Int): Future[Unit] = {
    val promise = Promise[Unit]()
    val task: TimerTask = new TimerTask {
      override def run(): Unit = {
        promise.success({})
      }
    }
    val timer = new Timer()
    timer.schedule(task, millis)
    promise.future
  }
}
