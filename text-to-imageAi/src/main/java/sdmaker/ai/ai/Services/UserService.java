package sdmaker.ai.ai.Services;



import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sdmaker.ai.ai.Exceptions.UserAlreadyExistsException;
import sdmaker.ai.ai.Repos.UserRepository;

import sdmaker.ai.ai.Entites.User;

import java.time.LocalDate;
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

        // Initial Registration Bonus

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        // Initial Registration Bonus
        user.setTokens(200);
        user.setLastTokenReset(LocalDate.now());



        userRepository.save(user);
    }

    @Transactional
    public User refreshAndGetTokens(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // DAILY RENEWAL LOGIC
        // If today is after the last reset date, reset tokens to 200
        if (user.getLastTokenReset() == null || user.getLastTokenReset().isBefore(LocalDate.now())) {
            user.setTokens(200);
            user.setLastTokenReset(LocalDate.now());
            userRepository.save(user);
        }

        return user;
    }




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}
