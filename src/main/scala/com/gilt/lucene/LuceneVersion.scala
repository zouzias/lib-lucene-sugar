package com.gilt.lucene

import org.apache.lucene.util.Version

trait LuceneVersion {
  protected def luceneVersion = Version.LATEST
}
