import java.util.concurrent.Semaphore;

public class JantarFilosofos {

    static class Filosofo extends Thread {
        int num;
        Semaphore[] garfos;

        Filosofo(int num, Semaphore[] garfos) {
            this.num = num;
            this.garfos = garfos;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    pensar();

                    System.out.println("Filósofo " + num + " está com fome.");

                    int esquerdo = num;
                    int direito = (num + 1) % garfos.length;

                    // PROTOCOLO ERRADO -> pode travar
                    garfos[esquerdo].acquire();
                    System.out.println("Filósofo " + num + " pegou o garfo esquerdo.");

                    // pausa rápida para piorar o deadlock
                    Thread.sleep(50);

                    garfos[direito].acquire();
                    System.out.println("Filósofo " + num + " pegou o garfo direito.");

                    comer();

                    garfos[direito].release();
                    garfos[esquerdo].release();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        void pensar() throws Exception {
            Thread.sleep(200);
        }

        void comer() throws Exception {
            System.out.println("Filósofo " + num + " está comendo.");
            Thread.sleep(200);
        }
    }

    public static void main(String[] args) {
        int N = 5;
        Semaphore[] garfos = new Semaphore[N];

        for (int i = 0; i < N; i++) {
            garfos[i] = new Semaphore(1);
        }

        for (int i = 0; i < N; i++) {
            new Filosofo(i, garfos).start();
        }
    }
}
