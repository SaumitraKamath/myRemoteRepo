package org.Trade_Automation.driver;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IOUtilities
{

	public static String readtextdata(String filepth)
	{
		BufferedReader br = null;
		FileReader fr = null;
		String sCurrentLine = null;
		try
		{
			fr = new FileReader(filepth);
			br = new BufferedReader(fr);
			br = new BufferedReader(new FileReader(filepth));

			while ((sCurrentLine = br.readLine()) != null)
			{
				// System.out.println(sCurrentLine);
				break;
			}

		}
		catch (IOException e)
		{

			e.printStackTrace();

		}
		finally
		{

			try
			{

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			}
			catch (IOException ex)
			{

				ex.printStackTrace();

			}

		}
		sCurrentLine = sCurrentLine.trim();
		// System.out.println(sCurrentLine);
		return sCurrentLine;
	}

	public static void writetextdata(String resultFile, String texttowrite, boolean append)
	{
		try
		{
			BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(resultFile, append));
			if (append)
				bw.write("##################################\n".getBytes());
			bw.write(texttowrite.getBytes());
			if (append)
				bw.write("##################################\n".getBytes());
			bw.flush();
			bw.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Map<String, String> readtextdatafull(String filepth) throws FileNotFoundException
	{
		BufferedReader br = null;
		FileReader fr = null;
		Map<String, String> map = new HashMap<String, String>();
		String sCurrentLine = null;
		try
		{

			fr = new FileReader(filepth);
			br = new BufferedReader(fr);

			br = new BufferedReader(new FileReader(filepth));

			while ((sCurrentLine = br.readLine()) != null)
			{

				String[] sCurrentLinetemp = sCurrentLine.split("\\$");
				map.put(sCurrentLinetemp[0], sCurrentLinetemp[1]);

			}

		}
		catch (IOException e)
		{

			e.printStackTrace();

		}
		finally
		{

			try
			{

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			}
			catch (IOException ex)
			{

				ex.printStackTrace();

			}

		}
		// sCurrentLine=map.get(key);
		// sCurrentLine=sCurrentLine.trim();
		// System.out.println(sCurrentLine);
		return map;
	}

}
