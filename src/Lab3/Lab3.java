package Lab3;

import mpi.*;

import java.util.Arrays;

public class Lab3 {
    public static void main(String[] args) throws Exception {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int TAG = 0;

        int[] data = null;

        if (rank == 0) {
            //0 процесс делит массив на блоки
            int[] full = {3, 1, 5, 2, 8, 4};
            int blockSize = full.length / (size - 1);
            int extra = full.length % (size - 1);

            int offset = 0;
            for (int i = 1; i < size; i++) {
                int len = blockSize + (i <= extra ? 1 : 0);
                int[] block = new int[len];
                System.arraycopy(full, offset, block, 0, len);
                offset += len;

                // неблокирующая отправка
                Request req = MPI.COMM_WORLD.Isend(block, 0, len, MPI.INT, i, TAG);
                req.Wait();
            }
        } else {
            // получение блока массива
            Status st = MPI.COMM_WORLD.Probe(0, TAG);
            int count = st.Get_count(MPI.INT);
            data = new int[count];

            Request rRecv = MPI.COMM_WORLD.Irecv(data, 0, count, MPI.INT, 0, TAG);
            rRecv.Wait();

            Arrays.sort(data); // сортировка отдельного блока

            System.out.printf("Rank %d sorted %s\n",
                    rank, Arrays.toString(data));

            // отправка отсортированного блока обратно
            Request rSend = MPI.COMM_WORLD.Isend(data, 0, count, MPI.INT, 0, TAG);
            rSend.Wait();
        }

        if (rank == 0) {
            // соединение всех отсортированных блоков
            int[] result = new int[0];
            for (int i = 1; i < size; i++) {
                Status st = MPI.COMM_WORLD.Probe(i, TAG);
                int count = st.Get_count(MPI.INT);
                int[] recvBuf = new int[count];

                Request rRecv = MPI.COMM_WORLD.Irecv(recvBuf, 0, count, MPI.INT, i, TAG);
                rRecv.Wait();

                // склеиваем в общий массив
                int[] tmp = new int[result.length + count];
                System.arraycopy(result, 0, tmp, 0, result.length);
                System.arraycopy(recvBuf, 0, tmp, result.length, count);
                result = tmp;
            }

            Arrays.sort(result);

            System.out.println("Result = " + Arrays.toString(result));
        }

        MPI.Finalize();
    }
}
