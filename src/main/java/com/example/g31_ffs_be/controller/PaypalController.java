package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.config.PaypalPaymentIntent;
import com.example.g31_ffs_be.config.PaypalPaymentMethod;
import com.example.g31_ffs_be.service.PaypalService;
import com.example.g31_ffs_be.utils.Utils;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PaypalController {
	
	public static final String URL_PAYPAL_SUCCESS = "pay/success";
	public static final String URL_PAYPAL_CANCEL = "pay/cancel";
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private PaypalService paypalService;
	
	@GetMapping("/")
	public String index(){
		return "index";
	}
	
	@GetMapping("/pay")
	public ResponseEntity<?> pay(HttpServletRequest request, @RequestParam("price") double price ){
		String cancelUrl = Utils.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;
		String successUrl = Utils.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS+"?price="+price;
		try {
			Payment payment = paypalService.createPayment(
					price, 
					"USD",
					PaypalPaymentMethod.paypal,
					PaypalPaymentIntent.sale,
					"payment description", 
					cancelUrl, 
					successUrl);
			for(Links links : payment.getLinks()){
				if(links.getRel().equals("approval_url")){
					return new ResponseEntity<>(links.getHref(), HttpStatus.OK);
				}
			}
		} catch (PayPalRESTException e) {
			log.error(e.getMessage());
		}
		return new ResponseEntity<>("Lỗi thanh toán!", HttpStatus.OK);
	}

	@GetMapping(URL_PAYPAL_CANCEL)
	public ResponseEntity<?> cancelPay(){
		return new ResponseEntity<>("Thanh toan Khong thanh Cong", HttpStatus.OK);
	}

	@GetMapping(URL_PAYPAL_SUCCESS)
	public ResponseEntity<?> successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId,@RequestParam("price") String price){
		try {
				return new ResponseEntity<>("Bạn đã nạp thành công" +price +"$ vào hệ thống!", HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return new ResponseEntity<>("", HttpStatus.OK);
	}
	
}
