package ca.datamagic.root.dao;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

public class SendMailDAOTester {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DOMConfigurator.configure("src/test/resources/log4j.cfg.xml");
	}

	@Test
	public void test1() throws Exception {
		SendMailDAO.sendMessage("no-reply@datamagic.ca", "gregorymaggio@gmail.com", "Test", "Test");
	}

}
