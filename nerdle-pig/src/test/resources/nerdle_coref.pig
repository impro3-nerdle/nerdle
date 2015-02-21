--REGISTER target/nerdle-pig-0.0.1-SNAPSHOT-job.jar;

SET mapred.child.java.opts '-Xmx4G -XX:+UseConcMarkSweepGC';
SET pig.tmpfilecompression 'true'
SET pig.tmpfilecompression.codec 'gz';
SET mapred.job.queue.name 'processing';
SET job.name 'NERDLE-COREF';
SET mapred.max.split.size 102400;
SET pig.splitCombination false;
SET mapred.task.timeout 1800000;
SET mapred.max.map.failures.percent 50;
SET mapred.map.max.attempts 1;

DEFINE CorefFunction edu.tuberlin.dima.nerdle.pig.CorefFunction('5000');

articleTexts = LOAD '$input' USING PigStorage('\t') AS (source:CHARARRAY, articleText:CHARARRAY);

articleTexts_coref = FOREACH articleTexts GENERATE source, articleText, FLATTEN(CorefFunction(source, articleText)) AS fact;

STORE articleTexts_coref INTO '$output' USING PigStorage('\t');