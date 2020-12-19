#include <jni.h>

JNIEXPORT void JNICALL
Java_com_example_dessy_MainActivity_blackAndWhite(JNIEnv *env, jclass clazz, jintArray pixals,
                                                  jint height, jint width, jint hardness_of_filter,
                                                  jint extar_red, jint extra_green, jint extra_blue,
                                                  jint choiceOfFilter,
                                                  jint extra_brightness_to_add) {
    // TODO: implement blackAndWhite()

    jint *pixels = (*env)->GetIntArrayElements(env, pixals, 0);

    char *colors = (char *) pixels;

    int pixelcount = width * height * 4;

//    if (extra_brightness_to_add >= hardness_of_filter) {
//        extra_brightness_to_add = hardness_of_filter - 1;
//    }
//    if (extar_red >= hardness_of_filter) {
//        extar_red = hardness_of_filter - 1;
//    }
//    if (extra_green >= hardness_of_filter) {
//        extra_green = hardness_of_filter - 1;
//    }
//    if (extra_blue >= hardness_of_filter) {
//        extra_blue = hardness_of_filter - 1;
//    }

    if (choiceOfFilter == 1) {
        for (int i = 0; i < pixelcount; i = i + 4) {

            colors[i] = colors[i + 1] = colors[i + 2] =
                    ((((colors[i] + colors[i + 1] + colors[i + 2]) / 3) / hardness_of_filter) *
                     hardness_of_filter) + extra_brightness_to_add;
            if(colors[i]> 255){
                colors[i]=colors[i+1]=colors[i+2]=255;
            }
        }
    } else {
        //simpliifcation
        for (int i = 0; i < pixelcount; i = i + 4) {

            colors[i] = (colors[i] / hardness_of_filter) * hardness_of_filter + extra_blue;
            if(colors[i]> 255){
                colors[i]=255;
            }

            colors[i + 1] = (colors[i + 1] / hardness_of_filter) * hardness_of_filter + extra_green;
            if(colors[i+1]> 255){
               colors[i+1]=255;
            }

            colors[i + 2] = (colors[i + 2] / hardness_of_filter) * hardness_of_filter + extar_red;
            if(colors[i+2]> 255){
                colors[i+2]=255;
            }
        }
    }

    (*env)->ReleaseIntArrayElements(env, pixals, pixels, 0);

}