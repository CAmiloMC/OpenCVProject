/*
    DESCRIPTION
*/
package finalproject;
//LIBRARIES
import static java.awt.Color.white;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static javafx.scene.paint.Color.color;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.CV_HOUGH_GRADIENT;
import static org.opencv.imgproc.Imgproc.HOUGH_GRADIENT;
import static org.opencv.imgproc.Imgproc.HoughCircles;
//MAIN CLASS
public class FinalProject {
    static int erSize = 5;
    static int diSize = 5;
    static int count = 0;
    
    public static void main(String... args) {
        Mat img = Imgcodecs.imread("D:\\Materias\\Procesamiento de imagenes\\Proyecto\\Captura.JPG");
        //Mat img = Imgcodecs.imread("D:\\Materias\\Procesamiento de imagenes\\Proyecto\\Dice.PNG");
        //Mat img = Imgcodecs.imread("D:\\Materias\\Procesamiento de imagenes\\Proyecto\\Dados.JPG");
        
        //GRAY SCALE
        Mat gray = new Mat(img.rows(), img.cols(), img.type());
        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
        
        
        //EQUIALIZE IMAGE
        Mat Equ = new Mat(img.rows(), img.cols(), img.type());
        
        
        //APPLY NOISE FILTER
        Mat filter = new Mat(img.rows(), img.cols(), img.type());
        Imgproc.medianBlur(gray, filter, 17);//17
        

        //FIND BORDERS
        Mat canny = new Mat(img.rows(), img.cols(), img.type());
        Imgproc.Canny(filter, canny, 190, 190);//100, 100
        
        
        //DETECT CIRCLES
        Mat circles = new Mat();
        Imgproc.HoughCircles(filter, circles, Imgproc.HOUGH_GRADIENT, 1.0, (double)gray.rows()/24, 120.0, 20.0, 1, 30);
        for (int x = 0; x < circles.cols(); x++) {
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            Imgproc.circle(canny, center, 1, new Scalar(0, 100, 100), 3, 8, 0);
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(canny, center, radius, new Scalar(255,0,255), 3, 8, 0);
        }
        
        
        Mat erode = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        Mat dilate = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
        Imgproc.dilate(canny, canny, dilate);
        Imgproc.dilate(canny, canny, dilate);
        Imgproc.erode(canny, canny, erode);
        
        
        //DETECT SQUARES
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        
        
        
        //DISPLAY RESULTS
        System.out.println("Sum of the dice: " + circles.cols());
        System.out.println("Amount of dice: " + contours.size()/10);
        
        
        //SHOW RESULTS
        HighGui.imshow("Image", canny);
        HighGui.waitKey();
        System.exit(0);
    }
    
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    
}//END MAIN CLASS
