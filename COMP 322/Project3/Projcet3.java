//Comp 322
//Project 3
//Kelvin Jinn
import java.util.Scanner;

public class Project3 {
    //Global variables
    private static Scanner input = new Scanner(System.in);
    private static Block[] table;
    private static int headIndex = -1;
    
    //Main method
    public static void main(String[] args) {
        //Enter the table size
        System.out.println("Enter the table size: ");
        if (!input.hasNextInt())//check datatype
        {
            System.out.println("Invalid input.");
            System.exit(0);
        }
        int tableSize = input.nextInt();
        input.nextLine(); //input flushing
        if (tableSize <=0)//valid data check
        {
            System.out.println("Invalid table size.");
            System.exit(0);
        }
        //Create the file allocation table
        table = new Block[tableSize];
        for (int i = 0; i<table.length; i++)
        {
            table[i] = new Block();
        }
        //Main Loop
        int option = 0; //Any value other than 4
        while (option != 4)
        {
            System.out.println();
            System.out.println("1) Print the table and the file");
            System.out.println("2) Add a block to the file");
            System.out.println("3) Remove a block from the file");
            System.out.println("4) Quit the program");
            System.out.println("Enter selection: ");
            if (input.hasNextInt()) //check datatype
            {
                option = input.nextInt();
            } else {
                option = 0; //Any value other than 1,2,3,4
            }
            input.nextLine(); //input flushing
            switch(option)
            {
                case 1:
                    printTableAndFile();
                    break;
                case 2:
                    addBlock();
                    break;
                case 3:
                    removeBlock();
                    break;
                case 4:
                    System.out.println("Good Bye.");
                    break;
                default:
                    System.out.println("Invalid option, try again.");
            }//End of switch statement
        }//End of main loop
    }//end of main method

    //Print Table and File
    public static void printTableAndFile()
    {
        //print the table view header row
        System.out.println("Table View:");
        System.out.println("Index\tData\tNext");
        //print every block from the table as a row
        for (int i=0; i<table.length; i++)
        {
            System.out.println(i + "\t" + table[i].getData() + "\t" + table[i].getNextIndex());
        }
        //print the file view header row
        System.out.println("File View:");
        System.out.println("Index\tData\tNext");
        //print every block from the file as a row
        int currentIndex = headIndex;
        while (currentIndex != -1)
        {
            System.out.println(currentIndex + "\t" + table[currentIndex].getData() + "\t" + table[currentIndex].getNextIndex());
            currentIndex = table[currentIndex].getNextIndex();
        }
    }//end of printTableAndFile

    //Add Block
    public static void addBlock()
    {
        //Enter the index to add
        System.out.println("Enter an index for the block to add:");
        if (!input.hasNextInt())//Data type check
        {
            System.out.println("Invalid input.");
            input.nextLine(); //Input flushing
            return;
        }
        int newIndex = input.nextInt();
        input.nextLine(); //input flushing
        if (newIndex < 0 || newIndex >=table.length) //valid data check
        {
            System.out.println("Invalid block index.");
            return;
        }
        //Enter the unsigned data
        System.out.println("Enter an unsigned integer of data: ");
        if (!input.hasNextInt()) //data type check
        {
            System.out.println("Invalid input.");
            input.nextLine(); //input flushing
            return;
        }

        int newData = input.nextInt();
        input.nextLine(); // input flushing
        if (newData < 0 ) //non-negative input
        {
            System.out.println("Invalid data.");
            return;
        }
        //Check the value of the head index
        if (headIndex == -1)
        {
            //Assign the new block to the head of the file
            headIndex = newIndex;
            table[newIndex].setData(newData);
            System.out.println("Added first block at index " + newIndex + ".");
        }
        else {
            //Iterate through the file and detect if the new index is in use or not
            //Note - The new index will always be in use if the table is full
            int currentIndex = headIndex;
            while (currentIndex != newIndex)
            {
                //The new index is not in use if the end of the file is reached
                if (table[currentIndex].getNextIndex() == -1)
                {
                    //Assign the new block to the end of the file
                    table[currentIndex].setNextIndex(newIndex);
                    table[newIndex].setData(newData);
                    System.out.println("Added new block at index " + newIndex + ".");
                    return;
                }
                currentIndex = table[currentIndex].getNextIndex();
            }
            System.out.println("Duplicate block index detected.");
        }
    }//end of addBlock

    //Remove Block
    public static void removeBlock()
    {
        //Enter the removal index
        System.out.println("Enter an index for the block to remove:");
        if (!input.hasNextInt()) //data type check
        {
            System.out.println("Invalid input.");
            input.nextLine(); //input flushing
            return;
        }
        int removalIndex = input.nextInt();
        input.nextLine(); //input flushing
        if (removalIndex < 0 || removalIndex >= table.length) //valid data check
        {
            System.out.println("Invalid block index.");
            return;
        }
        //Check the value of the head index
        if (headIndex == removalIndex)
        {
            //Replace the head block with its next block 
            //Note - This next block can be either empty or not empty
            int replacementIndex = table[removalIndex].getNextIndex();
            table[removalIndex].setData(0);
            table[removalIndex].setNextIndex(-1);
            headIndex = replacementIndex;
            System.out.println("Removed the block at head index " +
                        removalIndex + ".");
        }
        else {
            //Iterate through the file and search for the removal index 
            int currentIndex = headIndex;
            while (currentIndex != -1)
            {
                //Check the value of the current block's next index
                if (table[currentIndex].getNextIndex() == removalIndex)
                {
                    //Replace the current block's next block with its own next
                    //Note - This next block can be either empty or not empty 
                    int replacementIndex = table[removalIndex].getNextIndex();
                    table[removalIndex].setData(0);
                    table[removalIndex].setNextIndex(-1);
                    table[currentIndex].setNextIndex(replacementIndex);
                    System.out.println("Removed the block at index " + removalIndex + ".");
                    return;
                }
                currentIndex = table[currentIndex].getNextIndex();
            }
            System.out.println("Unable to locate the block.");
        }
    }//end of removeBlock
}//end of class


