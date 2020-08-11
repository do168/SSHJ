package sshj.controller;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class UserController {

    private UserService userService;

    //메인페이지
    @GetMapping("/")
    public String index() {
        return "/index";
    }

    //회원가입 페이지
    @GetMapping("/user/signup")
    public String dispSignup(){
        return "/signup";
    }

    //회원가입 처리
    @PostMapping("/user/signup")
    public String execSignup(UserDto userDto){
        userService.joinUser(userDto);

        return "redirect:/user/lgoin";
    }

    //로그인 페이지
    @GetMapping("/user/login")
    public String dispLogin() {
        return "/login";
    }

    //로그인 결과 페이지
    @GetMapping("/user/login/result")
    public String dispLoginResult() {
        return "/loginSuccess";
    }

    @GetMapping("/user/logout")
    public String dispLogout(){
        return "/logout";
    }

    @GetMapping("/user/denied")
    public String dispDenied() {
        return "/denied";
    }

    @GetMapping("/user/info")
    public String dispInfo(){
        return "/myinfo";
    }
}
