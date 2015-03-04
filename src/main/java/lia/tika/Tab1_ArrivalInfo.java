package lia.tika;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tab1_ArrivalInfo implements Runnable {
	public static void main(String args[]){
		String[] result = null;
		String strSiteURL = "http://www.aladin.co.kr/shop/UsedShop/wuseditemall.aspx?ISBN=8968480141&TabType=1";
		
		result = realtimeSchedule(strSiteURL);
		
	}
	

	/**
	 * @param stationURL
	 *            실시간 정보 수집 URL
	 * @return
	 */
	public static String[] realtimeSchedule(String siteURL) {
		// HTML 데이터 수신
		String StationPagedata = CommonFunction.DownloadHtml(siteURL).trim();
		String[] returnschedule;

		int nbeginIndex = 0;
		int nendIndex = 0;
		String strArrivaldata = "";
		int cnt = 0;

		if (StationPagedata.compareTo("") == 0) {
			return null;
		} else {
			returnschedule = new String[4]; // 도착정보마다 * 상,하행 2개씩
			nbeginIndex = StationPagedata.indexOf("<tr><td style=\"padding:8px 0 0 5px; background-color:#F7F7F7;\">", nendIndex);
			nendIndex = StationPagedata.indexOf("<td bgColor=\"#dddddd\" height=\"1\"></td>", nbeginIndex);

			if (nbeginIndex < 0){
				
			}else{
				strArrivaldata = StationPagedata.substring(nbeginIndex,
						nendIndex);
	
				String regex = "최저가 : (.*?)원";
				Pattern pattern = Pattern
						.compile(regex);
				Matcher matches = pattern.matcher(strArrivaldata);
				// 일치하는 패턴이 없으므로 return
	
				while (matches.find()) {
					String strDest = matches.group(1); // 행선지
					System.out.println(Integer.valueOf(strDest.replace(",", "")));
				} // 패턴이 일치할 때 까지
			}
		}

		return returnschedule;
	}


	public void run() {
		// TODO Auto-generated method stub
		
	}

}