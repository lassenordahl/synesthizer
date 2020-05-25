-- $Id: edth.sql 4068 2008-10-14 01:56:51Z rares $
--
-- Copyright (C) 2008 by The Regents of the University of California
--
-- Redistribution of this file is permitted under the terms of the 
-- BSD license
--
-- Date: 05/15/2008
-- Author: Rares Vernica <rares (at) ics.uci.edu>

DROP FUNCTION IF EXISTS edth;
CREATE FUNCTION edth RETURNS INTEGER SONAME 'libedth.so';

SELECT edth('abc', 'ad', 1);
SELECT edth('abc', 'ad', 2);
SELECT edth('abc', 'aaa', 1);
SELECT edth('abc', 'aaa', 2);
SELECT edth('abc', 'abcd', 1);
SELECT edth('abc', 'abcd', 2);
SELECT edth('a', 'abcdefghijklmnopqrstuvwxyz', 2);
