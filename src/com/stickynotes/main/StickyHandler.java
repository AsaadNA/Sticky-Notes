package com.stickynotes.main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

class StickyHandler extends MouseAdapter implements MouseListener , KeyListener , WindowFocusListener , MouseWheelListener
{
	private JFrame frame;
	private JScrollPane scrollPane;
	private UUID id;
	private JTextArea textarea;
	
	private Point point;
	private int rootPaneBorderHeight = 0;
	
	private boolean resizing = false;
	
	private int sw,sh;
	private int resizeWidth,resizeHeight;
	private int RotationSpeed = 8;
	
	private Color tabColor = new Color(69,69,69);
	private String fileLoadedFrom;
	
	public StickyHandler(String fileLoadedFrom,UUID id,JFrame frame , JTextArea textarea , Color stickyFileColor , JScrollPane scrollPane , int rootPaneBorderHeight) 
	{ 
		this.fileLoadedFrom = fileLoadedFrom;
		this.textarea = textarea;
		this.id = id;
		this.frame = frame; 
		this.scrollPane = scrollPane;
		resizeWidth = frame.getWidth();
		resizeHeight = frame.getHeight();
		this.rootPaneBorderHeight = rootPaneBorderHeight;
		
		if(stickyFileColor != null) this.tabColor = stickyFileColor;
	}
	
	public void mousePressed(MouseEvent e) 
	{ 
		if(e.getPoint().y > 0 && e.getPoint().y < rootPaneBorderHeight) { point = e.getPoint(); } //window drag 
		else if((e.getPoint().x >= frame.getWidth() - 10 && e.getPoint().x <= frame.getWidth()) && (e.getPoint().y >= frame.getHeight() - rootPaneBorderHeight && e.getPoint().y <= frame.getHeight())) //window resize
		{
			point = e.getPoint();
			sw = resizeWidth;
			sh = resizeHeight;
			resizing = true;
		}
	}
	
	public void mouseEntered(MouseEvent e)
	{
		if((e.getPoint().x >= frame.getWidth() - 10 && e.getPoint().x <= frame.getWidth()) && (e.getPoint().y >= frame.getHeight() - rootPaneBorderHeight && e.getPoint().y <= frame.getHeight())) //window resize
		{
			frame.setCursor(Cursor.NW_RESIZE_CURSOR);
		}
	}
	
	public void mouseExited(MouseEvent e)
	{
		frame.setCursor(Cursor.DEFAULT_CURSOR);
	}

    public void mouseReleased(MouseEvent e) 
	{ 
    	frame.setCursor(Cursor.DEFAULT_CURSOR);
		resizing = false;
		point = null; 
	}
	
	public void mouseDragged(MouseEvent e)
	{
		if(resizing == false && point != null)
		{
			Point currentPoint = e.getLocationOnScreen();
			frame.setLocation(currentPoint.x - point.x , currentPoint.y - point.y);	
		}
		
		if(resizing)
		{
			frame.setCursor(Cursor.NW_RESIZE_CURSOR);
			resizeWidth = Math.abs((int)((e.getPoint().x - point.getX()) + sw));
			resizeHeight = Math.abs((int)((e.getPoint().y - point.getY()) + sh));
			if(resizeWidth >= 100 && resizeHeight >= 100) frame.setSize(resizeWidth,resizeHeight);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	
	private void saveSticky()
	{
		if(fileLoadedFrom == null) fileLoadedFrom = id.toString();
		StickyFileStruct sfs = new StickyFileStruct(fileLoadedFrom,new Point(frame.getWidth(),frame.getHeight()),new Point(frame.getLocation()) ,tabColor,textarea.getText());
		StickyFileHandler.saveData(sfs);
	}
	
	/*
	 * - CTRL + C => Color Chooser
	 * - CTRL + E => Delete The Note Instance
	 * - CTRL + N => New Note
	 * - CTRL + D => Perma Delete The Note
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		
		int keyCode = e.getKeyCode();
		if(e.isAltDown())
		{
			if(keyCode == e.VK_C) tabColor = JColorChooser.showDialog(null,"Sticky Notes Color Chooser", Color.red);			
			if(keyCode == e.VK_E || keyCode == e.VK_D)
			{
				if(App.getStickyNoteCount() <= 0)
				{
					if(keyCode == e.VK_E) saveSticky();
					else StickyFileHandler.deleteData(fileLoadedFrom);
					System.exit(0);
				}
				
				else
				{
					for(int i = 0; i <= App.getStickyNoteCount(); i++)
					{
						if(App.getStickyNote(i).getID() == id)
						{
							App.popStickyNote(i);
							frame.hide();
							if(keyCode == e.VK_E) {
								saveSticky();
								System.out.println("Deleting: " + id);
							} else {
								StickyFileHandler.deleteData(fileLoadedFrom);
								System.out.println("Perma Delete Data in ID: " + id);
							}
						}
					}
				}
			} if(keyCode == e.VK_N) App.pushStickyNote(new Sticky());	
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void windowGainedFocus(WindowEvent e) {
		frame.getRootPane().setBorder(BorderFactory.createMatteBorder(rootPaneBorderHeight, 0, 5, 0, tabColor));		
	}

	@Override
	public void windowLostFocus(WindowEvent e) {
		frame.getRootPane().setBorder(BorderFactory.createMatteBorder(rootPaneBorderHeight/2-5, 0, 5, 0, tabColor));		
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(e.getWheelRotation() > 0) { 
			scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getValue() + e.getWheelRotation() + RotationSpeed); 
		}
		else {
			scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getValue() + e.getWheelRotation() - RotationSpeed);
		}
	}
}