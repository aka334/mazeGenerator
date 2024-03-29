import javax.swing.*;
import java.awt.*;

public class cell{
    int i, j, width;        // A position to define cell
    boolean walls[];    //walls of a cell
    boolean Visited;    //boolean to check if the cell  has been visited or not
    boolean solveVisited, solveBacktracker; //boolean value to store while solving the maze
    //stroke for incresing line width
    Stroke stroke1 = new BasicStroke(3f);

    //Constructor of cell function
    cell(int i ,int j, int width){
        this.i=  i;
        this.j = j;
        this.width = width;
        this.walls = new boolean[]{true, true, true, true};// top, right, bottom, left
        this.Visited = false;
        this.solveVisited =false;
        this.solveBacktracker = false;
    }
    
    //Draw method for cell
    void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(stroke1);
        g.setColor(Color.YELLOW);
        int x = this.i * width;
        int y = this.j * width;

        if(this.walls[0]){
            //top
            g2d.drawLine(x, y, x + this.width, y);
        }
        if(this.walls[1]){
            //right
            g2d.drawLine(x + this.width, y, x + this.width, y + this.width);
        }
        if(this.walls[2]){
            //bottom
            g2d.drawLine(x + this.width, y + this.width, x, y + this.width);
        }
        if(this.walls[3]){
            //left
            g2d.drawLine(x, y + this.width, x, y);
        }
    }
    //Highlight the cell with specified color
    void highlight(Graphics g, Color c, int wid){
        g.setColor(c);
        g.fillRect(this.i * width, this.j * width, wid, wid);
        g.setColor(Color.BLACK);
    }


}
