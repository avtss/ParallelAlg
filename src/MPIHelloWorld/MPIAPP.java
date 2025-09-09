package MPIHelloWorld;

import mpi.*;

public class MPIAPP {

    public static void main(String[] args) throws Exception{

        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();

        int size = MPI.COMM_WORLD.Size();
        System.out.println("Hi from <"+rank+">");
        MPI.Finalize();
    }
}
