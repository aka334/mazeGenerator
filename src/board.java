import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class board extends JPanel implements Runnable{
    //Label for footer
    JLabel info = new JLabel("Visited" + " " + visited() + "%");
    JLabel done = new JLabel("");
    boolean over = false;   //boolean to check if the path is found
    int col, rows;      //rows and column of the maze
    int width = 18;     //universal width of the cell
    public boolean paint = false;   //to paint on the panel
    Stack<cell> st = new Stack<>();     //stack for constructing maze
    Stack<cell> Solvestack = new Stack<>(); //stack for solving maze

    cell current;   //current cell
    int speed;//speed of the maze generation and solving path
    //2-D array to store the maze information
    public ArrayList<ArrayList<cell> > arr = new ArrayList<ArrayList<cell> >();

    //Constructor of the board method
    board(int r, int c){
        this.col = c;
        this.rows = r;
        info.setForeground(Color.WHITE);
        done.setForeground(Color.WHITE);
        setup();
    }
    // Method to set speed
    void setSpeed(int s){
        speed = s;
    }
    //Method to set Rows of the maze
    void setRows(int r){
        this.rows = r;
    }
    //Method to set Column of the maze
    void setCol(int c){
        this.col = c;
    }
    //Setuo of the code
    void setup(){

        //Initializing the position of each cell

        for(int i = 0; i < 50; i++){
            arr.add(new ArrayList<cell>());
            for(int j = 0; j < 50; j++){
                cell temp = new cell(i , j, width);

                arr.get(i).add(temp);
            }
        }
        //Setting current cell to the beginning
        current = arr.get(0).get(0);

    }
    boolean solve = false;//for solve method

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //if path found
        if(over){
            done.setText("PATH FOUND!");
        }

        info.setText("Visited" + " " + visited()+ "%");
        g.setColor(Color.BLACK);
        //draw on the screen with the cell settings
        if(paint){
            //Drawing each cell in the board by iterating over it
            for( int i = 0; i < col; i++){
                for(int j = 0; j < rows; j++) {
                    if(arr.get(i).get(j).Visited) {
                        arr.get(i).get(j).highlight(g, Color.BLACK, this.width);
                        arr.get(i).get(j).draw(g);
                    }
                }
            }
        }
        //Goes here when solve is pressed
        if(solve){
            //Iterating through the array
            for( int i = 0; i < col; i++){
                for(int j = 0; j < rows; j++) {
                    if(arr.get(i).get(j).solveVisited) {
                        arr.get(i).get(j).highlight(g, new Color(75,0,130), this.width);
                        arr.get(i).get(j).draw(g);
                        
                        if(arr.get(i).get(j).solveBacktracker) {
                            //Backtracking 
                            arr.get(i).get(j).highlight(g, new Color(135,206,235), this.width);
                            arr.get(i).get(j).draw(g);
                        }
                    }
                }
            }
        }
        //Highhlighting start and end with blue and red color
        arr.get(0).get(0).highlight(g, Color.BLUE, this.width-2);     //Start
        arr.get(this.col-1).get(this.rows-1).highlight(g,Color.red, this.width-2);//end
    }
    //Thread for solve method
    Thread th1 = new Thread(this::run1);
    public void solver(){th1.start();}
    boolean stopper = true;
    //run1 for the solve thread
    public void run1(){
        this.current = arr.get(0).get(0);
        //while stack is empty
        do{
            if(stopper) {
                try {
                    Thread.sleep(speed);    //speed of drawing
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                if (solveDFS()) {
                    over = true;
                    repaint();
                    return;
                }
            }
            repaint();
        }while (!Solvestack.empty());
        revalidate();
        repaint();

    }

    boolean  solveDFS(){
        //Current cell
        this.current.solveVisited = true;
        //Step 1
        if(this.current != arr.get(this.col-1).get(this.rows-1)) {
            cell next = checkneighborSolve(this.current);
            if (next != null) {
                next.solveVisited = true;
                //Step 2
                Solvestack.push(this.current);
                //Step 4
                this.current = next;

            } else if (Solvestack.size() > 0) {
                this.current = Solvestack.pop();
                this.current.solveBacktracker = true;

            }
            return false;
        }
        return true;
    }

    //Checking Neighbors while Solving
    //Prefers right in the beginning and chooses a random path when there is no right option
    cell checkneighborSolve(cell curr){
        List<cell> neighbors = new ArrayList<cell>();
        int tp = index(curr.i ,(curr.j - 1)); //top
        int rt = index((curr.i + 1) , (curr.j));  //right
        int bt = index(curr.i, (curr.j + 1)); //bottom
        int lt = index((curr.i - 1), curr.j); //left
        boolean chooseRight = false;

        if(tp >= 0) {
            cell top = arr.get(curr.i).get((curr.j - 1));
            if(!top.solveVisited && !curr.walls[0]){
                neighbors.add(top);
            }
        }
        if(rt >= 0) {
            cell right = arr.get((curr.i + 1)).get((curr.j));
            if(!right.solveVisited && !curr.walls[1]){
                neighbors.add(right);
                return right;
            }

        }
        if(bt >= 0){
            cell bottom = arr.get(curr.i).get((curr.j + 1));
            if(!bottom.solveVisited && !curr.walls[2]){
                neighbors.add(bottom);
            }
        }
        if(lt >= 0) {
            cell left = arr.get((curr.i - 1)).get(curr.j);
            if(!left.solveVisited && !curr.walls[3]){
                neighbors.add(left);
            }
        }
        if(!neighbors.isEmpty()){
            //random from other 3 paths
            Random rand = new Random();
            cell randomElement = neighbors.get(rand.nextInt(neighbors.size()));
            return randomElement;
        }else {
            return null;//dummmy cell
        }
    }
    //Thread for Generating the maze
    Thread th = new Thread(this);
    public void clicked(){th.start();}
    public void run(){
        //Until stack is empty
        do{

            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dfs();
            repaint();
        }while (!st.empty());

        revalidate();
        repaint();

    }
    //For Constructing a MAZE using DFS algorithm
    private void dfs() {
        this.current.Visited = true;
        //Step 1
        cell next = checkNeighbors();
        if(next != null){
            next.Visited = true;
            //Step 2
            st.push(this.current);

            //Step 3
            removeWalls(this.current, next);
            //Step 4
            this.current = next;

        }else  if(st.size() > 0){
            this.current = st.pop();
        }
    }
    //Calculate the edge case
    int index(int i, int j){
        if (i < 0 || j < 0 || i >= this.col || j >= this.rows) {
            return -1;
        }
        return 0;
    }
    //check neighbors and return random cell among 4 cells
    cell checkNeighbors(){
        List<cell> neighbors = new ArrayList<cell>();

        int tp = index(current.i ,(current.j - 1)); //top
        int rt = index((current.i + 1) , (current.j));  //right
        int bt = index(current.i, (current.j + 1)); //bottom
        int lt = index((current.i - 1), current.j); //left

        if(tp >= 0) {
            cell top = arr.get(current.i).get((current.j - 1));
            if(!top.Visited){
                neighbors.add(top);
            }
        }
        if(rt >= 0) {
            cell right = arr.get((current.i + 1)).get((current.j));
            if(!right.Visited){
                neighbors.add(right);
            }
        }
        if(bt >= 0){
            cell bottom = arr.get(current.i).get((current.j + 1));
            if(!bottom.Visited){
                neighbors.add(bottom);
            }
        }
        if(lt >= 0) {
            cell left = arr.get((current.i - 1)).get(current.j);
            if(!left.Visited){
                neighbors.add(left);
            }
        }
        if(!neighbors.isEmpty()){
            Random rand = new Random();
            cell randomElement = neighbors.get(rand.nextInt(neighbors.size()));
            return randomElement;
        }else {
            return null;//dummmy cell
        }

    }
    //Remove walls between cell a and b
    private void removeWalls(cell a, cell b){
        int x = a.i - b.i;
        int y = a.j - b.j;
        if (x == 1) {
            a.walls[3] = false;
            b.walls[1] = false;
        } else if (x == -1) {
            a.walls[1] = false;
            b.walls[3] = false;
        }
        if (y == 1) {
            a.walls[0] = false;
            b.walls[2] = false;
        } else if (y == -1) {
            a.walls[2] = false;
            b.walls[0] = false;
        }

    }
    //Calculate the percentage of visited cells
    float visited(){
        int count = 0;
        for( int i = 0; i < col; i++){
            for(int j = 0; j < rows; j++) {
                if(arr.get(i).get(j).solveVisited){
                    count++;
                }
            }
        }
        float per = ((float) count/(float) (rows*col))*100;
        return per;
    }


}
