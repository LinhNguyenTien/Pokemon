package code;
import static code.Menu.player;

public class Main {
    MainFrame frame;
    class Time extends Thread {

        private int score;
	public void run() {
	while(true){
            try {
		Thread.sleep(1000);
            } catch (InterruptedException e) {
		e.printStackTrace();
            }
            if(frame.isPause()){
                if(frame.isResume()){
                    frame.time--;
                }
            }
            else{
                frame.time--;
            }
            if (frame.time == 0) {
                connection c = new connection();
                c.saveScore(score, player);
		frame.showDialogNewGame("Hết giờ\nBạn có muốn chơi lại?", "Thất bại",1);
		}
            }
	}
    }
    public Main() {
        frame = new MainFrame();
        Time time = new Time();
        time.start();
        new Thread(frame).start();    
    }
}