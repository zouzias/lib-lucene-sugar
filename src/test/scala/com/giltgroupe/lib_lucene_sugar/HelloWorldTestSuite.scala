package com.giltgroupe.lib_lucene_sugar

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.prop.Checkers

class HelloWorldTestSuite extends FunSuite with ShouldMatchers with Checkers {

  test("The HelloWorld class should greet you") {
    val greeter = new HelloWorld("tester")

    greeter.greet should be ("Hello, tester")
  }
}
