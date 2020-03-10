package edu.self.orderservice.service;

import com.google.gson.Gson;
import edu.self.shopbiz.dtos.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Component
public class OrderProcessingServiceImpl {

    @Autowired
    private JavaMailSender javaMailSender;

    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
        OrderDTO orderDTO = new Gson().fromJson(message, OrderDTO.class);

        sendEmail(orderDTO);
    }

    void sendEmail(OrderDTO orderDTO) {
        String customerName = orderDTO.getUserUserName();

        SimpleMailMessage msg = new SimpleMailMessage();
        String receiver = orderDTO.getUserEmail();
        msg.setTo(receiver);

        msg.setSubject("Order Confirmation");

        msg.setText(getEmailBody(customerName, orderDTO.getTotalAmount()));

        javaMailSender.send(msg);

    }

    private String getEmailBody(String customerName, BigDecimal orderAmount) {
        String totalAmount = new DecimalFormat("#0.##").format(orderAmount);
        String body = String.format("Hello %s, Your order is on the way, Total amount is %s", customerName, totalAmount);
        return body;
    }

}
