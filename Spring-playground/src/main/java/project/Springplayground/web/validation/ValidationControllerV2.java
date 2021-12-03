package project.Springplayground.web.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.Springplayground.model.UserProfile;
import project.Springplayground.model.UserProfileRepository;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/validation/v2")
public class ValidationControllerV2 {

    private final UserProfileRepository userProfileRepository;

    //회원가입
    @GetMapping("/joinForm")
    public String addForm(Model model){
        //타임리프로 값을 불러오기 때문에 빈값을 넘겨줘야함
        model.addAttribute("userProfile", new UserProfile());
        return "/validation/v2/joinForm";
    }

    @PostMapping("/joinForm")
    public String addUser1(@ModelAttribute UserProfile userProfile,
                          BindingResult bindingResult,
                          Model model)
    {
        //검증 로직
        if(!StringUtils.hasText(userProfile.getName())){
            bindingResult.addError(new FieldError("userProfile", "name", "이름을 입력해주세요."));
        }
        if(!StringUtils.hasText(userProfile.getPhone())){
            bindingResult.addError(new FieldError("userProfile", "phone", "핸드폰번호를 입력해주세요."));
        }
        if(!StringUtils.hasText(userProfile.getAddress())){
            bindingResult.addError(new FieldError("userProfile", "address", "주소를 입력해주세요."));
        }
        if(userProfile.getPoint() == null || userProfile.getPoint() < 1000 || userProfile.getPoint() > 1000000){
            bindingResult.addError(new FieldError("userProfile", "point", "포인트는 최소 1000, 1,000,000 미만으로 입력해주세요."));
        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            //중요
            return "/validation/v2/joinForm";
        }

        //성공 로직
        userProfileRepository.save(userProfile);
        return "/validation/v2/User";
    }

    //회원 전체 조회
    @GetMapping("/searchUser")
    public String searchUser(Model model){
        List<UserProfile> userProfile = userProfileRepository.findAll();
        model.addAttribute("items", userProfile);
        return "/validation/v2/searchUser";
    }
    //회원 상세 조회
    @GetMapping("/items/{id}")
    public String item(@PathVariable Long id, Model model) {
        UserProfile userProfile = userProfileRepository.findById(id);
        model.addAttribute("item", userProfile);
        return "/validation/v2/userInform";
    }

}