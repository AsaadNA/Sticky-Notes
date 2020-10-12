package com.stickynotes.main;

import java.util.ArrayList;

//Each Sticky Window Created Will have an id

public class App
{
	private static ArrayList<Sticky> stickyNotes = new ArrayList<Sticky>();
	
	/* used in sticky handler */
	public static int getStickyNoteCount() { return stickyNotes.size() - 1; }
	public static Sticky getStickyNote(int index) { return stickyNotes.get(index); }
	public static void popStickyNote(int index) { stickyNotes.remove(index); }
	public static void pushStickyNote(Sticky s) { stickyNotes.add(s); }

	private void Run()
	{
		StickyFileHandler.loadDataOnInit();
	}
	
	public static void main(String args[])
	{
		App app = new App();
		app.Run();
	}
}
