package com.giltgroupe.lucene

import org.apache.lucene.util.Version
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.index.{IndexWriter, IndexWriterConfig}
import org.apache.lucene.queryparser.classic.QueryParser
import javax.annotation.Nonnull
import org.apache.lucene.search.Query
import org.apache.lucene.document.Document


trait LuceneVersion {
  protected val LUCENE_VERSION = Version.LUCENE_43
}

trait LuceneAnalyzer {
  protected val luceneAnalyzer: Analyzer
}

trait LuceneStandardAnalyzer extends LuceneAnalyzer { self: LuceneVersion =>
  protected val luceneAnalyzer = new StandardAnalyzer(LUCENE_VERSION)
}

trait LuceneIndexWriter { self: LuceneDirectory with LuceneAnalyzer with LuceneVersion =>
  private val indexWriterConfig = new IndexWriterConfig(LUCENE_VERSION, luceneAnalyzer)

  def withIndexWriter[T](f: IndexWriter => T): T = {
    val iwriter = new IndexWriter(directory, indexWriterConfig)
    try {
      f(iwriter)
    } finally {
      iwriter.close(true)
    }
  }

}

/**
 * Base trait for simple Lucene indexes
 * The index gets built once at construction
 */
trait LuceneIndex extends LuceneIndexWriter with LuceneVersion { self: LuceneDirectory with LuceneAnalyzer =>

  @Nonnull
  def queryParserForDefaultField(@Nonnull field: String) = new QueryParser(LUCENE_VERSION, field, luceneAnalyzer)

  /**
   * Process a Lucene query string and returns the resulting documents
   */
  @Nonnull
  def searchTopDocuments(@Nonnull query: Query, @Nonnull limit: Int): Iterable[Document] = withIndexSearcher { indexSearcherOption =>
    indexSearcherOption.map { indexSearcher =>
      val hits = indexSearcher.search(query, limit)

      hits.scoreDocs.map { hit =>
        indexSearcher.doc(hit.doc)
      }.toIterable

    }.getOrElse(Iterable.empty)
  }

  @Nonnull
  def allDocuments: Iterable[Document] = withDirectoryReader { directoryReaderOption =>
    directoryReaderOption.map { directoryReader =>
      (0 until directoryReader.numDocs).map(directoryReader.document)
    }.getOrElse(Iterable.empty)
  }

}
