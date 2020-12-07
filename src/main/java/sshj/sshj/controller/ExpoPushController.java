package sshj.sshj.controller;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import sshj.sshj.dto.ServiceResultModel;
import sshj.sshj.dto.UserHeaderModel;
import sshj.sshj.service.ExpoPushService;
import sshj.sshj.service.UserService;

@RestController
@Api(value = "Oauth-controller", description = "Oauth controller")
@Slf4j
@RequestMapping("/expopush")
public class ExpoPushController {


    @Autowired
    private ExpoPushService expoPushService;

    @Autowired
    private UserService userService;

    @ApiOperation(
            value = "expo_Push 토큰 저장"
            , notes = "expo_Push 토큰 저장"
            ,authorizations = {@Authorization(value = "JWT")}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "complete")
    })
    @RequestMapping(value = "/get/Token", method = RequestMethod.POST)
    public ResponseEntity<String> getExpoPushToken(
            @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel,
            @ApiParam(value = "expoPush 토큰", required = true) @RequestParam(name = "expoPushToken", required = true) String expoPushToken) throws Exception {

        ServiceResultModel result = expoPushService.createPushToken(userHeaderModel.getUserId(), expoPushToken);
        // 토큰 저장 성공
        if (result.getFlag()) {
            return new ResponseEntity<>(result.getMsg(), HttpStatus.OK);
        }
        // 토큰 저장 실패
        else {
            return new ResponseEntity<>(result.getMsg(), HttpStatus.BAD_REQUEST);
        }

    }

}
