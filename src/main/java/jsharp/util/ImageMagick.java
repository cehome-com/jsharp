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
	 * 加上水印
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
	 * 获取一个图片文件的宽裕高，如果获取失败，返回null
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
			r=read(p,1).trim(); // 如果是gif图片会返回多个图片的尺寸，即多行尺寸，所以应该取第一行即可。
			if(r.indexOf('X')==-1)
			{
				logger.error("获取尺寸失败,返回结果不是包含X的正确字符串:"+r);
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
	 * @param source  源文件名
	 * @param dest	目标文件名
	 * @param newSize  新尺寸
	 * @return
	 * @throws Exception
	 */
	public   boolean resize(String source, String dest, int newSize) throws Exception
	{
		 
		ArrayList<String> command = new ArrayList<String>();
		ProcessBuilder pb = null;
		Process p = null;
 
		command.add(imageMagickPath + "/convert");

		
		/*gif 转jpg 会生产多个jpg，下面代码只生成一个
		 * convert image.gif[0] image.jpg
			这时候就会把gif的第一帧保存为image.jpg。类似，可以有convert image.gif[0,3,5] image.jpg，就会把0、3、5帧保存为image-n.jpg
                               如果GIF文件使用了透明背景，那生成的图片就会看到一个黑色的背景，很丑，则convert image.gif[0] -background white -flatten -alpha off image.jpg
		 */
		if(source.toLowerCase().endsWith(".gif")&& !dest.toLowerCase().endsWith(".gif") )
		{
			command.add(source+"[0]");
			command.add("-background");
			command.add("white");
			command.add("-flatten");
			//command.add("-alpha"); 低版本的ImageMagick（如6.2.8） 不支持 -alpha 属性
			//command.add("off");
			
		}
		else
		{
			command.add(source);
		}
		command.add("-resize");
		command.add(newSize+"x"+newSize);
		command.add("-colorspace"); // 如果是cmyk则转成rgb，cmyk在ie中无法浏览
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
	 * 裁剪
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

		
		/*gif 转jpg 会生产多个jpg，下面代码只生成一个
		 * convert image.gif[0] image.jpg
			这时候就会把gif的第一帧保存为image.jpg。类似，可以有convert image.gif[0,3,5] image.jpg，就会把0、3、5帧保存为image-n.jpg
                               如果GIF文件使用了透明背景，那生成的图片就会看到一个黑色的背景，很丑，则convert image.gif[0] -background white -flatten -alpha off image.jpg
		 */
		if(source.toLowerCase().endsWith(".gif")&& !dest.toLowerCase().endsWith(".gif") )
		{
			command.add(source+"[0]");
			command.add("-background");
			command.add("white");
			command.add("-flatten");
			//command.add("-alpha"); 低版本的ImageMagick（如6.2.8） 不支持 -alpha 属性
			//command.add("off");
			
		}
		else
		{
			command.add(source);
		}
		command.add("-crop");
		command.add(w+"x"+h+"+"+x+"+"+y);
		command.add("-colorspace"); // 如果是cmyk则转成rgb，cmyk在ie中无法浏览
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
