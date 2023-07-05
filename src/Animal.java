import java.awt.Color;
import java.io.File;

/**
 * Animal spot detection using cellular automata.
 *
 * @author Takunda Charles Mudima: 23969156
 */

public class Animal {
  private static int height;
  private static int width;
  public static int count = 0;

  /**
   * grey-scaling the picture.
   *
   * @param picture Picture.
   * @param name String.
   * @param gui boolean.
   */
  public static void greyscale(Picture picture, String name, boolean gui) {
    width = picture.width();
    height = picture.height();
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        Color color = picture.get(i, j);
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int finalRgb = (int) (0.299 * r + 0.587 * g + 0.114 * b);
        Color c = new Color(finalRgb, finalRgb, finalRgb);
        picture.set(i, j, c);
      }
    }
    if (gui) {
      picture.show();
    } else {
      picture.save("../out/" + fileName(name) + "_GS.png");
    }
  }

  /**
   * noiseReduction method. Grey-scaled picture is manipulated to reduce noise.
   *
   * @param pic Picture.
   * @param name String.
   * @param gui boolean.
   */
  public static void noiseReduction(Picture pic, String name, boolean gui) {
    int centre = 0;
    int top = 0;
    int bottom = 0;
    int left = 0;
    int right = 0;
    Picture pic1 = new Picture(pic.width(), pic.height());
    for (int i = 0; i < pic.width(); i++) {
      for (int j = 0; j < pic.height(); j++) {
        if ((i != 0 && j != 0) && (j != pic.height() - 1
                && i != pic.width() - 1)) {
          centre = pic.get(i, j).getRGB();
          top = pic.get(i - 1, j).getRGB();
          bottom = pic.get(i + 1, j).getRGB();
          left = pic.get(i, j - 1).getRGB();
          right = pic.get(i, j + 1).getRGB();
          int mode = getMode(left, right, centre, top, bottom);
          Color c1 = new Color(mode);
          pic1.set(i, j, c1);
        } else {
          pic1.set(i, j, pic.get(i, j));
        }
      }
      if (gui) {
        pic1.show();
      } else {
        pic1.save("../out/" + fileName(name) + "_NR.png");
      }
    }
  }

  /**
   * Inherited method from: https://www.tutorialspoint .com/Java-program-
   * to-calculate-mode-in-Java,
   * to obtain my mode values.
   *
   * @param left int.
   * @param right int.
   * @param centre int.
   * @param top int.
   * @param bottom int.
   * @return maxValue.
   */
  private static int getMode(int left, int right, int centre, int top,
                             int bottom) {
    int[] a = {centre, right, left, top, bottom};
    int n = 4;
    int maxValue = 0;
    int maxCount = 0;
    for (int i = 0; i <= n; ++i) {
      int count = 0;
      for (int j = 0; j <= n; ++j) {
        if (a[j] == a[i]) {
          count++;
        }
      }
      if (count > maxCount) {
        maxCount = count;
        maxValue = a[i];
      }
    }
    return maxValue;
  }

  /**
   * Method for edgeDetection. new picture, pic2 is created and saved.
   *
   * @param picture Picture.
   * @param epsilon int.
   * @param name String.
   * @param gui boolean.
   */
  private static void edgeDetection(Picture picture, int epsilon, String name,
                                    boolean gui) {
    Picture pic2 = new Picture(picture.width(), picture.height());
    int centre = 0;
    int top = 0;
    int bottom = 0;
    int left = 0;
    int right = 0;
    for (int i = 0; i < picture.width(); i++) {
      for (int j = 0; j < picture.height(); j++) {
        if ((i == 0 && j == 0) && (j == picture.height() - 1 && i
                == picture.width() - 1)) {
          pic2.set(i, j, Color.black);
        }
        if ((i != 0 && j != 0) && (j != picture.height() - 1 && i
                != picture.width() - 1)) {

          centre = picture.get(i, j).getRed();
          top = picture.get(i - 1, j).getRed();
          bottom = picture.get(i + 1, j).getRed();
          left = picture.get(i, j - 1).getRed();
          right = picture.get(i, j + 1).getRed();

          if ((Math.abs(centre - top)) < epsilon
                  && (Math.abs(centre - bottom)) < epsilon
                  && (Math.abs(centre - left)) < epsilon
                  && (Math.abs(centre - right)) < epsilon) {
            pic2.set(i, j, Color.black);
          } else {
            pic2.set(i, j, Color.white);
          }
        }
      }
      if (gui) {
        pic2.show();
      } else {
        pic2.save("../out/" + fileName(name) + "_ED.png");
      }
    }
  }

  /**
   * Spot mask creation.
   *
   * @param radius int.
   * @param width int.
   * @param delta double.
   * @return spot mask.
   */
  public static int[][] createSpotMask(int radius, int width, double delta) {
    int[][] xx = new int[2 * radius + 1][2 * radius + 1];
    int[][] yy = new int[2 * radius + 1][2 * radius + 1];
    double[][] circle = new double[2 * radius + 1][2 * radius + 1];
    boolean[][] donut = new boolean[2 * radius + 1][2 * radius + 1];
    int[][] mask = new int[2 * radius + 1][2 * radius + 1];
    for (int i = 0; i < 2 * radius + 1; i++) {
      for (int j = 0; j < 2 * radius + 1; j++) {
        xx[i][j] = i;
        yy[i][j] = j;
      }
    }
    for (int k = 0; k < circle.length; k++) {
      for (int l = 0; l < circle[k].length; l++) {
        circle[k][l] = (Math.pow((xx[k][l] - radius), 2))
                + (Math.pow((yy[k][l] - radius), 2));
        donut[k][l] =
                (circle[k][l] < ((radius - delta) * (radius - delta) + width)
                        && (circle[k][l] > (radius - delta)
                        * (radius - delta) - width));
        if (donut[k][l]) {
          mask[k][l] = 255;
        } else {
          mask[k][l] = 0;
        }
      }
    }
    return mask;
  }

  /**
   * Find spots aims to identify the spots from the edge detected picture.
   *
   * @param edgePic [][] int
   * @param spot Picture
   * @param mask [][] int
   * @param radius [] int
   * @param difference [] int
   * @param name String
   * @return number of spots
   */

  public static int findSpots(
          int[][] edgePic, Picture spot, int[][] mask, int[] radius,
          int[] difference, String name) {
    int sum = 0;
    int maxValue = 0;
    int[][] imageBlock = new int[mask.length][mask[0].length];
    for (int x = 0; x < edgePic.length; x++) {
      for (int y = 0; y < edgePic[0].length; y++) {
        if ((x != 0 && y != 0) && (y != edgePic[0].length - 1 && x
                != edgePic.length - 1)) {
          for (int x1 = 0; x1 < imageBlock.length; x1++) {
            for (int y1 = 0; y1 < imageBlock[0].length; y1++) {
              imageBlock[x1][y1] = edgePic[x][y];
            }
          }

          maxValue = findMax(imageBlock);
          if (maxValue == 0) {
            spot.set(x, y, Color.black);
          } else {
            sum = sumImageblock(imageBlock, mask, maxValue);

            if (radius[0] == 4 && (sum <= difference[0])) {
              count = count + 1;
              spot.set(x, y, Color.white);
            } else if (radius[1] == 5 && (sum <= difference[1])) {
              count = count + 1;
              spot.set(x, y, Color.white);
            } else if (radius[2] == 6 && (sum <= difference[2])) {
              count = count + 1;
              spot.set(x, y, Color.white);
            } else if (radius[3] == 7 && (sum <= difference[3])) {
              count = count + 1;
              spot.set(x, y, Color.white);
            } else if (radius[4] == 8 && (sum <= difference[4])) {
              count = count + 1;
              spot.set(x, y, Color.white);
            } else if (radius[5] == 9 && (sum <= difference[5])) {
              count = count + 1;
              spot.set(x, y, Color.white);
            } else if (radius[6] == 10 && (sum <= difference[6])) {
              count = count + 1;
              spot.set(x, y, Color.white);
            } else if (radius[7] == 11 && (sum <= difference[7])) {
              count = count + 1;
              spot.set(x, y, Color.white);
            }
            sum = 0;
          }
        }
      }
    }
    System.out.println(count);
    spot.save("../out/" + fileName(name) + "_SD.png");
    return count;
  }

  /**
   * Summation of image block.
   *
   * @param imageBlock [][] int
   * @param mask [][] int
   * @param maxVal int
   * @return sum
   */
  public static int sumImageblock(int[][] imageBlock, int[][] mask,
                                  int maxVal) {
    int sum = 0;

    for (int i = 0; i < imageBlock.length; i++) {
      for (int j = 0; j < imageBlock[0].length; j++) {
        normalizeImageBlock(imageBlock, maxVal);
        sum += (Math.abs(imageBlock[i][j] - mask[i][j]));
      }
    }

    return sum;
  }

  /** Normalize the image block.
   *
   * @param block [][] int
   * @param max int
   * @return normalized image block
   */
  public static int[][] normalizeImageBlock(int[][] block, int max) {
    for (int i = 0; i < block.length; i++) {
      for (int j = 0; j < block[0].length; j++) {
        block[i][j] = block[i][j] * (255 / max);
      }
    }
    return block;
  }

  /**
   * looking for the maximum value from image block.
   *
   * @param block [][] int
   * @return maximum value
   */
  public static int findMax(int[][] block) {
    int maxVal = block[0][0];
    for (int j = 0; j < block.length; j++) {
      for (int i = 0; i < block[j].length; i++) {
        if (block[j][i] > maxVal) {
          maxVal = block[j][i];
        }
      }
    }
    return maxVal;
  }

  /**
   * Changes the edge detected picture to an integer array.
   *
   * @param edge Picture
   * @return Integer 2d array.
   */
  public static int[][] pictureToInt(Picture edge) {
    int[][] picture = new int[edge.width()][edge.height()];
    for (int i = 0; i < edge.width(); i++) {
      for (int j = 0; j < edge.height(); j++) {
        picture[i][j] = edge.get(i, j).getRed();
      }
    }
    return picture;
  }
  /**
   * fileName obtains the name of the file.
   *
   * @param name String.
   * @return filename
   */

  public static String fileName(String name) {
    String[] fileName = name.split("/");
    int d = fileName[fileName.length - 1].indexOf(".");
    return fileName[fileName.length - 1].substring(0, d);
  }

  /**
   * main method.
   *
   * @param args filename and mode.
   */
  public static void main(String[] args) {
    if (args.length < 2) {
      System.err.println("ERROR: invalid number of arguments ");
      System.exit(0);
    }
    int mode = Integer.parseInt(args[0]);
    if (mode == 0 || mode == 1 || mode == 2 || mode == 3) {
      File file = new File(args[1]);
      String filename = file.toString();
      Picture picture = new Picture(filename);
      height = picture.height();
      width = picture.width();
      boolean noGui = false;
      if (mode == 0) {
        greyscale(picture, args[1], noGui);

      } else if (mode == 1) {
        greyscale(picture, args[1], noGui);
        Picture greyPic = new Picture("../out/" + fileName(args[1])
                + "_GS.png");
        noiseReduction(greyPic, args[1], noGui);

      } else if (mode == 2) {
        greyscale(picture, args[1], noGui);
        Picture greyPic = new Picture("../out/" + fileName(args[1])
                + "_GS.png");
        noiseReduction(greyPic, args[1], noGui);
        if (args.length < 3) {

          System.err.println("ERROR: invalid number of arguments");
          System.exit(0);
        }

        int epsilon = Integer.parseInt(args[2]);
        if (epsilon < 0 || epsilon > 255) {
          System.err.println("ERROR: Invalid epsilon value");
          System.exit(0);
        }
        Picture noiseReduction = new Picture("../out/"
                + fileName(args[1]) + "_NR" + ".png");

        edgeDetection(noiseReduction, epsilon, args[1], noGui);
      } else if (mode == 3) {

        int epsilon = Integer.parseInt(args[2]);
        if (epsilon < 0 || epsilon > 255) {
          System.err.println("ERROR: Invalid epsilon value");
          System.exit(0);
        }
        int lowerLimit = Integer.parseInt(args[3]);
        int upperLimit = Integer.parseInt(args[4]);
        int[] radius = {4, 5, 6, 7, 8, 9, 10, 11};
        int[] delta = {0, 1, 1, 1, 1, 1, 2, 2};
        int[] difference = {4800, 6625, 11000, 15000, 19000, 23000, 28000,
                35000};
        int[] width = {6, 9, 12, 15, 18, 21, 24, 27};
        int low = 0;
        int high = 0;
        for (int i = 0; i < radius.length; i++) {
          if (lowerLimit == radius[i]) {
            low = i;
          }
        }
        for (int i = 0; i < radius.length; i++) {
          if ((upperLimit == radius[i])) {
            high = i;
          }
        }
        greyscale(picture, args[1], noGui);
        Picture greyPic = new Picture("../out/" + fileName(args[1])
                + "_GS.png");
        noiseReduction(greyPic, args[1], noGui);
        Picture noiseReduction = new Picture("../out/"
                + fileName(args[1]) + "_NR" + ".png");
        edgeDetection(noiseReduction, epsilon, args[1], noGui);
        Picture edgePic = new Picture("../out/" + fileName(args[1])
                + "_ED" + ".png");
        int[][] picInt = pictureToInt(edgePic);
        Picture spotDetection = new Picture(edgePic.width(), edgePic.height());

        for (int i = low; i <= high; i++) {
          findSpots(
                  picInt,
                  spotDetection,
                  createSpotMask(radius[i], width[i], delta[i]),
                  radius,
                  difference,
                  args[1]);
        }
      }
    } else if (mode == 6) {
      File file = new File(args[1]);
      int version = Integer.parseInt(args[2]);
      String filename = file.toString();
      Picture picture = new Picture(filename);
      boolean gui = true;
      boolean noGui = false;
      if (version == 0) {
        greyscale(picture, args[1], gui);
      } else if (version == 1) {
        greyscale(picture, args[1], noGui);
        Picture greyPic = new Picture("../out/" + fileName(args[1])
                + "_GS.png");
        noiseReduction(greyPic, args[1], gui);
      } else if (version == 2) {
        greyscale(picture, args[1], noGui);
        Picture greyPic = new Picture("../out/" + fileName(args[1])
                + "_GS.png");
        noiseReduction(greyPic, args[1], noGui);
        if (args.length < 3) {

          System.err.println("ERROR: invalid number of arguments");
          System.exit(0);
        }

        int epsilon = Integer.parseInt(args[3]);
        if (epsilon < 0 || epsilon > 255) {
          System.err.println("ERROR: Invalid epsilon value");
          System.exit(0);
        }
        Picture noiseReduction = new Picture("../out/"
                + fileName(args[1]) + "_NR" + ".png");

        edgeDetection(noiseReduction, epsilon, args[1], gui);
      }

    } else {
      System.err.println("ERROR: invalid mode");
      System.exit(0);
    }
  }
}
