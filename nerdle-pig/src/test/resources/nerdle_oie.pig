--REGISTER target/nerdle-pig-0.0.1-SNAPSHOT-job.jar;

SET mapred.child.java.opts '-Xmx8G -XX:+UseConcMarkSweepGC';
SET pig.tmpfilecompression 'true'
SET pig.tmpfilecompression.codec 'gz';
SET mapred.job.queue.name 'processing';
SET job.name 'NERDLE-OIE';
SET mapred.max.split.size 102400;
SET pig.splitCombination false;
SET mapred.task.timeout 1800000;
SET mapred.max.map.failures.percent 50;
SET mapred.map.max.attempts 1;

DEFINE OpenIEFunction edu.tuberlin.dima.nerdle.pig.OpenIEFunction();

articleTexts = LOAD '$input' USING PigStorage('\t') AS (source:CHARARRAY, articleText:CHARARRAY, articleTexts_coref:CHARARRAY);

facts = FOREACH articleTexts GENERATE FLATTEN(OpenIEFunction(source, articleText, articleTexts_coref, false, true, true)) AS fact;

STORE facts INTO '$output';