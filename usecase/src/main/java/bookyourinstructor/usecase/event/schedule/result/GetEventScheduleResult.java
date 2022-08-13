package bookyourinstructor.usecase.event.schedule.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(staticName = "of")
@Getter
public class GetEventScheduleResult {

    private final List<GetEventScheduleResultItem> items;
}
