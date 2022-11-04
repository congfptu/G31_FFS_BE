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
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
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
            acc.setCreatedDate(LocalDateTime.now());
            user.setVerificationCode(RandomString.make(36));
            user.setIsBanned(true);
            if(roleName.equals("freelancer")){
                Freelancer f=new Freelancer();
                f.setId(id);
                Subcareer subcareer=new Subcareer();
                subcareer.setId(registerDto.getSubCareer());
                f.setSubCareer(subcareer);
                f.setGender(registerDto.getGender());
                user.setFreelancer(f);
                acc.setUser(user);
                accountRepository.save(acc);
                sendVerificationEmail(acc, "http://localhost:3000/");
            }
            else{
                Recruiter recruiter=new Recruiter();
                recruiter.setId(id);
                recruiter.setTaxNumber(registerDto.getTaxNumber());
                recruiter.setCompanyName(registerDto.getCompanyName());
                recruiter.setWebsite(registerDto.getWebsite());
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
        return repo.findByEmail(email) != null ? true : false;
    }

    @Override
    public void sendVerificationEmail(Account account, String siteURL) {
        try {
            String toAddress = account.getEmail();
            String fromAddress = "lanceddywebsite@gmail.com";
            String senderName = "Lanceddy Team";
            String subject = "Xác thực tài khoản của bạn tại lanceddy.com";
            String content = "Chào bạn [[name]],<br>"
                    + "Vui lòng click vào link bên dưới để xác thực tài khoản của bạn!:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">Nhấp vào đây để xác thực</a></h3>"
                    + "Trân trọng!<br>"
                    + "Lanceddy Team!";
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            content = content.replace("[[name]]", account.getUser().getFullName());
            String verifyURL = siteURL + "verify?code=" + account.getUser().getVerificationCode();
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
            String subject = "Lấy lại mật khẩu lanceddy.com";
            String content = "Chào bạn [[name]],<br>"
                    + "Vui lòng click vào link bên dưới để thay đổi mật khẩu mới của bạn!:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">Nhấp vào đây để thay đổi mật khẩu</a></h3>"
                    + "Trân trọng!<br>"
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
            sendResetPasswordEmail(account, "http://localhost:3000");
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
}
