package net.therap.mealsystem.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static net.therap.mealsystem.domain.UserRole.VERIFIED_USER;

/**
 * @author sheikh.ishrak
 * @since 20/03/2022
 */
public class CustomAccessDeniedHandler extends AccessDeniedHandlerImpl {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        GrantedAuthority verifiedUserAuthority = authentication.getAuthorities()
                .stream()
                .filter(grantedAuthority -> ("ROLE_" + VERIFIED_USER.name()).equals(grantedAuthority.getAuthority()))
                .findAny().orElse(null);

        if (Objects.nonNull(verifiedUserAuthority)) {
            response.sendRedirect(request.getContextPath() + "/accessDenied");
        } else {
            response.sendRedirect(request.getContextPath() + "/accountUnverified");
        }

    }
}
