package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.AccountDto;
import com.example.g31_ffs_be.dto.RegisterDto;
import com.example.g31_ffs_be.model.*;
import com.example.g31_ffs_be.repository.AccountRepository;
import com.example.g31_ffs_be.repository.RoleRepository;
import com.example.g31_ffs_be.service.AccountService;
import net.bytebuddy.utility.RandomString;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@Component
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository repo;
    @Autowired
    JavaMailSender mailSender;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    private final static String siteUrl="https://lanceddy.vercel.app";

    
    @Override
    public void createAccount(RegisterDto registerDto) {
        try {
            Account acc = new Account();
            acc.setEmail(registerDto.getEmail());
            System.out.println(acc.getEmail());
            String roleName=registerDto.getRole();
            Role role = roleRepository.findByRoleName(roleName);
            acc.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            acc.setRole(role);
            String id = "";
            while (true) {
                String headerID= roleName.equals("freelancer")?"LF":"LR";
                id =headerID+RandomString.make(8);
                if (accountRepository.findById(id).equals(Optional.empty())) {
                    acc.setId(id);
                    break;
                }
            }
            User user = new User();
            user.setId(id);

            user.setFullName(registerDto.getFullName());
            user.setAddress(registerDto.getAddress());
            user.setCity(registerDto.getCity());
            user.setCountry(registerDto.getCountry());
            user.setPhone(registerDto.getPhone());
            user.setAccountBalance(0.0);
            user.setIsMemberShip(false);
            acc.setCreatedDate(LocalDateTime.now());
            user.setVerificationCode(RandomString.make(36));
            user.setIsBanned(true);
            if(roleName.equals("freelancer")){
                Freelancer f=new Freelancer();
                f.setId(id);
                f.setBirthdate(registerDto.getBirthdate());
                Subcareer subcareer=new Subcareer();
                subcareer.setId(registerDto.getSubCareer());
                f.setSubCareer(subcareer);
                f.setGender(registerDto.getGender());
                user.setFreelancer(f);
                acc.setUser(user);
                accountRepository.save(acc);
                sendVerificationEmail(acc, siteUrl);
            }
            else{
                Recruiter recruiter=new Recruiter();
                recruiter.setId(id);
                recruiter.setTaxNumber("");
                recruiter.setCompanyName("");
                recruiter.setWebsite("");
                recruiter.setIsActive(false);
                user.setIsBanned(false);

                user.setRecruiter(recruiter);
                acc.setUser(user);
                accountRepository.save(acc);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        return null;
    }

    @Override
    public Boolean checkIdExist(String id) {
        if (repo.existsById(id)) return true;

        return false;
    }

    @Override
    public Boolean checkEmailExist(String email) {
        return repo.checkEmailExist(email) != 0;
    }

    @Override
    public void sendVerificationEmail(Account account, String siteURL) {
        try {
            String toAddress = account.getEmail();
            String fromAddress = "lanceddywebsite@gmail.com";
            String senderName = "Lanceddy Team";
            String subject = "X??c th???c t??i kho???n c???a b???n t???i lanceddy.com";
            String content = "Ch??o b???n [[name]],<br>"
                    + "Vui l??ng click v??o link b??n d?????i ????? x??c th???c t??i kho???n c???a b???n!:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">Nh???p v??o ????y ????? x??c th???c</a></h3>"
                    + "Tr??n tr???ng!<br>"
                    + "Lanceddy Team!";
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            content = content.replace("[[name]]", account.getUser().getFullName());
            String verifyURL = siteURL + "/verify?code=" + account.getUser().getVerificationCode();
            content = content.replace("[[URL]]", verifyURL);
            helper.setText(content, true);
            mailSender.send(message);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void sendResetPasswordEmail(Account account, String siteURL) {
        try {
            String toAddress = account.getEmail();
            String fromAddress = "lanceddywebsite@gmail.com";
            String senderName = "Lanceddy Team";
            String subject = "L???y l???i m???t kh???u lanceddy.com";
            String content = "Ch??o b???n [[name]],<br>"
                    + "Vui l??ng click v??o link b??n d?????i ????? thay ?????i m???t kh???u m???i c???a b???n!:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">Nh???p v??o ????y ????? thay ?????i m???t kh???u</a></h3>"
                    + "Tr??n tr???ng!<br>"
                    + "Lanceddy Team!";
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            content = content.replace("[[name]]", account.getUser().getFullName());
            String verifyURL = siteURL + "/reset-password?resetPasswordToken=" + account.getUser().getResetPasswordToken();
            content = content.replace("[[URL]]", verifyURL);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public Boolean changePassword(AccountDto accountDto) {
        try {
            Account acc = accountRepository.findByEmail(accountDto.getEmail());
            acc.setPassword(passwordEncoder.encode(accountDto.getPassword()));
            acc.getUser().setResetPasswordToken(null);
            accountRepository.save(acc);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }

    @Override
    public Boolean forgotPassword(Account account) {
        try {
            String resetPasswordToken = RandomString.make(36);
            account.getUser().setResetPasswordToken(resetPasswordToken);
            account.getUser().setResetPasswordTime(Instant.now());
            accountRepository.save(account);
            sendResetPasswordEmail(account, siteUrl);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

     public Account getAccountFromToken(String token) {
        String[] parts = token.split("\\.");
        JSONObject payload = new JSONObject(decode(parts[1]));
        String email = payload.getString("sub");
        Account acc = accountRepository.findByEmail(email);
        return acc;

    }

    @Override
    public Boolean changePasswordUser(AccountDto account) {
        try {
            Account acc = accountRepository.findByEmail(account.getEmail());
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    account.getEmail(), account.getOldPassword()
            ));
                acc.setPassword(passwordEncoder.encode(account.getPassword()));
                accountRepository.save(acc);
                return true;
        } catch (Exception e) {
            return false;
        }
    }
}
