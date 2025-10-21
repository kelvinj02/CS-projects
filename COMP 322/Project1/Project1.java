//Kelvin Jinn
//COMP 322
//Project 1

import java.util.Scanner;

public class Project1 {
    //Global variables
    private static Scanner input = new Scanner(System.in);
    private static Process[] table;

    //Main Method
    public static void main(String[] args)
    {
        //Enter the table size
        System.out.println("Enter the table size: ");
        if (!input.hasNextInt()) //Data type check
        {
            System.out.print("Invalid input.");
            System.exit(0);
        }
        int tableSize = input.nextInt();
        input.nextLine(); //Input Flushing
        if (tableSize <=0) //Valid Data Check
        {
            System.out.println("Invalid table size.");
            System.exit(0);
        }
        //Create the process table
        table =new Process[tableSize];
        for (int i=0; i<table.length; i++)
        {
            table[i] = new Process();
        }
        //Initialize the first process in the table
        table[0].setParentIndex(0);
        //Main Loop
        int option = 0;
        while (option!=4)
        {
            System.out.println();
            System.out.println("1) Print the hierarchy from the table");
            System.out.println("2) Add a child process to the hierarchy");
            System.out.println("3) Remove a process's descendants from the hierarchy");
            System.out.println("4) Quit the program");
            System.out.println("Enter selection: ");
            if(input.hasNextInt()) //Data type Check
            {
                option = input.nextInt();
            }
            else {
                option = 0; //Any value other than 1,2,3,4
            }
            input.nextLine(); //Input Flushing
            switch (option)
            {
                case 1: 
                    printHierarchy();
                    break;
                case 2:
                    addProcess();
                    break;
                case 3:
                    removeProcess();
                    break;
                case 4:
                    System.out.println("Goodbye.");
                    break;
                default:
                    System.out.println("Invalid option, try again.");
            } //End of Switch Statement
        }
    } //End of Main Method

    //Print Hierarchy
    public static void printHierarchy()
    {
        //Print out the header row
        System.out.println("Index\tParent\tFirst\tYounger");
        //Print every valid process as a row
        for (int i=0; i<table.length; i++)
        {
            if (table[i].getParentIndex() == -1)
            {
                continue;
            }
            System.out.println(i + "\t" + table[i].getParentIndex() + "\t");
            if (table[i].getFirstChildIndex() != -1)
            {
                System.out.println(table[i].getFirstChildIndex());
            }
            System.out.println("\t");
            if (table[i].getYoungerSiblingIndex() != -1)
            {
                System.out.println(table[i].getYoungerSiblingIndex());
            }
        }
    } //End of printHierarchy Method

    //Add Process
    public static void addProcess()
    {
        //Enter the parent process index
        System.out.println("Enter the parent process index for the child process: ");
        if (!input.hasNextInt()) //Data type check
        {
            System.out.println("Invalid input.");
            input.nextLine(); //Input Flushing
            return;
        }
        int parentIndex = input.nextInt(); 
        input.nextLine(); //Input Flushing
        
        if(parentIndex < 0 || parentIndex >= table.length) //Valid Data Check
        {
            System.out.println("Invalid process index.");
            return;
        }
        if(table[parentIndex].getParentIndex() == -1)
        {
            System.out.println("Process index is not active.");
            return;
        }
        //Calculate the next available index as the child process index
        int childIndex = 1; //Can also be 0
        while (table[childIndex].getParentIndex() != -1)
        {
            childIndex++;
            if (childIndex == table.length)
            {
                System.out.println("Unable to assign an index for the child process.");
                return;
            }
        }
        //Set the parent process index for the child process
        table[childIndex].setParentIndex(parentIndex);
        //Set the child process index for the parent process
        if (table[parentIndex].getFirstChildIndex() == -1)
        {
            //Assign the child process index as the parent process' first child
            table[parentIndex].setFirstChildIndex(childIndex);
        }
        else {
            //Find the youngest sibling process among the parent process'children
            //Note - Start with the oldest and travel towards the youngest
            int youngestIndex = table[parentIndex].getFirstChildIndex();
            while(table[youngestIndex].getYoungerSiblingIndex() != -1)
            {
                youngestIndex = table[youngestIndex].getYoungerSiblingIndex();
            }
            //Assign the child process index as the youngest sibling process'younger sibling
            //Note - This now makes the child process the new youngest sibling process
            table[youngestIndex].setYoungerSiblingIndex(childIndex);
        }
        //Confirm that the child process was successfully added
        System.out.println("Process " + childIndex + " was added as a child of process " + parentIndex + ".");
    } //End of addProcess Method

    //Remove Process
    public static void removeProcess()
    {
        //Enter the parent process index
        System.out.println("Enter the parent process index whose descendants will be removed: ");
        if (!input.hasNextInt()) //Data Type check
        {
            System.out.println("Invalid input.");
            input.nextLine(); //Input Flushing
            return;
        }
        int parentIndex = input.nextInt();
        input.nextLine(); //Input Flushing
        if (parentIndex <0 || parentIndex >= table.length) //Valid Data Check
        {
            System.out.println("Invalid process index.");
            return;
        }
        if (table[parentIndex].getParentIndex() == -1)
        {
            System.out.println("Process index is not active.");
            return;
        }
        //Recursively remove processes, starting with the parent process' first child
        removeProcessRecursively(table[parentIndex].getFirstChildIndex());
        //Remove the reference to the parent process' first child
        table[parentIndex].setFirstChildIndex(-1);
        //Confirm that the parent process' descendants were removed
        System.out.println("All descendants of process " + parentIndex + " were removed.");
    } //End of removeProcess Method

    //Remove Process Recursively
    public static void removeProcessRecursively(int currentIndex)
    {
        if (currentIndex == -1)
        {
            //Base Case = Exit out when an invalid index is reached
            return;
        }
        //Recursively remove from the current process' younger sibling
        removeProcessRecursively(table[currentIndex].getYoungerSiblingIndex());
        //Recursively remove from the current process' first child
        removeProcessRecursively(table[currentIndex].getFirstChildIndex());
        //Remove all data from the current process
        table[currentIndex].setParentIndex(-1);
        table[currentIndex].setFirstChildIndex(-1);
        table[currentIndex].setYoungerSiblingIndex(-1);
    } //End of removeProcessRecursively Method
}// End of Class
