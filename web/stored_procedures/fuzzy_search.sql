DROP FUNCTION IF EXISTS fuzzy;
CREATE FUNCTION SPLIT_STR(
  x VARCHAR(255),
  delim VARCHAR(12),
  pos INT
)
RETURNS VARCHAR(255)
RETURN REPLACE(SUBSTRING(SUBSTRING_INDEX(x, delim, pos),
       LENGTH(SUBSTRING_INDEX(x, delim, pos -1)) + 1),
       delim, '');

DELIMITER $$

CREATE FUNCTION fuzzy(name VARCHAR(1000), search VARCHAR(1000))
	RETURNS INT
BEGIN
	DECLARE pass_count INT DEFAULT 0;
    # for search loop
    DECLARE i INT Default 0;
	DECLARE search_sub VARCHAR(255);
	search_loop: LOOP
		SET i = i + 1;
		SET search_sub=SPLIT_STR(search, " ", i);
		IF search_sub = '' THEN
			LEAVE search_loop;
		END IF;
        IF edrec(search_sub,name,2) THEN
			SET pass_count = pass_count + 1;
		END IF;
	END LOOP search_loop;
	RETURN pass_count = i - 1;
END
$$