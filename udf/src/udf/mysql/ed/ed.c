/*
  $Id: ed.c 4068 2008-10-14 01:56:51Z rares $

  Copyright (C) 2008 by The Regents of the University of California

  Redistribution of this file is permitted under the terms of the 
  BSD license

  Date: 05/15/2008
  Author: Rares Vernica <rares (at) ics.uci.edu>
*/

#include <my_global.h>
#include <my_sys.h>
#include <mysql.h>
#include <string.h>

#ifdef HAVE_DLOPEN

#include <math.h>
#define min(a,b) fmin(a,b)

my_bool ed_init(UDF_INIT*, UDF_ARGS*, char*);
void ed_deinit(UDF_INIT*);
longlong ed(UDF_INIT*, UDF_ARGS*, char*, char*);

/*
 * - assumes both strings are in the same case
 */
longlong ed(UDF_INIT *initid __attribute__((unused)),
            UDF_ARGS *args, char *is_null, 
            char *error __attribute__((unused)))
{
  const char *a = args->args[0], *b = args->args[1];

  if (!a || !b)	{
    *is_null=1;
    return 0;
  }
  
  const int a_len = args->lengths[0], b_len = args->lengths[1];

  if (a_len == 0) return b_len;
  if (b_len == 0) return a_len;
  
  int i, j, i_prv, i_crt, d[2][b_len + 1];

  for (j = 0; j <= b_len; j++) d[0][j] = j;
  i_prv = 0;
  i_crt = 1;
  for (i = 1; i <= a_len; i++) {
    d[i_crt][0] = i;
    for (j = 1; j <= b_len; j++)
      d[i_crt][j] = min(min(
                            d[i_prv][j  ] + 1,
                            d[i_crt][j-1] + 1),
                        d[i_prv][j-1] + (a[i-1] == b[j-1] ? 0 : 1));
    i_prv = i_prv ? 0 : 1;
    i_crt = i_crt ? 0 : 1;
  }

  return d[i_prv][b_len];
}

my_bool ed_init(UDF_INIT *initid, UDF_ARGS *args, char *message)
{
  if (args->arg_count != 2 ||
      args->arg_type[0] != STRING_RESULT || 
      args->arg_type[1] != STRING_RESULT)
    {
      strcpy(message, "Wrong arguments to ed;  Use the source");
      return 1;
    }
  return 0;
}

void ed_deinit(UDF_INIT *initid __attribute__((unused)))
{
}

#endif /* HAVE_DLOPEN */
