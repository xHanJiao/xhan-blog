package com.xhan.xhanblog.controller;

import com.xhan.xhanblog.entity.dao.TUser;
import com.xhan.xhanblog.entity.dto.UpdateUserDTO;
import com.xhan.xhanblog.entity.vo.UsernamePasswordToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.xhan.xhanblog.controller.ControllerConst.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.util.StringUtils.hasText;

@Controller
public class AdminController extends BaseController {

    @PostMapping(value = ADMIN_LOGIN_URL)
    public ResponseEntity<?> login(@RequestBody @Valid UsernamePasswordToken token,
            BindingResult result, HttpSession session, HttpServletRequest request) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getFieldError(), HttpStatus.BAD_REQUEST);
        }
        if (userHelper.tokenInvalid(token))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        TUser user = userHelper.getUserByNameAndModifyIp(token, request.getRemoteAddr());
        session.setAttribute("user", user);

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .header(HttpHeaders.LOCATION, CONTROL_CENTER_URL)
                .build();
    }

    @PostMapping(value = UPDATE_USER_INFO)
    public ResponseEntity<?> updateUserInfo(@RequestBody UpdateUserDTO newInfo,
                                            HttpSession session) {
        checkIfLogin(session);
        if (nullDTO(newInfo))
            return new ResponseEntity<>("Fields cannot be all blank", HttpStatus.BAD_REQUEST);
        if (!newInfo.isLegal())
            return new ResponseEntity<>("has illegal fields", HttpStatus.BAD_REQUEST);

        TUser user = (TUser) session.getAttribute("user");
        userHelper.updateUser(newInfo, user.getUId());

        return new ResponseEntity<>(OK);
    }

    @GetMapping(value = CONTROL_CENTER_URL)
    public ResponseEntity<?> getControlCenter(HttpSession session) {
        checkIfLogin(session);
        return new ResponseEntity<>(getFaces(session), OK);
    }

    @GetMapping(value = LOGOUT_URL)
    public ResponseEntity<?> logout(HttpSession session){
        checkIfLogin(session);
        session.invalidate();
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .header(HttpHeaders.LOCATION, SLASH)
                .build();
    }

    private boolean nullDTO(UpdateUserDTO newInfo) {
        return !hasText(newInfo.getEmail()) &&
                !hasText(newInfo.getPassword()) &&
                !hasText(newInfo.getUsername());
    }

}
