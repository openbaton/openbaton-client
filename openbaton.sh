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

function usage {
    echo -e "Open-Baton\n"
    echo -e "Usage:\n\t ./openbaton.sh [option] comand [comand] [comand]\n\t"
    echo -e "where option is"
    echo -e "\t\t * -c path of properties file"
    echo -e "\t\t * -d debug mode"
    echo -e "comand help to see the list of the comands"
    echo -e "./openbaton.sh help"
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
        usage
        exit 1
else
        check_already_running_client
        check_not_running
        #cd cli/build/libs/
        java -jar "cli/build/libs/cli-all-$_version.jar" $_openbaton_config_file $1 $2 $3
fi




