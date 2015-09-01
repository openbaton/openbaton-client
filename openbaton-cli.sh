#!/bin/bash
source gradle.properties



function check_already_running_client {
        result=$(ps aux | grep cli-all- | wc -l);
        if [ "${result}" -ne "1" ]; then
                echo "openbaton-client is already running.."
		exit;
        fi
}

function check_not_running {
        result=$(screen -ls | grep openbaton | wc -l);
        if [ "${result}" -eq "0" ]; then
                echo "openbaton is not running.."
		exit;
        fi
}


##
#   MAIN
##

if [ $# -eq 0 ]
then
        check_already_running_client
        check_not_running
        cd cli/build/libs/
        java -jar cli-all-0.4-SNAPSHOT.jar "help"
else
        check_already_running_client
        check_not_running
        cd cli/build/libs/
        java -jar cli-all-0.4-SNAPSHOT.jar $1 $2 $3
fi







