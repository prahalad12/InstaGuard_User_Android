package com.instaguard.user.Helpers.HorizontalCalendar.utils;

import com.instaguard.user.Helpers.HorizontalCalendar.model.CalendarEvent;

import java.util.Calendar;
import java.util.List;


public interface CalendarEventsPredicate {

    List<CalendarEvent> events(Calendar date);
}
