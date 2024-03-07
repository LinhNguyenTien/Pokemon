package code;
import java.util.ArrayList;
import java.util.Random;

public class Controller {
    private int row;
    private int col;
    private int[][] matrix;     //ma trận 10x10
    public Controller(int row, int col) {
        this.row = row;
        this.col = col;
        System.out.println(row + "," + col);
        createMatrix();
        showMatrix();
    }
    public void showMatrix() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.printf("%3d", matrix[i][j]);
            }
            System.out.println();
        }
    }
    private void createMatrix() {
	matrix = new int[row][col];
	Random rand = new Random();
	int imgCount = 16;          //Số lượng hình ảnh trong trò chơi
	int max = 4;                //Số lần xuất hiện tối đa của 1 hình
	int arr[] = new int[imgCount + 1];
	ArrayList<Point> listPoint = new ArrayList<Point>(); //x y
	for (int i = 1; i < row-1; i++) {
            for (int j = 1; j < col-1; j++) {
		listPoint.add(new Point(i, j)); //size: 64
            }
        }
        //Ma trận 10x10 sau khi khởi tạo viền số 0 bao quanh ma trận trở thành 8x8
        for(int i=0; i<row; i++){
            for(int j=0; j<col; j++){
                matrix[i][j]=0;
            }
        }
	int i = 0;
        do {
            int index = rand.nextInt(imgCount) + 1;     //Chọn 1 pokemon ngẫu nhiên
            if (arr[index] < max) {                          //Kiểm tra số lần xuất hiện
                arr[index] += 2;
                for (int j = 0; j < 2; j++) {                //Đặt 1 pokemon 2 lần để tạo thành 1 cặp
                    int size = listPoint.size();             //Lấy kích thước mảng (tối đa 64)
                    int pointIndex = rand.nextInt(size);//tìm 1 vị trí ngẫu nhiên trong MT để đặt pokemon
                    matrix[listPoint.get(pointIndex).x][listPoint.get(pointIndex).y] = index;
                    listPoint.remove(pointIndex);       //giảm kích thước mảng đi 1 => còn 63
                }
                i++;
            }
        } while (i < (row-2) * (col-2) / 2);    //thực hiện 64/2=32 lần
    }
    
    public int[][] getMatrix() {
	return matrix;
    }

    public void setMatrix(int[][] matrix) {
	this.matrix = matrix;
    }
    
    private boolean checkLineX(int y1, int y2, int x) {
        System.out.println("check line x");
        //Tìm điểm nhỏ hơn và lớn hơn giữa 2 điểm
        int min = Math.min(y1, y2);
        int max = Math.max(y1, y2);
        // Duyệt qua đướng đi
        for (int y = min + 1; y < max; y++) {
            if (matrix[x][y] != 0) {            //Nếu có 1 pokemon khác trên đường => thất bại
                System.out.println("die: " + x + "" + y);
                return false;
            }
            System.out.println("ok: " + x + "" + y);
        }
       
        //Không có pokemon nào trên đường => thành công
        return true;
}

    private boolean checkLineY(int x1, int x2, int y) {
        System.out.println("check line y");
        int min = Math.min(x1, x2);
        int max = Math.max(x1, x2);
        for (int x = min + 1; x < max; x++) {
            if (matrix[x][y] != 0) {
                System.out.println("die: " + x + "" + y);
                return false;
            }
            System.out.println("ok: " + x + "" + y);
        }
        return true;
    }
    
    private boolean checkRectX(Point p1, Point p2) {
        System.out.println("check rect x");
        // tìm điểm có y nhỏ hơn giữa 2 điểm
        Point pMinY = p1, pMaxY = p2;
        if (p1.y > p2.y) {
            pMinY = p2;
            pMaxY = p1;
        }
        for (int y = pMinY.y; y <= pMaxY.y; y++) {
            if (y > pMinY.y && matrix[pMinY.x][y] != 0) {
                return false;
            }
            // kiểm tra 2 đường ngang và dọc có trống đồng thời không theo từng giá trị y
            if ((matrix[pMaxY.x][y] == 0)
                    && checkLineY(pMinY.x, pMaxY.x, y)
                    && checkLineX(y, pMaxY.y, pMaxY.x)) {

                System.out.println("Rect x");
                System.out.println("(" + pMinY.x + "," + pMinY.y + ") -> ("
                        + pMinY.x + "," + y + ") -> (" + pMaxY.x + "," + y
                        + ") -> (" + pMaxY.x + "," + pMaxY.y + ")");
                // Tìm được thì trả về true
                return true;
            }
        }
        // Không tìm được thì trả về false
        return false;
    }

    private boolean checkRectY(Point p1, Point p2) {
        System.out.println("check rect y");
        Point pMinX = p1, pMaxX = p2;
        if (p1.x > p2.x) {
            pMinX = p2;
            pMaxX = p1;
        }
        
        for (int x = pMinX.x; x <= pMaxX.x; x++) {
            if (x > pMinX.x && matrix[x][pMinX.y] != 0) {
                return false;
            }
            if ((matrix[x][pMaxX.y] == 0)
                    && checkLineX(pMinX.y, pMaxX.y, x)
                    && checkLineY(x, pMaxX.x, pMaxX.y)) {

                System.out.println("Rect y");
                System.out.println("(" + pMinX.x + "," + pMinX.y + ") -> (" + x
                        + "," + pMinX.y + ") -> (" + x + "," + pMaxX.y
                        + ") -> (" + pMaxX.x + "," + pMaxX.y + ")");
                return true;
            }
        }
        return false;
    }
    
    private boolean checkMoreLineX(Point p1, Point p2, int type) {
        System.out.println("check chec more x");
        // tìm điểm có y nhỏ hơn
        Point pMinY = p1, pMaxY = p2;
        if (p1.y > p2.y) {
            pMinY = p2;
            pMaxY = p1;
        }
        // tìm đường đi
        int y = pMaxY.y + type;
        int row = pMinY.x;
        int colFinish = pMaxY.y;
        if (type == -1) {
            colFinish = pMinY.y;
            y = pMinY.y + type;
            row = pMaxY.x;
            System.out.println("colFinish = " + colFinish);
        }

        // tìm cột cuối cùng trong hàng
        // check more
        if ((matrix[row][colFinish] == 0 || pMinY.y == pMaxY.y)
                && checkLineX(pMinY.y, pMaxY.y, row)) {
            while (matrix[pMinY.x][y] == 0
                    && matrix[pMaxY.x][y] == 0) {
                if (checkLineY(pMinY.x, pMaxY.x, y)) {

                    System.out.println("TH X " + type);
                    System.out.println("(" + pMinY.x + "," + pMinY.y + ") -> ("
                            + pMinY.x + "," + y + ") -> (" + pMaxY.x + "," + y
                            + ") -> (" + pMaxY.x + "," + pMaxY.y + ")");
                    return true;
                }
                y += type;
            }
        }
        return false;
    }

    private boolean checkMoreLineY(Point p1, Point p2, int type) {
        System.out.println("check more y");
        Point pMinX = p1, pMaxX = p2;
        if (p1.x > p2.x) {
            pMinX = p2;
            pMaxX = p1;
        }
        int x = pMaxX.x + type;
        int col = pMinX.y;
        int rowFinish = pMaxX.x;
        if (type == -1) {
            rowFinish = pMinX.x;
            x = pMinX.x + type;
            col = pMaxX.y;
        }
        if ((matrix[rowFinish][col] == 0|| pMinX.x == pMaxX.x)
                && checkLineY(pMinX.x, pMaxX.x, col)) {
            while (matrix[x][pMinX.y] == 0
                    && matrix[x][pMaxX.y] == 0) {
                if (checkLineX(pMinX.y, pMaxX.y, x)) {
                    System.out.println("TH Y " + type);
                    System.out.println("(" + pMinX.x + "," + pMinX.y + ") -> ("
                            + x + "," + pMinX.y + ") -> (" + x + "," + pMaxX.y
                            + ") -> (" + pMaxX.x + "," + pMaxX.y + ")");
                    return true;
                }
                x += type;
            }
        }
        return false;
    }
    
    public PointLine checkTwoPoint(Point p1, Point p2) {
        if ((p1.x!=p2.x || p1.y!=p2.y) && matrix[p1.x][p1.y] == matrix[p2.x][p2.y]) {
            // kiểm tra hàng ngang
            if (p1.x == p2.x) {
                System.out.println("line x");
                if (checkLineX(p1.y, p2.y, p1.x)) {
                    return new PointLine(p1, p2);
                }
            }
            // kiểm tra hàng dọc
            if (p1.y == p2.y) {
                System.out.println("line y");
                if (checkLineY(p1.x, p2.x, p1.y)) {
                    System.out.println("ok line y");
                    return new PointLine(p1, p2);
                }
            }
            // kiểm tra 2 điểm theo hình chữ nhật X
            if ( checkRectX(p1, p2)) {
                System.out.println("rect x");
                return new PointLine(p1, p2);
            }
            // kiểm tra 2 điểm theo hình chữ nhật Y
            if (checkRectY(p1, p2)) {
                System.out.println("rect y");
                return new PointLine(p1, p2);
            }
            // check more right
            if (checkMoreLineX(p1, p2, 1)) {
                System.out.println("more right");
                return new PointLine(p1, p2);
            }
            // check more left
            if (checkMoreLineX(p1, p2, -1)) {
                System.out.println("more left");
                return new PointLine(p1, p2);
            }
            // check more down
            if (checkMoreLineY(p1, p2, 1)) {
                System.out.println("more down");
                return new PointLine(p1, p2);
            }
            // check more up
            if (checkMoreLineY(p1, p2, -1)) {
                System.out.println("more up");
                return new PointLine(p1, p2);
            }
        }
        return null;
    }
}