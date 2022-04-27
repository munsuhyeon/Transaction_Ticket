package com.suhyeon.ex_spring;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private TicketDao dao;
	@Autowired
	public void setDao(TicketDao dao) {
		this.dao = dao;
	}

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	@RequestMapping("/buy_ticket_card")
	public String buy_ticket_card(TicketDto ticketDto, Model model) {
		
		System.out.println("buy_ticket_card(티켓구매 카드결제)");
		System.out.println("고객아이디(consumerID:" + ticketDto.getConsumerid());
		System.out.println("카드결제한 티겟 수(amount:" + ticketDto.getAmount());
		
		dao.buyTicket(ticketDto);
		
		model.addAttribute("ticketInfo",ticketDto);
		
		int amountCard = ticketDto.getAmount();
		
		if(amountCard > 4) {//rollback 이루어짐!
			return "countError";
		}
		
		return "buy_ticket_end";
	}
	
	@RequestMapping("/buy_ticket")
	public String buy_ticket() {
		return "buy_ticket";
	}
	@RequestMapping("/countError")
	public String countError() {
		return "countError";
	}
}
