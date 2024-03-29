package bookyourinstructor.usecase.event.search.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SearchEventsData {

    private final DateRangeFilter dateRange;
    private final TextSearchFilter textSearch;
    private final EventTypeFilter eventType;
}
