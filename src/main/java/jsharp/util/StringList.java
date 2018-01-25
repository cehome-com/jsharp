package jsharp.util;

import java.util.*;
import java.io.*;

public class StringList {
  private  String lineSeparator=System.getProperty("line.separator");
  private List strings = new ArrayList();
  private List objects = new ArrayList();
  private boolean sorted = false;
  private char nameValueSeparator='=';

  public StringList() {
  }
  
  public void setLineSep(String sep)
  {
	  lineSeparator=sep;
  }

  public String[] getStrings() {
    return (String[])strings.toArray(new String[strings.size()]);
  }
  
 public String[] toArray()
 {
	 return  getStrings();
 }

  public String[][] toArray2()
  {
	  String[][] result=new String[ size()][2];
	  for(int i=0;i<size();i++)
	  {
		  
		  String s= get(i);
		  int n=s.indexOf(nameValueSeparator);
		  if(n==-1) 
		  {
			  result[i][0]=s;
			  result[i][1]="";
			   
		  }
		  else
		  {
			  result[i][0]=s.substring(0,n);
			  result[i][1]=s.substring(n+1);
		  }
		   
	  }
	  return result;
  }
  
  
  public void setStrings(String[] array){
    strings = null; objects=null;
    strings = new ArrayList(Arrays.asList(array));
    objects = new ArrayList(array.length);
  }

  private void rangeCheck(int Index) throws IndexOutOfBoundsException {
    if ((Index < 0) || (Index >= getCount()))
      throw new IndexOutOfBoundsException();
  }

  public int getCount() { 
    return strings.size();
  }
  
  public int size() //the same as getcount
  {
	  return strings.size();
  }

  public String get(int Index){
    return (String) strings.get(Index);
  }

  public int find(String S) {
    int I, L, H, C;
    L = 0;
    H = getCount() - 1;
    while (L <= H) {
      I = (L + H) / 2; 
      C = get(I).compareTo(S);// SysUtil.CompareStrings(get(I), S);
      if (C < 0)
        L = I + 1;
      else {
        H = I - 1;
        if (C == 0)
          L = I;
      }
    }
    return L;
  }

  public int add(String S, Object AObject) {
    int Result = -1;
    if (!sorted)
      Result = getCount();
    else
      Result = find(S);
    insert(Result, S, AObject);
    return Result;
  }

  public int add(String S) {
    return add(S, null);
  }

  public void addStrings(StringList Strings) {
    for (int i = 0; i < Strings.getCount(); i++)
      add(Strings.get(i));
  }

  public void addStrings(String[] Strings) {
    for (int i = 0; i < Strings.length; i++)
      add(Strings[i]);
  }


  public void clear() {
    strings.clear();
    objects.clear();
  }

  public void delete(int Index){
    strings.remove(Index);
    objects.remove(Index);
  }
  
  public void remove(String s)
  {
	  int n=indexOf(s);
	  if(n!=-1) delete(n);
  }
  
  public void insert(int Index, String S ) {
	    strings.add(Index, S);
	    
	  }

  public void insert(int Index, String S, Object AObject) {
    strings.add(Index, S);
    objects.add(Index, AObject);
  }

  public void set(int Index, String S) throws IllegalStateException{
    if (this.sorted)
      throw new IllegalStateException("list sorted!");
    else
      strings.set(Index, S);
  }

  public void setObject(int Index, Object AObject) {
    objects.set(Index, AObject);
  }

  public void exchange(int Index1, int Index2) {
    Object temp = null;
    temp = strings.get(Index1);
    strings.set(Index1, strings.get(Index2));
    strings.set(Index2, temp);
    temp = objects.get(Index1);
    objects.set(Index1, objects.get(Index2));
    objects.set(Index2, temp);
  }

  public void quickSort(int L, int R) {
    if (L < R) {
      int i = L;
      int j = R;
      String S = get(L);
      while (i < j) {
        while (get(i).compareTo(S)<= 0)
          i++;
        while (get(j).compareTo( S) > 0)
          j--;
        if (i < j)
          exchange(i, j);
      }
      exchange(i, L);
      if (L < j)
        quickSort(L, j);
      if (i < R)
        quickSort(i, R);
    }
  }

  public void setSorted(boolean value) {
    if (value != sorted) {
      if (value)
        quickSort(0, getCount() - 1);
      sorted = value;
    }
  }

  public int indexOf(String S) {
    return strings.indexOf(S);
  }

  public String getAllText() {
    StringBuffer result = new StringBuffer();
    for (int i = 0; i < strings.size(); i++) {
      result.append(get(i));  
      result.append(lineSeparator);
    }
    return result.toString();
  }
  
  /***
   * 
   * @param lines >0返回开始lines行  <0则从后返回lines行
   * @return 指定行数的数据
   */
  public String getLimitText(int lines) {
	    StringBuffer result = new StringBuffer();
	  
	    if(lines>0)
	    {
	    	if(lines>size())return getAllText();
	    	 for (int i = 0; i < lines; i++)
	    	 {
	   	      result.append(get(i));  
	   	      result.append(lineSeparator);
	   	    }
	    	
	    	
	    }
	    else
	    {
	    	lines=-lines;
	    	if(lines>size())return getAllText();
	    	 for (int i = strings.size()-lines-1; i < strings.size(); i++)
	    	 {
	   	      result.append(get(i));  
	   	      result.append(lineSeparator);
	   	    }
	    }
	    
	   
	    return result.toString();
	  }
  
  
  public void addStrings(String value)
  {
	  StringTokenizer tokenizer = new StringTokenizer(value,"\r\n");
	    while (tokenizer.hasMoreTokens())
	      add(tokenizer.nextToken()); 
  }

  public void setAllText(String value) {
    clear();
    addStrings(value);
  }
  
  private void saveToFile(FileOutputStream os,String charset )throws IOException
  {
	  	
	   
	 if(charset==null || charset.length()==0) 
	 {
	  for (int i = 0; i < strings.size(); i++) 
	  {  
		  String s=get(i)+lineSeparator;
		  os.write( s.getBytes());
		  
	  }
	 }
	 else
	 {
		 for (int i = 0; i < strings.size(); i++) 
		  {  
			  String s=get(i)+lineSeparator;
			  os.write( s.getBytes(charset));
			  
		  }
	 }
		  os.flush();
		  os.close();
  }
  
  public void saveToFile(String filename,String charset)throws IOException
  {
	  saveToFile(new FileOutputStream(filename),charset);
  }
  public void saveToFile(File file,String charset)throws IOException
  {
	  saveToFile(new FileOutputStream(file),charset);
  }
  
  
  private void loadFromFile(FileInputStream  is,String charset) throws IOException
  {
	  BufferedReader  r=null;
	  if(charset==null || charset.length()==0) 
		  r=new BufferedReader(new InputStreamReader(is));
	  else r=new BufferedReader(new InputStreamReader(is,charset));
	  String line=null;
	  clear();
	  while((line=r.readLine())!=null)
	  {
		  add(line);
	  }
	  is.close();
	  
  }

  public void loadFromFile(String filename,String charset) throws IOException
  {
	  
	  loadFromFile(new FileInputStream(filename),charset);
  }
  
  public void loadFromFile(File file,String charset) throws IOException
  {
	  
	  loadFromFile(new FileInputStream(file),charset);
  }

  public String getName(int i)
  {
	  String s= get(i);
	  int n=s.indexOf(nameValueSeparator);
	  if(n==-1) return s;
	  return s.substring(0,n);
  }
  
  public String getValue(int i)
  {
	  String s= get(i);
	  int n=s.indexOf(nameValueSeparator);
	  if(n==-1) return null;
	  return s.substring(n+1);
  }
  
  public String getValue(String name)
  {
	  int n= indexOfName(name);
	  if(n!=-1) return getValue(n);
	 
	  return null;
  }
  
  
  public void setValue(String name,String value)
  {
	  int n= indexOfName(name);
	  String s=name+nameValueSeparator+value;
	  if(n==-1)  add(s);
	  else set(n,s);
  }
  
  
  public int indexOfName(String name)
  {
	  for (int i = 0; i < strings.size(); i++)
	  {
		  String s= get(i);
		  int n=s.indexOf(nameValueSeparator);
		  if(n!=-1 && s.substring(0,n).equals(name) ) return i;
		  
		  
	  }
	  return  -1; 
  }
  
  public void removeByName(String name)
  {
	  int n=indexOfName(name);
	  if(n!=-1) delete(n);
  } 

  
  public void setNameValueSeparator(char separator)
  {
	  this.nameValueSeparator=separator;
  }
  public char getNameValueSeparator()
  {
	  return nameValueSeparator;
  }
}




