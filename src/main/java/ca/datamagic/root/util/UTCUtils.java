/**
 * 
 */
package ca.datamagic.root.util;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Greg
 *
 */
public class UTCUtils {
	private static SimpleDateFormat dateFormat = null;
	private static SimpleDateFormat timeFormat = null;

	static {
		dateFormat = new SimpleDateFormat("yyyy-MMM-dd");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		timeFormat = new SimpleDateFormat("HH:mm:ss");
		timeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	}
	
	public static String getTimeStamp() {
		Date now = new Date();		
		return MessageFormat.format("{0}T{1}Z", dateFormat.format(now), timeFormat.format(now));
	}
	
	public static String getTimeStamp(Date date) {
		return MessageFormat.format("{0}T{1}Z", dateFormat.format(date), timeFormat.format(date));
	}
}
