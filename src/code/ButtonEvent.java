package code;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import static code.Menu.player;
public class ButtonEvent extends JPanel implements ActionListener {
    private int row;
    private int col;
    private int bound = 2;
    private int size = 50;
    private JButton[][] btn;
    private Controller controller;
    private Color backGroundColor = Color.lightGray;
    private Point p1 = null;
    private Point p2 = null;      
    private PointLine line;
    public int score = 0;   
    private int item;
    private MainFrame frame;
	
    public ButtonEvent(MainFrame frame, int row, int col) {
        this.frame = frame;
	this.row = row;
	this.col = col;   
        item = (row-2) * (col-2) / 2;   
	setLayout(new GridLayout(row-2, col-2, bound, bound));
	setBackground(backGroundColor);
	setPreferredSize(new Dimension((size + bound) * col, (size + bound)* row));
	setBorder(new EmptyBorder(10, 10, 10, 10));
	setAlignmentY(JPanel.CENTER_ALIGNMENT);
	newGame();
    }

    public void newGame() {
	controller = new Controller(this.row, this.col);
	addArrayButton();
    }

    private void addArrayButton() {
	btn = new JButton[row][col];
	for (int i = 1; i < row-1; i++) {
            for (int j = 1; j < col-1; j++) {
		btn[i][j] = createButton(i + "," + j);
		Icon icon = getIcon(controller.getMatrix()[i][j]);
		btn[i][j].setIcon(icon);
		add(btn[i][j]);
            }
	}
    }

    private Icon getIcon(int index) {
    	int width = 48, height = 48;
	Image image = new ImageIcon(getClass().getResource("/icon/icon" + index + ".png")).getImage();
	Icon icon = new ImageIcon(image.getScaledInstance(width, height,image.SCALE_SMOOTH));
	return icon;
    }
    //Hàm tạo ra 1 button
    private JButton createButton(String action) {
	JButton btn = new JButton();
	btn.setActionCommand(action);
	btn.setBorder(null);
	btn.addActionListener(this);
	return btn;
    }
    
    public void execute(Point p1, Point p2) {   //Gọi hàm xóa 2 button hợp lệ (giống nhau)
	System.out.println("delete");
	setDisable(btn[p1.x][p1.y]);
	setDisable(btn[p2.x][p2.y]);
    }

    private void setDisable(JButton btn) {      //Xóa đi các Button được chọn hợp lệ
	btn.setIcon(null);
	btn.setBackground(backGroundColor);
	btn.setEnabled(false);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String btnIndex = e.getActionCommand();
	int indexDot = btnIndex.lastIndexOf(",");
	int x = Integer.parseInt(btnIndex.substring(0, indexDot));
	int y = Integer.parseInt(btnIndex.substring(indexDot + 1,btnIndex.length()));
	if (p1 == null) {
            p1 = new Point(x, y);
	} 
        else{
            p2 = new Point(x, y);
            System.out.println("(" + p1.x + "," + p1.y + ")" + " --> " + "("+ p2.x + "," + p2.y + ")");
            line = controller.checkTwoPoint(p1, p2);
            if (line != null) { 
            	System.out.println("line != null");
            	controller.getMatrix()[p1.x][p1.y] = 0;
		controller.getMatrix()[p2.x][p2.y] = 0;
		controller.showMatrix();
		execute(p1, p2);
		line = null;
		score += 10;
		item--;
		frame.time++;
		frame.lbScore.setText(score + "");
            }
            p1 = null;
            p2 = null;
            System.out.println("done");
            if (item == 0) {
                connection c = new connection();
                c.saveScore(score,player);
        	frame.showDialogNewGame("Bạn đã chiến thắng!\nBạn có muốn chơi lại?", "Chiến thắng",1);
            }
	}
    }
}