cmake_minimum_required(VERSION 3.6)

add_library(dessy SHARED dessy.c)

target_link_libraries(dessy android)