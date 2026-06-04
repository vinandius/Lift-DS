package com.datastruct;

public class Lift {
    private String name;
    private int currentFloor;
    private int latestFloor;
    private Direction movingDirection;
    private Direction requestDirection;

    BinarySearchTree<Integer, String> dest = new BinarySearchTree<Integer, String>();

    public Lift(String name, int currentFloor) {
        this.name = name;
        this.currentFloor = currentFloor;
        this.latestFloor = currentFloor;
        this.dest = new BinarySearchTree<Integer, String>();
        this.movingDirection = Direction.IDLE;
        this.requestDirection = Direction.IDLE;
    }

    public String getName() {
        return name;
    }

    public Direction getMovingDirection() {
        return movingDirection;
    }

    public void setMovingDirection(Direction direction) {
        this.movingDirection = direction;
    }

    public Direction getRequestDirection() {
        return requestDirection;
    }

    public void setRequestDirection(Direction direction) {
        this.requestDirection = direction;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void addDest(int floor, Direction direction){
        dest.insert(floor, floor + " : " + direction);
    }

    public int getLatestFloor() {
        return latestFloor;
    }

    public void setLatestFloor(int latestFloor) {
        this.latestFloor = latestFloor;
    }

    //Metode untuk animas lift naik
    public void sleep() {
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //Metode untuk memberi jeda ketika lift sampai di lantai tujuan
    public void arrivedPause() {
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void depart(int destFloor, LiftUI ui){
        //Cek kondisi untuk pergerakkan lift
        if(currentFloor < destFloor) {
            //Mengatur arah lift jika posisi lift <  lantai tujuan atau > lantai tujuan 
            this.movingDirection = Direction.UP;
            //Menaikkan angka currentFloor untuk menggerakkan lift naik
            while (currentFloor < destFloor){
                currentFloor++;
                ui.prosesLift();
                sleep();
            }
        } else if(currentFloor > destFloor) {
            this.movingDirection = Direction.DOWN;
            //Menurunkan angka currentFloor untuk menggerakkan lift turun
            while (currentFloor > destFloor){
                currentFloor--;
                ui.prosesLift();
                sleep();
            }
        } else {
            this.movingDirection = Direction.IDLE;
        }
        System.out.println("Lift " + name + " sedang menuju lantai " + destFloor);

        //Ketika kondisi currentFloor == floor, maka while loop akan berhenti dulu lalu dilanjut dengan arrivedPause() untuk jeda
        arrivedPause();

        currentFloor = destFloor;
        setMovingDirection(Direction.IDLE);
        setLatestFloor(destFloor);

        dest.delete(destFloor);

        ui.prosesLift();
    }

    //Untuk menampilkan tujuan di panel BST secara in order
    public String showDestString() {
        return dest.inOrderString();
    }

    //Untuk menampilkan tujuan di panel BST secara reverse in order
    public String showDestRevString() {
        return dest.reverseInOrderString();
    }

    //Untuk mendapatkan rute lift secara in order
    public MyArrayList<Integer> getDestInOrder() {
        return dest.inOrderKeys();
    }

    //Untuk mendapatkan rute lift secara reverse in order
    public MyArrayList<Integer> getDestReverseInOrder() {
        return dest.reverseInOrderKeys();
    }
}
