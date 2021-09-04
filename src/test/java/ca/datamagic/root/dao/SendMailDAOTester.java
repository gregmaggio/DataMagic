package ca.datamagic.root.dao;

import org.junit.Test;

public class SendMailDAOTester {
	@Test
	public void test1() throws Exception {
		SendMailDAO.sendMessage("no-reply@datamagic.ca", "gregorymaggio@gmail.com", "Test", "Test");
	}

}
