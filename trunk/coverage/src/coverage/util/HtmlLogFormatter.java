/**
 * 
 */
package coverage.util;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * @author user
 *
 */
public class HtmlLogFormatter extends Formatter {

	/**
	 * 
	 */
	public HtmlLogFormatter() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	@Override
	public String format(LogRecord rec) {
		// TODO Auto-generated method stub
		StringBuffer buf = new StringBuffer(1000);
	    // Bold any levels >= WARNING
	    buf.append("<tr>");
	    buf.append("<td>");

	    if (rec.getLevel().intValue() >= Level.WARNING.intValue()) {
	      buf.append("<b color='red'>");
	      buf.append(rec.getLevel());
	      buf.append("</b>");
		  buf.append("</td>");
		  buf.append("<td><font color='red'>");
		  buf.append(formatMessage(rec));
		  buf.append('\n');
		  buf.append("</font></td>");
		  buf.append("</tr>\n");
	    } else {
	      buf.append(rec.getLevel());
		  buf.append("</td>");
		  buf.append("<td>");
		  buf.append(formatMessage(rec));
		  buf.append('\n');
		  buf.append("</td>");
		  buf.append("</tr>\n");
	    }

	    return buf.toString();
	}

	
	// This method is called just after the handler using this
	  // formatter is created
	  public String getHead(Handler h) {
	    return "<HTML>\n<HEAD>\n" + (new Date()) 
	        + "\n</HEAD>\n<BODY>\n<PRE>\n"
	        + "<table width=\"100%\" border>\n  "
	        + "<tr><th>Level</th>" +
	        //"<th>Time</th>" +
	        "<th>Log Message</th>" +
	        "</tr>\n";
	  }

	  // This method is called just after the handler using this
	  // formatter is closed
	  public String getTail(Handler h) {
	    return "</table>\n  </PRE></BODY>\n</HTML>\n";
	  }
}
