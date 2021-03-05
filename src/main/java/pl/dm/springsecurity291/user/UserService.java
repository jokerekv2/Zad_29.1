package pl.dm.springsecurity291.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findAllWithout() {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findAll().stream()
                .filter(user -> !user.getUsername().equals(currentUser.getName()))
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User findByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new NoSuchElementException("There is no user with username: " + username);
        }
    }

    public void registerUser(String username, String rawPassword) {
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(rawPassword);
        List<UserRole> userRole = Collections.singletonList(new UserRole(newUser, Role.ROLE_USER));

        newUser.setUsername(username);
        newUser.setPassword(encryptedPassword);
        newUser.setRoles(new HashSet<>(userRole));
        userRepository.save(newUser);
    }

    public void updateUserData(String username, String password) {

        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        User user = findByUsername(currentUser.getName());

        if (username.isEmpty()) {
            String encryptedPassword = passwordEncoder.encode(password);
            user.setPassword(encryptedPassword);
        } else if (password.isEmpty()) {
            user.setUsername(username);
        } else {
            String encryptedPassword = passwordEncoder.encode(password);
            user.setPassword(encryptedPassword);
            user.setUsername(username);
        }
        userRepository.save(user);
    }

    public boolean hasRole(String role) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
    }

    public void promoteOrDegradeById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            List<String> roleList = user.getRoles().stream()
                    .map(UserRole::getRole)
                    .map(Enum::name)
                    .collect(Collectors.toList());

            if (roleList.stream().anyMatch(s -> s.equals("ROLE_ADMIN"))) {

                List<UserRole> list = userRoleRepository.findAll().stream()
                        .filter(userRole -> userRole.getUser().getUsername().equals(user.getUsername()))
                        .collect(Collectors.toList());
                userRoleRepository.deleteAll(list);

                List<UserRole> userRole = Collections.singletonList(new UserRole(user, Role.ROLE_USER));
                user.setRoles(new HashSet<>(userRole));
                userRepository.save(user);
            } else {
                List<UserRole> userRole = Collections.singletonList(new UserRole(user, Role.ROLE_ADMIN));
                user.setRoles(new HashSet<>(userRole));
                userRepository.save(user);
            }
        } else {
            throw new NoSuchElementException("There is no such user.");
        }

    }
}
