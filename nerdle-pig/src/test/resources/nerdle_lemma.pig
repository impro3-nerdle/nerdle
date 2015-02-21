--REGISTER target/nerdle-pig-0.0.1-SNAPSHOT-job.jar;

SET mapred.child.java.opts '-Xmx4G';
SET pig.tmpfilecompression 'true'
SET pig.tmpfilecompression.codec 'gz';
SET mapred.job.queue.name 'processing';
SET job.name 'NERDLE-LEMMA';
--SET mapred.max.split.size 102400;
SET pig.splitCombination false;
SET mapred.task.timeout 1800000;

DEFINE LemmaFunction edu.tuberlin.dima.nerdle.pig.LemmaFunction();

facts = LOAD '$input' USING PigStorage() AS (fact:CHARARRAY);

facts_lemma = FOREACH facts GENERATE LemmaFunction(fact) AS fact;

STORE facts_lemma INTO '$output' using PigStorage(' ');