package com.gilt.lucene

import org.scalatest.FlatSpec

class LuceneRamIndexSuite extends FlatSpec with LuceneIndexBehaviors {

  def ramIndex = {
    new ReadableLuceneIndex with WritableLuceneIndex with LuceneStandardAnalyzer with RamLuceneDirectory
  }

  it should behave like emptyIndex(ramIndex)
  it should behave like writableIndex(ramIndex)
  it should behave like searchableIndex(ramIndex)

}

