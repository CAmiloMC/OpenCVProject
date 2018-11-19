/*
    This is a project than use the OpenCV library.
    The objective is identifier on a image the dice amount
    and the sum of this.
*/

/**
 * @author Camilo Andres Montenegro C.
 */

//Package
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
    static int erSize = 4;
    static double resize = 0;
    
    public static void main(String... args) {
        //Load image
        Mat img = Imgcodecs.imread("D:\\Materias\\Procesamiento de imagenes\\Proyecto\\d0.JPG");
        //Mat img = Imgcodecs.imread("D:\\Materias\\Procesamiento de imagenes\\Proyecto\\d1.PNG");
        //Mat img = Imgcodecs.imread("D:\\Materias\\Procesamiento de imagenes\\Proyecto\\d2.PNG");
        //Mat img = Imgcodecs.imread("D:\\Materias\\Procesamiento de imagenes\\Proyecto\\d3.PNG");
        //Mat img = Imgcodecs.imread("D:\\Materias\\Procesamiento de imagenes\\Proyecto\\d4.PNG");//FALTA
        //Mat img = Imgcodecs.imread("D:\\Materias\\Procesamiento de imagenes\\Proyecto\\d5.PNG");
        //Mat img = Imgcodecs.imread("D:\\Materias\\Procesamiento de imagenes\\Proyecto\\d6.PNG");
        
        
        //Verify sizes
        if((img.cols() + img.rows()) < 4000 && (img.cols() + img.rows()) > 2000)
            Imgproc.resize(img, img, new Size(1302,781));
        if(img.cols() > 927 && img.rows() > 879)
            Imgproc.resize(img, img, new Size(528,840));
        
        
        //Gray scale converting
        Mat gray = new Mat(img.rows(), img.cols(), img.type());
        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
        
        
        //Equalize image
        //Mat Equ = new Mat(img.rows(), img.cols(), img.type());
        
        
        //Noise filter apply
        Mat filter = new Mat(img.rows(), img.cols(), img.type());
        Imgproc.medianBlur(gray, filter, 21);
        
        
        //Find edges
        Mat canny = new Mat(img.rows(), img.cols(), img.type());
        Imgproc.Canny(filter, canny, 180, 180);
        
        
        //Morphology
        Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2*erSize + 1, 2*erSize + 1));
        Imgproc.dilate(canny, canny, element1);

        
        //Detect Circles
        Mat circles = new Mat();
        Imgproc.HoughCircles(filter, circles, Imgproc.HOUGH_GRADIENT, 1.0, (double)gray.rows()/24, 120, 20, 1, 40);
        for (int x = 0; x < circles.cols(); x++) {
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            Imgproc.circle(canny, center, 1, new Scalar(0, 100, 100), 3, 8, 0);
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(canny, center, radius, new Scalar(255,0,255), 3, 8, 0);
        }
        
        
        //Detect contourns
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(canny, contours, new  Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        
        
        //Display results
        int sums = circles.cols();
        int dice = contours.size();
        System.out.println("Sum of values: " + sums);
        System.out.println("Number of dice: " + dice);
        
        
        //Show results
        HighGui.imshow("Image", canny);
        HighGui.waitKey();
        System.exit(0);
    }
    
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    
}//End main class
