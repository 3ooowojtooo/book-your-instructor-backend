package bookyourinstructor.usecase.authentication.facebook;

public interface FacebookProfileDataFetcher {

    FacebookUserData fetchEmailAndExternalId(String accessToken);
}
