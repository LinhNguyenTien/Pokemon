package pikachu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class MainFrame extends JFrame implements ActionListener, Runnable {
    private int row = 10;
    private int col = 10;
    private int width = 900;
    private int height = 600;
    private ButtonEvent graphicsPanel;
    private JPanel mainPanel;
    private int MAX_TIME = 300;     //300 giây
    public int time = MAX_TIME;
    public JLabel lbScore;
    private JProgressBar progressTime;
    private JButton btnNewGame, btnExit;
    public JLabel lbhighest;
    public String highestScore;
    public connection c;
    private int score;
    public MainFrame() {
	add(mainPanel = createMainPanel());
    	setTitle("Pokemon Game");
    	setResizable(false);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setSize(width, height);
	setLocationRelativeTo(null);
	setVisible(true); 
    }
    //Khởi tạo một button
    private JButton createButton(String buttonName) {
	JButton btn = new JButton(buttonName);
	btn.addActionListener(this);
	return btn;
    }
    
    private JPanel createControlPanel() {
        //Lấy điểm cao nhất
        c = new connection();
        highestScore = c.getHighest();
        System.out.printf(highestScore);
        
        //tạo JLabel lblScore với giá trị ban đầu là 0
	lbScore = new JLabel("0");
	progressTime = new JProgressBar(0, 100);
	progressTime.setValue(100);
        lbhighest = new JLabel(highestScore);
        
        //tạo Panel chứa Score và Time
	JPanel panelLeft = new JPanel(new GridLayout(3, 1, 5, 5));
	panelLeft.add(new JLabel("Điểm:"));
	panelLeft.add(new JLabel("Thời gian:"));
        panelLeft.add(new JLabel("Điểm cao nhất:"));
        
        //tạo Panel chứa ô điểm và thanh thời gian
	JPanel panelCenter = new JPanel(new GridLayout(3, 1, 5, 5));
	panelCenter.add(lbScore);
	panelCenter.add(progressTime);
        panelCenter.add(lbhighest);

        //Đặt vị trí cho panelLeft và panelCenter trong panelScoreAndTime
	JPanel panelScoreAndTime = new JPanel(new BorderLayout(5, 0));
	panelScoreAndTime.add(panelLeft, BorderLayout.WEST);
	panelScoreAndTime.add(panelCenter, BorderLayout.CENTER);
       
	// tạo Panel chính chứa panelScoreAndTime và nút New Game
	JPanel panelControl = new JPanel(new BorderLayout(10, 10));
	panelControl.setBorder(new EmptyBorder(10, 3, 5, 3));
	panelControl.add(panelScoreAndTime, BorderLayout.PAGE_START);
	panelControl.add(btnNewGame = createButton("Chơi lại"),BorderLayout.CENTER);
        panelControl.add(btnExit = createButton("Về trang chủ"), BorderLayout.PAGE_END);
        
        // Set BorderLayout để panelControl xuất hiện ở đầu trang
	JPanel panel = new JPanel(new BorderLayout());
	panel.setBorder(new TitledBorder("Good luck"));
	panel.add(panelControl, BorderLayout.PAGE_START);
	return panel;
    }
    
    //Hàm khởi tạo game mới
    public void newGame() {
	time = MAX_TIME;
	graphicsPanel.removeAll();
	mainPanel.add(createGraphicsPanel(), BorderLayout.CENTER);
	mainPanel.validate();
	mainPanel.setVisible(true);
	lbScore.setText("0");
        progressTime.setValue(100);
        c = new connection();
        highestScore = c.getHighest();
        lbhighest.setText(highestScore);
    }
    
    private boolean pause=false;
    private boolean resume= false;

    public boolean isResume() {
        return resume;
    }

    public void setResume(boolean resume) {
        this.resume = resume;
    }
        
    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }   
    
    //Hàm xác nhận chơi lại game hay không
    public void showDialogNewGame(String message, String title, int t) {
	pause=true;
        resume=false;
	int select = JOptionPane.showOptionDialog(null, message, title,
	JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				null, null);
	if (select == 0) {
            pause=false;
            newGame();
	} else {
            if(t==1){
                System.exit(0);
            }else{
                resume=true;
            }
	}
    }  
    
    private JPanel createMainPanel() {
	JPanel panel = new JPanel(new BorderLayout());
	panel.add(createGraphicsPanel(), BorderLayout.CENTER);
        panel.add(createControlPanel(), BorderLayout.EAST);
	return panel;
    }

    private JPanel createGraphicsPanel() {
	graphicsPanel = new ButtonEvent(this, col, row);
	JPanel panel = new JPanel(new GridBagLayout());
	panel.setBackground(Color.gray);
	panel.add(graphicsPanel);
	return panel;
    }
    @Override
    public void actionPerformed(ActionEvent e) {   
        if (e.getSource() == btnNewGame) {
	   showDialogNewGame("Trò chơi vẫn chưa kết thúc. Bạn muốn chơi lại ván mới?", "Thông báo",0);
	}
        if(e.getSource() == btnExit){
            this.setVisible(false);
            Menu m = new Menu();
            m.setVisible(true);
            m.setLocationRelativeTo(null);
        }
    }


    @Override
    public void run() {
        while (true) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		progressTime.setValue((int) ((double) time / MAX_TIME * 100));
	}
    }
}