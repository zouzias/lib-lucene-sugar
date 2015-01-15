package com.gilt.lucene

import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.Matchers
import java.io.File
import com.google.common.io.Files
import org.apache.lucene.store.{MMapDirectory, Directory, SimpleFSDirectory}
import org.scalatest.mock.MockitoSugar

class FSLuceneDirectoryCreatorSuite extends FunSuite with Matchers with BeforeAndAfter with MockitoSugar {

  var directoryFile: File = _

  before {
    directoryFile = Files.createTempDir()
  }

  test("FSLuceneDirectoryCreator should check path nullity") {
    a[NullPointerException] should be thrownBy {
      new FSLuceneDirectoryCreator {
        override protected def directoryConstructor = { file: File =>
          mock[Directory]
        }
      }.luceneDirectoryFromPath(null)
    }
  }

  test("SimpleFSLuceneDirectoryCreator should create a SimpleFSDirectory") {
    val directory = new SimpleFSLuceneDirectoryCreator {
    }.luceneDirectoryFromPath(directoryFile)
    directory should not equal null
    directory.isInstanceOf[SimpleFSDirectory] should equal(true)
  }

  test("MMapFSLuceneDirectoryCreator should create a SimpleFSDirectory") {
    val directory = new MMapFSLuceneDirectoryCreator {
    }.luceneDirectoryFromPath(directoryFile)
    directory should not equal null
    directory.isInstanceOf[MMapDirectory] should equal(true)
  }

}
