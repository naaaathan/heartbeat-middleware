javac ./src/main/java/org/ufu/ds/*.java ./src/main/java/org/ufu/ds/heartbeat/*.java  ./src/main/java/org/ufu/ds/schedule/*.java  ./src/main/java/org/ufu/ds/election/*.java -d generated/
cd generated/
cp ../src/main/resources/nodes.properties ./main/java/org/ufu/ds/
jar cfe heartbeat-middleware.jar main.java.org.ufu.ds.Main main/java/org/ufu/ds/*.class main/java/org/ufu/ds/election/*.class main/java/org/ufu/ds/heartbeat/*.class main/java/org/ufu/ds/schedule/*.class main/java/org/ufu/ds/nodes.properties
java -jar heartbeat-middleware.jar LEADER