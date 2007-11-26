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
import javax.time.calendar.field.MonthOfYear;
import javax.time.period.PeriodView;
import javax.time.period.Periods;

/**
 * A calendrical representation of a year and month without a time zone.
 * <p>
 * YearMonth is an immutable calendrical that represents a year-month combination.
 * This class does not store or represent a day, time or time zone.
 * Thus, for example, the value "October 2007" can be stored in a YearMonth.
 * <p>
 * Static factory methods allow you to constuct instances.
 * <p>
 * YearMonth is thread-safe and immutable.
 *
 * @author Stephen Colebourne
 */
public final class YearMonth
        implements Calendrical, Comparable<YearMonth>, Serializable {

    /**
     * A serialization identifier for this class.
     */
    private static final long serialVersionUID = 1507289123L;

    /**
     * The year, from MIN_YEAR to MAX_YEAR.
     */
    private final int year;
    /**
     * The month, from 1 to 12.
     */
    private final int month;

    //-----------------------------------------------------------------------
    /**
     * Obtains an instance of <code>YearMonth</code>.
     *
     * @param year  the year to represent, from MIN_VALUE + 1 to MAX_VALUE
     * @param monthOfYear  the month of year to represent, not null
     * @return a YearMonth object, never null
     * @throws IllegalCalendarFieldValueException if any field is invalid
     */
    public static YearMonth yearMonth(int year, MonthOfYear monthOfYear) {
        ISOChronology.INSTANCE.yearRule().checkValue(year);
        return new YearMonth(year, monthOfYear.getMonthOfYear());
    }

    /**
     * Obtains an instance of <code>YearMonth</code>.
     *
     * @param year  the year to represent, from MIN_VALUE + 1 to MAX_VALUE
     * @param monthOfYear  the month of year to represent, from 1 (January) to 12 (December)
     * @return a YearMonth object, never null
     * @throws IllegalCalendarFieldValueException if any field is invalid
     */
    public static YearMonth yearMonth(int year, int monthOfYear) {
        ISOChronology.INSTANCE.yearRule().checkValue(year);
        ISOChronology.INSTANCE.monthOfYearRule().checkValue(monthOfYear);
        return new YearMonth(year, monthOfYear);
    }

    /**
     * Obtains an instance of <code>YearMonth</code> from a set of calendricals.
     * <p>
     * This can be used to pass in any combination of calendricals that fully specify
     * a calendar month. For example, Year + MonthOfYear.
     *
     * @param calendricals  a set of calendricals that fully represent a calendar month
     * @return a YearMonth object, never null
     */
    public static YearMonth yearMonth(Calendrical... calendricals) {
        return new YearMonth(0, 0);  // TODO
    }

    //-----------------------------------------------------------------------
    /**
     * Constructor.
     *
     * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
     * @param monthOfYear  the month of year to represent, from 1 (January) to 12 (December)
     */
    private YearMonth(int year, int monthOfYear) {
        this.year = year;
        this.month = monthOfYear;
    }

    /**
     * Returns a copy of this year-month with the new year and month, checking
     * to see if a new object is in fact required.
     *
     * @param newYear  the year to represent, from MIN_YEAR to MAX_YEAR
     * @param newMonth  the month of year to represent, from 1 (January) to 12 (December)
     * @return the year-month, never null
     */
    private YearMonth withYearMonth(int newYear, int newMonth) {
        if (year == newYear && month == newMonth) {
            return this;
        }
        return new YearMonth(newYear, newMonth);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the calendrical state which provides internal access to
     * this year-month.
     *
     * @return the calendar state for this instance, never null
     */
    @Override
    public CalendricalState getCalendricalState() {
        return null;  // TODO
    }

    /**
     * Gets the chronology that describes the calendar system rules for
     * this year-month.
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
     * This method queries whether this <code>YearMonth</code> can
     * be queried using the specified calendar field.
     *
     * @param field  the field to query, not null
     * @return true if the field is supported
     */
    public boolean isSupported(TimeFieldRule field) {
        return field.isSupported(Periods.MONTHS, Periods.FOREVER);
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
            throw new UnsupportedCalendarFieldException(field, "year-month");
        }
        if (field == ISOChronology.INSTANCE.yearRule()) {
            return year;
        }
        if (field == ISOChronology.INSTANCE.monthOfYearRule()) {
            return month;
        }
        return field.getValue(getCalendricalState());
    }

    //-----------------------------------------------------------------------
    /**
     * Gets an instance of <code>Year</code> initialised to the
     * year of this year-month.
     *
     * @return the year object, never null
     */
    public Year year() {
        return Year.isoYear(year);
    }

    /**
     * Gets an instance of <code>MonthOfYear</code> initialised to the
     * month of this year-month.
     *
     * @return the month object, never null
     */
    public MonthOfYear monthOfYear() {
        return MonthOfYear.monthOfYear(month);
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

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this YearMonth with the specified values altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param calendrical  the calendrical values to update to, not null
     * @return a new updated YearMonth, never null
     */
    public YearMonth with(Calendrical calendrical) {
        if (calendrical instanceof Year) {
            return withYearMonth(((Year) calendrical).getISOYear(), month);
        }
        if (calendrical instanceof MonthOfYear) {
            return withYearMonth(year, ((MonthOfYear) calendrical).getMonthOfYear());
        }
        if (calendrical instanceof YearMonth) {
            return (YearMonth) calendrical;
        }
        // TODO
        return null;
    }

    /**
     * Returns a copy of this YearMonth with the specified values altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param calendricals  the calendrical values to update to, no nulls
     * @return a new updated YearMonth, never null
     */
    public YearMonth with(Calendrical... calendricals) {
        return null;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this YearMonth with the year value altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
     * @return a new updated YearMonth, never null
     */
    public YearMonth withYear(int year) {
        ISOChronology.INSTANCE.yearRule().checkValue(year);
        return withYearMonth(year, month);
    }

    /**
     * Returns a copy of this YearMonth with the month of year value altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param monthOfYear  the month of year to represent, from 1 (January) to 12 (December)
     * @return a new updated YearMonth, never null
     */
    public YearMonth withMonthOfYear(int monthOfYear) {
        ISOChronology.INSTANCE.monthOfYearRule().checkValue(monthOfYear);
        return withYearMonth(year, monthOfYear);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this YearMonth with the specified period added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param period  the period to add, not null
     * @return a new updated YearMonth, never null
     */
    public YearMonth plus(PeriodView period) {
        // TODO
        return null;
    }

    /**
     * Returns a copy of this YearMonth with the specified periods added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param periods  the periods to add, no nulls
     * @return a new updated YearMonth, never null
     */
    public YearMonth plus(PeriodView... periods) {
        // TODO
        return null;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this YearMonth with the specified period in years added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param years  the years to add, may be negative
     * @return a new updated YearMonth, never null
     * @throws ArithmeticException if the calculation overflows
     * @throws IllegalCalendarFieldValueException if the result contains an invalid field
     */
    public YearMonth plusYears(int years) {
        if (years == 0) {
            return this;
        }
        int newYear = MathUtils.safeAdd(year, years);
        return withYear(newYear);
    }

    /**
     * Returns a copy of this YearMonth with the specified period in months added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param months  the months to add, may be negative
     * @return a new updated YearMonth, never null
     * @throws ArithmeticException if the calculation overflows
     * @throws IllegalCalendarFieldValueException if the result contains an invalid field
     */
    public YearMonth plusMonths(int months) {
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
        ISOChronology.INSTANCE.yearRule().checkValue(year);
        return withYearMonth(newYear, (int) ++newMonth0);
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this year-month to another year-month.
     *
     * @param other  the other year-month to compare to, not null
     * @return the comparator value, negative if less, postive if greater
     * @throws NullPointerException if <code>other</code> is null
     */
    public int compareTo(YearMonth other) {
        int cmp = MathUtils.safeCompare(year, other.year);
        if (cmp == 0) {
            cmp = MathUtils.safeCompare(month, other.month);
        }
        return cmp;
    }

    /**
     * Is this year-month after the specified year-month.
     *
     * @param other  the other year-month to compare to, not null
     * @return true if this is after the specified year-month
     * @throws NullPointerException if <code>other</code> is null
     */
    public boolean isAfter(YearMonth other) {
        return compareTo(other) > 0;
    }

    /**
     * Is this year-month before the specified year-month.
     *
     * @param other  the other year-month to compare to, not null
     * @return true if this point is before the specified year-month
     * @throws NullPointerException if <code>other</code> is null
     */
    public boolean isBefore(YearMonth other) {
        return compareTo(other) < 0;
    }

    //-----------------------------------------------------------------------
    /**
     * Is this year-month equal to the specified year-month.
     *
     * @param other  the other year-month to compare to, null returns false
     * @return true if this point is equal to the specified year-month
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof YearMonth) {
            YearMonth otherYM = (YearMonth) other;
            return year == otherYM.year && month == otherYM.month;
        }
        return false;
    }

    /**
     * A hashcode for this year-month.
     *
     * @return a suitable hashcode
     */
    @Override
    public int hashCode() {
        return year ^ (month << 27);
    }

    //-----------------------------------------------------------------------
    /**
     * Outputs the year-month as a <code>String</code>.
     * <p>
     * The output will be in the format 'yyyy-MM':
     *
     * @return the string form of the year-month
     */
    @Override
    public String toString() {
        int absYear = Math.abs(year);
        StringBuilder buf = new StringBuilder(9);
        if (absYear < 1000) {
            if (year < 0) {
                buf.append(year - 10000).deleteCharAt(1);
            } else {
                buf.append(year + 10000).deleteCharAt(0);
            }
        } else {
            buf.append(year);
        }
        return buf.append(month < 10 ? "-0" : "-").append(month)
            .toString();
    }

}