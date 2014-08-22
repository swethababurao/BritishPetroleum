package com.example.britishpetroleum.helper;

/**
 * Helper Class
 */
public final class APIHelper {

	public static String BASE_URL = "http://www.wowlabz.com/projects/bp_oman/index.php/api/";

	/**
	 * Helper method to get Api url based on ApiMethod call
	 * 
	 * @param iMethod
	 *            Type of Api
	 * @return Url String
	 */
	public static String getUrl(APIMethods iMethod) {

		String URL = "";
		switch (iMethod) {

		case GET_ALL_JOBS:
			URL = "getAllJobs";
			break;

		case REGISTER_FOR_ALERT:
			URL = "registerForAlert";
			break;
		}
		return BASE_URL + URL;
	}

	/*
	 * public static String convertStreamToString(InputStream instream) { //
	 * TODO Auto-generated method stub String result = "";
	 * 
	 * try { BufferedReader reader = new BufferedReader(new InputStreamReader(
	 * instream, "iso-8859-1"), 8); StringBuilder sb = new StringBuilder();
	 * String line = null; while ((line = reader.readLine()) != null) {
	 * sb.append(line); } result = sb.toString();
	 * 
	 * } catch (Exception e) { Log.d("log_tag", "Error converting result " +
	 * e.toString()); } return result; }
	 * 
	 * public static String getTimestamp(String iTimeMillis) { Date d = new
	 * Date(Long.parseLong(iTimeMillis)); SimpleDateFormat aTimeStampFormatter =
	 * new SimpleDateFormat( "dd MMM,yyyy"); String aTimeStamp =
	 * aTimeStampFormatter.format(d); return aTimeStamp; }
	 * 
	 * public static String convertTimeStamp(String iDate) { String finalDate =
	 * ""; try { SimpleDateFormat dateFormat = new
	 * SimpleDateFormat("yyyy-MM-dd"); Date myDate = null; try { try { myDate =
	 * dateFormat.parse(iDate); } catch (java.text.ParseException e) {
	 * e.printStackTrace(); } } catch (ParseException e) { e.printStackTrace();
	 * } SimpleDateFormat timeFormat = new SimpleDateFormat("dd MMM"); finalDate
	 * = timeFormat.format(myDate); } catch (Exception e) { e.printStackTrace();
	 * } return finalDate; }
	 * 
	 * public static String convertTimeStampToEventFormat(String iDate) { String
	 * finalDate = ""; try { SimpleDateFormat dateFormat = new
	 * SimpleDateFormat("yyyy-MM-dd"); Date myDate = null; try { try { myDate =
	 * dateFormat.parse(iDate); } catch (java.text.ParseException e) {
	 * e.printStackTrace(); } } catch (ParseException e) { e.printStackTrace();
	 * } SimpleDateFormat timeFormat = new SimpleDateFormat(
	 * "dd MMMM yyyy, EEEE"); finalDate = timeFormat.format(myDate); } catch
	 * (Exception e) { e.printStackTrace(); } return finalDate; }
	 * 
	 * public static long convertTimeStampToMillis(String iDate) { Date myDate =
	 * null; try { SimpleDateFormat dateFormat = new
	 * SimpleDateFormat("dd-MM-yyyy"); try { try { myDate =
	 * dateFormat.parse(iDate); } catch (java.text.ParseException e) {
	 * e.printStackTrace(); } } catch (ParseException e) { e.printStackTrace();
	 * } } catch (Exception e) { e.printStackTrace(); } return myDate.getTime();
	 * }
	 */
}
