--REGISTER target/nerdle-pig-0.0.1-SNAPSHOT-job.jar;

SET mapred.child.java.opts '-Xmx4G';
SET pig.tmpfilecompression 'true'
SET pig.tmpfilecompression.codec 'gz';
SET mapred.job.queue.name 'processing';
SET job.name 'NERDLE-SYNONYMS';
--SET mapred.max.split.size 102400;
SET pig.splitCombination false;
SET mapred.task.timeout 1800000;

DEFINE SynonymsFunction edu.tuberlin.dima.nerdle.pig.SynonymsFunction();

facts = LOAD '$input' USING PigStorage() AS (fact:CHARARRAY);

facts_synonyms = FOREACH facts GENERATE SynonymsFunction(fact) AS fact;

STORE facts_synonyms INTO 'output_facts_synonyms';