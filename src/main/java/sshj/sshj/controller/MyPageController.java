package sshj.sshj.controller;

import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import sshj.sshj.dto.UserHeaderModel;
import sshj.sshj.service.UserService;

@RestController
@Api(value = "Oauth-controller", description = "Oauth controller")
@AllArgsConstructor
@Slf4j
@RequestMapping("/myPage")
public class MyPageController {
    @Autowired
    private UserService userService;

    @ApiOperation(
            value = "닉네임 변경"
            , notes = "닉네임 변경"
            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/changenickname", method = RequestMethod.POST)
    public ResponseEntity<Boolean> changeNickname(
            @ApiParam(value = "입력 닉네임", required = true) @RequestParam(name = "chNickname", required = true) String chnickname
    ,@ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel) throws Exception {
        String loginId = userHeaderModel.getLoginId();
        if (userService.selectUserNicknameIsOk(chnickname) == 0) {
            log.info("사용 가능한 닉네임입니다.");
            try {
                userService.updateUserNickname(loginId, chnickname);
            }
            catch (Exception e) {
                log.error(String.valueOf(e));
            }
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            log.info("중복된 닉네임입니다");
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
