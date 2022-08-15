package bookyourinstructor.usecase.event.common.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(staticName = "of")
@Getter
public class GetEventListResult {

    private final List<GetEventListResultItem> events;
}
