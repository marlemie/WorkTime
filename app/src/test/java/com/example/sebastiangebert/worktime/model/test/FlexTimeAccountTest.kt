package com.example.sebastiangebert.worktime.model.test

import com.example.sebastiangebert.worktime.infrastructure.FlexTimeRepository
import com.example.sebastiangebert.worktime.model.FlexTimeAccount
import org.joda.time.DateTime
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FlexTimeAccountTest {
    companion object TestRepo : FlexTimeRepository {
        var Entries: MutableList<DateTime> = MutableList(0, { DateTime() })

        override fun write(timestamp: DateTime) {
            this.Entries.add(timestamp)
        }

        override fun readAll() = this.Entries.toList()
    }

    val account = FlexTimeAccount(TestRepo)
    val start = "1990-01-01"

    @Before
    fun Setup() {
        TestRepo.Entries = MutableList(0, { DateTime() })
    }

    @Test
    fun NoEntries_Get_ReturnEmptyList() {
        val expected = List(0, { DateTime() })

        val actual = account.Entries

        assertEquals(expected, actual)
    }

    @Test
    fun SingleEntry_Get_ReturnSingleEntry() {
        val expected = List(1, { DateTime() })
        TestRepo.Entries = expected.toMutableList()

        val actual = account.Entries

        assertEquals(expected, actual)
    }

    @Test
    fun MultipleEntries_Get_ReturnAllEntries() {
        val expected = this.testValues(1000)
        TestRepo.Entries = expected.toMutableList()

        val actual = account.Entries

        assertEquals(expected, actual)
    }

    @Test
    fun NoEntries_GetWithoutParameters_ReturnEmptyList() {
        val expected = List(0, { DateTime() })

        val actual = account.entriesInPeriod()

        assertEquals(expected, actual)
    }

    @Test
    fun NoEntries_AddOne_ReturnOneEntry() {
        val date = DateTime(this.start)
        val expected = listOf(date)

        account.add(date)
        val actual = account.Entries

        assertEquals(expected, actual)
    }

    @Test
    fun OneEntry_AddOne_ReturnTwoEntries() {
        TestRepo.write(DateTime(this.start))
        val expected = this.testValues(2)

        account.add(DateTime(this.start).plusDays(1))
        val actual = account.Entries

        assertEquals(expected, actual)
    }

    @Test
    fun OneEntry_AddMultiple_ReturnOnePlusMultipleEntries() {
        TestRepo.write(DateTime(this.start))
        val expected = this.testValues(1001)

        List(1000, { DateTime(this.start).plusDays(it + 1) })
                .forEach { account.add(it) }
        val actual = account.Entries

        assertEquals(expected, actual)
    }

    @Test
    fun MultipleEntries_AddOne_ReturnMultiplePlusOneEntries() {
        this.testValues(1000)
                .forEach { TestRepo.write(it) }
        val expected = TestRepo.Entries
                .plus(DateTime(this.start).plusDays(1000))

        account.add(DateTime(this.start).plusDays(1000))
        val actual = account.Entries

        assertEquals(expected, actual)
    }

    @Test
    fun MultipleEntries_AddMultiple_ReturnMultiplePlusMultipleEntries() {
        this.testValues(1000)
                .forEach { TestRepo.write(it) }
        val expected = this.testValues(1000)
                .plus(TestRepo.Entries)

        this.testValues(1000)
                .forEach { account.add(it) }
        val actual = account.Entries

        assertEquals(expected, actual)
    }

    fun testValues(days: Int) = List(days, { DateTime(this.start).plusDays(it) })
}
