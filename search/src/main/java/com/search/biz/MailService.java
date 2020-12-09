package com.search.biz;

import com.search.common.utils.R;
import com.search.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 */
@Service
@Slf4j
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value(value = "${spring.mail.username}")
    private String fromEmail;

    @Value(value = "${EMAIL_REGEXP}")
    private String reg ;

    public R sendSimpleMail(String to, String subject, String content) {
        if(!StringUtils.checkMail(reg,to)){
            return R.error("非法的邮箱输入");
        }
        SimpleMailMessage simpleMessage=new SimpleMailMessage();
        simpleMessage.setFrom(fromEmail);
        simpleMessage.setTo(to);
        String redirectUrl = "https://127.0.0.1:19999/login";
        simpleMessage.setSubject(subject);
        simpleMessage.setText(content);
        try{
            javaMailSender.send(simpleMessage);
            log.info("简单邮件已发送");
            return  R.ok("发送邮件成功");
        }catch (Exception e){
            log.error("发送简单邮件时发生异常！", e);
            return R.error("发送邮件失败");
        }
    }

}
