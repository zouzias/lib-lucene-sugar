package com.gilt.lucene

import org.scalatest.Matchers
import org.scalatest.FlatSpec
import org.apache.lucene.document.{StringField, Document}
import org.apache.lucene.document.Field.Store
import org.apache.lucene.search.TermQuery
import org.apache.lucene.index.Term

trait LuceneIndexBehaviors extends Matchers { self: FlatSpec =>

  val documentInIndex: Document = {
    val doc = new Document
    doc.add(new StringField("field", "value", Store.YES))
    doc
  }

  def emptyIndex(newIndex: => ReadableLuceneIndex) {

    it should "contain no documents" in {
      val luceneIndex = newIndex
      luceneIndex.allDocuments should be('empty)
    }

  }

  def searchableIndex(newIndex: => ReadableLuceneIndex with WritableLuceneIndex) {
    it should "search for a document" in {
      val luceneIndex = newIndex

      luceneIndex.addDocument(documentInIndex)

      val successfulQuery = new TermQuery(new Term("field", "value"))
      val unsuccessfulQuery = new TermQuery(new Term("field", "othervalue"))

      val results = luceneIndex.searchTopDocuments(successfulQuery, 1)
      results should have size 1
      results.head.get("field") should equal("value")

      luceneIndex.searchTopDocuments(unsuccessfulQuery, 1) should be('empty)
    }

    it should "provide a QueryParser for a default field" in {
      val luceneIndex = newIndex
      val qp = luceneIndex.queryParserForDefaultField("afield")
      qp should not equal null
      qp.parse("avalue").toString should equal("afield:avalue")
    }
  }

  def writableIndex(newIndex: => ReadableLuceneIndex with WritableLuceneIndex) {

    it should "add a Document" in {
      val luceneIndex = newIndex

      luceneIndex.addDocument(documentInIndex)

      luceneIndex.allDocuments should have size 1
    }

    it should "delete documents" in {
      val luceneIndex = newIndex

      luceneIndex.addDocument(documentInIndex)

      luceneIndex.allDocuments should have size 1

      luceneIndex.withIndexWriter { indexWriter =>
        indexWriter.deleteDocuments(new Term("field", "value"))
      }

      luceneIndex.allDocuments should have size 0
    }

  }

}
