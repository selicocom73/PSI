package com.example.psi.service;

import com.example.psi.entity.UserEntity;
import com.example.psi.repository.UserRepository;
import com.example.psi.request.VerifyCodeRequest;
import com.example.psi.response.VerifyCodeResponse;
import com.example.psi.singletone.Storage;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerifyCodeService {



    private final UserRepository userRepository;

    public VerifyCodeService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public VerifyCodeResponse verifyCode(VerifyCodeRequest request) {

      String email =   Storage.getInstance().getCurrentEmail();
      Optional<UserEntity> userEntityOptional = userRepository.findByEmail(email);
      if(userEntityOptional.isPresent()) {
          UserEntity userEntity = userEntityOptional.get();
          String requireCodeToChangePassword = userEntity.getCodeToChangePassword();
          if(request.code().equals(requireCodeToChangePassword)) {
              return new VerifyCodeResponse(true);
          }
          return new VerifyCodeResponse("Wrong code");
      }
        return new VerifyCodeResponse("Not found user");
    }
}
