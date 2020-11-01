package sshj.sshj.component;


import io.swagger.annotations.*;
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
@Slf4j
@RequestMapping("/push")
public class ExpoPush {

    @Autowired
    private UserService userService;

    @ApiOperation(
            value = "디바이스 토큰 저장"
            , notes = "디바이스 토큰 저장"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/getDeviceToken", method = RequestMethod.POST, produces="text/plain;charset=UTF-8")
    public ResponseEntity<String> getDeviceToken(
            @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel,
            @ApiParam(value = "디바이스 토큰", required = true) @RequestParam(name = "deviceToken", required = true) String deviceToken) throws Exception {

            if (userService.selectUser(userHeaderModel.getLoginId()) == null) {
                String error = "유저가 존재하지 않습니다";
                return new ResponseEntity<String>(error, HttpStatus.BAD_REQUEST);
            }

            try{
                userService.insertDeviceToken(userHeaderModel.getLoginId(), deviceToken);
            } catch (Exception e) {
                log.error(e.toString());
                return new ResponseEntity<>("에러", HttpStatus.BAD_REQUEST);
            }
            String msg = "디바이스 토큰 저장 성공";
            return new ResponseEntity<>(msg, HttpStatus.OK);


    }

}
