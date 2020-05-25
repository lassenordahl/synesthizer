-- $Id: edrec.sql 4068 2008-10-14 01:56:51Z rares $
--
-- Copyright (C) 2008 by The Regents of the University of California
--
-- Redistribution of this file is permitted under the terms of the 
-- BSD license
--
-- Date: 05/15/2008
-- Author: Rares Vernica <rares (at) ics.uci.edu>

DROP FUNCTION IF EXISTS edrec;
CREATE FUNCTION edrec RETURNS INTEGER SONAME 'libedrec.so';

SELECT edrec('ab', 'xx ad xx', 1);
SELECT edrec('abc', 'ad', 2);
SELECT edrec('abc', 'aaa abd', 1);
SELECT edrec('abc', 'aaa', 2);
SELECT edrec('abc', 'abcd', 1);
SELECT edrec('abc', 'abcd', 2);
SELECT edrec('a', 'abcdefghijklmnopqrstuvwxyz', 2);

SELECT edrec('abc', 'xxx abcd yyy', 1);
SELECT edrec('abc', 'xxx abcd, yyy', 1);
SELECT edrec('abc', 'xxx abcd. yyy', 1);
SELECT edrec('abc', 'xxx ,.abcd,. yyy', 1);

SELECT edrec('abc', 'xxx Abcd yyy', 1);
SELECT edrec('abc', 'xxx ABCD yyy', 1);

SELECT edrec('xy yx ab', 'xx ad xx', 1);
