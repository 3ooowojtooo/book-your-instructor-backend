package bookyourinstructor.usecase.event.search;

import bookyourinstructor.usecase.event.common.store.EventStore;
import bookyourinstructor.usecase.event.search.data.SearchEventsData;
import bookyourinstructor.usecase.event.search.result.SearchEventsResult;
import bookyourinstructor.usecase.event.search.result.SearchEventsResultItem;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SearchEventsUseCase {

    private final EventStore eventStore;

    public SearchEventsResult searchEvents(SearchEventsData data) {
        List<SearchEventsResultItem> events = eventStore.searchEvents(data.getDateRange(), data.getTextSearch(), data.getEventType());
        return new SearchEventsResult(events);
    }
}
