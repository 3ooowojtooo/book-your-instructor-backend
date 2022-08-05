package com.quary.bookyourinstructor.controller.event.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quary.bookyourinstructor.model.event.EventType;
import com.quary.bookyourinstructor.model.filter.search.TextSearchCategory;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class SearchEventsRequest {

    private DateRangeFilter dateRange;
    private TextSearchFilter textSearch;
    private EventTypeFilter eventType;

    @Data
    public static class DateRangeFilter {

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "UTC")
        private LocalDateTime from;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "UTC")
        private LocalDateTime to;
    }

    @Data
    public static class TextSearchFilter {

        private Set<TextSearchCategory> selectedCategories;
        private String searchText;
    }

    @Data
    public static class EventTypeFilter {

        private EventType eventType;
    }
}
