package com.gilt.lucene

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.analysis.Analyzer

/**
 * Provides a Lucene Analyzer
 */
trait LuceneAnalyzerProvider {
  protected val luceneAnalyzer: Analyzer
}

/**
 * A LuceneAnalyzerProvider that provides a StandardAnalyzer
 */
trait LuceneStandardAnalyzer extends LuceneAnalyzerProvider { self: LuceneVersion =>
  protected val luceneAnalyzer = new StandardAnalyzer(luceneVersion)
}