import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileNotFoundException;

/* KimDohyeongA3Q1
 * COMP 2140 SECTION D01*  
 * Assignment 3, question 1 @author       
 * Dohyeong Kim, 7892470    
 * July/13th/2021
 * 
 * PURPOSE: The purpose of this program is to read in a sample file of a maze and find a path from start to finish and
 * mark the path with asterisks.*/

class KimDohyeongA3Q1 {
    public static void main(String[] args) throws FileNotFoundException
    {
       Maze maze = new Maze();
       maze.StackSearch();
       maze.resetMaze();
       maze.QueueSearch();
       System.out.println("Processing terminated normally.");
    }
}

enum SquareType{
    START,
    FINISH,
    PATH,
    WALL,
    VISITED
}

class Position {
    int row;
    int col;
    SquareType type;
    boolean visited; 
    Position previous;
    
    public Position(int r, int c, SquareType squareType)
    {
        row = r;
        col = c;
        type = squareType;
        visited = false;
    }

    public boolean isVisited()
    {
        return visited;
    }
    public void visited()
    {
        visited = true;
    }

    public void setType(SquareType newType)
    {
        type = newType;
    }

    public SquareType getType()
    {
        return type;
    }

    public void unvisit()
    {
        visited = false;
    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    public void setPrev(Position prev)
    {
        previous = prev;
    }

    public Position getPrev()
    {
        return previous;
    }

    /*
    * this method takes in the end position of the maze and back tracks while getting the coordinates of the paths
    */
    public String printPath(Position end)
    {
        String output = "";
        Position temp = end;

        while(temp.getPrev() != null)
        {
            output = temp.coordinates() + output;
            temp = temp.getPrev();
        }
        
        return output;
    }

    /*
    * this method takes in the position of the end and back tracks the path
    * and sets the "visited" paths to VISITED enum
    */
    public void visitedPath(Position end)
    {
        Position temp = end.getPrev();
        
        while(temp != null && temp.getPrev() != null)
        {
            temp.setType(SquareType.VISITED);
            temp = temp.getPrev();
        }
        
    }

    public String coordinates()
    {
        return "(" + row + ", " + col + ")";
    }
    
    public String toString()
    {
        String output = "";
        if(type == SquareType.WALL)
        {
            output = "#";
        }
        else if(type == SquareType.PATH)
        {
            output = ".";
        }
        else if(type == SquareType.START)
        {
            output = "S";
        }
        else if(type == SquareType.FINISH)
        {
            output = "F";
        }
        else if(type == SquareType.VISITED)
        {
            output = "*";
        }
        return output;
    }
}

/////////STACK CLASS USING ARRAY
class Stack 
{
    int top;
    int size;
    Position arr [];
    int realSize = 0;
    
    //constructor takes in size for the array

    public Stack (int size)
    {
        this.size = size;
        arr = new Position [size];
        top = -1;
    }

    //method for getting the size of the array

    public int getSize()
    {
        return realSize;
    }

    //method for checking if the array is empty

    public boolean isEmpty()
    {
        return (top == -1);
    }

    //method for inserting a item into the array

    public void push(Position item)
    {
        arr[++top] = item;
        realSize++;
    }

    //method for removing items in the array
    public Position pop()
    {
        Position x = arr[top--];
        realSize--;
        return x;
    }

    //method that returns the top item of the array
    public Position top()
    {
        if(!isEmpty())
        {
            return arr [top];
        }
        else return null;  
    }

    
}

//////QUEUE CLASS USING LINKED LIST
class Queue 
{
    Node front;
    Node rear;
    
    public Queue()
    {
        front = null;
        rear = null;
    }

    private class Node
    {
        Position item;
        Node next;
        
        public Node(Position item)
        {
            this.item = item;
        }
    }

    //method for inserting a item into the linked list

    public void enqueue(Position item)
    {
        Node newNode = new Node(item);
        if(isEmpty())
        {
            front = newNode;
        }
        else
        {
            rear.next = newNode;
        }
        rear = newNode;
    }
    
    //method for removing the first item in the linked list
    public Position removeFirst()
    {  
        Position temp = front.item;

        if(front.next == null)
        {
            rear = null;
        }
        front = front.next;
        return temp;
    } 

    //method for checking if the list is empty
    public boolean isEmpty()
    {
        return front == null;
    }

    public Position dequeue()
    {
        if(isEmpty())
        {
            System.out.println("Queue is empty.");
        }
        return removeFirst();
    }
    
    public Position peek()
    {
        if(isEmpty())
        {
            System.out.println("Queue is empty.");
        }
        return front.item;
    }
}



class Maze 
{
    Position maze[][];
    int startRow;
    int startCol;
    int finishRow;
    int finishCol;
    SquareType type; 
    Position up;
    Position down;
    Position left;
    Position right;

    public Maze () throws FileNotFoundException
    {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Please enter the input file name (.txt files only): \n");
        parseFile(keyboard.nextLine());

    }

    //this method is for taking in the file and scanning and putting them into the maze array
    //while giving the specific characters a specific type and prints out the initial maze.

    private void parseFile(String fileName) throws FileNotFoundException
    {
        String size [];
        System.out.println("\n" + "Processing " + fileName + "...\n");
        Scanner scanner = new Scanner(new File(fileName));
        size = scanner.nextLine().split(" ");
        maze = new Position[Integer.parseInt(size[0])][Integer.parseInt(size[1])];
        
        String input;
       
       
        for(int row = 0; row < maze.length; row++)
        {
            input = scanner.nextLine();
            for(int i = 0; i < maze[row].length; i++)
            {
                if(input.charAt(i) == '#')
                {
                    type = SquareType.WALL;
                }
                else if(input.charAt(i) == '.')
                {
                    type = SquareType.PATH;
                }
                else if(input.charAt(i) == 'S')
                {
                    type = SquareType.START;
                    startRow = row;
                    startCol = i;
                }
                else if(input.charAt(i) == 'F')
                {
                    type = SquareType.FINISH;
                    finishRow = row;
                    finishCol = i;
                }
                maze[row][i] = new Position(row, i, type); 
            }
        }

        System.out.println("The initial maze is:\n");
        for(int i = 0; i < maze.length; i++)
        {
            for(int j = 0; j < maze[i].length; j++)
            {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }
        
        
    }

    public Position getUp(Position curr)
    {   
        return getPositionAt(curr.getRow()-1, curr.getCol());
    }

    public Position getDown(Position curr)
    {
        return getPositionAt(curr.getRow()+1, curr.getCol());
    }

    public Position getLeft(Position curr)
    {
        return getPositionAt(curr.getRow(), curr.getCol()-1);
    }

    public Position getRight(Position curr)
    {
        return getPositionAt(curr.getRow(), curr.getCol()+1);
    }

    public int getStartRow()
    {
       return startRow;
    }

    public int getStartCol()
    {
        return startCol;
    }

    public int getEndRow()
    {
        return finishRow;
    }

    public int getFinishCol()
    {
        return finishCol;
    }

    public Position getPositionAt(int i, int j)
    {
        return maze[i][j];
    }

    public void resetMaze()
    {
        for(int i = 0; i < maze.length; i++)
        {
            for(int j = 0; j < maze[i].length; j++)
            {
                maze[i][j].unvisit();
                maze[i][j].setPrev(null);
                if(maze[i][j].getType() == SquareType.VISITED)
                {
                    maze[i][j].setType(SquareType.PATH);
                }
            }
        }
    }

    //stack search method 
    public void StackSearch()
    {
        Position curr;
        Stack search = new Stack(maze.length*maze[0].length);
        search.push(maze[startRow][startCol]);
        search.top().visited();

        while(!search.isEmpty())
        {
            curr = search.pop();

            if(curr.getType() == SquareType.FINISH){
                System.out.println("\nThe Path found using a stack is:\n");

                curr.visitedPath(curr); //this is for backtracking through the maze and setting them "VISITED"
                for(int i = 0; i < maze.length; i++)
                {
                    for(int j = 0; j < maze[i].length; j++)
                    {
                        System.out.print(maze[i][j] + " ");
                    }
                    System.out.println();
                }
                System.out.println("Path from start to finish: " + maze[getStartRow()][getStartCol()].coordinates() + curr.printPath(curr) + "\n"); 
               
            }
            for(int row = 0; row < maze.length; row++)
            {
                for(int col = 0; col < maze[row].length; col++)
                {
                    if(curr.row > 0 && !getUp(curr).isVisited() && getUp(curr).getType() != SquareType.WALL)
                    {
                        getUp(curr).visited();
                        getUp(curr).setPrev(curr);
                        search.push(getUp(curr));
                    }
                    else if(curr.row < maze.length-1 && !getDown(curr).isVisited() && getDown(curr).getType() != SquareType.WALL)
                    {
                        getDown(curr).visited();
                        getDown(curr).setPrev(curr);
                        search.push(getDown(curr));
                    }
                    else if(curr.col > 0 && !getLeft(curr).isVisited() && getLeft(curr).getType() != SquareType.WALL)
                    {
                        getLeft(curr).visited();
                        getLeft(curr).setPrev(curr);
                        search.push(getLeft(curr));
                    }
                    else if(curr.col < maze[row].length -1 && !getRight(curr).isVisited() && getRight(curr).getType() != SquareType.WALL)
                    {
                        getRight(curr).visited();
                        getRight(curr).setPrev(curr);
                        search.push(getRight(curr));
                    }     
                }
            }
        }
    }


    //QUEUE SEARCH METHOD 
    public void QueueSearch()
    {
        Position curr;
        Queue search = new Queue();
        search.enqueue(maze[startRow][startCol]);
        search.peek().visited();

        while(!search.isEmpty())
        {
            curr = search.dequeue();
            if(curr.getType() == SquareType.FINISH)
            {   
                System.out.println("The Path found using a queue is:\n");

                curr.visitedPath(curr);
                for(int i = 0; i < maze.length; i++){
                    for(int j = 0; j < maze[i].length; j++)
                    {
                        System.out.print(maze[i][j] + " ");
                    }
                    System.out.println();
                }
                System.out.println("Path from start to finish: " + maze[getStartRow()][getStartCol()].coordinates() + curr.printPath(curr));

            }

            for(int row = 0; row < maze.length; row++)
            {
                for(int col = 0; col < maze[row].length; col++)
                {
                    if(curr.row > 0 && !getUp(curr).isVisited() && getUp(curr).getType() != SquareType.WALL)
                    {
                        getUp(curr).visited();
                        getUp(curr).setPrev(curr);
                        search.enqueue(getUp(curr));
                    }
                    else if(curr.row < maze.length-1 && !getDown(curr).isVisited() && getDown(curr).getType() != SquareType.WALL)
                    {
                        getDown(curr).visited();
                        getDown(curr).setPrev(curr);
                        search.enqueue(getDown(curr));
                    }
                    else if(curr.col > 0 && !getLeft(curr).isVisited() && getLeft(curr).getType() != SquareType.WALL)
                    {
                        getLeft(curr).visited();
                        getLeft(curr).setPrev(curr);
                        search.enqueue(getLeft(curr));
                    }
                    else if(curr.col < maze[row].length -1 && !getRight(curr).isVisited() && getRight(curr).getType() != SquareType.WALL)
                    {
                        getRight(curr).visited();
                        getRight(curr).setPrev(curr);
                        search.enqueue(getRight(curr));
                    }        
                }
            }
        }

    }
}
