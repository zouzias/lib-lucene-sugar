package com.giltgroupe.scala.lucene

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.prop.Checkers

class LuceneIndexTest extends FunSuite with ShouldMatchers with Checkers {

  test("An empty index should contain no documents") {
    val index = new LuceneIndex with LuceneStandardAnalyzer with RamLuceneDirectory
    index.allDocuments should be('empty)
  }

}
