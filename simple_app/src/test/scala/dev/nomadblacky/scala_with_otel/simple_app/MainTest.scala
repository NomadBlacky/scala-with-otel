package dev.nomadblacky.scala_with_otel.simple_app

import munit.FunSuite

class MainTest extends FunSuite:
  test("Main.sum"):
    assertEquals(Main.sum(1, 2), 3)
