nohup ./target/Node1/jboss-eap-6.4/bin/standalone.sh -Djboss.node.name=Node1 -Djboss.socket.binding.port-offset=100 -Djgroups.bind_addr=`ipconfig getifaddr en0` > Node1.out &
