package Lab1;

import mpi.*;

public class Lab1 {
    public static void main(String[] args) throws Exception {
        MPI.Init(args);

        int myrank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int TAG = 0;
        int[] message = new int[1];

        if (myrank % 2 == 0) {
            // Чётный процесс отправляет свой ранг следующему
            if (myrank + 1 < size) {
                message[0] = myrank;
                MPI.COMM_WORLD.Send(message, 0, 1, MPI.INT, myrank + 1, TAG);
                System.out.println("Process " + myrank + " sent: " + message[0] + " to " + (myrank + 1));
            }
        } else {
            // Нечётный процесс принимает сообщение от предыдущего
            MPI.COMM_WORLD.Recv(message, 0, 1, MPI.INT, myrank - 1, TAG);
            System.out.println("Process " + myrank + " received: " + message[0] + " from " + (myrank - 1));
        }

        MPI.Finalize();
    }
}
