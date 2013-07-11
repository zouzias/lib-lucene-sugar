package com.giltgroupe.lucene

import org.scalatest.FlatSpec
import com.google.common.io.Files
import java.io.File

class LuceneFSIndexSuite extends FlatSpec with LuceneIndexBehaviors {

  def fsIndex = {
    val directoryFile = Files.createTempDir()
    new LuceneIndex with LuceneStandardAnalyzer with FSLuceneDirectory with SimpleFSLuceneDirectoryCreator with LuceneIndexPathProvider {
      protected def withIndexPath[T](f: (File) => T): T = f(directoryFile)
    }
  }

  it should behave like emptyIndex(fsIndex)
  it should behave like writableIndex(fsIndex)
  it should behave like searchableIndex(fsIndex)

}
