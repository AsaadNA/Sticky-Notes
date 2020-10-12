package com.stickynotes.main;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

class StickyFileStruct
{
	public Point size;
	public Point location;
	public Color color;
	public String data;
	public String fileLoadedFrom;
	
	public StickyFileStruct() {}
	public StickyFileStruct(String fileLoadedFrom,Point size , Point location , Color color , String data)
	{
		this.size = size;
		this.color = color;
		this.data = data;
		this.location = location;
		this.fileLoadedFrom = fileLoadedFrom;
	}
}

public class StickyFileHandler {
	
    private static Path dir = Paths.get("C:/Workspace/Sticky-Notes/data");
    private static ArrayList<String> fileList = new ArrayList<String>();
    
	public static int getFileCount(){
    	int count = 0;
	    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
	        for (Path p : stream) {
	        	fileList.add(p.getFileName().toString());
	            count++;
	        }
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    } return count;
	}
	
	public static void loadDataOnInit()
	{
		int count = getFileCount();
		if(count == 0) App.pushStickyNote(new Sticky());
		else
		{
			Scanner fileScanner = null;
			for(int i = 0; i <= fileList.size() - 1; i++)
			{
				File file = new File("C:/Workspace/Sticky-Notes/data/" + fileList.get(i));
				StickyFileStruct stickyFileStruct = new StickyFileStruct();
				stickyFileStruct.data = "";
				try
				{
					fileScanner = new Scanner(file);
					int lineCount = 0;
					while(fileScanner.hasNextLine())
					{
						String line = fileScanner.nextLine();
						
						//Size
						if(lineCount == 0)
						{
							String parts[] = line.split(",");
							Point size = new Point(Integer.parseInt(parts[0]) , Integer.parseInt(parts[1]));
							stickyFileStruct.size = size;
						}
						
						//Color
						if(lineCount == 1)
						{
							String parts[] = line.split(",");
							Color color = new Color(Integer.parseInt(parts[0]) , Integer.parseInt(parts[1]),Integer.parseInt(parts[2]));
							stickyFileStruct.color = color;
						}
						
						//Location
						if(lineCount == 2)
						{
							String parts[] = line.split(",");
							Point location = new Point(Integer.parseInt(parts[0]) , Integer.parseInt(parts[1]));
							stickyFileStruct.location = location;	
						}
						
						//Data
						else if(lineCount > 2) stickyFileStruct.data += line;
						lineCount++;
					}
						
				} catch (IOException e) { System.out.println(e); }			
				
				stickyFileStruct.fileLoadedFrom = fileList.get(i);
				fileScanner.close();
				App.pushStickyNote(new Sticky(stickyFileStruct));
				System.out.println("Sticky Loaded From " + stickyFileStruct.fileLoadedFrom);
			
			}
		}
	}
	
	public static void deleteData(String fileName)
	{
		File file = new File("C:/Workspace/Sticky-Notes/data/" + fileName);
		file.delete();
	}
	
	public static void saveData(StickyFileStruct sfs)
	{
		PrintWriter writer = null;
		try
		{
			writer = new PrintWriter("C:/Workspace/Sticky-Notes/data/" + sfs.fileLoadedFrom);
			writer.println(sfs.size.x + "," + sfs.size.y);
			writer.println(sfs.color.getRed() + "," + sfs.color.getGreen() + "," + sfs.color.getBlue());
			writer.println(sfs.location.x + "," + sfs.location.y);
			writer.println(sfs.data);
			writer.close();
		} catch(IOException e) { System.out.println(e); }
	}
}
