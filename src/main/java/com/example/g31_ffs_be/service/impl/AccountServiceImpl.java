package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.AccountDto;
import com.example.g31_ffs_be.model.Account;
import com.example.g31_ffs_be.repository.AccountRepository;
import com.example.g31_ffs_be.service.AccountService;
import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import java.util.List;
import java.util.Random;

@Service
@Component
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository repo;
    @Autowired
    JavaMailSender mailSender;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Bean
    @ElementCollection(fetch = FetchType.LAZY)
    public List<AccountDto> getAllAccounts() {
      /*  List<AccountDto> lists=new ArrayList<>();
        for (Account a:repo.findAll()) {
            AccountDto acc = modelMapper.map(a, AccountDto.class);

            lists.add(acc);

        }*/
        return null;
    }

    public List<Account> getAllAccount() {
        return (List<Account>) repo.findAll();
    }

    @Override
    public void addAccount(Account f) {
        try {
            repo.save(f);
        } catch (Exception e) {

        }

    }

    @Override
    public Boolean checkIdExist(String id) {
        if (repo.existsById(id)) return true;

        return false;
    }

    @Override
    public Boolean checkEmailExist(String email) {

      return repo.findByEmail(email)!=null?true:false;
    }

    @Override
    public void sendVerificationEmail(Account account,String siteURL) {
        try {
            String toAddress = "bienvancong1@gmail.com";
            String fromAddress = "lanceddywebsite@gmail.com";
            String senderName = "Your company name";
            String subject = "Please verify your registration";
            String content = "Dear Cong,<br>"
                    + "Please click the link below to verify your registration:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                    + "Thank you,<br>"
                    + "Your company name.";

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);

            helper.setText(content, true);
            System.out.println(helper.getMimeMessage());

            mailSender.send(message);

        }
        catch(Exception e){

        }
    }

    @Override
    public void addAccountRole(String account_id, int role_id) {

    }


}
