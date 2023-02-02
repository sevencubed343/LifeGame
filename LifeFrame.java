import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class LifeFrame extends JFrame implements ActionListener, ChangeListener, Runnable{
    private JButton Cellsbtn[][];
    private JButton StartStopButton, RandomSetButton, ClearButton;
    private JSlider RandomRateSlider, SpeedSlider;
    private JLabel RandomRateLabel, SpeedLabel;
    private LifeCells LCells = new LifeCells(Const.ROW_CELLS, Const.COLUMN_CELLS);
    private Container c;
    private boolean running = false;
    private int stoptime = 100;

	public LifeFrame(int width, int height) {
        this.setTitle("LifeGame");
        this.setSize(width, height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        c = this.getContentPane();
		c.setLayout(null);

        GreenThinBorder gborder = new GreenThinBorder();
        
        //セルを生成
		Cellsbtn = new JButton[Const.COLUMN_CELLS][Const.ROW_CELLS];
        for(int i = 0; i < Const.COLUMN_CELLS; i++){
            for(int j = 0; j < Const.ROW_CELLS; j++){
                Cellsbtn[i][j] = new JButton();
                Cellsbtn[i][j].setBackground(Color.black);
                Cellsbtn[i][j].setBorder(gborder);
                Cellsbtn[i][j].setBounds(j*Const.CELL_SIZE + 5, i*Const.CELL_SIZE + 5, Const.CELL_SIZE, Const.CELL_SIZE);
                Cellsbtn[i][j].setActionCommand(Integer.toString(i*Const.ROW_CELLS + j));   //セルとボタンを対応付ける
                Cellsbtn[i][j].addActionListener(this);
                c.add(Cellsbtn[i][j]);
            }
		}

        //スタート・ストップボタンを生成
        StartStopButton = new JButton("Start");
        StartStopButton.setBounds(10, 20 + Const.COLUMN_CELLS*Const.CELL_SIZE, 80, 30);
        StartStopButton.setActionCommand("StartStop");
        StartStopButton.addActionListener(this);
        c.add(StartStopButton);

        //ランダムセットボタンを生成
        RandomSetButton = new JButton("Random");
        RandomSetButton.setBounds(100, 20 + Const.COLUMN_CELLS*Const.CELL_SIZE, 100, 30);
        RandomSetButton.setActionCommand("Random");
        RandomSetButton.addActionListener(this);
        c.add(RandomSetButton);

        //ランダムセットの確率を操作するスライダーを生成
        RandomRateSlider = new JSlider(0, 100, 40);
        RandomRateSlider.setBounds(270, 20 + Const.COLUMN_CELLS*Const.CELL_SIZE, 150, 30);
        RandomRateSlider.addChangeListener(this);
        c.add(RandomRateSlider);

        //ランダムセットの確率を表示
        RandomRateLabel = new JLabel();
        RandomRateLabel.setBounds(210, 20 + Const.COLUMN_CELLS*Const.CELL_SIZE, 80, 30);
        RandomRateLabel.setText("rate:" + RandomRateSlider.getValue() + "%");
        c.add(RandomRateLabel);

        //速さ（待機時間）を操作するスライダーを生成
        SpeedSlider = new JSlider(10, 30, 20);
        SpeedSlider.setInverted(true);
        SpeedSlider.setBounds(Const.FRAME_WIDTH - 250, 20 + Const.COLUMN_CELLS*Const.CELL_SIZE, 150, 30);
        SpeedSlider.addChangeListener(this);
        c.add(SpeedSlider);
        
        //速さを表示
        SpeedLabel = new JLabel();
        SpeedLabel.setBounds(Const.FRAME_WIDTH - 320, 20 + Const.COLUMN_CELLS*Const.CELL_SIZE, 80, 30);
        SpeedLabel.setText("speed:" + 1000/stoptime);
        c.add(SpeedLabel);

        //クリアボタンを生成
        ClearButton = new JButton("Clear");
        ClearButton.setBounds(Const.FRAME_WIDTH - 100, 20 + Const.COLUMN_CELLS*Const.CELL_SIZE, 80, 30);
        ClearButton.setActionCommand("Clear");
        ClearButton.addActionListener(this);
        c.add(ClearButton);

    }
    
    //セルの生死に応じて表示する
    public void display() {
        for(int i = 0; i < Const.COLUMN_CELLS; i++){
            for(int j = 0; j < Const.ROW_CELLS; j++){
                if (LCells.isLiving(i,j)) {
                    Cellsbtn[i][j].setBackground(Color.green);
                } else {
                    Cellsbtn[i][j].setBackground(Color.black);
                }
            }
        }
    }
    
    //stoptimeごとに世代交代する
    public void run() {
        while (true) {
            if (running) {
                LCells.proceedGen();
                display();
            }

            try {
                Thread.sleep(stoptime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "StartStop":
                running = !running;
                if(running) {
                    StartStopButton.setText("Stop");
                } else {
                    StartStopButton.setText("Start");
                }
                break;

            case "Random":
                LCells.randomSet((double)RandomRateSlider.getValue()/100);
                display();
                break;

            case "Clear":
                LCells.Clear();
                display();
                break;
        
            default:    //セルをクリックしたときに生死を反転させる
                int cellnumber = Integer.valueOf(command).intValue();
                int i = cellnumber / Const.ROW_CELLS;
                int j = cellnumber % Const.ROW_CELLS;

                LCells.ChangeLivingOrDead(i,j);
                if (LCells.isLiving(i,j)) {
                    Cellsbtn[i][j].setBackground(Color.green);
                } else {
                    Cellsbtn[i][j].setBackground(Color.black);
                }
                break;
        }
    }

    //スライダーが操作されたら表示する値を更新
    @Override
    public void stateChanged(ChangeEvent e) {
        RandomRateLabel.setText("rate:" + RandomRateSlider.getValue() + "%");
        stoptime = (int) Math.pow(10, (double)SpeedSlider.getValue()/10);
        SpeedLabel.setText("speed:" + 1000/stoptime);
    }
    

    public static void main(String[] args) {
		LifeFrame LFrame = new LifeFrame(Const.FRAME_WIDTH, Const.FRAME_HEIGHT);
		LFrame.setVisible(true);
        Thread t = new Thread(LFrame);
        t.start();
	}

}
