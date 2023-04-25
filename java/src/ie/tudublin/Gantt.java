package ie.tudublin;

import java.util.ArrayList;
import processing.core.PApplet;
import processing.data.*;

public class Gantt extends PApplet
{	
	/*
	 * Arraylist stores a list of task objects, 
	 * Task class represents a task in the gant chart and is created by loading in data 
	 * from the tasks.csv
	 * THIS ARRAY LIST HOLDS OBJECTS
	 */
	ArrayList<Task> tasks = new ArrayList<Task>();
	

	//settings is automatically called by processing !!!
	public void settings()
	{
		size(800, 600);

		loadTasks();
		printTasks();
		
	}

	public void loadTasks()
	{	//loadtable is built in functionality from processing
		/*
		 * iterates through each row of the table and creates a task object for each row
		 * using the Task constructor(in Task.java)
		 * 
		 */
		Table table = loadTable("tasks.csv", "header");
		//table row is also from the processing library
		for(TableRow row:table.rows())
		{
			Task task = new Task(row);
			tasks.add(task); //adds object to task arrayList
		}
	}

	public void printTasks()
	{
		for(Task t:tasks)
		{
			println(t);
		}
	}

	private boolean isEnd = false;
	private int whichTask = -1;
	private int maxMonths = 30;

	private float border = 40;
	private float rowHeight = 40;
	float namesPart = 150;

	public void mousePressed()
	{
		for(int i = 0 ; i < tasks.size() ; i ++)
		{
			float y1 = (border + border + rowHeight * i) - 15;
			float y2 = (border + border + rowHeight * i) + 20;

			float x1 = map(tasks.get(i).getStart(), 1, maxMonths, namesPart, width - border);
			float x2 = map(tasks.get(i).getEnd(), 1, maxMonths, namesPart, width - border);
			/*
			 * This for loop constantly checks if our mouse is inside the boundaries of our objects
			 */
			if (mouseX >= x1 && mouseX <= x1 + 20 && mouseY >= y1 && mouseY <= y2)
			{
				whichTask = i;
				isEnd = false;
				return;
			}

			if (mouseX <= x2 && mouseX >= x2 - 20 && mouseY >= y1 && mouseY <= y2)
			{
				whichTask = i;
				isEnd = true;
				return;
			}
		}		
		// default value for whichTask
		whichTask = -1;	
	}

	public void mouseDragged()
	{
		if (whichTask != -1)
		{
			int month = (int)map(mouseX, namesPart, width - border, 1, maxMonths);
			
			if (month >= 1 && month <= maxMonths)
			{
				Task task = tasks.get(whichTask); 
				if (isEnd)
				{
					if (month - task.getStart() > 0)
					{
						task.setEnd(month);
					}
				}
				else
				{
					if (task.getEnd() - month > 0 )
					{
						task.setStart(month);
					}
				}
			}
		}
	}

	void displayTasks()
	{
		/*
		 * This just sets size and alignment for the names of the tasks
		 */
		textSize(14);
		textAlign(LEFT,CENTER);
		
		/*
		 * This just assigns the lines 
		 * border is a value set to 40 that can be used as a constant 
		 * line draws the vertical line between two sets of coords
		 * fill 
		 */
		textAlign(CENTER, CENTER);
		stroke(128);
		for(int i = 1 ; i <= maxMonths ; i ++)
		{
			float x = map(i, 1, maxMonths, namesPart, width - border);
			line(x, border, x, height - border);
			//fill tells the draw what colour to make everything
			fill(255);
			//this literally just writes the numbers from 1-30 using i and plots them ontop of the lines.
			text(i, x, border * 0.5f);
		}

		textAlign(LEFT, CENTER);
		for(int i = 0 ; i < tasks.size() ; i ++)
		{
			float y = border + border + rowHeight * i;
			fill(255);
			text(tasks.get(i).getName(), border, y);

			// Print the task
			noStroke();
			//this is really clever cuz it fills the rectangles but depending on where it is on the drawing itll change colour.
			fill(map(i, 0, tasks.size(), 0, 255), 255, 255);
			float x1 = map(tasks.get(i).getStart(), 1, maxMonths, namesPart, width - border);
			float x2 = map(tasks.get(i).getEnd(), 1, maxMonths, namesPart, width - border);
			rect(x1, y - 15, x2 - x1, rowHeight - 5, 5.0f);
		}

	}

	public void setup() 
	{
		colorMode(HSB);
	}
	
	public void draw()
	{			
		background(0);
		displayTasks();
	}
}
