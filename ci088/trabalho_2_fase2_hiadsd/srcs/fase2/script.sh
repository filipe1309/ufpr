#!/bin/bash

if [ $1 -eq 0 ]; then
	mv cisj.c.txt cisj.c
	mv cisj.h.txt cisj.h
	mv diagnostico.c.txt diagnostico.c
	mv makefile.txt makefile
	mv rand.c.txt rand.c
	mv smpl.c.txt smpl.c
	mv smpl.h.txt smpl.h
else
	mv cisj.c cisj.c.txt
	mv cisj.h cisj.h.txt
	mv diagnostico.c diagnostico.c.txt
	mv makefile makefile.txt
	mv rand.c rand.c.txt
	mv smpl.c smpl.c.txt
	mv smpl.h smpl.h.txt
fi