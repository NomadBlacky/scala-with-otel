package dev.nomadblacky.scala_with_otel.simple_app

import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.context.Context

import scala.util.Using

object Main:

  // Get a tracer from GlobalOpenTelemetry
  private val tracer = GlobalOpenTelemetry.getTracer("manual-instrumentation")

  def main(args: Array[String]): Unit =
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
          .setParent(Context.current().`with`(mainSpan)) // this is not required.
          .startSpan()

        Using(heavyTaskSpan.makeCurrent()): _ =>
          try heavyTask()
          finally heavyTaskSpan.end()

      finally mainSpan.end()

    println("Done!")

  def sum(a: Int, b: Int): Int = a + b

  private def heavyTask(): Unit =
    Thread.sleep(3000)
