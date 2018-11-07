/*
    This is a project than use the OpenCV library.
    The objective is identifier on a image the dice amount
    and the sum of this.
*/

/**
 * @author Camilo Andres Montenegro C.
 */

package finalproject;
//OpenCV Libraries used
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

//Main class
public class FinalProject {
    static int erSize = 5;
    static int diSize = 5;
    static int count = 0;
    
    public static void main(String... args) {
        //Load image
        Mat img = Imgcodecs.imread("D:\\Materias\\Procesamiento de imagenes\\Proyecto\\Captura.JPG");
        
        
        //Gray scale converting
        Mat gray = new Mat(img.rows(), img.cols(), img.type());
        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
        
        
        //Equalize image
        Mat Equ = new Mat(img.rows(), img.cols(), img.type());
        
        
        //Noise filter apply
        Mat filter = new Mat(img.rows(), img.cols(), img.type());
        Imgproc.medianBlur(gray, filter, 17);//17
        

        //Find edges
        Mat canny = new Mat(img.rows(), img.cols(), img.type());
        Imgproc.Canny(filter, canny, 190, 190);//100, 100
        
        
        //Detect Circles
        Mat circles = new Mat();
        Imgproc.HoughCircles(filter, circles, Imgproc.HOUGH_GRADIENT, 1.0, (double)gray.rows()/24, 120.0, 20.0, 1, 30);
        for (int x = 0; x < circles.cols(); x++) {
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            Imgproc.circle(canny, center, 1, new Scalar(0, 100, 100), 3, 8, 0);
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(canny, center, radius, new Scalar(255,0,255), 3, 8, 0);
        }
        
        
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
    
}//End main class
