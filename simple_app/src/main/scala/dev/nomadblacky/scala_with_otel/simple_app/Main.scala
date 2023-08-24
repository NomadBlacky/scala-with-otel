package dev.nomadblacky.scala_with_otel.simple_app

import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.api.trace.{Span, StatusCode}
import io.opentelemetry.context.Context

import scala.util.Using

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

        Using(heavyTaskSpan.makeCurrent()): _ =>
          try heavyTask(heavyTaskDurationSeconds * 1000)
          finally heavyTaskSpan.end()

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
