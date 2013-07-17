package com.gilt.lucene

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar
import org.apache.lucene.document.Document
import LuceneFieldHelpers._
import LuceneText._

class LuceneFieldHelpersSuite extends FlatSpec with ShouldMatchers with MockitoSugar {

  def searchableReadableField[T: LuceneFieldLike](value: T) {

    it should "add an indexed only field" in {
      val doc = new Document
      doc.addIndexedOnlyField("field", value)
      val field = doc.getField("field")
      field.stringValue() should equal(value.toString)
      field.fieldType() should be ('indexed)
      field.fieldType() should not be ('stored)
    }

    it should "add a stored only field" in {
      val doc = new Document
      doc.addStoredOnlyField("field", value)
      val field = doc.getField("field")
      field.stringValue() should equal(value.toString)
      field.fieldType() should not be ('indexed)
      field.fieldType() should be ('stored)
    }

    it should "add an indexed and stored field" in {
      val doc = new Document
      doc.addIndexedStoredField("field", value)
      val field = doc.getField("field")
      field.stringValue() should equal(value.toString)
      field.fieldType() should be ('indexed)
      field.fieldType() should be ('stored)
    }

  }

  "String helper" should behave like searchableReadableField("string")

  "Int helper" should behave like searchableReadableField(10)

  "Long helper" should behave like searchableReadableField(1000000L)

  "Text helper" should behave like searchableReadableField("text".toLuceneText)

}
