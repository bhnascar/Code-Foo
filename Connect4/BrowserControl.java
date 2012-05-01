/* Thanks and credits to Javax Den for this webpage-launching class 
 * Source from: http://javaxden.blogspot.com/2007/09/launch-web-browser-through-java.html
 */

import java.lang.reflect.Method;

public class BrowserControl {
	/**
	* Method to Open the Browser with Given URL
	* @param url
	*/
	public static void openUrl(String url) {
		String os = System.getProperty("os.name");
		Runtime runtime=Runtime.getRuntime();
		try {
			// Block for Windows
			if (os.startsWith("Windows")) {
				String cmd = "rundll32 url.dll,FileProtocolHandler "+ url;
				runtime.exec(cmd);
			}
			// Block for OS X
			else if (os.startsWith("Mac OS")) {
				Class fileMgr = Class.forName("com.apple.eio.FileManager");
				Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] {String.class});
				openURL.invoke(null, new Object[] {url});
			}
			// Block for Unix
			else {
				String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
				String browser = null;
				for (int count = 0; count < browsers.length && browser == null; count++)
					if (runtime.exec(new String[] {"which", browsers[count]}).waitFor() == 0)
				browser = browsers[count];
				if (browser == null) throw new Exception("Could not find web browser");
				else runtime.exec(new String[] {browser, url});
			}
		}
		catch (Exception e) {
			System.err.println("Exception occured while invoking browser!");
			e.printStackTrace();
		}
	}
}
