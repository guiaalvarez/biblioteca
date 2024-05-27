package guiaalvarez.biblioteca.controller;

import guiaalvarez.biblioteca.dto.LoginUserDTO;
import guiaalvarez.biblioteca.entity.Book;
import guiaalvarez.biblioteca.entity.User;
import guiaalvarez.biblioteca.service.JwtUserService;
import guiaalvarez.biblioteca.repository.BookRepository;
import guiaalvarez.biblioteca.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import guiaalvarez.biblioteca.repository.UserRepository;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private JwtUserService userService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
    @GetMapping
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
    @PostMapping("/login")
    public String authenticateUser(@RequestBody LoginUserDTO loginUserDto) {
        return userService.authenticateUser(loginUserDto);
    }


    @GetMapping("/")
    public String showIndex(Model model) {
        model.addAttribute("user", userRepository.findAll());
        return "index";
    }

    @GetMapping("/add-user")
    public String showAddUserForm(User user) {
        return "add-user";
    }

    @PostMapping("/add-user")
    public String addUser(User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-user";
        }
        userService.createUser(user);
        return "redirect:/";
    }

}
