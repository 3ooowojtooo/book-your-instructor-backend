package bookyourinstructor.usecase.event.search;

import bookyourinstructor.usecase.event.common.store.EventStore;
import bookyourinstructor.usecase.event.search.data.SearchEventsData;
import bookyourinstructor.usecase.event.search.result.SearchEventsResult;
import bookyourinstructor.usecase.event.search.result.SearchEventsResultItem;
import bookyourinstructor.usecase.util.time.TimeUtils;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
public class SearchEventsUseCase {

    private final EventStore eventStore;
    private final TimeUtils timeUtils;

    public SearchEventsResult searchEvents(SearchEventsData data) {
        Instant now = timeUtils.nowInstant();
        List<SearchEventsResultItem> events = eventStore.searchEvents(data.getDateRange(), data.getTextSearch(), data.getEventType(), now);
        return new SearchEventsResult(events);
    }
}
