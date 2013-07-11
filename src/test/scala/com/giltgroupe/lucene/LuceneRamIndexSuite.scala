package com.giltgroupe.lucene

import org.scalatest.FlatSpec

class LuceneRamIndexSuite extends FlatSpec with LuceneIndexBehaviors {

  def ramIndex = {
    new LuceneIndex with LuceneStandardAnalyzer with RamLuceneDirectory
  }

  it should behave like emptyIndex(ramIndex)
  it should behave like writableIndex(ramIndex)
  it should behave like searchableIndex(ramIndex)

}

