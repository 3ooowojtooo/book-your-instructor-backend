package bookyourinstructor.usecase.authentication.jwt;

public interface JwtValidator {

    boolean isTokenValid(String token, String subject);

    boolean isTokenExpired(String token);
}
