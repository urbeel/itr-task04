package by.urbel.task04.controller;

import by.urbel.task04.entity.UserDTO;
import by.urbel.task04.service.UserService;
import by.urbel.task04.service.exception.EmailAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {
    private final UserService userService;

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(HttpServletRequest request, @ModelAttribute @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "registration";
        String password = userDTO.getPasswordHash();
        try {
            userService.register(userDTO);
        } catch (EmailAlreadyExistsException e) {
            return "redirect:/registration?error=" + e.getMessage();
        }
        authWithHttpServletRequest(request, userDTO.getEmail(), password);
        return "redirect:/users";
    }

    private void authWithHttpServletRequest(HttpServletRequest request, String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            log.error("Error while login ", e);
        }
    }
}
