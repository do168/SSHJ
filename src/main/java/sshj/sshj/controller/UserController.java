package sshj.sshj.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jdk.internal.jline.internal.Log;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sshj.sshj.dto.UserInfoModel;
import sshj.sshj.service.UserService;

@RestController
@Api(value="Oauth-controller", description="Oauth controller")
@AllArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(
            value = "회원가입"
            ,notes = "회원가입"
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="complete")
    })
    @RequestMapping(value="/signup", method = RequestMethod.POST)
    public ResponseEntity<Void> signUp(
            @ModelAttribute final UserInfoModel userInfoModel) throws Exception {
        if(userService.selectUserInfo(userInfoModel.getId())==null) {
            userService.insertUser(userInfoModel);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        else {
            Log.info("중복된 아이디입니다"); //여기 처리 어케해야할까?
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST); //여기도 리턴 어케해야하지?
        }

    }

}
