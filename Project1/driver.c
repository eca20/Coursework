/***************************
*
* Name:     Ed Anderson
* Class:    CS 1550 Summer 2018
* Project:  1
* Email:    eca20@pitt.edu
* Program:  driver.c - simple driver program that
*           shows different lines being drawn in red, green, or blue
*
* Use:      Pressing R, G, and B in ay order will draw an A on the screen
*           using three hard coded lines. NOTE: Repeating any letter (R,G,B)
*           will overwrite the current drawn line, and thus make you unable
*           to see the A drawn. After three draw attempts, the screen clears.
*
****************************/

#include "library.c"

int main(int argc, char **argv){

  // init graphics library
  init_graphics();

  // init new off_screen_buffer
  // init length of line to 15
  void *buf = new_offscreen_buffer();
  char key;
  color_t color = RGB(0, 0, 0); //set desired color parameter
  puts("Type R, G, and B to calculate grade. C to clear. Q to Quit.");
  do {
    key = getkey();
    if(key == 'q'){
      clear_screen(buf);
      blit(buf);
      break;
    }
    else if (key == 'r'){
        color = RGB(31, 0, 0);
        draw_line(buf, 320, 10, 280, 100, color);
	      blit(buf);
        puts("Type R, G, and B to calculate grade. C to clear. Q to Quit.");
    }
    else if (key == 'g'){
        color = RGB(0, 63, 0);
        draw_line(buf, 320, 10, 360, 100, color);
        blit(buf);
        puts("Type R, G, and B to calculate grade. C to clear. Q to Quit.");
    }
    else if (key == 'b'){
        color = RGB(0, 0, 31);
        draw_line(buf, 300, 58, 340, 58, color);
        blit(buf);
        puts("Type R, G, and B to calculate grade. C to clear. Q to Quit.");
    }
    else if (key == 'c'){
        clear_screen(buf);
        blit(buf);
        puts("Type R, G, and B to calculate grade. C to clear. Q to Quit.");

    }
  } while(1);
  exit_graphics();
  return 0;
}
