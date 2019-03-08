/**
 * 
 */
package ca.datamagic.root.servlet;

import java.text.MessageFormat;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * @author Greg
 *
 */
public class RootContextListener implements ServletContextListener {
	private static Logger _logger = LogManager.getLogger(RootContextListener.class);
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String realPath = sce.getServletContext().getRealPath("/");
		String fileName = MessageFormat.format("{0}/WEB-INF/classes/log4j.cfg.xml", realPath);
		DOMConfigurator.configure(fileName);
		_logger.debug("contextInitialized");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		_logger.debug("contextDestroyed");
	}
}
