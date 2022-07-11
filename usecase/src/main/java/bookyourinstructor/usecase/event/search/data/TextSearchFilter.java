package bookyourinstructor.usecase.event.search.data;

import com.quary.bookyourinstructor.model.filter.search.TextSearchCategory;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

@Getter
public class TextSearchFilter {

    private final Set<TextSearchCategory> selectedCategories;
    private final String searchText;
    private final List<String> searchTokens;

    public TextSearchFilter(Set<TextSearchCategory> selectedCategories, String searchText) {
        validateConstructorArgs(searchText);
        this.selectedCategories = selectedCategories;
        this.searchText = searchText.trim();
        this.searchTokens = splitToSearchTokens(searchText);
    }

    private static void validateConstructorArgs(String searchText) {
        checkArgument(StringUtils.isNotBlank(searchText), "Text search filter - search text cannot be blank");
    }

    private static List<String> splitToSearchTokens(final String searchText) {
        return Arrays.stream(searchText.trim().split(" "))
                .map(String::toLowerCase)
                .map(String::trim)
                .distinct()
                .collect(Collectors.toList());
    }

    public boolean areAllCategoriesSelected() {
        return selectedCategories == null || selectedCategories.isEmpty() || selectedCategories.containsAll(List.of(TextSearchCategory.values()));
    }

    public boolean isCategorySelected(TextSearchCategory category) {
        return areAllCategoriesSelected() || selectedCategories.contains(category);
    }
}
