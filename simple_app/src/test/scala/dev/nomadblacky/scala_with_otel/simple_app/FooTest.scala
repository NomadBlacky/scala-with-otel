package dev.nomadblacky.scala_with_otel.simple_app

import munit.FunSuite

class FooTest extends FunSuite:
  test("Main.sum"):
    assertEquals(Foo.sum(1, 2), 3)
