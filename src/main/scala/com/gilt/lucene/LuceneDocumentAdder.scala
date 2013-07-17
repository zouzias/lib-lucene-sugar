package com.gilt.lucene

import javax.annotation.Nonnull
import scala.annotation.implicitNotFound
import org.apache.lucene.document.Document
import scala.collection.JavaConverters._
import LuceneFieldHelpers._

object LuceneDocumentAdder {

  /**
   * A type class that provides logic to convert an object f
   * type T to a collection of Lucene Document objects
   */
  @implicitNotFound("No member of type class LuceneDocumentLike in scope for ${T}")
  trait LuceneDocumentLike[T] {

    /**
     * Convert object of type T to a Lucene document
     */
    def toDocuments(v: T): Iterable[Document]

  }

  /**
   * Implementation of LuceneDocumentLike for collection of Lucene Documents
   */
  implicit object TransparentLuceneDocumentLike extends LuceneDocumentLike[Document] {
    def toDocuments(v: Document) = Seq(v)
  }

  type StringStringMap = Map[String, Any]
  implicit object StringStringMapLuceneDocumentLike extends LuceneDocumentLike[StringStringMap] {
    def toDocuments(map: StringStringMap): Iterable[Document] = {
      val doc = new Document
      map.foreach { case (k, v) =>
        doc.addIndexedStoredField(k, v.toString)
      }
      Seq(doc)
    }
  }

}

trait LuceneDocumentAdder { self: LuceneIndexWriter =>
  import LuceneDocumentAdder.LuceneDocumentLike

  /**
   * Adds a collection of documents to the Index
   */
  def addDocuments[T: LuceneDocumentLike](@Nonnull docs: Iterable[T]): Unit =
    withIndexWriter { indexWriter =>
      val documents = docs.flatMap { doc => implicitly[LuceneDocumentLike[T]].toDocuments(doc) }
      indexWriter.addDocuments(documents.asJava)
    }

  /**
   * Adds a single document to the index
   */
  def addDocument[T: LuceneDocumentLike](@Nonnull doc: T): Unit =
    addDocuments[T](Seq(doc))

}
