package Lab2;

import mpi.*;

public class Lab2Blocking {
    public static void main(String[] args) throws Exception {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        int[] sum = new int[1];
        sum[0] = rank;

        int right = (rank + 1) % size;
        int left = (rank - 1 + size) % size;

        for (int i = 0; i < size - 1; i++) {
            int[] recv = new int[1];
            // атомарный обмен "отправить и принять" за один вызов
            MPI.COMM_WORLD.Sendrecv(
                    sum, 0, 1, MPI.INT, right, 0,
                    recv, 0, 1, MPI.INT, left, 0
            );
            sum[0] += recv[0];
            System.out.printf("Rank %d: got %d from %d (step %d)%n", rank, recv[0], left, i + 1);
        }

        if (rank == 0) {
            System.out.println("Final sum of ranks = " + sum[0]);
        }

        MPI.Finalize();
    }
}
