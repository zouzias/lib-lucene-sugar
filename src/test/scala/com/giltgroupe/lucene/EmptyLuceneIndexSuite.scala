package com.giltgroupe.lucene

import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.prop.Checkers

class EmptyLuceneIndexSuite extends FunSuite with BeforeAndAfter with ShouldMatchers with Checkers {

  var luceneIndex: LuceneIndex = _

  before {
    luceneIndex = new LuceneIndex with LuceneStandardAnalyzer with RamLuceneDirectory
  }

  test("should contain no documents") {
    luceneIndex.allDocuments should be('empty)
  }

}
