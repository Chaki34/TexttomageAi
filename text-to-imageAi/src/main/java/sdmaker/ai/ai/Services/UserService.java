package sdmaker.ai.ai.Services;



import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sdmaker.ai.ai.Exceptions.UserAlreadyExistsException;
import sdmaker.ai.ai.Repos.UserRepository;

import sdmaker.ai.ai.Entites.User;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(String username, String email, String password) {
        if (userRepository.existsByUsername(username)) throw new UserAlreadyExistsException("Username taken");
        if (userRepository.existsByEmail(email)) throw new UserAlreadyExistsException("Email taken");

        User user = new User(null, username, passwordEncoder.encode(password), email);
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}
