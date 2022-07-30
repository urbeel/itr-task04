package by.urbel.task04.controller;

import by.urbel.task04.entity.IdsForm;
import by.urbel.task04.entity.UserDTO;
import by.urbel.task04.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping()
    public String getAllUsers(Model model) {
        List<UserDTO> userDTOS = userService.findAll();
        model.addAttribute("users", userDTOS);
        IdsForm idsForm = new IdsForm();
        idsForm.setIds(new ArrayList<>());
        model.addAttribute("idsForm", idsForm);
        return "users";
    }

    @PostMapping("/block")
    public String blockUsers(@ModelAttribute IdsForm idsForm) {
        this.changeUsersStatus(idsForm.getIds(), false);
        return "redirect:/users";
    }

    @PostMapping("/activate")
    public String activateUsers(@ModelAttribute IdsForm idsForm) {
        this.changeUsersStatus(idsForm.getIds(), true);
        return "redirect:/users";
    }

    @DeleteMapping()
    public String delete(@ModelAttribute IdsForm idsForm) {
        idsForm.getIds().forEach(userService::delete);
        return "redirect:/users";
    }

    private void changeUsersStatus(List<Long> usersIds, boolean status) {
        List<UserDTO> userDTOs = usersIds.stream().map(userService::getById).toList();
        userDTOs.forEach(userDTO -> userDTO.setIsActive(status));
        userDTOs.forEach(userService::update);
    }
}
