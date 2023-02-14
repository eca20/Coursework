/***************************
*
* Name:     Ed Anderson
* Class:    CS 1550 Summer 2018
* Project:  1
* Email:    eca20@pitt.edu
* Program:  graphics.h - header file
*
****************************/


/*
* typedefs
*/
typedef unsigned short int color_t;

/*
* prototypes
*/
void init_graphics();
void exit_graphics();
char getkey();
void sleep_ms(long);
void clear_screen(void *);
void draw_pixel(void*, int, int, color_t);
void draw_line(void *, int, int, int, int, color_t);
void * new_offscreen_buffer();
void blit(void *);

/*
* macros
*/
#define RGB(r, g, b) ((color_t) ((r << 11) + ((g & 0x003f) << 5) + (b & 0x001f)))
#define NS_OFFSET 1000000
#define CLEAR_SCREEN_SEQ "\033[2J"
