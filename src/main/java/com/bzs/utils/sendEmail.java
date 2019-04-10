package com.bzs.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class sendEmail {
    @Autowired
    private JavaMailSender jms;
    public String send(String email,String code){
        MimeMessage mMessage=jms.createMimeMessage();//创建邮件对象
        MimeMessageHelper mMessageHelper;
        Properties prop = new Properties();
        String from;
        try{
            //从配置文件中拿到发件人邮箱地址
            prop.load(this.getClass().getResourceAsStream("/mail.properties"));
            from = prop.get("mail.smtp.username")+"";
            mMessageHelper=new MimeMessageHelper(mMessage,true);
            mMessageHelper.setFrom(from);//发件人邮箱
            mMessageHelper.setCc(from);
            mMessageHelper.setTo(email);//收件人邮箱
            mMessageHelper.setSubject("用户激活");//邮件的主题
            mMessageHelper.setText("<p>请点击<a href='#'>用户激活</a>" +code+"来激活账号</p>",true);//邮件的文本内容，true表示文本以html格式打开
            jms.send(mMessage);//发送邮件
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1";
    }
}
