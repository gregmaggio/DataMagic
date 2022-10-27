package ca.datamagic.root.dao;

import org.junit.Test;

import ca.datamagic.root.testing.BaseTester;

public class SendMailDAOTester extends BaseTester {
	@Test
	public void test1() throws Exception {
		SendMailDAO.sendMessage("no-reply@datamagic.ca", "gregorymaggio@gmail.com", "Test", "Test");
	}

}
