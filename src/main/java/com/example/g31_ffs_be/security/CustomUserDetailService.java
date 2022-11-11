package com.example.g31_ffs_be.security;

import com.example.g31_ffs_be.exception.ResourceNotFoundException;
import com.example.g31_ffs_be.model.Account;
import com.example.g31_ffs_be.repository.AccountRepository;
import com.sun.deploy.security.BlockedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;


@Service
public class CustomUserDetailService implements UserDetailsService {

 @Autowired
    AccountRepository accountRepository;
    public CustomUserDetailService() {

    }


    @Override

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

            Account acc = accountRepository.findByEmail(email);
            /*if ( !acc.getRole().getRoleName().equals("admin")&&!acc.getRole().getRoleName().equals("staff")&&acc.getUser().getIsBanned()) {
                  return  null;
            }*/
            List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
            grantedAuthorityList.add(new SimpleGrantedAuthority(acc.getRole().getRoleName()));
            return new org.springframework.security.core.userdetails.User(acc.getEmail(),
                    acc.getPassword(), grantedAuthorityList);


    }

}
