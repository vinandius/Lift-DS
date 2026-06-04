import com.datastruct.*;

public class Main {
    public static void main(String[] args) {
        Lift lift1 = new Lift("A", 1);
        Lift lift2 = new Lift("B", 5);
        Lift lift3 = new Lift("C", 10);
        Lift lift4 = new Lift("D", 15);
        Lift[] lifts = {lift1, lift2, lift3, lift4};

        //Mengirimkan data lift ke LiftDispatch
        LiftDispatch dispatch = new LiftDispatch(lifts);

        new LiftUI(lifts,dispatch);
    }
}