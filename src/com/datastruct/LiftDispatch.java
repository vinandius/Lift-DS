package com.datastruct;

public class LiftDispatch {
    
    //Membuat variable lifts untuk mewakili class lift
    private Lift[] lifts;

    public LiftDispatch(Lift[] lifts){
        this.lifts = lifts;
    }

    private int hitungJarak(Lift lift, int targetFloor, Direction direction){

        //Menghitung jarak antara lantai pemanggil dan posisi lift
        int dist = Math.abs(lift.getLatestFloor() - targetFloor);

        //Mengembalikan nilai dist dari suatu lift ketika berada dalam kondisi idle
        if (lift.getRequestDirection() == Direction.IDLE) {
            return dist;
        }

        //Melakukan pengecekan apakah arah pergerakan lift sesuai
        //Misal getRequestDirection() bernilai UP, lalu ada lift yang memanggil turun sehingga direction bernilai DOWN, maka lanjut ke dist+100
        if (lift.getRequestDirection() == direction) {
            if (lift.getRequestDirection() == Direction.UP && lift.getLatestFloor() <= targetFloor || lift.getLatestFloor() >= targetFloor) {
                return dist;
            } else if (lift.getRequestDirection() == Direction.DOWN && lift.getLatestFloor() >= targetFloor || lift.getLatestFloor() <= targetFloor) {
                return dist;
            }
        }
        return dist+100;
    }

    //Fungi untuk menentukan lift
    public Lift liftCall(int floor, Direction direction) {
        //Membuat objek heap
        Heap<Integer, Lift> heap = new Heap<>(20, true);

        //Menghitung selisih jarak setiap lift dengan lantai pemanggil
        for (Lift lift : lifts) {
            int dist = hitungJarak(lift, floor, direction);
            heap.insert(dist, lift);
        }

        //Membuat variable untuk menampung data lift yang dikeluarkan
        BTNode<Integer, Lift> closestLiftFromFloor = heap.removeFirst();

        //Membuat variable untuk mengambil data dari lift yang dikeluarkan dari heap
        Lift chosenLift = closestLiftFromFloor.getData();

        System.out.println("Lift " + chosenLift.getName() + " dipilih untuk lantai " + floor);

        chosenLift.addDest(floor, direction);
        chosenLift.setLatestFloor(floor);
        chosenLift.setRequestDirection(direction);

        return chosenLift;
    }

    public void moveLift(Lift lift, int floor, LiftUI ui) {
        lift.depart(floor, ui);
    }
}
