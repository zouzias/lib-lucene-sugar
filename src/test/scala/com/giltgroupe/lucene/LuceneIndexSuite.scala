package com.giltgroupe.lucene

import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.matchers.ShouldMatchers
import org.apache.lucene.document.{StringField, Document}
import org.apache.lucene.document.Field.Store
import org.apache.lucene.search.TermQuery
import org.apache.lucene.index.Term

class LuceneIndexSuite extends FunSuite with BeforeAndAfter with ShouldMatchers {

  var luceneIndex: LuceneIndex = _
  val documentInIndex: Document = {
    val doc = new Document
    doc.add(new StringField("field", "value", Store.YES))
    doc
  }

  before {
    luceneIndex = new LuceneIndex with LuceneStandardAnalyzer with RamLuceneDirectory
  }

  test("should contain no documents") {
    luceneIndex.allDocuments should be('empty)
  }

  test("provides an IndexWriter") {
    luceneIndex.withIndexWriter { indexWriter =>
      luceneIndex should not equals null
    }
  }

  test("can add a Document") {
    luceneIndex.withIndexWriter { indexWriter =>
      indexWriter.addDocument(documentInIndex)
    }

    luceneIndex.allDocuments should have size 1
  }

  test("can search for a Document") {
    luceneIndex.withIndexWriter { indexWriter =>
      indexWriter.addDocument(documentInIndex)
    }

    val successfulQuery = new TermQuery(new Term("field", "value"))
    val unsuccessfulQuery = new TermQuery(new Term("field", "othervalue"))

    val results = luceneIndex.searchTopDocuments(successfulQuery, 1)
    results should have size 1
    results.head.get("field") should equal("value")

    luceneIndex.searchTopDocuments(unsuccessfulQuery, 1) should be('empty)
  }

  test("should provide a QueryParser for a default field") {
    val qp = luceneIndex.queryParserForDefaultField("afield")
    qp should not equal null
    qp.parse("avalue").toString should equal("afield:avalue")
  }

  test("should allow deletion of documents") {
    luceneIndex.withIndexWriter { indexWriter =>
      indexWriter.addDocument(documentInIndex)
    }

    luceneIndex.allDocuments should have size 1

    luceneIndex.withIndexWriter { indexWriter =>
      indexWriter.deleteDocuments(new Term("field", "value"))
    }

    luceneIndex.allDocuments should have size 0
  }

}