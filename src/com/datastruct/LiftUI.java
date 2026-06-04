package com.datastruct;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LiftUI extends JFrame {
    //membuat objek dari class lift
    private Lift[] lifts;

    //membuat objek dari class dispatch
    private LiftDispatch dispatch;

    //liftKe merepresentasikan posisi setelah naik dan turun lift A, B, C, D
    private JLabel[][] liftKe;
    
    //bstText untuk menyimpan text urutan lantai untuk lift
    private JTextArea[] bstText;

    //Queue berfungsi untuk menyimpan request (panggilan) lift untuk ditampilkan di panel BST (urutan untuk lift)
    private MyArrayList<LiftCall> queue = new MyArrayList<>(100);

    public LiftUI(Lift[] lifts, LiftDispatch dispatch) {
        this.lifts = lifts;
        this.dispatch = dispatch;

        //Menentukan konfigurasi tampilan GUI
        setTitle("Lift System");
        setSize(1200, 700);
        setLocationRelativeTo(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //Membuat panel
        JPanel allPanel = new JPanel();

        //Konfigurasi panel
        allPanel.setLayout(new GridLayout(21, 3));
        allPanel.add(new JLabel("Lantai", SwingConstants.CENTER));
        allPanel.add(new JLabel("Arah", SwingConstants.CENTER));
        allPanel.add(new JLabel("Lift", SwingConstants.CENTER));
        allPanel.setPreferredSize(new Dimension(800,600));

        //Variable liftKe untuk menampilkan slot lift di setiap lantai
        liftKe= new JLabel[20][4];

        //membuat tampilan untuk lantai, arah, dan lift
        for(int floor = 20; floor >= 1; floor--) {
            allPanel.add(new JLabel("Floor " + floor, SwingConstants.CENTER), BorderFactory.createLineBorder(Color.BLACK));
            int destFloor = floor;
            //panel untuk tombol arah naik atau turun
            JPanel dirPanel = new JPanel();
            //menampilkan tombol arah naik (kecuali lantai 20)
            if (floor != 20) {
                //Membuat tombol naik
                JButton up = new JButton("↑");
                //Membuat aksi untuk tombol naik
                up.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new Thread(() -> {
                            //Variable chosenLift untuk menyimpan data panggilan dari suatu lantai
                            Lift chosenLift = LiftUI.this.dispatch.recordCall(destFloor, Direction.UP);
                            //Menambahkan variable chosenLift ke antrian
                            queue.add(new LiftCall(destFloor, Direction.UP, chosenLift));
                            //Dilakukan pemanggilan fungsi prosesLift() untuk memperbarui isi dari panel BST (urutan lantai untuk lift)
                            prosesLift();
                        }).start();
                    }
                });
                up.setBackground(Color.lightGray);
                up.setPreferredSize(new Dimension(41, 21));
                //Menambahkan tombol up ke panel direction
                dirPanel.add(up);
            } else {
                dirPanel.add(new JLabel(" "));
            }
            //menampilkan tombol arah turun (kecuali lantai 1)
            if (floor != 1) {
                JButton down = new JButton("↓");
                down.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new Thread(() -> {
                            Lift chosenLift = LiftUI.this.dispatch.recordCall(destFloor, Direction.DOWN);
                            queue.add(new LiftCall(destFloor, Direction.DOWN, chosenLift));
                            prosesLift();
                        }).start();
                    }
                });
                down.setBackground(Color.lightGray);
                down.setPreferredSize(new Dimension(41, 21));
                
                dirPanel.add(down);
            } else {
                dirPanel.add(new JLabel(" "));
            }

            //menampilkan slot lift setiap lantai
            JPanel liftPanel = new JPanel();
            liftPanel.setLayout(new GridLayout(1, 4));

            for(int i = 0; i < 4; i++) {
                //Menampilkan slot lift menggunakan [ ]
                JLabel liftName = new JLabel("[ ]",SwingConstants.CENTER);
                //Variable liftKe digunakan untuk memperbarui kondisi setiap lift (memberikan informasi posisi lift setelah naik atau turun)
                //Text [ ] harus disimpan ke array liftKe, karena variable liftName adalah variable lokal (khusus dalam loop ini)
                liftKe[floor - 1][i] = liftName;
                liftPanel.add(liftName);
            }
            allPanel.add(dirPanel, BorderLayout.CENTER);
            allPanel.add(liftPanel, BorderLayout.CENTER);
        }

        //membuat tombol start untuk memulai
        JButton start = new JButton("Start");
        start.setBackground(Color.lightGray);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                new Thread(() -> {
                    for (int i = 0; i < lifts.length; i++) {
                        //Membuat variable untuk merujuk (mewakili) sebuah lift
                        Lift lift = lifts[i];

                        //MyArrayList digunakan untuk menyimpan daftar urutan dari sebuah lift yang telah diwakili variable lift
                        MyArrayList<Integer> order = lift.getDestInOrder();

                        //Membuat logical condition dimana jika request lantai adalah turun, gunakan reverse in order. Kalau naik, gunakan in order
                        if(lift.getRequestDirection() == Direction.DOWN){
                            order = lift.getDestReverseInOrder();
                        } else {
                            order = lift.getDestInOrder();
                        }
                        
                        //Melakukan start untuk mengeksekusi / menggerakkan lift ke tujuan
                        for (int j = 0; j < order.size(); j++) {
                            int floor = order.get(j);

                            LiftUI.this.dispatch.moveLift(lift, floor, LiftUI.this);

                            prosesLift();
                        }
                    }

                    //Clear queue (antrian) agar tidak terjadi looping
                    queue.clear();

                    //Mengupdate kondisi lift setelah mencapai tujuan
                    for (int i = 0; i < lifts.length; i++) {
                        lifts[i].setLatestFloor(lifts[i].getCurrentFloor());
                        lifts[i].setRequestDirection(Direction.IDLE);
                    }

                    prosesLift();
                }).start();
            }
        });

        //membuat panel untuk menampilkan urutan lantai yang harus dituju setiap lift
        JPanel bst = new JPanel();
        bst.setBorder(BorderFactory.createLineBorder(Color.black));
        bst.setLayout(new GridLayout(4, 1));
        bst.setPreferredSize(new Dimension(300,300));

        //menampilkan urutan lantai
        bstText = new JTextArea[4];

        //membuat 4 buat panel untuk lift A, B, C, dan D
        for (int i = 0; i < 4; i++) {
            JPanel liftsName = new JPanel();
            liftsName.setLayout(new BorderLayout());
            
            //membuat header untuk menampilkan nama lift
            JLabel name = new JLabel("Lift " + lifts[i].getName(), SwingConstants.CENTER);
            name.setBorder(BorderFactory.createLineBorder(Color.black));

            //membuat area untuk menampilkan urutan lantai
            bstText[i] = new JTextArea();
            bstText[i].setEditable(false);
            
            liftsName.add(name, BorderLayout.NORTH);
            liftsName.add(new JScrollPane(bstText[i]), BorderLayout.CENTER);

            bst.add(liftsName);
        }

        add(allPanel, BorderLayout.WEST);
        add(start, BorderLayout.SOUTH);
        add(bst, BorderLayout.EAST);
        prosesLift();
        setVisible(true);
        }

        //Membuat fungsi prosesLift
        public void prosesLift() {
        for(int floor = 0; floor < 20; floor++) {
            for(int lift = 0; lift < 4; lift++) {
                //Set slot lift kembali ke [ ] setelah lift naik atau turun
                liftKe[floor][lift].setText("[ ]");
            }
        }

        //Loop untuk menampilkan nama lift beserta arah geraknya lift
        for(int i = 0; i < lifts.length; i++) {
            //Mendapatkan informasi terkait keberadaan dan kondisi suatu lift
            int floor = lifts[i].getCurrentFloor();
            Direction direction = lifts[i].getMovingDirection();

            //Untuk menampilkan arah gerak lift
            String arah = "";
            if (direction == Direction.UP) {
                arah = "↑";
            } else if (direction == Direction.DOWN) {
                arah = "↓";
            }

            //Menampilkan posisi lift setelah dilakukan fungsi moveLift()
            liftKe[floor - 1][i].setText("[" + lifts[i].getName() + arah + "]");
            }

            updateBST();
        }

        //Fungsi untuk menampilkan urutan (rute) lift dipanel lift
        private void updateBST() {
            for (int i = 0; i < lifts.length; i++) {
                Lift lift = lifts[i];
                
                //menampilkan tujuan lift secara in order atau reverse in order di panel bst
                if (lift.getRequestDirection() ==  Direction.DOWN){
                    bstText[i].setText(lifts[i].showDestRevString());
                } else {
                    bstText[i].setText(lifts[i].showDestString());
                }
            }
        }
}

