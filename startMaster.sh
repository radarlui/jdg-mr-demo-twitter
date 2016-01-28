#nohup target/masterNode/jboss-eap-6.4/bin/standalone.sh -Djgroups.bind_addr=`ipconfig getifaddr en0` -Dhttp.proxyHost=squid.corp.redhat.com -Dhttp.proxyPort=3128 -Dhttps.proxyHost=squid.corp.redhat.com -Dhttps.proxyPort=3128 &
nohup target/masterNode/jboss-eap-6.4/bin/standalone.sh -Djgroups.bind_addr=`ipconfig getifaddr en0` &
