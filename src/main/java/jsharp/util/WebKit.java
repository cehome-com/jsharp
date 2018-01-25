package jsharp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;


public class WebKit
{

	/**
	 * 设置基础URL。同时把此PageFlip通过key：FLIP_SOURCE保存到request的attribute中
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param pageNoParamName
	 *            存储在request中，用于表示当前页码的参数名称
	 */
	public static String getFilterQueryString(HttpServletRequest request, String... filterParamNames) {
		String queryString = request.getQueryString();
		if (filterParamNames!=null&&filterParamNames.length>0)
		{
			for(String filterParamsName:filterParamNames)
				if(filterParamsName!=null&& filterParamsName.length()>0)
					queryString = getSubQueryString(queryString, filterParamsName);
		}
		 
		StringBuffer spath = new StringBuffer(request.getRequestURI());
 
		spath.append("?").append(queryString);
	
		return spath.toString();

	 
	}
 

	/**
	 * 将URL地址中给定的一个参数去掉。
	 * 
	 * @param queryString
	 *            将要处理的URL地址
	 * @param toescape
	 *            要删掉的参数名称。
	 */
	private static String getSubQueryString(String queryString, String toescape) {
		// String queryString = request.getQueryString() ;
		// FIXME 有个BUG
		if (queryString == null)
			return "";

		int pos = queryString.indexOf(toescape);
		if (pos < 0)
			return queryString; // 不存在需要的数

		StringBuffer sb = new StringBuffer(128);
		int total = queryString.length();

		if (pos > 0) {
			int i = 0;
			while (i < pos) {
				sb.append(queryString.charAt(i));
				i++;
			}
		}

		while (pos < total && queryString.charAt(pos++) != '&') {
			;
		}
		while (pos < total) {
			sb.append(queryString.charAt(pos));
			pos++;
		}
		int sbl = sb.length();
		if (sbl > 0 && sb.charAt(sbl - 1) == '&') {
			sb.setLength(sbl - 1);
		}

		return sb.toString();
	}
	
	public static Cookie getCookie(HttpServletRequest request, String name) {
		 
		Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					return cookie;
				}
			}
		}
		return null;
	}
	
	public static String getContentType(String filename)
	{
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		return  fileNameMap.getContentTypeFor(filename);
	}
	 
	public static void sendFile(HttpServletResponse response, String filename,
			boolean isAttachment, String attachmentName)
			throws java.io.IOException {
		
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String contentType = fileNameMap.getContentTypeFor(filename);
		if (!new File(filename).exists())
			throw new java.io.FileNotFoundException(filename);
		InputStream is =null;
		if (isAttachment && (attachmentName == null || attachmentName.length() == 0))
				attachmentName = FileKit.extractFileName(filename);
		 
		try
		{
		  is = new FileInputStream(filename);
		sendFile(response,is,contentType,isAttachment,attachmentName);
		}
		finally
		{
			Common.closeObject(is);
			
		}
		
	}
	
	/**
	 * 
	 * @param response
	 * @param filename
	 *            源文件名
	 * @param isAttachment
	 *            是否作为附件下载
	 * @param attachmentName
	 *            附件名称
	 */
	public static void sendFile(HttpServletResponse response, InputStream is, String contentType,
			boolean isAttachment, String attachmentName)
			throws java.io.IOException {
		
		if (contentType == null)
			contentType = "application/unknown";
		response.setContentType(contentType);
		if (isAttachment) {
			 
			response.setHeader("Content-Disposition",
					"attachment;filename="
							+ new String(attachmentName.getBytes("gb2312"),
									"iso8859-1"));

		}

		 
		OutputStream os = response.getOutputStream();
		//FileMan fm = null;
		try {

			// 本地测试用
			/*if (media.core.Common.isTestEnv()
					&& Config.getProperty("url-file-root", "").length() > 0
					&& filename.indexOf(Config.getFilePathTemp()) == -1) {
				fm = new FileMan(Config.getProperty("url-file-root", ""), "");
				is = fm.getFile(filename).getInputStream();

			} else {*/

				 
			//}
			IOUtils.copy(is, os);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
			response.setContentType("text/html; charset=UTF-8");
			//os.write(Convert.toString(e).getBytes("UTF-8")); 
			//logger.error("", e);
			// throw e;
		} finally {
			 
			 
			try {
				os.close();
			} catch (Exception e) {
			}

		}

	}

	
	
}
