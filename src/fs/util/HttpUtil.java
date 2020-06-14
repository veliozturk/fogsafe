package fs.util;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;



public class HttpUtil {
	private static boolean debug = true;
	public static String send(String targetURL, String urlParameters) {
		return send(targetURL, urlParameters, "POST", null);
		
	}
	public static String send(String targetURL, String urlParameters, String method, Map<String, String> reqPropMap) {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method);
			if(reqPropMap==null || reqPropMap.isEmpty()){
				connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
				connection.setRequestProperty("Content-Language", "en-EN");
			} else for(String key:reqPropMap.keySet()){
				connection.setRequestProperty(key,reqPropMap.get(key));
			}
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			if((urlParameters!=null && urlParameters.length()>0)|| method.equals("POST")){
				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				wr.write((urlParameters==null ? "":urlParameters).getBytes("UTF-8"));
				wr.flush();
				wr.close();
			}

			// Get Response
			boolean success = connection.getResponseCode()>=200 && connection.getResponseCode()<300;
			InputStream is = success ? connection.getInputStream() : connection.getErrorStream();

			StringBuilder response = new StringBuilder();
			if(is!=null) {
				BufferedReader rd = new BufferedReader(new InputStreamReader(is,"UTF-8"));
				String line;
				
				while ((line = rd.readLine()) != null) {
					response.append(line);
					response.append('\r');
				}
				rd.close();
			} else {
				response.append("ERROR CODE: ").append(connection.getResponseCode());
			}
			if(!success) {
				if(response.length()>0) {
					if(response.charAt(0)=='{') 
						response = response.insert(1, "\"_code_\":"+connection.getResponseCode()+",");
				} else response.append("{\"_code_\":").append(connection.getResponseCode()).append("}");
			}
			return response.toString();


		} catch (Exception e) {
			if(debug)e.printStackTrace();
//			 throw e;
			return "{\"error\":\""+e.getMessage()+"\"}";
			
//			throw ne;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
	
}
