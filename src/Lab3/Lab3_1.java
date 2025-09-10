package Lab3;

import mpi.*;

public class Lab3_1 {
    public static void main(String[] args) throws Exception {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        int[] data = new int[1];
        int[] buf = {1, 3, 5};
        int[] back_buf;
        int[] back_buf2;
        int count;
        int TAG = 0;
        Status st;

        data[0] = 2016;

        if (rank == 0) {
            // Процесс 0 отправляет одно число
            MPI.COMM_WORLD.Send(data, 0, 1, MPI.INT, 2, TAG);
        } else if (rank == 1) {
            // Процесс 1 отправляет массив
            MPI.COMM_WORLD.Send(buf, 0, buf.length, MPI.INT, 2, TAG);
        } else if (rank == 2) {
            // Приём сообщения от процесса 0
            st = MPI.COMM_WORLD.Probe(0, TAG);
            count = st.Get_count(MPI.INT);
            back_buf = new int[count];
            MPI.COMM_WORLD.Recv(back_buf, 0, count, MPI.INT, 0, TAG);
            System.out.print("Received from rank 0: ");
            for (int i = 0; i < count; i++) {
                System.out.print(back_buf[i] + " ");
            }
            System.out.println();

            // Приём сообщения от процесса 1
            st = MPI.COMM_WORLD.Probe(1, TAG);
            count = st.Get_count(MPI.INT);
            back_buf2 = new int[count];
            MPI.COMM_WORLD.Recv(back_buf2, 0, count, MPI.INT, 1, TAG);
            System.out.print("Received from rank 1: ");
            for (int i = 0; i < count; i++) {
                System.out.print(back_buf2[i] + " ");
            }
            System.out.println();
        }

        MPI.Finalize();
    }
}
