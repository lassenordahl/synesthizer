# $Id: makefile 4064 2008-10-14 00:59:29Z rares $
#
# Copyright (C) 2008 by The Regents of the University of California
#
# Redistribution of this file is permitted under the terms of the BSD
# license
#
# Date: 05/15/2008
#
# Author: Rares Vernica <rares (at) ics.uci.edu>
#

CC=gcc
# on ubuntu
CFLAGS=-fPIC -Wall -O3 -I/usr/include/mysql
# on mac
# CFLAGS=-fPIC -Wall -O3 -I/usr/local/include/mysql
LDFLAGS=-L. -ledrec

all: libed.so libedth.so libedrec.so

libed.so: ed.c
	${CC} ${CFLAGS} -shared -o $@ $?

libedth.so: edth.c
	${CC} ${CFLAGS} -shared -o $@ $?

libedrec.so: edrec.c
	${CC} ${CFLAGS} -shared -o $@ $?

.PHONY: test
test:
	cat ed.sql edth.sql edrec.sql | mysql --user root --database=mysql --password

.PHONY: clean
clean:
	-rm unittest *.so *.o *~

unittest: unittest.o libedrec.so

unittest.o: edrec.h
