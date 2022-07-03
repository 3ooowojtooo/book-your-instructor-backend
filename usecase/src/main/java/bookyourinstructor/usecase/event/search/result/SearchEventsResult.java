package bookyourinstructor.usecase.event.search.result;

import lombok.Getter;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Getter
public class SearchEventsResult {

    private final List<SearchEventsResultItem> events;

    public SearchEventsResult(List<SearchEventsResultItem> events) {
        validateConstructorArgs(events);
        this.events = events;
    }

    private static void validateConstructorArgs(List<SearchEventsResultItem> events) {
        checkNotNull(events, "Searched events list cannot be null");
    }
}
