CREATE OR REPLACE PROCEDURE DELETE_OLD_SESSION
IS
BEGIN
    DELETE FROM Sesja WHERE (Expr <= sysdate - INTERVAL '12' HOUR);
END;
/

BEGIN
    DBMS_SCHEDULER.DROP_JOB (job_name => 'check_session');
END;
/
BEGIN
    DBMS_SCHEDULER.CREATE_JOB (
         job_name           => 'check_session',
         job_type           => 'STORED_PROCEDURE',
         job_action         => 'DELETE_OLD_SESSION',
         start_date         => current_timestamp,
         repeat_interval    => 'FREQ=hourly;',
         enabled            => true);

END;
/