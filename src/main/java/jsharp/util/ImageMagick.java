package jsharp.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Images dispose utils
 * 
 * 
 */
public class ImageMagick
{
	protected static final Logger logger = LoggerFactory.getLogger(ImageMagick.class);

	/**
	 * do security watermark
	 * 
	 * @param watermark
	 *            file
	 * @param source
	 *            image
	 * @param magic's
	 *            setup path
	 * @return
	 * @throws Exception
	 */
	
	public ImageMagick(String imageMagickPath)
	{
		this.imageMagickPath=imageMagickPath;
	}
	
	private   String imageMagickPath=null;
	 
	public   String getImageMagickPath()
	{
		return imageMagickPath;
	}

	public   void setImageMagickPath(String imageMagickPath)
	{
		 imageMagickPath = imageMagickPath;
	}

	public   long secWatermark(String file, String info) throws Exception
	{

		long cT = System.currentTimeMillis();
		ArrayList<String> command = new ArrayList<String>();
		ProcessBuilder pb = null;
		Process p = null;
		command.add(imageMagickPath);
		command.add("-E");
		command.add(file);
		command.add(info);
		pb = new ProcessBuilder();
		pb.command(command);
		logger.debug("command :{}", pb.command());
		try
		{
			p = pb.start();
		}
		catch (Exception e)
		{
			logger.error("convert fail. logs:{}", e.getMessage());
			return 0;
		}
		print(p);
		long time = System.currentTimeMillis() - cT;
		logger.debug("founder wm value: {}", time / 1000);
		return time;
	}

	/**
	 * ����ˮӡ
	 * 
	 * @param watermark
	 *            file
	 * @param source
	 *            image
	 * @param magic's
	 *            setup path
	 * @return
	 * @throws Exception
	 */
	public   boolean watermark(String source,String dest,String markFile) throws Exception
	{
		ArrayList<String> command = new ArrayList<String>();
		ProcessBuilder pb = null;
		Process p = null;
		command.add(imageMagickPath + "/convert");
		command.add(source);

		command.add("-gravity");
		command.add("Center");
		command.add(markFile);
		command.add("-compose");
		command.add("Multiply");
		command.add("-composite");

/*		command.add("-gravity");
		command.add("NorthWest");
		command.add(markFile);
		command.add("-compose");
		command.add("Multiply");
		command.add("-composite");

		command.add("-gravity");
		command.add("SouthEast");
		command.add(markFile);
		command.add("-compose");
		command.add("Multiply");
		command.add("-composite");*/

		command.add(dest);
		pb = new ProcessBuilder();
		pb.command(command);
		// System.out.println(pb.command());
		logger.debug("command :{}", pb.command());
		try
		{
			p = pb.start();
		}
		catch (Exception e)
		{
			logger.error("convert fail. logs:{}", e.getMessage());
			return false;
		}

		return print(p);

	}

	/**
	 * pritin logs
	 * 
	 * @param p
	 * @return
	 * @throws Exception
	 */
	private   boolean print(Process p) throws Exception
	{
		InputStream is = p.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		InputStream es = p.getErrorStream();
		BufferedReader brEs = new BufferedReader(new InputStreamReader(es));
		boolean flag = true;
		try
		{
			String line;
			//logger.debug("magick's inputStream:");
			while ((line = br.readLine()) != null)
			{
				logger.debug(line);
			}

			//logger.debug("magick's errorStream:");
			while ((line = brEs.readLine()) != null)
			{
				logger.error("magick's error:");
				logger.error(line);
				flag = false;
			}

		}
		finally
		{
			
			Common.closeObjects(br,brEs,is,es);
		 
		}
		return flag;

	}
	
	
	private String  read(Process p,int lineCount)throws Exception
	{
		InputStream is = p.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		InputStream es = p.getErrorStream();
		BufferedReader brEs = new BufferedReader(new InputStreamReader(es));
		StringBuffer sb=new StringBuffer();  
		try
		{
			String line;
			//logger.debug("magick's inputStream:");
			int count=0;
			while ((line = br.readLine()) != null)
			{
				sb.append(line).append("\r\n");
				count++;
				if(count>=lineCount) break;
			}
			
			while ((line = brEs.readLine()) != null)
			{
				logger.error("magick's error:");
				logger.error(line);
				 
			}
			
			
			return sb.toString();

		 
		}
		finally
		{
			Common.closeObjects(br,brEs,is,es);
			  
		}
		 
	}
	
	/**
	 * ��ȡһ��ͼƬ�ļ��Ŀ�ԣ�ߣ������ȡʧ�ܣ�����null
	 * @param source
	 * @return
	 */
	public java.awt.Dimension getSize(String source)
	{
		ArrayList<String> command = new ArrayList<String>();
		ProcessBuilder pb = null;
		Process p = null;
 
		command.add(imageMagickPath + "/convert");

		command.add(source);
		command.add("-format");
		command.add("%wX%h" );
		command.add("info:");
		pb = new ProcessBuilder();
		pb.command(command);
		logger.debug("command :{}", pb.command());
		String r=null;
		try
		{ 
			p = pb.start();
			logger.debug("read info"); 
			r=read(p,1).trim(); // �����gifͼƬ�᷵�ض��ͼƬ�ĳߴ磬�����гߴ磬����Ӧ��ȡ��һ�м��ɡ�
			if(r.indexOf('X')==-1)
			{
				logger.error("��ȡ�ߴ�ʧ��,���ؽ�����ǰ���X����ȷ�ַ���:"+r);
				return null;
			}
			 
			logger.debug("parse width and height from "+r); 
			String[] x=r.split("X");
			
			java.awt.Dimension d=new java.awt.Dimension(Integer.parseInt(x[0]), Integer.parseInt(x[1]));
		 	
		    return d;
			
			
		}
		catch (Exception e)
		{
			
			logger.error( java.text.MessageFormat.format("getSize fail. file={0};result={1};exception={2}",
					source,r,e.getMessage()));
			return null ;
		}
		 
	}

	/**
	 * 
	 * @param source  Դ�ļ���
	 * @param dest	Ŀ���ļ���
	 * @param newSize  �³ߴ�
	 * @return
	 * @throws Exception
	 */
	public   boolean resize(String source, String dest, int newSize) throws Exception
	{
		 
		ArrayList<String> command = new ArrayList<String>();
		ProcessBuilder pb = null;
		Process p = null;
 
		command.add(imageMagickPath + "/convert");

		
		/*gif תjpg ���������jpg���������ֻ����һ��
		 * convert image.gif[0] image.jpg
			��ʱ��ͻ��gif�ĵ�һ֡����Ϊimage.jpg�����ƣ�������convert image.gif[0,3,5] image.jpg���ͻ��0��3��5֡����Ϊimage-n.jpg
                               ���GIF�ļ�ʹ����͸�������������ɵ�ͼƬ�ͻῴ��һ����ɫ�ı������ܳ���convert image.gif[0] -background white -flatten -alpha off image.jpg
		 */
		if(source.toLowerCase().endsWith(".gif")&& !dest.toLowerCase().endsWith(".gif") )
		{
			command.add(source+"[0]");
			command.add("-background");
			command.add("white");
			command.add("-flatten");
			//command.add("-alpha"); �Ͱ汾��ImageMagick����6.2.8�� ��֧�� -alpha ����
			//command.add("off");
			
		}
		else
		{
			command.add(source);
		}
		command.add("-resize");
		command.add(newSize+"x"+newSize);
		command.add("-colorspace"); // �����cmyk��ת��rgb��cmyk��ie���޷����
		command.add("sRGB");
		command.add(dest);
		pb = new ProcessBuilder();
		pb.command(command);
		logger.debug("command :{}", pb.command());
		try
		{
			p = pb.start();
		}
		catch (Exception e)
		{
			logger.error( java.text.MessageFormat.format("resize fail. source={0};dest={1};exception={2}",
					source,dest,e.getMessage()));
			 
			return false ;
		}
		return print(p);
	 
	 
	 
	}
	
	/**
	 * �ü�
	 * @param source
	 * @param dest
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 * @throws Exception
	 */
	public boolean crop(String source,String dest, int x, int y, int w,int h )throws Exception
	{
		ArrayList<String> command = new ArrayList<String>();
		ProcessBuilder pb = null;
		Process p = null;
 
		command.add(imageMagickPath + "/convert");

		
		/*gif תjpg ���������jpg���������ֻ����һ��
		 * convert image.gif[0] image.jpg
			��ʱ��ͻ��gif�ĵ�һ֡����Ϊimage.jpg�����ƣ�������convert image.gif[0,3,5] image.jpg���ͻ��0��3��5֡����Ϊimage-n.jpg
                               ���GIF�ļ�ʹ����͸�������������ɵ�ͼƬ�ͻῴ��һ����ɫ�ı������ܳ���convert image.gif[0] -background white -flatten -alpha off image.jpg
		 */
		if(source.toLowerCase().endsWith(".gif")&& !dest.toLowerCase().endsWith(".gif") )
		{
			command.add(source+"[0]");
			command.add("-background");
			command.add("white");
			command.add("-flatten");
			//command.add("-alpha"); �Ͱ汾��ImageMagick����6.2.8�� ��֧�� -alpha ����
			//command.add("off");
			
		}
		else
		{
			command.add(source);
		}
		command.add("-crop");
		command.add(w+"x"+h+"+"+x+"+"+y);
		command.add("-colorspace"); // �����cmyk��ת��rgb��cmyk��ie���޷����
		command.add("sRGB");
		command.add(dest);
		pb = new ProcessBuilder();
		pb.command(command);
		logger.debug("command :{}", pb.command());
		try
		{
			p = pb.start();
		}
		catch (Exception e)
		{
			logger.error( java.text.MessageFormat.format("resize fail. source={0};dest={1};exception={2}",
					source,dest,e.getMessage()));
			 
			return false ;
		}
		return print(p);
		 
	}
	
	public static  void main(String[] args) throws Exception
	{
		ImageMagick im=new ImageMagick("D:/ImageMagick-6.5.8-8");
		
		//System.out.println("height="+im.getSize("D:/ImageMagick-6.5.8-8/a1.jpg").height);
	
		//im.resize("D:/ImageMagick-6.5.8-8/down.gif","D:/ImageMagick-6.5.8-8/zf.jpg",150);
		//im.watermark("D:/ImageMagick-6.5.8-8/a.jpg","D:/ImageMagick-6.5.8-8/d.jpg","D:/ImageMagick-6.5.8-8/mark.jpg");
	}

}
