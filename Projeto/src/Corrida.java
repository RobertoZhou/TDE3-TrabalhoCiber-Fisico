import java.util.concurrent.*;

public class Corrida {
    static int count = 0;
    static final Semaphore sem = new Semaphore(1, true); // FIFO

    public static void main(String[] args) throws Exception {
        int T = 8, M = 250_000;
        ExecutorService pool = Executors.newFixedThreadPool(T);
        Runnable r = () -> {
            for (int i = 0; i < M; i++) {
                try {
                    sem.acquire();
                    count++;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    sem.release();
                }
            }
        };
        long t0 = System.nanoTime();
        for (int i = 0; i < T; i++)
            pool.submit(r);
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);
        long t1 = System.nanoTime();
        System.out.printf("Esperado=%d, Obtido=%d, Tempo=%.3fs%n",
                T * M, count, (t1 - t0) / 1e9);
    }
}