package by.urbel.task04.filter;

import by.urbel.task04.entity.UserDTO;
import by.urbel.task04.repos.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.session.ConcurrentSessionFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
public class CustomConcurrentSessionFilter extends ConcurrentSessionFilter {
    @Autowired
    private UserRepository userRepository;

    public CustomConcurrentSessionFilter(SessionRegistry sessionRegistry) {
        super(sessionRegistry);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();
        if (auth != null) {
            Optional<UserDTO> user = userRepository.findUserByEmail(auth.getName());
            if (user.isEmpty() || !user.get().getIsActive()){
                auth.setAuthenticated(false);
            }
        }
        super.doFilter(req, res, chain);
    }
}
