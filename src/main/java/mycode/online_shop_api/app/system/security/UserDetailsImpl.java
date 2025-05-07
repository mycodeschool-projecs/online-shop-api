package mycode.online_shop_api.app.system.security;

import mycode.online_shop_api.app.users.model.User;
import mycode.online_shop_api.app.users.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            return (UserDetails) userOptional.get();
        }

        throw new UsernameNotFoundException("User with email" + email +"not found");
    }
}
