package com.suhyeon.ex_spring;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

public class TicketDao {
	JdbcTemplate template;
	TransactionTemplate transactionTemplate;
	PlatformTransactionManager transactionManager;
	
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public TicketDao() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void buyTicket(final TicketDto dto) {
		
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				
				try {
				template.update(new PreparedStatementCreator() {
					
					//카드결제
					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				
						String query="INSERT INTO card (consumerid, amount) VALUES (?, ?)";
						PreparedStatement pstmt = con.prepareStatement(query);
						pstmt.setString(1,dto.getConsumerid());
						pstmt.setInt(2,dto.getAmount());
						
						return pstmt;
					}
				});
				
				template.update(new PreparedStatementCreator() {
					
					//티켓구매
					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				
						String query="INSERT INTO ticket (consumerid, countnum) VALUES (?, ?)";
						PreparedStatement pstmt = con.prepareStatement(query);
						pstmt.setString(1,dto.getConsumerid());
						pstmt.setInt(2,dto.getAmount());
						return pstmt;
					}
				});
				
				
				
				}catch(Exception e) {
					e.printStackTrace();
					
					HomeController cont = new HomeController();
					
					cont.countError();
					
					System.out.println("Rollback!!");
					
					
				}
			}
		});
	}
}
