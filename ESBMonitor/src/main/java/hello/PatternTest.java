package hello;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternTest {
	public static void main(String[] args) {
		String url = "SH_SH_MOBILE_CNOS_HUAWEI_CXDR_RNC003_0020_20170317014249_S1UHTTP_20_0.tar.gz";
		Pattern pattern = Pattern.compile("_(20\\d{2}\\d{2}\\d{2}\\d{2}\\d{2}\\d{2})");
		Matcher matcher = pattern.matcher(url);
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		if (matcher.find()) {
			String datestr= matcher.group(1);
		    System.out.println(datestr); //prints /{item}/
		    
		    System.out.println(tryParse(df, datestr));
		} else {
		    System.out.println("Match not found");
		}
	}
	
	  private static Date tryParse(DateFormat df, String s) {
	        try {
	            return df.parse(s);
	        } catch (ParseException e) {
	            return null;
	        }
	    }
}
