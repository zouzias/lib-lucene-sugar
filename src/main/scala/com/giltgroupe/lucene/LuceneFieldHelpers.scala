package com.giltgroupe.scala.lucene

import org.apache.lucene.document.{LongField, StoredField, StringField, Document}
import org.apache.lucene.document.Field.Store


object LuceneFieldHelpers {
  import annotation.implicitNotFound

  @implicitNotFound("No member of type class LuceneFieldLike in scope for ${T}")
  trait LuceneFieldLike[T] {
    def addIndexedField(doc: Document, name: String, value: T, stored: Store)
    def addStoredOnlyField(doc: Document, name: String, value: T)
  }

  object LuceneFieldLike {

    implicit object LuceneFieldLikeString extends LuceneFieldLike[String] {
      def addIndexedField(doc: Document, name: String, value: String, stored: Store) {
        doc.add(new StringField(name, value, stored))
      }

      def addStoredOnlyField(doc: Document, name: String, value: String) {
        doc.add(new StoredField(name, value))
      }
    }

    implicit object LuceneFieldLikeLong extends LuceneFieldLike[Long] {
      def addIndexedField(doc: Document, name: String, value: Long, stored: Store) {
        doc.add(new LongField(name, value, stored))
      }

      def addStoredOnlyField(doc: Document, name: String, value: Long) {
        doc.add(new StoredField(name, value))
      }
    }

  }

  /**
   * Wraps a Lucene Document with utility methods to make it less verbose to
   * add new fields
   */
  implicit def documentWrapper(doc: Document) = new {

    /**
     * Adds a new STORED field
     */
    def addIndexedStoredField[T: LuceneFieldLike](name: String, value: T): Document = {
      implicitly[LuceneFieldLike[T]].addIndexedField(doc, name, value, Store.YES)
      doc
    }

    def addIndexedStoredField[T: LuceneFieldLike](name: String, valueOpt: Option[T]): Document = {
      valueOpt.foreach { value => implicitly[LuceneFieldLike[T]].addIndexedField(doc, name, value, Store.YES) }
      doc
    }

    def addStoredOnlyField[T: LuceneFieldLike](name: String, value: T): Document = {
      implicitly[LuceneFieldLike[T]].addStoredOnlyField(doc, name, value)
      doc
    }

    def addStoredOnlyField[T: LuceneFieldLike](name: String, valueOpt: Option[T]): Document = {
      valueOpt.foreach { value => implicitly[LuceneFieldLike[T]].addStoredOnlyField(doc, name, value) }
      doc
    }

    /**
     * Adds a new NOT_STORED field
     */
    def addIndexedOnlyField[T: LuceneFieldLike](name: String, value: T): Document = {
      implicitly[LuceneFieldLike[T]].addIndexedField(doc, name, value, Store.NO)
      doc
    }

    def addIndexedOnlyField[T: LuceneFieldLike](name: String, valueOpt: Option[T]): Document = {
      valueOpt.foreach { value => implicitly[LuceneFieldLike[T]].addIndexedField(doc, name, value, Store.NO) }
      doc
    }

  }

}
