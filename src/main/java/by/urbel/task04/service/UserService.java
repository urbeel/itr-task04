package by.urbel.task04.service;

import by.urbel.task04.entity.UserDTO;
import by.urbel.task04.repos.UserRepository;
import by.urbel.task04.service.exception.EmailAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(final UserDTO userDTO) throws EmailAlreadyExistsException {
        if (userDTO.getId() == null) {
            try {
                userDTO.setPasswordHash(passwordEncoder.encode(userDTO.getPasswordHash()));
                userRepository.save(userDTO);
            } catch (RuntimeException e) {
                log.error(e.getMessage());
                throw new EmailAlreadyExistsException("User with this email address already exists.");
            }
        }
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll(Sort.by("id"));
    }

    public UserDTO getById(final Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void update(final UserDTO userDTO) {
        if (userRepository.existsById(userDTO.getId())) {
            userRepository.save(userDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public void delete(final Long id) {
        userRepository.deleteById(id);
    }
}
