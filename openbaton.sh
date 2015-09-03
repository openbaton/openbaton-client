#!/bin/bash
source gradle.properties

_version=${version}
_openbaton_config_file=/etc/openbaton/cli.properties

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

while getopts “:c:d” OPTION
do
     case $OPTION in
         c)
             _openbaton_config_file=$OPTARG
             #echo $_openbaton_config_file
             ;;
         d)
             echo debug mode
             ;;
     esac
done
shift $(( OPTIND - 1 ))


if [ $# -eq 0 ]
then
        check_already_running_client
        check_not_running
        cd cli/build/libs/
        java -jar "cli-all-$_version.jar" "help"
else
        check_already_running_client
        check_not_running
        cd cli/build/libs/
        java -jar "cli-all-$_version.jar" $1 $2 $3
fi




