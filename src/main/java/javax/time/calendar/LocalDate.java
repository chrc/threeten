/*
 * Copyright (c) 2007, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package javax.time.calendar;

import java.io.Serializable;

import javax.time.MathUtils;
import javax.time.calendar.field.DayOfWeek;
import javax.time.calendar.field.MonthOfYear;
import javax.time.period.PeriodView;
import javax.time.period.Periods;

/**
 * A calendrical representation of a date without a time zone, such as 2007-12-03.
 * <p>
 * LocalDate is an immutable calendrical that represents a date, often viewed
 * as year-month-day. This object can also access other date fields such as
 * day of year, day of week and week of year.
 * <p>
 * This class does not store or represent a time or time zone.
 * Thus, for example, the value "2nd October 2007" can be stored in a LocalDate.
 * <p>
 * LocalDate is thread-safe and immutable.
 *
 * @author Stephen Colebourne
 */
public final class LocalDate
        implements ReadableDate, Calendrical, Comparable<LocalDate>, Serializable {

    /**
     * A serialization identifier for this class.
     */
    private static final long serialVersionUID = 798274969L;

    /**
     * The year, from MIN_YEAR to MAX_YEAR.
     */
    private final int year;
    /**
     * The month, from 1 to 12.
     */
    private final int month;
    /**
     * The dayOfMonth, from 1 to 31.
     */
    private final int dayOfMonth;

    //-----------------------------------------------------------------------
    /**
     * Obtains an instance of <code>LocalDate</code> from a year, month and day.
     *
     * @param year  the year to represent, from MIN_VALUE + 1 to MAX_VALUE
     * @param monthOfYear  the month of year to represent, not null
     * @param dayOfMonth  the day of month to represent, from 1 to 31
     * @return a LocalDate object, never null
     * @throws IllegalCalendarFieldValueException if any field is invalid
     */
    public static LocalDate date(int year, MonthOfYear monthOfYear, int dayOfMonth) {
        return date(year, monthOfYear.getMonthOfYear(), dayOfMonth);
    }

    /**
     * Obtains an instance of <code>LocalDate</code> from a year, month and day.
     *
     * @param year  the year to represent, from MIN_VALUE + 1 to MAX_VALUE
     * @param monthOfYear  the month of year to represent, from 1 (January) to 12 (December)
     * @param dayOfMonth  the day of month to represent, from 1 to 31
     * @return a LocalDate object, never null
     * @throws IllegalCalendarFieldValueException if any field is invalid
     */
    public static LocalDate date(int year, int monthOfYear, int dayOfMonth) {
        ISOChronology.INSTANCE.checkValidDate(year, monthOfYear, dayOfMonth);
        return new LocalDate(year, monthOfYear, dayOfMonth);
    }

    /**
     * Obtains an instance of <code>LocalDate</code> from a set of calendricals.
     * <p>
     * This can be used to pass in any combination of calendricals that fully specify
     * a date. For example, Year + MonthOfYear + DayOfMonth, or Year + DayOfYear.
     *
     * @param calendricals  a set of calendricals that fully represent a calendar day
     * @return a LocalDate object, never null
     */
    public static LocalDate date(Calendrical... calendricals) {
        // TODO
        return null;
    }

    //-----------------------------------------------------------------------
    /**
     * Constructor.
     *
     * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
     * @param monthOfYear  the month of year to represent, from 1 (January) to 12 (December)
     * @param dayOfMonth  the day of month to represent, from 1 to 31
     */
    private LocalDate(int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.dayOfMonth = dayOfMonth;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the calendrical state which provides internal access to
     * this date.
     *
     * @return the calendar state for this instance, never null
     */
    @Override
    public CalendricalState getCalendricalState() {
        return null;  // TODO
    }

    /**
     * Gets the chronology that describes the calendar system rules for
     * this date.
     *
     * @return the ISO chronology, never null
     */
    public ISOChronology getChronology() {
        return ISOChronology.INSTANCE;
    }

    //-----------------------------------------------------------------------
    /**
     * Checks if the specified calendar field is supported.
     * <p>
     * This method queries whether this <code>LocalDate</code> can
     * be queried using the specified calendar field.
     *
     * @param field  the field to query, not null
     * @return true if the field is supported
     */
    public boolean isSupported(TimeFieldRule field) {
        return field.isSupported(Periods.DAYS, Periods.FOREVER);
    }

    /**
     * Gets the value of the specified calendar field.
     * <p>
     * This method queries the value of the specified calendar field.
     * If the calendar field is not supported then an exception is thrown.
     *
     * @param field  the field to query, not null
     * @return the value for the field
     * @throws UnsupportedCalendarFieldException if the field is not supported
     */
    public int get(TimeFieldRule field) {
        if (!isSupported(field)) {
            throw new UnsupportedCalendarFieldException(field, "date");
        }
        if (field == ISOChronology.INSTANCE.yearRule()) {
            return year;
        }
        if (field == ISOChronology.INSTANCE.monthOfYearRule()) {
            return month;
        }
        if (field == ISOChronology.INSTANCE.dayOfMonthRule()) {
            return dayOfMonth;
        }
        return field.getValue(getCalendricalState());
    }

    //-----------------------------------------------------------------------
    /**
     * Gets an instance of <code>Year</code> initialised to the
     * year of this date.
     *
     * @return the year object, never null
     */
    public Year year() {
        return Year.isoYear(year);
    }

    /**
     * Gets an instance of <code>MonthOfYear</code> initialised to the
     * month of this date.
     *
     * @return the month object, never null
     */
    public MonthOfYear monthOfYear() {
        return MonthOfYear.monthOfYear(month);
    }

    /**
     * Gets an instance of <code>YearMonth</code> initialised to the
     * year and month of this date.
     *
     * @return the year-month object, never null
     */
    public YearMonth yearMonth() {
        return YearMonth.yearMonth(year, month);
    }

    /**
     * Gets an instance of <code>MonthDay</code> initialised to the
     * month and day of month of this date.
     *
     * @return the month-day object, never null
     */
    public MonthDay monthDay() {
        return MonthDay.monthDay(month, dayOfMonth);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the ISO proleptic year value.
     * <p>
     * The year 1AD is represented by 1.<br />
     * The year 1BC is represented by 0.<br />
     * The year 2BC is represented by -1.<br />
     *
     * @return the year, from MIN_YEAR to MAX_YEAR
     */
    public int getYear() {
        return year;
    }

    /**
     * Gets the month of year value.
     * <p>
     * This method returns the numerical value for the month, from 1 to 12.
     * The enumerated constant is returned by {@link #monthOfYear()}.
     *
     * @return the month of year, from 1 (January) to 12 (December)
     */
    public int getMonthOfYear() {
        return month;
    }

    /**
     * Gets the day of year value.
     *
     * @return the day of year, from 1 to 366
     */
    public int getDayOfYear() {
        return ISOChronology.INSTANCE.getDayOfYear(year, month, dayOfMonth);
    }

    /**
     * Gets the day of month value.
     *
     * @return the day of month, from 1 to 31
     */
    public int getDayOfMonth() {
        return dayOfMonth;
    }

    /**
     * Gets the day of week value.
     *
     * @return the day of week, never null
     */
    public DayOfWeek getDayOfWeek() {
        return null;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this LocalDate with the specified values altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param calendrical  the calendrical values to update to, not null
     * @return a new updated LocalDate, never null
     */
    public LocalDate with(Calendrical calendrical) {
        // TODO
        return null;
    }

    /**
     * Returns a copy of this LocalDate with the specified values altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param calendricals  the calendrical values to update to, no nulls
     * @return a new updated LocalDate, never null
     */
    public LocalDate with(Calendrical... calendricals) {
        // TODO
        return null;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this LocalDate with the year value altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
     * @return a new updated LocalDate, never null
     */
    public LocalDate withYear(int year) {
        if (this.year == year) {
            return this;
        }
        int[] resolved = CalendricalResolvers.previousValid().resolveDate(year, month, dayOfMonth);
        return new LocalDate(resolved[0], resolved[1], resolved[2]);
    }

    /**
     * Returns a copy of this LocalDate with the month of year value altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param monthOfYear  the month of year to represent, from 1 (January) to 12 (December)
     * @return a new updated LocalDate, never null
     */
    public LocalDate withMonthOfYear(int monthOfYear) {
        if (this.month == monthOfYear) {
            return this;
        }
        int[] resolved = CalendricalResolvers.previousValid().resolveDate(year, monthOfYear, dayOfMonth);
        return new LocalDate(resolved[0], resolved[1], resolved[2]);
    }

    /**
     * Returns a copy of this LocalDate with the day of month value altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param dayOfMonth  the day of month to represent, from 1 to 31
     * @return a new updated LocalDate, never null
     */
    public LocalDate withDayOfMonth(int dayOfMonth) {
        if (this.dayOfMonth == dayOfMonth) {
            return this;
        }
        ISOChronology.INSTANCE.checkValidDate(year, month, dayOfMonth);
        return new LocalDate(year, month, dayOfMonth);
    }

    /**
     * Returns a copy of this LocalDate with the date set to the last day of month.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @return a new updated LocalDate, never null
     */
    public LocalDate withLastDayOfMonth() {
        int dom = ISOChronology.INSTANCE.getMonthLength(year, month);
        if (this.dayOfMonth == dom) {
            return this;
        }
        return new LocalDate(year, month, dom);
    }

    /**
     * Returns a copy of this LocalDate with the day of year value altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param dayOfYear  the day of year to represent, from 1 to 366
     * @return a new updated LocalDate, never null
     */
    public LocalDate withDayOfYear(int dayOfYear) {
        return null;
    }

    /**
     * Returns a copy of this LocalDate with the date set to the last day of year.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @return a new updated LocalDate, never null
     */
    public LocalDate withLastDayOfYear() {
        return new LocalDate(year, 12, 31);
    }

    /**
     * Returns a copy of this LocalDate with the day of week value altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param dayOfWeek  the day of week to represent, from 1 (Monday) to 7 (Sunday)
     * @return a new updated LocalDate, never null
     */
    public LocalDate withDayOfWeek(int dayOfWeek) {
        return null;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this LocalDate with the specified period added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param period  the period to add, not null
     * @return a new updated LocalDate, never null
     */
    public LocalDate plus(PeriodView period) {
        // TODO
        return null;
    }

    /**
     * Returns a copy of this LocalDate with the specified periods added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param periods  the periods to add, no nulls
     * @return a new updated LocalDate, never null
     */
    public LocalDate plus(PeriodView... periods) {
        // TODO
        return null;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this LocalDate with the specified period in years added.
     * <p>
     * This method add the specified amount to the years field in three steps:
     * <ol>
     * <li>Add the input years to the year field</li>
     * <li>Check if the resulting date would be invalid</li>
     * <li>Adjust the day of month to the last valid day if necessary</li>
     * </ol>
     * <p>
     * For example, 2008-02-29 (leap year) plus one year would result in the
     * invalid date 2009-02-29 (standard year). Instead of returning an invalid
     * result, the last valid day of the month, 2009-02-28, is selected instead.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param years  the years to add, may be negative
     * @return a new updated LocalDate, never null
     * @throws ArithmeticException if the calculation overflows
     * @throws IllegalCalendarFieldValueException if the result contains an invalid field
     */
    public LocalDate plusYears(int years) {
        if (years == 0) {
            return this;
        }
        long newYear = ((long) year) + years;
        if (newYear < Year.MIN_YEAR || newYear > Year.MAX_YEAR) {
            throw new IllegalCalendarFieldValueException("Year", newYear, Year.MIN_YEAR, Year.MAX_YEAR);
        }
        return withYear((int) newYear);
    }

    /**
     * Returns a copy of this LocalDate with the specified period in months added.
     * <p>
     * This method add the specified amount to the months field in three steps:
     * <ol>
     * <li>Add the input months to the month of year field</li>
     * <li>Check if the resulting date would be invalid</li>
     * <li>Adjust the day of month to the last valid day if necessary</li>
     * </ol>
     * <p>
     * For example, 2007-03-31 plus one month would result in the invalid date
     * 2007-04-31. Instead of returning an invalid result, the last valid day
     * of the month, 2007-04-30, is selected instead.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param months  the months to add, may be negative
     * @return a new updated LocalDate, never null
     * @throws ArithmeticException if the calculation overflows
     * @throws IllegalCalendarFieldValueException if the result contains an invalid field
     */
    public LocalDate plusMonths(int months) {
        if (months == 0) {
            return this;
        }
        long newMonth0 = month - 1;
        newMonth0 = newMonth0 + months;
        int years = (int) (newMonth0 / 12);
        newMonth0 = newMonth0 % 12;
        if (newMonth0 < 0) {
            newMonth0 += 12;
            years--;
        }
        int newYear = MathUtils.safeAdd(year, years);
        int[] resolved = CalendricalResolvers.previousValid().resolveDate(newYear, (int) ++newMonth0, dayOfMonth);
        return new LocalDate(resolved[0], resolved[1], resolved[2]);
    }

    /**
     * Returns a copy of this LocalDate with the specified period in weeks added.
     * <p>
     * This method add the specified amount in weeks to the days field incrementing
     * the month and year fields as necessary to ensure the result remains valid.
     * The result is only invalid if the maximum/minimum year is exceeded.
     * <p>
     * For example, 2008-12-31 plus one week would result in the 2009-01-07.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param weeks  the weeks to add, may be negative
     * @return a new updated LocalDate, never null
     * @throws ArithmeticException if the calculation overflows
     */
    public LocalDate plusWeeks(int weeks) {
        return plusDays(7L * weeks);
    }

    /**
     * Returns a copy of this LocalDate with the specified period in days added.
     * <p>
     * This method add the specified amount to the days field incrementing the
     * month and year fields as necessary to ensure the result remains valid.
     * The result is only invalid if the maximum/minimum year is exceeded.
     * <p>
     * For example, 2008-12-31 plus one day would result in the 2009-01-01.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param days  the days to add, may be negative
     * @return a new updated LocalDate, never null
     */
    public LocalDate plusDays(int days) {
        return plusDays((long) days);
    }

    /**
     * Returns a copy of this LocalDate with the specified number of days added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param days  the days to add
     * @return a new updated LocalDate, never null
     */
    private LocalDate plusDays(long days) {
        if (days == 0) {
            return this;
        }
        int monthLen = ISOChronology.INSTANCE.getMonthLength(year, month);
        long possDOM = dayOfMonth + days;
        if (possDOM >= 1) {
            if (possDOM <= monthLen) {
                // same month
                return new LocalDate(year, month, (int) possDOM);
            } else if (possDOM <= monthLen + 28) {
                // next month (28 guarantees only one month later)
                possDOM -= monthLen;
                if (month == 12) {
                    return new LocalDate(MathUtils.safeIncrement(year), 1, (int) possDOM);
                } else {
                    return new LocalDate(year, month + 1, (int) possDOM);
                }
            }
        }
        long epochDays = 0L;
        epochDays += days;
        return null;  // TODO
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this date to a <code>LocalDate</code>, trivially
     * returning <code>this</code>.
     *
     * @return <code>this</code>, never null
     */
    public LocalDate toLocalDate() {
        return this;
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this date to another date.
     *
     * @param other  the other date to compare to, not null
     * @return the comparator value, negative if less, postive if greater
     * @throws NullPointerException if <code>other</code> is null
     */
    public int compareTo(LocalDate other) {
        int cmp = MathUtils.safeCompare(year, other.year);
        if (cmp == 0) {
            cmp = MathUtils.safeCompare(month, other.month);
            if (cmp == 0) {
                cmp = MathUtils.safeCompare(dayOfMonth, other.dayOfMonth);
            }
        }
        return cmp;
    }

    /**
     * Is this date after the specified date.
     *
     * @param other  the other date to compare to, not null
     * @return true if this is after the specified date
     * @throws NullPointerException if <code>other</code> is null
     */
    public boolean isAfter(LocalDate other) {
        return compareTo(other) > 0;
    }

    /**
     * Is this date before the specified date.
     *
     * @param other  the other date to compare to, not null
     * @return true if this point is before the specified date
     * @throws NullPointerException if <code>other</code> is null
     */
    public boolean isBefore(LocalDate other) {
        return compareTo(other) < 0;
    }

    //-----------------------------------------------------------------------
    /**
     * Is this date equal to the specified date.
     *
     * @param other  the other date to compare to, null returns false
     * @return true if this point is equal to the specified date
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof LocalDate) {
            LocalDate otherDate = (LocalDate) other;
            return (year == otherDate.year && month == otherDate.month && dayOfMonth == otherDate.dayOfMonth);
        }
        return false;
    }

    /**
     * A hashcode for this date.
     *
     * @return a suitable hashcode
     */
    @Override
    public int hashCode() {
        return (year & 0xFFFFF800) ^ ((year << 11) + (month << 6) + (dayOfMonth));
    }

    //-----------------------------------------------------------------------
    /**
     * Outputs the string form of the date.
     *
     * @return the string form of the date
     */
    @Override
    public String toString() {
        int absYear = Math.abs(year);
        StringBuilder buf = new StringBuilder(12);
        if (absYear < 1000) {
            if (year < 0) {
                buf.append(year - 10000).deleteCharAt(1);
            } else {
                buf.append(year + 10000).deleteCharAt(0);
            }
        } else {
            buf.append(year);
        }
        return buf.append(month < 10 ? "-0" : "-")
            .append(month)
            .append(dayOfMonth < 10 ? "-0" : "-")
            .append(dayOfMonth)
            .toString();
    }

}