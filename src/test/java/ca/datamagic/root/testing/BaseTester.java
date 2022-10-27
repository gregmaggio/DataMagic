/**
 * 
 */
package ca.datamagic.root.testing;

import java.io.File;
import java.io.IOException;

import org.junit.BeforeClass;

import ca.datamagic.root.dao.BaseDAO;

/**
 * @author Greg
 *
 */
public abstract class BaseTester {
	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		String dataPath = (new File("src/test/resources")).getCanonicalPath();
		BaseDAO.setDataPath(dataPath);
	}
}
