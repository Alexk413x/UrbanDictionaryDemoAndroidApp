package com.alexk413x.mobile.urbandictionarydemo;

import com.alexk413x.mobile.urbandictionarydemo.Utils.DateUtils;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class WordSearchUnitTest {

    private DateUtils dateUtils;

    @Before
    public void setUp() {
        dateUtils = new DateUtils();
    }

    @Test
    public void createDateFromString_DatesMatch() {
        Calendar resultDate = dateUtils.createDateFromString("2019-01-01T00:00:00.000Z");

        Calendar expectedDate = Calendar.getInstance();
        expectedDate.setTime(new Date(1546156800000L));

        assert expectedDate.equals(resultDate);
    }
}