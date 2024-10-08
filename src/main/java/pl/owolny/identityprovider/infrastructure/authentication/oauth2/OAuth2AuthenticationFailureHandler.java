package pl.owolny.identityprovider.infrastructure.authentication.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.util.UriComponentsBuilder;
import pl.owolny.identityprovider.domain.token.OAuth2LinkingTokenService;
import pl.owolny.identityprovider.infrastructure.authentication.oauth2.exception.OAuth2EmailUnverifiedException;

import java.io.IOException;

import static pl.owolny.identityprovider.infrastructure.authorization.LoginController.LOGIN_VIEW;

class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final OAuth2LinkingTokenService tokenService;

    public OAuth2AuthenticationFailureHandler(OAuth2LinkingTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String redirectUrl = UriComponentsBuilder.fromUriString("/" + LOGIN_VIEW)
                .queryParam("error")
                .build()
                .toUriString();

        if (exception instanceof OAuth2EmailUnverifiedException oAuth2EmailUnverifiedException
                && oAuth2EmailUnverifiedException.getUserInfo() != null
                && oAuth2EmailUnverifiedException.getoAuth2UserInfo() != null) {

            tokenService.saveLinkData(oAuth2EmailUnverifiedException.getUserInfo(), oAuth2EmailUnverifiedException.getoAuth2UserInfo());
            request.getSession().setAttribute("OAUTH2_LINK_USER_ID", oAuth2EmailUnverifiedException.getUserInfo().getId());
            request.getSession().setAttribute("OAUTH2_LINK_EXTERNAL_ID", oAuth2EmailUnverifiedException.getoAuth2UserInfo().externalId());
            redirectUrl = UriComponentsBuilder.fromUriString("/" + "verify").toUriString();
        }

        super.setDefaultFailureUrl(redirectUrl);
        super.onAuthenticationFailure(request, response, exception);
    }
}
