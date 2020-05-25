/*
  $Id: edrec.c 4068 2008-10-14 01:56:51Z rares $

  Copyright (C) 2008 by The Regents of the University of California

  Redistribution of this file is permitted under the terms of the 
  BSD license

  Date: 05/15/2008
  Author: Rares Vernica <rares (at) ics.uci.edu>
*/

#include <my_global.h>
#include <my_sys.h>
#include <mysql.h>

#include <ctype.h>
#include <string.h>

#ifdef HAVE_DLOPEN

#include <math.h>
#define min(a,b) fmin(a,b)

my_bool edrec_init(UDF_INIT*, UDF_ARGS*, char*);
void edrec_deinit(UDF_INIT*);
longlong edrec(UDF_INIT*, UDF_ARGS*, char*, char*);

const char* strpbrknot(const char *s, const char *set);
void strtolower(char *s);

#define min3(X, Y, Z) min(min((X), (Y)), (Z))

/*
 * - assumes query in lower case
 * - delimiters: " ,."
 */
longlong edrec(UDF_INIT *initid __attribute__((unused)),
               UDF_ARGS *args, char *is_null, 
               char *error __attribute__((unused)))
{
  const char *que = args->args[0], *rec_case = args->args[1];
  const int thrhld = *((longlong*) args->args[2]);

  if (!que || !rec_case)	{ /* Null argument */
    *is_null=1;
    return 0;
  }  

  const int n = args->lengths[0], rec_len = args->lengths[1];

  if (n == 0) return rec_len <= thrhld;
  if (rec_len == 0) return n <= thrhld;

  char rec[rec_len + 1];
  strncpy(rec, rec_case, rec_len);
  rec[rec_len] = 0;
  strtolower(rec);

  const char delim[] = " ,.";
  const char *rec_kw_st, *rec_kw_en;

  const char *a = que, *b;
  int c_min, ovr;
  int i, j, r, c, m;

  /* --- tokenize record --- */
  
  /* skip beginning delimiters */
  rec_kw_st = strpbrknot(rec, delim);

  /* find next delimiter */
  rec_kw_en = strpbrk(rec_kw_st, delim);
  if (rec_kw_en == NULL) rec_kw_en = rec_kw_st+ strlen(rec_kw_st);

  /* tokenize record and compute edit distance in the same time */
  while (rec_kw_st != rec_kw_en) {
    ovr = 0;
    b = rec_kw_st;
    m = rec_kw_en - rec_kw_st;

    int d[n + 1][m + 1];
      
    for (i = 0; i <= n; i++) d[i][0] = i;
    for (j = 1; j <= m; j++) d[0][j] = j;

    for (i = 1; i <= n; i++) {
      c_min = d[i][0] + 1;
      for (j = 1; j <= min(i, m); j++) {
        r = i - j + 1;
        c = j;
        d[r][c] = min3(d[r - 1][c] + 1, 
                       d[r][c - 1] + 1, 
                       d[r - 1][c - 1] + (a[r - 1] == b[c - 1]? 0 : 1));
        c_min = min3(c_min, d[r][c - 1], d[r][c]);
      }
      if (c_min > thrhld) {
        ovr = 1;
        break;
      }
    }
    if (!ovr) {
      for (j = 2; j <= m; j++) {
        c_min = d[n][j - 1] + 1;
        for (i = 1; i <= min(n, m - j + 1); i++) {
          r =  n - i +  1;
          c = j + i - 1;
          d[r][c] = min3(d[r - 1][c] + 1, 
                         d[r][c - 1] + 1, 
                         d[r - 1][c - 1] + (a[r - 1] == b[c - 1]? 0 : 1));
          c_min = min3(c_min, d[r][c - 1], d[r][c]);
        }
        if (c_min > thrhld) {
          ovr = 1;
          break;
        }
      }
    }
    
    if (!ovr && d[n][m] <= thrhld) return 1;
    
    /* skip beginning delimiters */
    rec_kw_st = strpbrknot(rec_kw_en, delim);

    /* find next delimiter */
    rec_kw_en = strpbrk(rec_kw_st, delim);
    if (rec_kw_en == NULL) rec_kw_en = rec_kw_st + strlen(rec_kw_st);
  }

  return 0;  
}

const char * strpbrknot(const char *s, const char *set)
{
  for (; *s; s++) {
    int found = 0;
    const char *x;
    for (x = set; *x; x++)
      if (*s == *x) {
        found = 1;
        break;
      }
    if (!found)
      return s;
  }
  return s;
}

void strtolower(char *s) 
{
  for (; *s; s++)
    *s = tolower(*s);
}

my_bool edrec_init(UDF_INIT *initid, UDF_ARGS *args, char *message)
{
  if (args->arg_count != 3 ||
      args->arg_type[0] != STRING_RESULT || 
      args->arg_type[1] != STRING_RESULT)
    {
      strcpy(message, "Wrong arguments to editdist;  Use the source");
      return 1;
    }
  return 0;
}

void edrec_deinit(UDF_INIT *initid __attribute__((unused)))
{
}

#endif /* HAVE_DLOPEN */
