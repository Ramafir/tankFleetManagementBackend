package pl.daniel.kolban.tankfleetmanagement.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import pl.daniel.kolban.tankfleetmanagement.user.UserLoginAttemptService;
import pl.daniel.kolban.tankfleetmanagement.user.UserPrincipal;


@Component
public class AuthenticationSuccessListener {
    private final UserLoginAttemptService loginAttemptService;

    @Autowired
    public AuthenticationSuccessListener(UserLoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }


    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event){
        Object principal=event.getAuthentication().getPrincipal();
        if(principal instanceof UserPrincipal){
            UserPrincipal user=(UserPrincipal) event.getAuthentication().getPrincipal();
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }
}
