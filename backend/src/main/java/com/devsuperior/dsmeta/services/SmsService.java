package com.devsuperior.dsmeta.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SmsService {

	@Value("${TWILIO_SID}")
	private String twilioSid;

	@Value("${TWILIO_KEY}")
	private String twilioKey;

	@Value("${TWILIO_PHONE_FROM}")
	private String twilioPhoneFrom;

	@Value("${TWILIO_PHONE_TO}")
	private String twilioPhoneTo;
	
	@Autowired
	private SaleRepository saleRepository;

	public void sendSms(Long saleId) {

		Sale sale = saleRepository.findById(saleId).get();
		
		String date = sale.getDate().getMonthValue() + "/"+ sale.getDate().getYear();
		
		String msg = "O Vendedor "+ sale.getSellerName() + " foi destaque em " + date
				+ ". Com um total de R$ "+ String.format("%.2f", sale.getAmount()) + " em vendas.";
		
		Twilio.init(twilioSid, twilioKey);

		PhoneNumber to = new PhoneNumber(twilioPhoneTo);
		PhoneNumber from = new PhoneNumber(twilioPhoneFrom);

		Message message = Message.creator(to, from, msg).create();

		System.out.println(message.getSid());
	}
}
