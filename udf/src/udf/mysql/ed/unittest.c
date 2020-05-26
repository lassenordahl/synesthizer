/*
  $Id: unittest.c 4068 2008-10-14 01:56:51Z rares $

  Copyright (C) 2008 by The Regents of the University of California

  Redistribution of this file is permitted under the terms of the 
  BSD license

  Date: 05/15/2008
  Author: Rares Vernica <rares (at) ics.uci.edu>
*/

#include <assert.h>
#include <string.h>

#include "edrec.h"
q

void test_edrec() 
{
  int t = 0;
  
  char s[20];

  /* --- strtolower --- */
  strcpy(s, "Abcd");
  strtolower(s);
  assert(strcmp(s, "abcd") == 0); t++;

  strcpy(s, "ABCD");
  strtolower(s);
  assert(strcmp(s, "abcd") == 0); t++;

  strcpy(s, "aBCD");
  strtolower(s);
  assert(strcmp(s, "abcd") == 0); t++;
  
  strcpy(s, "AbCd");
  strtolower(s);
  assert(strcmp(s, "abcd") == 0); t++;

  /* --- strpbrknot --- */

  char set[20];
  
  strcpy(s, "xabc");
  strcpy(set, "x");
  assert(strpbrknot(s, set) == s + 1); t++;

  strcpy(s, "abc");
  strcpy(set, "x");
  assert(strpbrknot(s, set) == s); t++;

  strcpy(s, "xxxabc");
  strcpy(set, "x");
  assert(strpbrknot(s, set) == s + 3); t++;

  strcpy(s, "xyabc");
  strcpy(set, "xy");
  assert(strpbrknot(s, set) == s + 2); t++;

  strcpy(s, "xyxxyyabc");
  strcpy(set, "xy");
  assert(strpbrknot(s, set) == s + 6); t++;

  strcpy(s, "yabc");
  strcpy(set, "xy");
  assert(strpbrknot(s, set) == s + 1); t++;

  strcpy(s, " , . abc");
  strcpy(set, " ,.");
  assert(strpbrknot(s, set) == s + 5); t++;

  /* --- editdistset --- */

  UDF_ARGS args;
  char s1[20], s2[20];
  unsigned long len[3];
  int thr;

  args.args = (char**)malloc(3 * sizeof(char*));
  args.args[0] = s1;
  args.args[1] = s2;
  args.args[2] = (char*)&thr;
  args.lengths = len;
  len[2] = 0;
  
  strcpy(s1, "abc");
  strcpy(s2, "add adc eec");
  thr = 1;
  len[0] = strlen(s1);
  len[1] = strlen(s2);
  assert(edrec(NULL, &args, NULL, NULL) == 1); t++;

  printf("edrec: %d\n", t);
}


int main() 
{
  test_edrec();

  return 0;
}
