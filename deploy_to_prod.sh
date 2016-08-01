#! /bin/sh/expect
spawn scp target/upload.war root@121.40.123.201:/data/www/webapps/upload.new.war
expect "100%"
spawn ssh root@121.40.123.201
sleep 2
expect "#"
send "cd /data/www/webapps\r"
expect "#"
send "/usr/local/tomcat/bin/shutdown.sh\r"
expect "#"
send "rm -rf upload.war upload/\r"
expect "#"
send "mv upload.new.war upload.war\r"
expect "#"
send "/usr/local/tomcat/bin/startup.sh\n"
interact


