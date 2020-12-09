
// Java program to convert image into vector file 
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class tryjava {
    public static void main(String args[]) throws IOException {
        Scanner sc = new Scanner(System.in);

        BufferedImage img = null;
        File f = null;
        String inppath;
        int hrdnsOfRslt;
        int r1, g1, b1;

        // taking inputs

        System.out.print("enter path of image: ");
        inppath = sc.nextLine();
        System.out.println();
        System.out.print("enter value of hardness of image ( greater than 1 ( multiple of 2 reccomended ) ): ");
        hrdnsOfRslt = sc.nextInt();
        System.out.println("enter value of extra rgb respectively : \n");
        r1 = sc.nextInt();
        System.err.println();
        g1 = sc.nextInt();
        System.err.println();
        b1 = sc.nextInt();

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

        // creating rgb array

        int[][][] rgbarr = new int[height][width][3];

        // // cooosing an option
        // int chice;
        // chice = sc.nextInt();
        long start = System.currentTimeMillis();

        // if (chice == 1) {

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {

                    int color = img.getRGB(x, y);
                    int a = 255;
                    int b = color & 0xff;
                    int g = (color & 0xff00) >> 8;
                    int r = (color & 0xff0000) >> 16;

                    // main operation begins
                    // arrayma save krta pela devide kari nakhyu. nahitar array ma value change
                    // karvama
                    // time jay
                    // aa rgb array n hoy to pan chale pan p6i koi feature add karvu hoy to pa6i aki
                    // process karvi nahi

                    // // exception handling for 256
                    // km k value ghati jay atle dim pan thay jay photo a o6u karvu pade

                    if (r == 256) {
                        rgbarr[y][x][0] = (r / hrdnsOfRslt) * hrdnsOfRslt;
                    } else {
                        rgbarr[y][x][0] = (r / hrdnsOfRslt) * hrdnsOfRslt + r1;
                    }

                    if (g == 256) {
                        rgbarr[y][x][1] = (g / hrdnsOfRslt) * hrdnsOfRslt;
                    } else {
                        rgbarr[y][x][1] = (g / hrdnsOfRslt) * hrdnsOfRslt + g1;
                    }

                    if (g == 256) {
                        rgbarr[y][x][2] = (b / hrdnsOfRslt) * hrdnsOfRslt;
                    } else {
                        rgbarr[y][x][2] = (b / hrdnsOfRslt) * hrdnsOfRslt + b1;
                    }

                    // making colour

                    color = (a << 24) | (rgbarr[y][x][0] << 16) | (rgbarr[y][x][1] << 8) | rgbarr[y][x][2];

                    // implementing to image

                    img.setRGB(x, y, color);
                }

                // progress check karva

                System.out.println(i++);

            }
        // }
        // if (chice == 2) {

        //     for (int y = 0; y < height; y++) {
        //         for (int x = 0; x < width; x++) {

        //             int color = img.getRGB(x, y);
        //             int a = 255;
        //             int b = color & 0xff;
        //             int g = (color & 0xff00) >> 8;
        //             int r = (color & 0xff0000) >> 16;

        //             // main operation begins
        //             // arrayma save krta pela devide kari nakhyu. nahitar array ma value change
        //             // karvama
        //             // time jay
        //             // aa rgb array n hoy to pan chale pan p6i koi feature add karvu hoy to pa6i aki
        //             // process karvi nahi

        //             // // exception handling for 256
        //             // km k value ghati jay atle dim pan thay jay photo a o6u karvu pade

        //             int clr;
        //             clr = (r + g + b) / 3;
        //             rgbarr[y][x][0] = rgbarr[y][x][1] = rgbarr[y][x][2] = clr;
        //             // making colour

        //             color = (a << 24) | (rgbarr[y][x][0] << 16) | (rgbarr[y][x][1] << 8) | rgbarr[y][x][2];

        //             // implementing to image

        //             img.setRGB(x, y, color);
        //         }

        //         // progress check karva

        //         System.out.println(i++);

        //     }
        // }

        long finish = System.currentTimeMillis();

        long timeElapsed = finish - start;

        // write image

        try {
            f = new File("out.jpg");
            ImageIO.write(img, "jpg", f);
            System.out.println("susccesfull image transferred");
            System.out.println("time taken " + timeElapsed + " milisecond");
        } catch (IOException e) {
            System.out.println(e);
        }

        sc.close();

    }
}