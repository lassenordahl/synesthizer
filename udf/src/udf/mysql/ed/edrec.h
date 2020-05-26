/*
  $Id: edrec.h 4068 2008-10-14 01:56:51Z rares $

  Copyright (C) 2008 by The Regents of the University of California

  Redistribution of this file is permitted under the terms of the 
  BSD license

  Date: 05/15/2008
  Author: Rares Vernica <rares (at) ics.uci.edu>
*/

#include <my_global.h>
#include <my_sys.h>
#include <mysql.h>

void strtolower(char *s);
const char * strpbrknot(const char *s, const char *set);

longlong edrec(UDF_INIT *initid __attribute__((unused)),
               UDF_ARGS *args, 
               char *is_null, 
               char *error __attribute__((unused)));


