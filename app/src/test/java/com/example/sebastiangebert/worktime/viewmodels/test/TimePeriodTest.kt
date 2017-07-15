package com.example.sebastiangebert.worktime.viewmodels.test

import com.example.sebastiangebert.worktime.viewmodels.TimePeriod
import org.joda.time.DateTime
import org.junit.Assert
import org.junit.Test

class TimePeriodTest {
    @Test
    fun NoEndPointsSet_ReturnAllDateTimes() {
        val dates = arrayOf(
                DateTime("2017-01-02T08:00:00"),
                DateTime("2017-01-02T17:00:00"),
                DateTime("2017-01-03T09:01:15"),
                DateTime("2017-01-03T17:35:26")
        )
        val expected = dates
        val testInstance = TimePeriod(RepoMock(expected.toMutableList()))

        val actual = testInstance.All.toTypedArray()

        Assert.assertArrayEquals(expected, actual)
    }

    @Test
    fun MoveStartAhead_DontReturnEarlierEntries() {
        val dates = arrayOf(
                DateTime("2017-01-02T08:00:00"),
                DateTime("2017-01-02T17:00:00"),
                DateTime("2017-01-03T09:01:15"),
                DateTime("2017-01-03T17:35:26")
        )
        val expected = dates.copyOfRange(2, 4)
        val testInstance = TimePeriod(RepoMock(dates.toMutableList()))

        testInstance.Start = DateTime("2017-01-03")
        val actual = testInstance.All.toTypedArray()

        Assert.assertArrayEquals(expected, actual)
    }

    @Test
    fun MoveStartBeyondLastEntry_ReturnEmpty() {
        val dates = arrayOf(
                DateTime("2017-01-02T08:00:00"),
                DateTime("2017-01-02T17:00:00")
        )
        val expected = Array<DateTime>(0, { DateTime() })
        val testInstance = TimePeriod(RepoMock(dates.toMutableList()))

        testInstance.Start = DateTime("2017-01-03")
        val actual = testInstance.All.toTypedArray()

        Assert.assertArrayEquals(expected, actual)
    }

    @Test
    fun MoveEndBeyondLastEntry_ReturnAll() {
        val dates = arrayOf(
                DateTime("2017-01-02T08:00:00"),
                DateTime("2017-01-02T17:00:00")
        )
        val expected = dates
        val testInstance = TimePeriod(RepoMock(dates.toMutableList()))

        testInstance.End = DateTime("2017-01-03")
        val actual = testInstance.All.toTypedArray()

        Assert.assertArrayEquals(expected, actual)
    }

    @Test
    fun MoveEndBack_DontReturnLaterEntries() {
        val dates = arrayOf(
                DateTime("2017-01-02T08:00:00"),
                DateTime("2017-01-02T17:00:00"),
                DateTime("2017-01-03T09:01:15"),
                DateTime("2017-01-03T17:35:26")
        )
        val expected = dates
                .filter { it < DateTime("2017-01-03") }
                .toTypedArray()
        val testInstance = TimePeriod(RepoMock(dates.toMutableList()))

        testInstance.End = DateTime("2017-01-03")
        val actual = testInstance.All.toTypedArray()

        Assert.assertArrayEquals(expected, actual)
    }

    @Test
    fun EmptyRepository_StartIsEpoch() {
        val expected = DateTime(0)
        val testInstance = TimePeriod(RepoMock(mutableListOf<DateTime>()))

        val actual = testInstance.Start

        Assert.assertTrue(actual.isEqual(expected))
    }

    @Test
    fun EmptyRepository_EndIsEpoch() {
        val expected = DateTime(0)
        val testInstance = TimePeriod(RepoMock(mutableListOf<DateTime>()))

        val actual = testInstance.End

        Assert.assertTrue(actual.isEqual(expected))
    }

    @Test
    fun EmptyRepository_AllIsEmpty() {
        val expected = emptyArray<DateTime>()
        val testInstance = TimePeriod(RepoMock(mutableListOf<DateTime>()))

        val actual = testInstance.All.toArray()

        Assert.assertArrayEquals(expected, actual)
    }
}