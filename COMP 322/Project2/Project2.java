//Kelvin Jinn
//Comp 322
//Project 2

import java.util.Scanner;

public class Project2 {
    //Global Variables
    private static Scanner input = new Scanner(System.in);
    private static Relation[] process;
    private static final int EMPTY =0;
    private static final int REQUEST =1;
    private static final int ALLOCATE =2;

    //Main Method
    public static void main(String args[])
    {
        //Enter the number of processes
        System.out.println("Enter the number of processes: ");
        if (!input.hasNextInt()) // Data type check
        {
            System.out.println("Invalid input.");
            System.exit(0);
        }
        int processes = input.nextInt();
        input.nextLine(); //Input Flushing

        //Enter the number of resources
        System.out.println("Enter the number of resources: ");
        if (!input.hasNextInt()) //Data type check
        {
            System.out.println("Invalid input.");
            System.exit(0);
        }
        int resources = input.nextInt();
        input.nextLine(); //Input Flushing
        
        process = new Relation[processes];
        for (int i = 0; i < processes; i++) {
            process[i] = new Relation(resources); // all zeros by default
        }

        //Main Loop 
        int option = 0;
        while (option != 4)
        {   
            System.out.println();
            System.out.println("1) Print the process-to-resource relations");
            System.out.println("2) Change a process-to-resource relation");
            System.out.println("3) Detect the system for deadlock");
            System.out.println("4) Quit the program");
            System.out.println("Enter selection: ");
            if (input.hasNextInt()) //Data type check
            {
                option = input.nextInt();
            }
            else {
                option = 0; //Any value other than 1,2,3,4
            }
            input.nextLine(); //input flushing
            switch(option)
            {
                case 1: 
                    printRelations();
                    break;
                case 2:
                    changeRelation();
                    break;
                case 3:
                    detectDeadlock();
                    break;
                case 4:
                    System.out.println("Goodbye.");
                    break;
                default:
                    System.out.println("Invalid option, try again.");
            } //End of switch statement
        }
        
    } //End of Main Method

    //Print Relations
    public static void printRelations()
    {
        //Print the header row
        for (int j=0; j<process[0].getResourceLength(); j++)
        {
            System.out.print("\tr" + j);
        }
        System.out.println();
        //Print every process-to-resource relation as a row
        for (int i=0; i<process.length; i++)
        {
            System.out.print("p" + i);
            for (int j=0; j< process[0].getResourceLength(); j++)
            {
                System.out.print("\t");
                if (process[i].getResource(j) !=0)
                {
                    System.out.print(process[i].getResource(j));
                }
            }
            System.out.println();
        }
    } //End of Print Relations

    //Change Relation
    public static void changeRelation()
    {
        //Enter the process index
        System.out.println("Enter the process index:");
        if(!input.hasNextInt()) //Data type check
        {
            System.out.println("Invalid input.");
            input.nextLine(); //Input flushing
            return;
        }
        int processIndex = input.nextInt();
        input.nextLine(); //Input flushing
        if (processIndex < 0 || processIndex >= process.length)
        {
            System.out.println("Invalid process index.");
            return;
        }
        //Enter the resource index
        System.out.println("Enter the resource index:");
        if (!input.hasNextInt()) //Data type check
        {
            System.out.println("Invalid input.");
            input.nextLine(); //Input flushing
            return;
        }
        int resourceIndex = input.nextInt();
        input.nextLine(); //Input flushing
        if (resourceIndex < 0 || resourceIndex >= process[processIndex].getResourceLength())
        {
            System.out.println("Invalid resource index.");
            return;
        }
        //Enter the new relation between the process and the index
        System.out.println("Enter the new relation (0: None, 1: P requests R, 2: P allocated R):");
        if (!input.hasNextInt()) //Data type check
        {
            System.out.println("Invalid input.");
            input.nextLine(); //Input flushing
            return;
        }
        int newRelation = input.nextInt();
        input.nextLine(); //Input flushing
        //Perform a different action based on the new relation
        switch(newRelation)
        {
            case EMPTY:
                process[processIndex].setResource(resourceIndex, EMPTY);
                System.out.println("There is now no relation process " + processIndex + 
                    " and resource " + resourceIndex + ".");
                break;
            case REQUEST:
                process[processIndex].setResource(resourceIndex, REQUEST);
                System.out.println("Process " + processIndex + " is now requesting " + 
                    "to use resource " + resourceIndex + ".");
                break;
            case ALLOCATE:
                for (int i=0; i<process.length; i++)
                {
                    if (i != processIndex && 
                            process[i].getResource(resourceIndex) == ALLOCATE)
                    {
                        System.out.println("Process " + processIndex + " cannot be " + 
                                "allocated resource " + resourceIndex + " because " +
                                "process " + i + " is using it.");
                        return;
                    }
                }
                process[processIndex].setResource(resourceIndex, ALLOCATE);
                System.out.println("Process " + processIndex + " has been allocated " + 
                    "resource " + resourceIndex + " for usage.");
                break;
            default:
                System.out.println("Invalid relation value.");
        }//End of switch statement
    } //End of Change Relation

    //Detect Deadlock
    public static void detectDeadlock()
    {
        //Check all process-to-resource relations for a directed cycle of dependencies
        for (int i=0; i< process.length; i++)
        {
            for (int j=0; j< process[i].getResourceLength(); j++)
            {
                if (process[i].getResource(j) == ALLOCATE)
                {
                    //Perform an additional for-loop if an allocation is detected
                    for (int k=0; k<process.length; k++)
                    {
                        if (process[k].getResource(j) == REQUEST)
                        {
                            //Run the recursive function if a request is detected
                            if (detectDeadlockRecursively(i, k))
                            {
                                //DeadLock detected - Print out the request & allocation
                                System.out.println("Base: ");
                                System.out.println("Process " + k + 
                                        " is requesting resource " + j + ".");
                                System.out.println("Process " + i + 
                                        " is allocated resource " + j + ".");
                                return;
                            }
                        }//End of Request Check   
                    }//End of Third Loop 
                }//End of Allocation check
            }//End of Second Loop 
        }//End of First Loop 
        //No deadlocks exist if all relation comparisons are exhausted
        System.out.println("The system is not deadlocked, and is currently safe.");
    } //End of detect deadlock

    //Detect Deadlock Recursively
    public static boolean detectDeadlockRecursively(int targetIndex, int currentIndex)
    {
        //Check all process-to-resource relations for a directed cycle of dependencies
        for (int j=0; j< process[currentIndex].getResourceLength(); j++)
        {
            if (process[currentIndex].getResource(j) == ALLOCATE)
            {
                //Perform an additional for-loop if an allocation is detected
                for (int k=0; k < process.length; k++)
                {
                    if (process[k].getResource(j) == REQUEST)
                    {
                        //Perform additional evaluations if a request is detected
                        if (k == targetIndex) //Source of the deadlock
                        {
                            //Deadlock detected - Print out the request & allocation
                            System.out.println("The system is deadlocked due to " + 
                                        "the following relations:");
                            System.out.println("Process " + k +
                                        " is requesting resource " + j + ".");
                            System.out.println("Process " + currentIndex + 
                                        " is allocated resource " + j + ".");
                            return true;
                        }
                        else if (detectDeadlockRecursively(targetIndex, k)) {
                            //Deadlock detected - Print out the request & allocation
                            System.out.println("Backtrack: ");
                            System.out.println("Process " + k +
                                        " is requesting resource " + j + ".");
                            System.out.println("Process " + currentIndex + 
                                        " is allocated resource " + j + ".");
                            return true;
                        }
                    }//End of Request Check
                }//End of Second Loop 
            }//End of Allocation Check
        }//End of First Loop
        //Return false if all relation comparisons are exhausted
        return false;
    } //End of deadlock Recursively
}//End of Class
