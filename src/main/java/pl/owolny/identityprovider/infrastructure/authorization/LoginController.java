package pl.owolny.identityprovider.infrastructure.authorization;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;
import pl.owolny.identityprovider.domain.user.UserInfo;
import pl.owolny.identityprovider.infrastructure.authentication.oauth2.OAuth2UserInfo;

@Controller
public class LoginController {

    public static final String LOGIN_VIEW = "login";

    @GetMapping("/login")
    public String login() {
        return LOGIN_VIEW;
    }

    @GetMapping("/verify")
    public String verify(
            @RequestAttribute(name = "OAUTH2_LINK_USER") UserInfo userInfo,
            @RequestAttribute(name = "OAUTH2_LINK_EXTERNAL") OAuth2UserInfo oAuth2UserInfo,
            Model model) {
        model.addAttribute("userId", userInfo.getId().value());
        model.addAttribute("externalId", oAuth2UserInfo.externalId());
        model.addAttribute("userEmail", userInfo.getEmail().value());
        model.addAttribute("externalEmail", oAuth2UserInfo.externalEmail().value());
        model.addAttribute("userName", userInfo.getUsername());
        model.addAttribute("externalName", oAuth2UserInfo.externalUsername());
        model.addAttribute("provider", oAuth2UserInfo.provider().name());
        return "link-accounts";
    }
}
