package dev.nomadblacky.scala_with_otel.simple_app

import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.api.trace.{Span, StatusCode}
import io.opentelemetry.context.Context

import scala.concurrent.{Await, Future}
import scala.util.Using
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object Main:

  // Get a tracer from GlobalOpenTelemetry
  private val tracer = GlobalOpenTelemetry.getTracer("manual-instrumentation")

  def main(args: Array[String]): Unit =
    val heavyTaskDurationSeconds = args.headOption.map(_.toInt).getOrElse(3)

    // Start a span
    // https://opentelemetry.io/docs/instrumentation/java/manual/#create-spans
    val mainSpan = tracer.spanBuilder("main").startSpan()
    Using(mainSpan.makeCurrent()): _ =>
      try
        println("Hello, world!")
        println(s"sum(1, 2) = ${sum(1, 2)}")

        println("doing heavy task...")
        val heavyTaskSpan = tracer
          .spanBuilder("heavy-task")
          // this is not required.
          .setParent(Context.current().`with`(mainSpan))
          // Add attributes to the span
          .setAttribute("heavy-task-duration-seconds", heavyTaskDurationSeconds)
          .startSpan()

        val future = futureTask()

        Using(heavyTaskSpan.makeCurrent()): _ =>
          try heavyTask(heavyTaskDurationSeconds * 1000)
          finally heavyTaskSpan.end()

        try errorTask()
        catch
          case e: Throwable =>
            println(e.getMessage)

        Await.ready(future, Duration.Inf)
      finally mainSpan.end()

    println("Done!")

  def sum(a: Int, b: Int): Int = a + b

  private def heavyTask(millis: Long): Unit =
    // Get the current span from the current context
    val span = Span.current()
    // Add an event to the span
    span.addEvent("heavy-task-start")
    Thread.sleep(millis)
    span.addEvent("heavy-task-end")
    // Set the status of the span
    span.setStatus(StatusCode.OK)

  private def futureTask(): Future[Unit] =
    Future:
      val futureTaskSpan = tracer
        .spanBuilder("future-task")
        .startSpan()

      Using(futureTaskSpan.makeCurrent()): _ =>
        try
          println("future task start!")
          Thread.sleep(1000)
          println("future task done!")
        finally futureTaskSpan.end()

  private def errorTask(): Unit =
    val errorTaskSpan = tracer
      .spanBuilder("error-task")
      .startSpan()

    Using(errorTaskSpan.makeCurrent()): _ =>
      try
        println("error task start!")
        throw new RuntimeException("error!")
      catch
        case e: Throwable =>
          // Set the status of the span
          errorTaskSpan.setStatus(StatusCode.ERROR, e.getMessage)
          errorTaskSpan.recordException(e)
      finally errorTaskSpan.end()
