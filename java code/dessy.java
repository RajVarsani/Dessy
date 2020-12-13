
// Java program to convert image into vector file 
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

// import jdk.internal.org.objectweb.asm.tree.analysis.Value;

public class dessy {
    public static void main(String args[]) throws IOException {
        Scanner sc = new Scanner(System.in);

        BufferedImage img = null;
        File f = null;
        String inppath;
        int hrdnsOfRslt;
        int r1, g1, b1;

        // taking inputs for image path

        System.out.print("enter path of image: ");
        inppath = sc.nextLine();
        System.out.println();

        // taking input for result intencity

        System.out.print("enter value of hardness of image ( greater than 1 ( multiple of 2 reccomended ) ): ");
        hrdnsOfRslt = sc.nextInt();

        // Taking Input for image Sharpening

        float SharpeningStrength;
        System.out.println("enter value of sharprning strenght (0.5 recommended) : \n");
        SharpeningStrength = sc.nextFloat();

        // Taking Input for image Sharpening repetation

        int SharpeningIndex;
        System.out.println("enter value of how much time you want to do sharpening: \n");
        SharpeningIndex = sc.nextInt();

        // reading image

        try {
            f = new File(inppath);
            img = ImageIO.read(f);
        } catch (IOException e) {
            System.out.println(e);
        }

        // get width and height
        int width = img.getWidth();
        int height = img.getHeight();
        int i = 0;

        // creating rgb array for more effitiency

        int[][][] rgbarr = new int[height][width][3];

        // cooosing an option

        int chice;

        System.out.println("choose how you want to proceed : \n 1.coloured \n 2.grayscale\n");

        chice = sc.nextInt();

        long start = System.currentTimeMillis();

        // taking rgb Value to array

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int color = img.getRGB(x, y);
                rgbarr[y][x][2] = color & 0xff;
                rgbarr[y][x][1] = (color & 0xff00) >> 8;
                rgbarr[y][x][0] = (color & 0xff0000) >> 16;

            }
            // progress checking
            System.out.println(i++);

        }

        i = 0;

        // Sharpening code
        if (SharpeningIndex > 0) {

            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {
                    for (int k = 0; k < SharpeningIndex; k++) {

                        // for red

                        rgbarr[y][x][0] = rgbarr[y][x][0]
                                + (int) (SharpeningStrength * (rgbarr[y][x + 1][0] - rgbarr[y][x - 1][0]));

                        if (rgbarr[y][x][0] > 255) {
                            rgbarr[y][x][0] = 255;
                        }

                        else if (rgbarr[y][x][0] < 0) {
                            rgbarr[y][x][0] = 0;
                        }

                        // for red

                        rgbarr[y][x][1] = rgbarr[y][x][1]
                                + (int) (SharpeningStrength * (rgbarr[y][x + 1][1] - rgbarr[y][x - 1][1]));

                        if (rgbarr[y][x][1] > 255) {
                            rgbarr[y][x][1] = 255;
                        }

                        else if (rgbarr[y][x][1] < 0) {
                            rgbarr[y][x][1] = 0;
                        }

                        // for re1

                        rgbarr[y][x][2] = rgbarr[y][x][2]
                                + (int) (SharpeningStrength * (rgbarr[y][x + 1][2] - rgbarr[y][x - 1][2]));

                        if (rgbarr[y][x][2] > 255) {
                            rgbarr[y][x][2] = 255;
                        }

                        else if (rgbarr[y][x][2] < 0) {
                            rgbarr[y][x][2] = 0;
                        }
                    }

                }

                // progress checking

                System.out.println(i++);
            }
        }

        i = 0;

        // coloured Image

        if (chice == 1) {
            // taking extra rgb values for coloured image tuning

            System.out.println("Enter value of extra rgb respectively for tuning : \n");
            r1 = sc.nextInt();
            System.out.println();
            g1 = sc.nextInt();
            System.out.println();
            b1 = sc.nextInt();
            System.out.println();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (rgbarr[y][x][0] != 255) {
                        rgbarr[y][x][0] = (rgbarr[y][x][0] / hrdnsOfRslt) * hrdnsOfRslt + r1;
                    }
                    if (rgbarr[y][x][1] != 255) {
                        rgbarr[y][x][1] = (rgbarr[y][x][1] / hrdnsOfRslt) * hrdnsOfRslt + g1;
                    }
                    if (rgbarr[y][x][2] != 255) {
                        rgbarr[y][x][2] = (rgbarr[y][x][2] / hrdnsOfRslt) * hrdnsOfRslt + b1;
                    }
                }

                // progress checking

                System.out.println(i++);

            }
        }

        // Grayscale Image

        if (chice == 2) {

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {

                    rgbarr[y][x][0] = rgbarr[y][x][1] = rgbarr[y][x][2] = (rgbarr[y][x][0] + rgbarr[y][x][1]
                            + rgbarr[y][x][2]) / 3;
                    if (rgbarr[y][x][0] != 255) {
                        rgbarr[y][x][0] = (rgbarr[y][x][0] / hrdnsOfRslt) * hrdnsOfRslt;
                    }
                    if (rgbarr[y][x][1] != 255) {
                        rgbarr[y][x][1] = (rgbarr[y][x][1] / hrdnsOfRslt) * hrdnsOfRslt;
                    }
                    if (rgbarr[y][x][2] != 255) {
                        rgbarr[y][x][2] = (rgbarr[y][x][2] / hrdnsOfRslt) * hrdnsOfRslt;
                    }
                }

                // progress checking

                System.out.println(i++);
            }
        }

        i = 0;

        // implementing to Output image

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int color = img.getRGB(x, y);
                int a = 255;

                color = (a << 24) | (rgbarr[y][x][0] << 16) | (rgbarr[y][x][1] << 8) | rgbarr[y][x][2];

                // implementing to image

                img.setRGB(x, y, color);
            }

            // progress checking

            System.out.println(i++);

        }

        long finish = System.currentTimeMillis();

        long timeElapsed = finish - start;

        // write image

        try {
            f = new File("trial outputs/out.jpg");
            ImageIO.write(img, "jpg", f);
            System.out.println("susccesfull image transferred");
            System.out.println("time taken " + timeElapsed + " milisecond");
        } catch (IOException e) {
            System.out.println(e);
        }

        sc.close();

    }
}