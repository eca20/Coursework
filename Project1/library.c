/***************************
*
* Name:     Ed Anderson
* Class:    CS 1550 Summer 2018
* Project:  1
* Email:    eca20@pitt.edu
* Program:  library.c - graphics library
*
****************************/

#include <linux/fb.h>
#include <sys/ioctl.h>
#include <sys/mman.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <termios.h>
#include <unistd.h>
#include "graphics.h"

int fd;
color_t * frame_buffer;
color_t * offscreen_buffer;
int size;
struct termios terminal_settings;
struct fb_var_screeninfo var_screen_info;
struct fb_fix_screeninfo fix_screen_info;


/*
* Initialize the Graphics Library by:
* 1.  open the graphics device via the open() syscall using
*     framebuffer via /dev/fb0 (0th framebuffer)
* 2.  memory mapping via mmap() system call
*     use MAP_SHARED parameter so other parts of OS can use frame_buffer
* 3.  Screen size is 640x480 using typedef color type color_t
*      leftmost 0 <-----------> 639 rightmost
*      topmost  0 <-----------> 479 bottomost
*      16 bits for color per pixel:
*                        5 bit   6 bit    5 bit
*                        Red    Green    Blue
*                       00000  000000   00000
*      possible values: 0-31   0 - 63   0-31
* 4.  Use an ANSI escape code to clear screen.
*       Printing the string "\033[2J" (an octal code) to clear Screen
* 5.  ioctl system call to display keys are you type and buffer keypresses
*      - use TCGETS and TCSETS commands with a struct termios
*      - disable canonical mode by unsetting ICANON bit
*      - disabling ECHO by forcing that bit to zero
*/
void init_graphics() {

  fd = open("/dev/fb0", O_RDWR);
  if(fd >= 0){
    if (ioctl(fd, FBIOGET_VSCREENINFO, &var_screen_info) >= 0
	  && ioctl(fd, FBIOGET_FSCREENINFO, &fix_screen_info) >= 0)
    {
      size = var_screen_info.yres_virtual * fix_screen_info.line_length;
    }
  }

  frame_buffer = (color_t *) mmap(NULL, size, PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);

  ioctl(STDIN_FILENO, TCGETS, &terminal_settings);

  terminal_settings.c_lflag &= ~ECHO;
  terminal_settings.c_lflag &= ~ICANON;

  ioctl(STDIN_FILENO, TCSETS, &terminal_settings);
  write(1, CLEAR_SCREEN_SEQ, 8);
}


/*
*  clear the screen by looping over framebuffer and setting all bytes to 0
*/
void clear_screen(void * img) {
  int x, y;
  for(y = 0; y < var_screen_info.yres_virtual; y++){
    for(x = 0; x < fix_screen_info.line_length / 2; x++){
      draw_pixel((color_t*)img, x, y, (color_t) 0);
    }
  }
  write(1, CLEAR_SCREEN_SEQ, 7);
  return;
}


/*
* Exit Graphics does all cleanup before program exits
*   - munmap to undo framebuffer memory mapping
*   - ioctl to reenable keypress echo and buffering
*   - close() syscall
*
*/
void exit_graphics(){
  munmap(frame_buffer, size);
  munmap(offscreen_buffer, size);
  if(ioctl(STDIN_FILENO, TCGETS, &terminal_settings) >= 0){
    terminal_settings.c_lflag |= ECHO;
    terminal_settings.c_lflag |= ICANON;
    ioctl(STDIN_FILENO, TCSETS, &terminal_settings);
  }
  close(fd);
}


/*
* get keypresses by using select system call
* to allow for non-blocking reading of keypresses
*
*/
char getkey(){
  char key;
  fd_set fd_set_var;
  struct timeval tval;
  int read_value = 0;

  tval.tv_sec = 0;
  tval.tv_usec = 0;

  FD_ZERO(&fd_set_var);
  FD_SET(STDIN_FILENO, &fd_set_var);

  read_value = select(STDOUT_FILENO, &fd_set_var, NULL, NULL, &tval);

  if(read_value > 0){
    read(STDIN_FILENO, &key, sizeof(char));
    return key;
  }
  else {
    return ;
  }
}


/*
* use systemcall nanosleep() to make program sleep between frames
* of graphic being drawn
*   - sleep for specified number of milliseconds
      and multiply that by 1,000,000, as ns precision is unnecesary
*/
void sleep_ms(long ms){
  struct timespec timer;
  timer.tv_sec = 0;
  timer.tv_nsec = ms * NS_OFFSET;
  nanosleep(&timer, NULL);
}


/*
*  set pixel at coordinate (x,y) to specified color_t
*  coordinates are used to scale base address of mmapped frame_buffer
*  first row starts at offset 0, followed by 2nd row etc.
*/
void draw_pixel(void *img, int x, int y, color_t color){
  //is x a valid pixel on screen
  if(x < 0 || x > fix_screen_info.line_length / 2){
    return;
  }
  //is y a valid pixel on screen
  if(y < 0 || y > var_screen_info.yres_virtual){
    return;
  }
  //calc offset
  color_t * temp_clr = (color_t *)img + ( y * fix_screen_info.line_length / 2 + x);
  //assign color to pixel
  *temp_clr = color;
}


/*
*  using draw_pixel, draws line from (x1,y1) to (x2,y2) using Bresenhams Algorithm
*/
/* Bresnan's Line Drawing Algorithm from Rosetta Code
*  rosettacode.org accessed on June 9, 2018
*  https://rosettacode.org/wiki/Bitmap/Bresenham%27s_line_algorithm#C
*/
void draw_line(void *img, int x1, int y1, int x2, int y2, color_t c){
  int dx = abs( x2 - x1 ), sx = x1 < x2 ? 1 : -1;
  int dy = abs( y2 - y1 ), sy = y1 < y2 ? 1 : -1;
  int err = ( dx > dy ? dx : - dy) / 2, e2;

  while(1){
    draw_pixel((color_t *)img, x1, y1, c);
    if ( x1 == x2 && y1 == y2) break;
    e2 = err;
    if (e2 >- dx) { err -= dy; x1 += sx; }
    if (e2 <  dy) { err += dx; y1 += sy; }
  }
}


/*
* To give illusion of fluid graphics, double buffering is unsigned
* mmap is used to map memory with parameters:
*  MAP_PRIVATE | MAP_ANONYMOUS and file desc -1
*  On success, returns address of screen sized contiguous block of address space
*/
void * new_offscreen_buffer(){

  offscreen_buffer = (color_t *) mmap(NULL, size, PROT_READ | PROT_WRITE,
    MAP_PRIVATE | MAP_ANONYMOUS, -1, 0);
  return offscreen_buffer;
}


/*
*  memory copy from offscreen buffer to frame buffering
*  copies every byte from source offscreen buffer onto frame buffer
*/
void blit(void *src){
  int x,y;
  color_t * temp_buf;

  for( y = 0; y < var_screen_info.yres_virtual; y++ ){
    for( x = 0; x < fix_screen_info.line_length / 2; x++ ){
      temp_buf = ( color_t * ) src + ( y * fix_screen_info.line_length / 2 + x);
      *( frame_buffer + ( y*fix_screen_info.line_length / 2 + x) ) = *temp_buf;

    }
  }
}
