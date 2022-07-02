package bookyourinstructor.usecase.event.search.data;

import com.quary.bookyourinstructor.model.filter.search.TextSearchCategory;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

@Getter
public class TextSearchFilter {

    private final Set<TextSearchCategory> selectedCategories;
    private final String searchText;

    public TextSearchFilter(Set<TextSearchCategory> selectedCategories, String searchText) {
        validateConstructorArgs(searchText);
        this.selectedCategories = selectedCategories;
        this.searchText = searchText.trim();
    }

    private static void validateConstructorArgs(String searchText) {
        checkArgument(StringUtils.isNotBlank(searchText), "Text search filter - search text cannot be blank");
    }

    public boolean areAllCategoriesSelected() {
        return selectedCategories == null || selectedCategories.isEmpty() || selectedCategories.containsAll(List.of(TextSearchCategory.values()));
    }

    public boolean categorySelected(TextSearchCategory category) {
        return areAllCategoriesSelected() || selectedCategories.contains(category);
    }
}
