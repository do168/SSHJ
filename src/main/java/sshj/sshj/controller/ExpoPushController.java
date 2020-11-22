package sshj.sshj.controller;

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
public class ExpoPushController {




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
    @RequestMapping(value = "/getExpoPushToken", method = RequestMethod.POST)
    public ResponseEntity<String> getExpoPushToken(
            @ApiIgnore @RequestAttribute("UserHeaderInfo") UserHeaderModel userHeaderModel,
            @ApiParam(value = "expoPush 토큰", required = true) @RequestParam(name = "expoPushToken", required = true) String expoPushToken) throws Exception {

        if (userService.selectUser(userHeaderModel.getUserId()) == null) {
            String error = "user doesn't exit";
            return new ResponseEntity<String>(error, HttpStatus.BAD_REQUEST);
        }

        try{
            userService.updateDeviceToken(userHeaderModel.getUserId(), expoPushToken);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>("error", HttpStatus.BAD_REQUEST);
        }
        String msg = "expoPush token saved success";
        log.info("msg");
        return new ResponseEntity<>(msg, HttpStatus.OK);


    }

}
