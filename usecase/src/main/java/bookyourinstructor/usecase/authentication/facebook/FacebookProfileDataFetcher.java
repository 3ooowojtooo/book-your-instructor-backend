package bookyourinstructor.usecase.authentication.facebook;

import com.quary.bookyourinstructor.model.authentication.EmailAndExternalIdentity;

public interface FacebookProfileDataFetcher {

    EmailAndExternalIdentity fetchEmailAndExternalId(String accessToken);
}
