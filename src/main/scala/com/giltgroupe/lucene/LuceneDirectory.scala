package com.giltgroupe.lucene

import java.io.File
import org.apache.lucene.store.{MMapDirectory, SimpleFSDirectory, RAMDirectory, Directory}
import org.apache.lucene.index.{IndexNotFoundException, DirectoryReader}
import org.apache.lucene.search.IndexSearcher
import javax.annotation.Nonnull

/**
 * Base trait for configuring the Directory of a Lucene index
 */
trait LuceneDirectory {
  protected def directory: Directory

  def withDirectoryReader[T](@Nonnull f: Option[DirectoryReader] => T): T =
    try {
      val indexReader = DirectoryReader.open(directory)
      try {
        f(Some(indexReader))
      } finally {
        indexReader.close()
      }
    } catch {
      case e: IndexNotFoundException =>
        f(None)
    }

  def withIndexSearcher[T](@Nonnull f: Option[IndexSearcher] => T): T =
    withDirectoryReader { indexReaderOpt =>
      f(indexReaderOpt.map(new IndexSearcher(_)))
    }

}

/**
 * Configures a Lucene index to use a RAM based directory to store the index
 */
trait RamLuceneDirectory extends LuceneDirectory {
  protected lazy val directory = new RAMDirectory()
}

trait LuceneIndexPathProvider {
  protected def withIndexPath[T](f: (File) => T): T
}

trait ServiceRootLuceneDirectory extends LuceneIndexPathProvider {
  private lazy val INDEX_DIRECTORY = "index"
  private lazy val serviceRootPath = System.getProperty("user.dir")
  private lazy val indexPath = new File(serviceRootPath, INDEX_DIRECTORY).getAbsolutePath

  protected def withIndexPath[T](f: (File) => T): T = {
    val indexFilePath = new File(indexPath)
    indexFilePath.mkdirs() // make sure path exist
    f(indexFilePath)
  }

}

trait FSLuceneDirectoryCreator {
  def luceneDirectoryFromPath(path: File): Directory
}

trait SimpleFSLuceneDirectoryCreator extends FSLuceneDirectoryCreator {
  def luceneDirectoryFromPath(path: File) = new SimpleFSDirectory(path)
}

trait MMapFSLuceneDirectoryCreator extends FSLuceneDirectoryCreator {
  def luceneDirectoryFromPath(path: File) = new MMapDirectory(path)
}

trait FSLuceneDirectory extends LuceneDirectory { self: LuceneIndexPathProvider =>
  protected lazy val directory = withIndexPath { (path) =>
    new SimpleFSDirectory(path)
  }
}

trait DefaultFSLuceneDirectory extends FSLuceneDirectory with ServiceRootLuceneDirectory with SimpleFSLuceneDirectoryCreator
