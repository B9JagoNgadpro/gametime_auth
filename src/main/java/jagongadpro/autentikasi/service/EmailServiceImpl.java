package jagongadpro.autentikasi.service;

import jagongadpro.autentikasi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailServiceImpl{

    @Autowired
    private JavaMailSender emailSender;
    public void  constructResetTokenEmail(String contextPath, String token, User user) {
        String url = contextPath + "/user/password/changePassword?token=" + token;
        emailSender.send(constructEmail("Reset Password", "This is your url \n" + url, user));
    }

    public SimpleMailMessage constructEmail(String subject, String body,
                                             User user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom("gametimejagongadpro@gmail.com");
        return email;
    }

}