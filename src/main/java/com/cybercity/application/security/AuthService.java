package com.cybercity.application.security;

import com.cybercity.application.dtos.LoginDTO;
import com.cybercity.application.dtos.SignUpDTO;
import com.cybercity.application.dtos.UserDTO;
import com.cybercity.application.entities.UserEntity;
import com.cybercity.application.entities.enums.Role;
import com.cybercity.application.exceptions.UserNotFoundException;
import com.cybercity.application.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private  final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public UserDTO signUp(SignUpDTO signUpDTO){
        UserEntity user = userRepository.findByEmail(signUpDTO.getEmail()).orElse(null);

        if(user!=null){
            throw  new BadCredentialsException("User is already present with the same email id");
        }

        UserEntity newUser = modelMapper.map(signUpDTO,UserEntity.class);
        newUser.setRoles(Set.of(Role.STUDENT));
        newUser.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        newUser=userRepository.save(newUser);

        return modelMapper.map(newUser,UserDTO.class);
    }

    public String[] login(LoginDTO loginDTO){
        Authentication authentication = authenticationManager.authenticate((
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),loginDTO.getPassword())
        ));

        UserEntity user =  (UserEntity) authentication.getPrincipal();

        String[] arr = new String[2];
        arr[0]=jwtService.generateAccessToken(user);
        arr[1]=jwtService.generateRefreshToken(user);

        return arr;
    }

    public String refreshToken(String refreshToken){
        Long id = jwtService.getUserIdByToken(refreshToken);

        UserEntity user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("User not found with id: "+id));

        return jwtService.generateAccessToken(user);
    }

}
