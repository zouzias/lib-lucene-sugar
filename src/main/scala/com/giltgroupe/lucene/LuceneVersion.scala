package com.giltgroupe.lucene

import org.apache.lucene.util.Version

trait LuceneVersion {
  protected def luceneVersion = Version.LUCENE_43
}
