package com.stickynotes.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Sticky extends Canvas
{
	private static final long serialVersionUID = 5450652909382832597L;

	private JFrame frame;
	private JTextArea textArea;
	private JScrollPane scrollPane;
		
	private final Color bgColor = new Color(51,51,51);
	
	private UUID id;

	public UUID getID() { return id;}
	
	private void createSticky(boolean loaded , StickyFileStruct stickyFileStruct)
	{
		//Each sticky has unique uuid
		id = UUID.randomUUID();
		System.out.println("Generated: " + id);
					
		frame = new JFrame();
		
		if(loaded) 
		{
			frame.setSize(new Dimension(stickyFileStruct.size.x , stickyFileStruct.size.y));
			frame.setLocation(stickyFileStruct.location.x , stickyFileStruct.location.y);
		}
		
		else frame.setSize(new Dimension(400,600));
			
		frame.setUndecorated(true);
		frame.setResizable(true);
		
		ImageIcon img = new ImageIcon("C:/Workspace/Sticky-Notes/res/icon.png");
		frame.setIconImage(img.getImage());
		
		Font textFont = new Font("Consolas",Font.PLAIN,14);
				
		textArea = new JTextArea();
		textArea.setSize(this.getSize());
		textArea.setBackground(bgColor);
		textArea.setForeground(Color.white);
		textArea.setCaretColor(Color.white);
		textArea.setLineWrap(true);
		textArea.setFont(textFont);
		textArea.setSelectionColor(new Color(85,85,85));
		if(loaded) textArea.append(stickyFileStruct.data); //loaded data
								
		scrollPane = new JScrollPane(textArea);
				
		scrollPane.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, bgColor));
		scrollPane.getViewport().setBorder(null);
		scrollPane.setViewportView(textArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setWheelScrollingEnabled(true);
				
		frame.add(scrollPane);
			
		int rootPaneBorderHeight = 30;
		frame.setVisible(true);
		frame.getContentPane().setBackground(bgColor);	
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		StickyHandler handler = null;
		if(loaded) handler = new StickyHandler(stickyFileStruct.fileLoadedFrom,id,frame,textArea,stickyFileStruct.color,scrollPane,rootPaneBorderHeight);
		else handler = new StickyHandler(null,id,frame,textArea,null,scrollPane,rootPaneBorderHeight);
		
		frame.addMouseListener(handler);
		frame.addMouseMotionListener(handler);	
		frame.addWindowFocusListener(handler);
		frame.addMouseWheelListener(handler);
				
		textArea.addKeyListener(handler);
	}
	
	public Sticky()
	{
		createSticky(false, null);
	}
	
	public Sticky(StickyFileStruct stickyFileStruct)
	{
		createSticky(true, stickyFileStruct);
	}
}