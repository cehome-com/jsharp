package jsharp.util;

import javax.servlet.http.HttpServletRequest;


/**
 * ��װ��ҳ���������
 * 
 * @param <T>
 *            Page�еļ�¼����
 * @author Liang
 */
public class PageBar<T>{
	

	/** ��ҳ��Ϣ������request�еĲ������� */
	public static final String PAGE_BAR_OBJECT = "PAGE_BAR";

	//public static final String REQ_FLIP_LC = "lc";

	private String pageNoParamName = "pageNo";

	//private String licenseCode;
	
	//private int pageStart;

	private String pageUrl;
	private String baseUrl;
	public static final int DEFAULT_PAGESIZE = 10;

	public static final int DEFAULT_PAGE = 1;

	private long pageNo = DEFAULT_PAGE;

	private long pageSize = DEFAULT_PAGESIZE;

	private long pageCount;
	private long recordCount;
 
	private long recordStart;

	 
	private int pagesShow = 10; // ��ҳ������ʾ�ķ�ҳ�� like google
	
	private T data;
	
	public void setStart(int start) {
		this.recordStart = start;
	}

	public int getPagesShow() {
		return pagesShow;
	}
	

	public void setPagesShow(int pagesShow) {
		this.pagesShow = pagesShow;
	}
	
	public  PageBar(long nPageNo,long nPageSize,long nRecordCount)  {
		if(nRecordCount==0) return; 
		recordCount=nRecordCount;
		if(nPageSize<1) throw new RuntimeException("Invalid PageSize:"+nPageSize);
	 
		
		long nPageCount = (nRecordCount - 1) / nPageSize + 1;
		if (nPageNo==-2|| nPageNo > nPageCount) nPageNo = nPageCount;
		if (nPageNo < 1) nPageNo = 1;
		
	 
		pageSize=nPageSize;
		pageCount=nPageCount;
		pageNo=nPageNo;
		
		recordStart = (pageNo - 1) * pageSize ; 
	}
	
	public  PageBar(long nPageNo,long nPageSize,long nRecordCount,HttpServletRequest request, String pageNoParamName,String... filterParamNames)  {
		this(nPageNo,nPageSize,nRecordCount);
		this.setRequest(request, pageNoParamName, filterParamNames);
	}
	

	// like google
	public long getPageStart() {
		long before = pageNo - 1;
		long after = pageCount - pageNo;

		if (before < 5)
			return 1; // ���ǰ��С��5ҳ��ȫ�����£��������󷭡�
		else {
			if (after > pagesShow - 5) {
				return pageNo - 4;
			} else {
				if (pageCount > pagesShow) {
					if (after <= 5) {
						return pageCount - 9;
					}
					return pageCount - 10;
				} else {
					return 1;
				}
			}
		}
	}

	/**
	 * google like. pageend.
	 * 
	 * @return
	 */
	public long getPageEnd() {
		if (pageCount < 1)
			return 1;

		// int before = pageNo - 1 ;
		long after = pageCount - pageNo;

		if (after < 5)
			return pageCount;

		else {
			if (getPageStart() > 1) {
				return Math.min(getPageStart() + pagesShow - 1, pageCount);
			} else {
				return Math.min(pageCount, pagesShow);
			}
		}
	}

	 
 
	 /* ��һ����¼�ڽ�����е�λ��,��Ŵ�0��ʼ.
	 */
	public long getRecordStart() {
		return recordStart;
	}

	/**
	 * ��ҳ��.
	 */
	public long getPageCount() {
		
		return pageCount;
	 
	}

	/**
	 * �Ƿ�����һҳ.
	 */
	public boolean isNext() {
		return (pageNo + 1 <= getPageCount());
	}

	/**
	 * ������ҳ��ҳ��,��Ŵ�1��ʼ.
	 */
	public long getNextPage() {
		if (isNext())
			return pageNo + 1;
		else
			return pageNo;
	}

	/**
	 * �Ƿ�����һҳ.
	 */
	public boolean isPrev() {
		return (pageNo - 1 >= 1);
	}

	/**
	 * ������ҳ��ҳ��,��Ŵ�1��ʼ.
	 */
	public long getPrevPage() {
		if (isPrev())
			return pageNo - 1;
		else
			return pageNo;
	}

	/**
	 * ÿҳ�ļ�¼����.
	 */
	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * ��ǰҳ��ҳ��,��Ŵ�1��ʼ.
	 */
	public long getPageNo() {
		return pageNo;
	}

	public void setPageNo(long page) {
		this.pageNo = page;
	}

	/**
	 * �ܼ�¼����.
	 */
	public long getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	
 

	/**
	 * ���÷�ҳ�Ļ���URL��ͬʱ�Ѵ�PageFlipͨ��key��FLIP_SOURCE���浽request��attribute��
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param pageNoParamName
	 *            �洢��request�У����ڱ�ʾ��ǰҳ��Ĳ�������
	 */
	public void setRequest(HttpServletRequest request, String pageNoParamName,String... filterParamNames) {
		String queryString = request.getQueryString();
		queryString = removeParam(queryString, pageNoParamName);
		if (filterParamNames!=null&&filterParamNames.length>0)
		{
			for(String filterParamsName:filterParamNames)
			queryString = removeParam(queryString, filterParamsName);
		}

		/*String url = request.getServletPath();
		if (!request.getContextPath().equals("/"))
			url = request.getContextPath() + url;
		if (request.getQueryString() != null) {
			url += "?" + request.getQueryString();
		} 
		String baseUrl=removeParam(url,"pn");*/
		
		StringBuilder spath = new StringBuilder(request.getRequestURI());
		spath.append("?").append(queryString);
		 
		this.baseUrl=spath.toString();
		this.pageUrl=this.baseUrl+(queryString.length()==0?"":"&")+pageNoParamName+"="+this.getPageNo();

		this.pageNoParamName = pageNoParamName;

		request.setAttribute(PAGE_BAR_OBJECT, this);
	}

	public String getPageNoParamName() {
		return pageNoParamName;
	}

	public void setPageNoParamName(String pageNoParamName) {
		this.pageNoParamName = pageNoParamName;
	}

	/**
	 * ��URL��ַ�и�����һ������ȥ����
	 * 
	 * @param queryString
	 *            ��Ҫ�����URL��ַ
	 * @param toescape
	 *            Ҫɾ���Ĳ������ơ�
	 */
	/*private static String getSubQueryString(String queryString, String toescape) {
		// String queryString = request.getQueryString() ;
		// FIXME �и�BUG
		if (queryString == null)
			return "";

		int pos = queryString.indexOf(toescape);
		if (pos < 0)
			return queryString; // ��������Ҫ����

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
	*/
	// id=5&pn=dd ==> id=5
		private String removeParam(String query, String name) {
			if(query==null) return "";
			int n = query.indexOf(name + "=");
			if (n >= 0) {
				int begin = n>0&&query.charAt(n-1)=='&'?n-1: n;
				int end = query.indexOf('&', begin+1);
				if (end == -1)
					return query.substring(0, begin);
				else
					return query.substring(0, begin) + query.substring(end);
			}
			return query;
		}

	public String getPageUrl() {
		return pageUrl;
	}

	 
	public String getBaseUrl() {
		return baseUrl;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
 

	 
	
	
}
