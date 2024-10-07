package pl.owolny.identityprovider.infrastructure.authentication.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.util.UriComponentsBuilder;
import pl.owolny.identityprovider.infrastructure.authentication.oauth2.exception.OAuth2EmailUnverifiedException;

import java.io.IOException;

import static pl.owolny.identityprovider.infrastructure.authorization.LoginController.LOGIN_VIEW;

public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String redirectUrl = UriComponentsBuilder.fromUriString("/" + LOGIN_VIEW)
                .queryParam("error")
                .build()
                .toUriString();

        if (exception instanceof OAuth2EmailUnverifiedException OAuth2EmailUnverifiedException) {
            redirectUrl = UriComponentsBuilder.fromUriString("/" + "verify").toUriString();
        }

        super.setDefaultFailureUrl(redirectUrl);
        super.onAuthenticationFailure(request, response, exception);
    }
}
