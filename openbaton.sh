#!/bin/bash

# Copyright (c) 2016 Open Baton (http://www.openbaton.org)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


source gradle.properties

_version=${version}
_level=INFO
_openbaton_cli_jar="cli/build/libs/cli-all-$_version.jar"

function usage {
    echo -e "Open-Baton\n"
    echo -e "Usage:\n\t ./openbaton.sh [option] command [command] [command]\n\t"
    echo -e "where option is"
    echo -e "\t\t * -c show configuration"
    echo -e "\t\t * -d activate debug mode"
    echo -e "\t\t * -h prints this help"
    echo -e "\t\t * -l lists available commands"
}

function showConfiguration {
    echo "Using the following Environment Variables:"
    echo -e "\tNFVO_IP=$NFVO_IP"
    echo -e "\tNFVO_PORT=$NFVO_PORT"
    echo -e "\tNFVO_API_VERSION=$NFVO_API_VERSION"
    echo -e "\tNFVO_USERNAME=$NFVO_USERNAME"
    echo -e "\tNFVO_PASSWORD=$NFVO_PASSWORD"
    echo -e "\tNFVO_PROJECT_ID=$NFVO_PROJECT_ID"
    echo -e "\tNFVO_SSL_ENABLED=$NFVO_SSL_ENABLED"

}

function checkEnvironmentVariables {
    checkEnvironmentVariable NFVO_IP
    checkEnvironmentVariable NFVO_PORT
    checkEnvironmentVariable NFVO_API_VERSION
    checkEnvironmentVariable NFVO_USERNAME
    checkEnvironmentVariable NFVO_PASSWORD
    checkEnvironmentVariable NFVO_PROJECT_ID
    checkEnvironmentVariable NFVO_SSL_ENABLED
}

function checkEnvironmentVariable {
    if ! env | grep -q ^$1= ; then
        echo "$1 is not defined as an Environment Variable"
        echo "Please run 'source nfvo.properties' or define it manually..."
        exit 1
    fi
}

function execute {
    java -jar -DrootLevel=$_level $_openbaton_cli_jar ${1+"$@"}    # the symbol ${1+"$@"} to handle spaces in arguments correctly
}

##
#   MAIN
##

while getopts "hlcd" OPTION
    do
         case $OPTION in
             c)
                showConfiguration
                exit 1
                 ;;
             d)
                 _level=DEBUG
                 ;;
             l)
                execute help
                exit 1
                ;;
             h) usage
                exit 1
                ;;
         esac
    done
    shift $(( OPTIND - 1 ))

if [ $# -eq 0 ]
then
    usage
    exit 1
else
    checkEnvironmentVariables
    execute ${1+"$@"}
fi




