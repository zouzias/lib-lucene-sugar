package com.giltgroupe.lucene

import java.io.File
import org.apache.lucene.store.{MMapDirectory, SimpleFSDirectory, Directory}
import javax.annotation.Nonnull
import com.google.common.base.Preconditions

/**
 * Base trait that creates a Lucene filesystem Directory from a root path
 */
trait FSLuceneDirectoryCreator {
  protected def directoryConstructor: File => Directory

  @Nonnull
  def luceneDirectoryFromPath(@Nonnull path: File): Directory = {
    Preconditions.checkNotNull(path)
    directoryConstructor(path)
  }

}

/**
 * An FSLuceneDirectoryCreator that creates a SimpleFSDirectory
 */
trait SimpleFSLuceneDirectoryCreator extends FSLuceneDirectoryCreator {
  override protected def directoryConstructor = new SimpleFSDirectory(_)
}

/**
 * An FSLuceneDirectoryCreator that creates an MMapDirectory
 */
trait MMapFSLuceneDirectoryCreator extends FSLuceneDirectoryCreator {
  override protected def directoryConstructor = new MMapDirectory(_)
}