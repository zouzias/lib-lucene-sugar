package com.gilt.lucene

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.mock.MockitoSugar
import org.apache.lucene.document.{Field, Document}
import LuceneFieldHelpers._
import LuceneText._
import org.apache.lucene.index.IndexableField
import scala.language.reflectiveCalls

class LuceneFieldHelpersSuite extends FlatSpec with Matchers with MockitoSugar {

  def searchableReadableField[T: LuceneFieldLike](value: T, valueReader: IndexableField => T) {

    it should "add an indexed only field" in {
      val doc = new Document
      doc.addIndexedOnlyField("field", value)
      val field = doc.getField("field")
      valueReader(field) should equal(value)
      field.fieldType() should be ('indexed)
      field.fieldType() should not be ('stored)
    }

    it should "add a stored only field" in {
      val doc = new Document
      doc.addStoredOnlyField("field", value)
      val field = doc.getField("field")
      valueReader(field) should equal(value)
      field.fieldType() should not be ('indexed)
      field.fieldType() should be ('stored)
    }

    it should "add an indexed and stored field" in {
      val doc = new Document
      doc.addIndexedStoredField("field", value)
      val field = doc.getField("field")
      valueReader(field) should equal(value)
      field.fieldType() should be ('indexed)
      field.fieldType() should be ('stored)
    }

  }

  "String helper" should behave like searchableReadableField("string", _.stringValue())

  "Text helper" should behave like searchableReadableField("text".toLuceneText, { f => LuceneText(f.stringValue()) })

  "Int helper" should behave like searchableReadableField(10, _.numericValue().intValue())

  "Long helper" should behave like searchableReadableField(1000000L, _.numericValue().longValue())

  "Float helper" should behave like searchableReadableField(12.1f, _.numericValue().floatValue())

  "Double helper" should behave like searchableReadableField(12.1d, _.numericValue().doubleValue())

}
