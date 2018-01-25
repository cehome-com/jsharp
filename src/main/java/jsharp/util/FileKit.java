package jsharp.util;



import java.io.*;

public class FileKit
{
  public FileKit()
  {
  }
  public static String extractFileExt(String _sFilePathName)
  {
    int nPos=_sFilePathName.lastIndexOf('.');
    return nPos<0?"":_sFilePathName.substring(nPos);
  }
  public static String deleteFileExt(String f)
  {
	  
	  int nPos=f.lastIndexOf('.');
	  return nPos<0?f:f.substring(0,nPos);
  }
  
  public static String changeFileExt(String f,String newExt)
  {
	  
	   return deleteFileExt(f)+newExt;
  }
  
  public static void appendFileText(String _sFileName ,String _sAddContent) throws IOException
  {

    RandomAccessFile raf=new RandomAccessFile(_sFileName ,"rw");
    raf.seek(raf.length());
    raf.writeBytes(_sAddContent);
    raf.close();

  }
  public static void setFileText(String _sFileName ,String _sAddContent) throws IOException
  {

    RandomAccessFile raf=new RandomAccessFile(_sFileName ,"rw");
    raf.setLength(_sAddContent.length());
    raf.writeBytes(_sAddContent);
    raf.close();

  }
  public static void setFileText(String _sFileName ,String _sAddContent,String charset) throws IOException
  {
	  setFileText(new File(_sFileName),_sAddContent,charset);
  }
  public static void setFileText(File _sFileName ,String _sAddContent,String charset) throws IOException
  {
    FileOutputStream fos=null;
    try
    {
    	fos=new FileOutputStream(_sFileName);
    
    if(charset==null || charset.length()==0)
    fos.write(_sAddContent.getBytes());
    else fos.write(_sAddContent.getBytes(charset));
   
    }
    finally
    {
    	if(fos!=null) fos.close();
    }

  }
  public static String getFileText(String fileName) throws IOException
  {

    return getFileText(fileName, null);

  }

  public static String getFileText(String fileName ,String charsetName) throws IOException
  {
	  return getFileText(new File(fileName),charsetName);
  }
  
  public static String getFileText(InputStream is ,String charsetName) throws IOException
  {
	    InputStreamReader r=null;
		if(charsetName==null) r=new InputStreamReader(is);
		else r=new InputStreamReader(is,charsetName);
		StringWriter sw=new StringWriter();
		try
		{
			char[] buf=new char[512];
			int length = -1;
			while ((length = r.read(buf)) != -1)
			{
				sw.write(buf,0,length);						
			}
			return sw.toString();
		}
		finally
		{
			sw.close();
		}
		 
	  
	  
	   
		  
	  
  }
  
  public static String getFileText(File f ,String charsetName) throws IOException
  {
	  if(!f.exists() ) return "";
	  FileInputStream in=null;
	  try{
		  in=new FileInputStream(f);
		  return getFileText(in,charsetName);
		   
		  
	  }
	  finally
	  {
		  if(in!=null) in.close();
	  }
  }
  public static boolean fileExists(String _sPathFileName)
  {
    File file=new File(_sPathFileName);
    return file.exists();
  }
  public static boolean pathExists(String _sPathFileName)
  {
    String sPath=extractFilePath(_sPathFileName);
    return fileExists(sPath);
  }

  public static String extractFileName(String _sFilePathName)
  {
    int nPos=_sFilePathName.lastIndexOf('/');
    int nPos2=_sFilePathName.lastIndexOf('\\');
    if(nPos2>nPos) nPos=nPos2;   		
    return _sFilePathName.substring(nPos+1);
  }

  /*public static String extractHttpFileName(String _sFilePathName)
  {
    int nPos=_sFilePathName.lastIndexOf("/");
    return _sFilePathName.substring(nPos+1);
  }*/
  /*
  public static String extractFilePath(String _sFilePathName)
  {
    int nPos=_sFilePathName.lastIndexOf(File.separatorChar);
    return nPos<0?"":_sFilePathName.substring(0 ,nPos+1);
  }
  */
  public static String extractFilePath(String _sFilePathName)
	{
		int nPos=_sFilePathName.lastIndexOf('/');
	    int nPos2=_sFilePathName.lastIndexOf('\\');
	    if(nPos2>nPos) nPos=nPos2;   		
	    return _sFilePathName.substring(0,nPos);
	}
  
  public static boolean deleteDir(String _sDir)
  {
    return deleteDir(_sDir ,false);
  }

  public static boolean deleteDir(String _sDir ,boolean _bDeleteChildren)
  {
    File file=new File(_sDir);
    if(!file.exists())
      return false;
    if(_bDeleteChildren)
    {
      File files[]=file.listFiles();
      for(int i=0; i<files.length; i++)
        if(files[i].isDirectory())
          deleteDir(files[i].getAbsolutePath() ,_bDeleteChildren);
        else
          files[i].delete();

    }
    return file.delete();
  }
  public static boolean copyFile(File file1, File file2)
  {
		 
	    FileInputStream fis = null;
		FileOutputStream fos = null;
		try
		{
			byte[] bytes = new byte[1024];
			int nOff = 0;

			try
			{
				fis = new FileInputStream(file1);
				try
				{
					file2.getParentFile().mkdirs();
					fos = new FileOutputStream(file2);
					while ((nOff = fis.read(bytes)) != -1)
						fos.write(bytes, 0, nOff);
				} finally
				{

					if (fos != null) fos.close();
				}

			} finally
			{
				if (fis != null) fis.close();
			}
			return true;

		} catch (Exception e)
		{
			System.out.println(e);
			return false;
		} 
	}
  
  public static java.util.List getFileTextList(String filename,String charset) throws IOException
  {
	    java.util.ArrayList list = new java.util.ArrayList();
		File f = new File(filename);
		if (f.exists())
		{
			FileInputStream fis =null;
			try
			{
			fis=new FileInputStream(f);
			InputStreamReader isr = new InputStreamReader(fis,charset);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
			{
				list.add(line);
			}
			}
			finally
			{
			 if(fis!=null) fis.close();
			}
		}
		return list;
  }
  
  
  public static String contactFilePath(String file1, String file2,boolean unixSeparator)
	{
		if (file1 == null && file2 == null) 
			return null;
		if (file1 == null)
			file1 = "";
		if (file2 == null)
			file2 = "";
		int n = 0;
		if (file1.endsWith("/") || file1.endsWith("\\"))
			n++;
		if (file2.startsWith("/") || file2.startsWith("\\"))
			n++;
		if (n == 0)
			return file1 + (unixSeparator?"/":"\\") + file2;
		if (n == 1)
			return file1 + file2;
		return file1 + file2.substring(1);
	}
  
  
  public static File[] listFiles(File dir, final String regex) throws IOException {
		File[] files = dir.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.matches(regex);
			}

		});
		if (files == null)
			files = new File[0];
		return files;
	}

  
  
  public static void main(String[] args) throws Exception
  {
	  System.out.println( getFileText("c:/plugin.properties","UTF-8"));
  }
  public static String addSeparator(String s)
  {
	  if(! s.endsWith("\\")&& ! s.endsWith("/")) s+=File.separator;
	  return s;
  }

}
