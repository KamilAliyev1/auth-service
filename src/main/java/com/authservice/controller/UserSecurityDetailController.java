package com.authservice.controller;



import com.authservice.dto.UserSecurityDetailSaveDTO;
import com.authservice.dto.UserSecurityDetailUpdateDTO;
import com.authservice.util.CustomRestController;
import com.authservice.util.CustomService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/v1/usersecuritydetails")
public class UserSecurityDetailController extends CustomRestController<UserSecurityDetailSaveDTO, UserSecurityDetailUpdateDTO> {

    public UserSecurityDetailController(CustomService service) {
        super(service);
    }


}
