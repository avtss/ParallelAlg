package Lab3;

import mpi.*;

public class Lab3_1 {
    public static void main(String[] args) throws Exception {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        int[] data = new int[1]; //одно число
        int[] buf = {1, 3, 5}; //массив
        int[] back_buf; //прием данных от процесса 0
        int[] back_buf2; //прием данных от процесса 1
        int count; //длина сообщения
        int TAG = 0; //тег сообщения
        Status st; //информация о сообщении

        data[0] = 2016;

        if (rank == 0) {
            MPI.COMM_WORLD.Send(data, 0, 1, MPI.INT, 2, TAG);
        } else if (rank == 1) {
            MPI.COMM_WORLD.Send(buf, 0, buf.length, MPI.INT, 2, TAG);
        } else if (rank == 2) {
            //прием от процесса 0
            st = MPI.COMM_WORLD.Probe(0, TAG);
            count = st.Get_count(MPI.INT);
            back_buf = new int[count];
            MPI.COMM_WORLD.Recv(back_buf, 0, count, MPI.INT, 0, TAG);
            System.out.print("Rank = 0: ");
            for (int i = 0; i < count; i++) {
                System.out.print(back_buf[i] + " ");
            }
            System.out.println();

            //прием от процесса 1
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
