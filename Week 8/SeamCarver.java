import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import java.awt.Color;
import java.lang.Math;
import edu.princeton.cs.algs4.AcyclicSP;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.DirectedEdge;
import java.util.ArrayList;
import java.util.List;

public class SeamCarver {
   private Picture picture;
   // private EdgeWeightedDigraph G;
   // create a seam carver object based on the given picture
   public SeamCarver(Picture picture) {
      if (picture == null) throw new IllegalArgumentException();
      this.picture = picture;
   }

   // picture picture
   public Picture picture() {
      return this.picture;
   }

   // width of picture picture
   public int width() {
      return this.picture.width();
   }

   // height of picture picture
   public int height() {
      return this.picture.height();
   }

   // energy of pixel at column x and row y
   public double energy(int x, int y) {

      if (x < 0 || x > this.width() - 1 || y < 0 || y > this.height() - 1)
         throw new IllegalArgumentException();
      if (x == 0 || x == this.width() - 1 || y == 0 || y == this.height() - 1)
         return 1000;

      Color colorGrad_xR = picture.get(x+1,y); Color colorGrad_xL = picture.get(x-1,y);
      Color colorGrad_yUp = picture.get(x,y+1); Color colorGrad_yDown = picture.get(x,y-1);
      double R_x = colorGrad_xR.getRed()    - colorGrad_xL.getRed();
      double G_x = colorGrad_xR.getGreen()  - colorGrad_xL.getGreen(); 
      double B_x = colorGrad_xR.getBlue()   - colorGrad_xL.getBlue(); 
      double R_y = colorGrad_yUp.getRed()   - colorGrad_yDown.getRed(); 
      double G_y = colorGrad_yUp.getGreen() - colorGrad_yDown.getGreen(); 
      double B_y = colorGrad_yUp.getBlue()  - colorGrad_yDown.getBlue(); 
      double delta_x = R_x*R_x+G_x*G_x+B_x*B_x;
      double delta_y = R_y*R_y+G_y*G_y+B_y*B_y;

      return Math.sqrt(delta_x+delta_y);
   }

   // sequence of indices for horizontal seam
   public int[] findHorizontalSeam() {

      int W = this.width();
      int H = this.height();
      double[][] energyMatrix = this.toEnergyMatrix();

      EdgeWeightedDigraph G = new EdgeWeightedDigraph(1 + W*H + 1);

      // connect top and bottom rows to auxiliary vertices with edges of weight zero
      for (int i = 0; i < H; i++) {
         G.addEdge(new DirectedEdge(0, i + 1, 1000));
         G.addEdge(new DirectedEdge( H*(W-1) + 1 + i, W*H + 1, 0)); // ??
      }

      for (int w = 1; w < W; w++) {
         for (int i = 0; i < H; i++) {  
            if (((w-1)*H + i + 1) % H == 1) {
               for (int j = 0; j <= 1 ; j++)
                  G.addEdge(new DirectedEdge( (w-1)*H + i + 1, w*H + i + 1 + j, energyMatrix[w][i+j]));     
            }
            else if (((w-1)*H + i + 1) % H == 0) {
               for (int j = -1; j <= 0 ; j++)
                  G.addEdge(new DirectedEdge( (w-1)*H + i + 1, w*H + i + 1 + j, energyMatrix[w][i+j]));     
            }
            else {
               for (int j = -1; j <= 1 ; j++) 
                  G.addEdge(new DirectedEdge( (w-1)*H + i + 1, w*H + i + 1 + j, energyMatrix[w][i+j]));     
            }
         }
      }

      AcyclicSP sp = new AcyclicSP(G, 0);
      List<Integer> list = new ArrayList<>();

      for (DirectedEdge e: sp.pathTo(W*H+1))
          list.add(e.to());

      int[] horizontalSeam = new int[W];

      for (int i = 0; i < W; i++) {
         if (list.get(i)%H == 0)
             horizontalSeam[i] = H - 1;
         else
             horizontalSeam[i] = list.get(i) % H - 1;
      }

      return horizontalSeam;
   }

   // sequence of indices for vertical seam
   public int[] findVerticalSeam() {

      int W = this.width();
      int H = this.height();
      double[][] energyMatrix = this.toEnergyMatrix();

      EdgeWeightedDigraph G = new EdgeWeightedDigraph(1 + W*H + 1);

      // connect top and bottom rows to auxiliary vertices with edges of weight zero
      for (int i = 0; i < W; i++) {
         G.addEdge(new DirectedEdge(0, i + 1, 1000));
         G.addEdge(new DirectedEdge( W*(H-1) + 1 + i, W*H + 1, 0)); // ??
      }

      for (int h = 1; h < H; h++) {
         for (int i = 0; i < W; i++) {  
            if (((h-1)*W + i + 1) % W == 1) {
               for (int j = 0; j <= 1 ; j++)
                  G.addEdge(new DirectedEdge( (h-1)*W + i + 1, h*W + i + 1 + j, energyMatrix[i+j][h]));     
            }
            else if (((h-1)*W + i + 1) % W == 0) {
               for (int j = -1; j <= 0 ; j++)
                  G.addEdge(new DirectedEdge( (h-1)*W + i + 1, h*W + i + 1 + j, energyMatrix[i+j][h]));     
            }
            else {
               for (int j = -1; j <= 1 ; j++) 
                  G.addEdge(new DirectedEdge( (h-1)*W + i + 1, h*W + i + 1 + j, energyMatrix[i+j][h]));     
            }
         }
      }

      AcyclicSP sp = new AcyclicSP(G, 0);
      List<Integer> list = new ArrayList<>();
      for (DirectedEdge e: sp.pathTo(W*H+1))
          list.add(e.to());

      int[] verticalSeam = new int[H];

      for (int i = 0; i < H; i++) {
         if (list.get(i) % W == 0)
             verticalSeam[i] = W - 1;
         else
            verticalSeam[i] = list.get(i) % W-1;
      }

      return verticalSeam;
   }

   public void removeVerticalSeam(int[] seam) {
      
      if(seam == null)
          throw new java.lang.IllegalArgumentException();
      for( int val : seam) {
         if (val <0 || val >= picture.width())
            throw new java.lang.IllegalArgumentException(); 
      }
      
      if ( seam.length != picture.height() )
         throw new java.lang.IllegalArgumentException(); 

      for (int i = 0;i < seam.length-1;i++) {

         if (Math.abs(seam[i]-seam[i+1])>=2)
            throw new java.lang.IllegalArgumentException(); 
      }

      Picture temp = new Picture(picture.width()-1,picture.height());
      int target_slot=0;

      for (int i = 0;i < picture.height();i++) {
         for (int j =0;j < picture.width();j++) {
            if(j!=seam[i]) {
               temp.set(target_slot, i, picture.get(j, i));
               target_slot++;
            }
         }
         target_slot = 0;
      }
      picture = temp;
   }

   public void removeHorizontalSeam(int[] seam) {
      
      if (seam == null)
          throw new java.lang.IllegalArgumentException();

      for( int val : seam) {
         if (val < 0 || val >= picture.height())
            throw new java.lang.IllegalArgumentException();
      }

      if (seam.length != picture.width())
         throw new java.lang.IllegalArgumentException(); 

      for (int i = 0; i < seam.length - 1;i++) {
         if (Math.abs(seam[i]-seam[i+1]) >= 2)
            throw new java.lang.IllegalArgumentException(); 
      }

      Picture temp = new Picture(picture.width(),picture.height()-1);
      int target_slot=0;

      for(int col=0;col<picture.width();col++) {
         for(int row=0;row<picture.height();row++) {
            if(row!=seam[col]) {
               temp.set(col, target_slot, picture.get(col, row));
               target_slot++;
            }
         }
         target_slot = 0;
      }
      picture = temp;
   }

   private double[][] toEnergyMatrix() {

       double[][] returnDouble = new double[this.picture.width()][this.picture.height()];

       for (int i = 0; i < this.width(); i++)
           for (int j = 0; j < this.height(); j++)
               returnDouble[i][j] = this.energy(i, j);
   
       return returnDouble;        
   }

   //  unit testing (optional)
   public static void main(String[] args) {
      // Picture picture = new Picture("6x5.png");
      // SeamCarver sc = new SeamCarver(picture);
      // double[][] energyMatrix = sc.toEnergyMatrix();
//
//      // for (int i = 0; i < sc.height(); i++) {
//      //     for (int j = 0; j < sc.width(); j++)
//      //         StdOut.printf("%.2f ", energyMatrix[j][i]);
//      //      StdOut.println(' ');
      // }
      Picture picture = new Picture("6x5.png");
      SeamCarver sc = new SeamCarver(picture);

      for (int i : sc.findVerticalSeam() ) {
         StdOut.println(i);
      }
      StdOut.println("-------------");
      for (int i : sc.findHorizontalSeam() ) {
         StdOut.println(i);
      }
   }
}
